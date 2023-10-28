package com.cube.cubeacademy.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.Observer
import com.cube.cubeacademy.R
import com.cube.cubeacademy.databinding.ActivityCreateNominationBinding
import com.cube.cubeacademy.lib.di.Repository
import com.cube.cubeacademy.lib.models.Nominee
import com.cube.cubeacademy.viewmodel.NominationViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateNominationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateNominationBinding

    @Inject
    lateinit var repository: Repository
    private val viewModel: NominationViewModel by viewModels()
    private lateinit var nomineeSpinner: AppCompatSpinner
    private lateinit var howWeRunTextView: TextView
    private lateinit var submitNominationButton: Button
    private lateinit var backButton: Button
    private lateinit var reasonEditText: EditText
    private lateinit var nomineeList: List<Nominee>
    private lateinit var radioGroup: RadioGroup
    private var processText: String = ""
    private lateinit var formModal: Dialog
    private lateinit var leaveButton: Button
    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateNominationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        nomineeSpinner = binding.cubeNomineeSpinner
        submitNominationButton = binding.submitNominationButton
        backButton = binding.backButton
        radioGroup = binding.radioGroup
        submitNominationButton.isEnabled = false // Initially disable the  submit nomination button
        reasonEditText = binding.reasoningEditText
        formModal = Dialog(this)
        formModal.setContentView(R.layout.modaldialog)

        // Set the dialog background to be transparent
        formModal.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        leaveButton = formModal.findViewById(R.id.leave_button)
        cancelButton = formModal.findViewById(R.id.cancel_button)

        populateUI()
        multiColourTextView()
        handleBackButton()
        handleSubmitButton()
        handleLeaveButton()
        cancelButton.setOnClickListener { formModal.dismiss() }

    }

    private fun populateUI() {
        /**
         * TODO: Populate the form after having added the views to the xml file (Look for TODO comments in the xml file)
         * 		 Add the logic for the views and at the end, add the logic to create the new nomination using the api
         * 		 The nominees drop down list items should come from the api (By fetching the nominee list)
         */

        // Here we fetch the list of cube nominees from the repository or API
        viewModel.nomineeList.observe(this, Observer { nominees ->
            val nomineeNames = nominees.map { "${it.firstName} ${it.lastName}" }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nomineeNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            nomineeSpinner.adapter = adapter
            // Set the nomineeList when it's available
            nomineeList = nominees
        })

        viewModel.fetchCubeNominees()

        // Set an OnCheckedChangeListener for the RadioGroup
        radioGroup.setOnCheckedChangeListener { _: RadioGroup, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            processText = radioButton.text.toString().toLowerCase()
            when (processText) {
                "not sure" -> processText = "not_sure"
                "very fair" -> processText = "very_fair"
                "very unfair" -> processText = "very_unfair"
            }
            checkEnableSubmitButton()
        }


        // Observe the nomination result
        viewModel.nominationResult.observe(this, Observer { result ->
            Toast.makeText(
                applicationContext,
                "You submitted  on " + result?.dateSubmitted.toString(),
                Toast.LENGTH_LONG
            ).show()
        })
    }

    private fun multiColourTextView() {
        howWeRunTextView = binding.howwerunTextview
        val spannableText = SpannableString(howWeRunTextView.text)
        spannableText.setSpan(
            ForegroundColorSpan(Color.parseColor(getString(R.string.red_colour))),
            23,
            howWeRunTextView.text.length - 5,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        howWeRunTextView.text = spannableText
    }

    private fun checkEnableSubmitButton() {
        val nomineeSelected = nomineeList.isNotEmpty() && nomineeSpinner.selectedItemPosition != -1
        val reasonNotEmpty = reasonEditText.text.toString().isNotBlank()
        val processNotEmpty = processText.isNotBlank()
        submitNominationButton.isEnabled = nomineeSelected && reasonNotEmpty && processNotEmpty
    }

    private fun navigateToMainActivity(){
        val intent = Intent(this@CreateNominationActivity, MainActivity::class.java)
        startActivity(intent)
    }

    private fun showConfirmationDialog(){
        val window = formModal.window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.BOTTOM
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND
        wlp.dimAmount = 0.7f // dim the background by 70%
        // Set dialog width to match activity window width. I actually tried using xml with matchparent but it didn't work
        // I will have to find out why later but for now this works. lol'
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        wlp.width = displayMetrics.widthPixels
        window!!.attributes = wlp
        formModal.show()
    }

    private fun handleBackButton(){
        backButton.setOnClickListener {
            val reasonNotEmpty = reasonEditText.text.toString().isNotBlank()
            val processNotEmpty = processText.isNotBlank()
            if (reasonNotEmpty || processNotEmpty) {
                showConfirmationDialog()
            } else {
                navigateToMainActivity()
            }
        }
    }

    private fun handleSubmitButton() {
        submitNominationButton.setOnClickListener {
            if (nomineeList.isNotEmpty()) {
                // Get the selected nominee's ID
                val selectedNominee = nomineeList[nomineeSpinner.selectedItemPosition]
                val nomineeId = selectedNominee.nomineeId

                // Get reason and process from the EditTexts
                val reason = reasonEditText.text.toString()

                // Call the ViewModel function to submit the nomination
                viewModel.submitNomination(nomineeId, reason, processText)
                val intent =
                    Intent(this@CreateNominationActivity, NominationSubmittedActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    applicationContext,
                    "There is no nominee list yet",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun handleLeaveButton(){
        leaveButton.setOnClickListener {
            navigateToMainActivity()
        }
    }
}