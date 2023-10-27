package com.cube.cubeacademy.activities

import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.Observer
import com.cube.cubeacademy.databinding.ActivityCreateNominationBinding
import com.cube.cubeacademy.lib.di.Repository
import com.cube.cubeacademy.viewmodel.NominationViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.activity.viewModels
import com.cube.cubeacademy.lib.models.Nominee
import java.util.Locale

@AndroidEntryPoint
class CreateNominationActivity : AppCompatActivity() {
	private lateinit var binding: ActivityCreateNominationBinding

	@Inject
	lateinit var repository: Repository
	private val viewModel: NominationViewModel by viewModels()
	private lateinit var nomineeSpinner: AppCompatSpinner
	private lateinit var howWeRunTextView : TextView
	private lateinit var submitNominationButton : Button
	private lateinit var reasonEditText : EditText
	private lateinit var nomineeList: List<Nominee>
	private lateinit var radioGroup: RadioGroup
	private var processText: String = ""

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityCreateNominationBinding.inflate(layoutInflater)
		setContentView(binding.root)
		nomineeSpinner = binding.cubeNomineeSpinner
		submitNominationButton = binding.submitNominationButton
		radioGroup = binding.radioGroup
		submitNominationButton.isEnabled = false // Initially disable the button
		reasonEditText = binding.reasoningEditText

		populateUI()
		multiColourTextView()
	}

	private fun populateUI() {
		/**
		 * TODO: Populate the form after having added the views to the xml file (Look for TODO comments in the xml file)
		 * 		 Add the logic for the views and at the end, add the logic to create the new nomination using the api
		 * 		 The nominees drop down list items should come from the api (By fetching the nominee list)
		 */

		// Here we fetch the list of cube nominees from the repository or API
		viewModel.nomineeList.observe(this, Observer{ nominees ->
			val nomineeNames = nominees.map{ "${it.firstName} ${it.lastName}"}
			val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,nomineeNames)
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
			Log.i(TAG, "process: $processText")
			checkEnableSubmitButton()
		}


		submitNominationButton.setOnClickListener {
			if (nomineeList.isNotEmpty()){
				// Get the selected nominee's ID
				val selectedNominee = nomineeList[nomineeSpinner.selectedItemPosition]
				val nomineeId = selectedNominee.nomineeId

				// Get reason and process from the EditTexts
				val reason = reasonEditText.text.toString()



				// Call the ViewModel function to submit the nomination
				viewModel.submitNomination(nomineeId, reason, processText)
			}else {
				Toast.makeText(applicationContext, "There is no nominee list yet", Toast.LENGTH_LONG).show()
			}
		}

		// Observe the nomination result
		viewModel.nominationResult.observe(this, Observer { result ->
			Toast.makeText(applicationContext, result.toString(), Toast.LENGTH_LONG).show()
		})
	}

	private fun multiColourTextView(){
		howWeRunTextView = binding.howwerunTextview
		val spannableText = SpannableString(howWeRunTextView.text)
		spannableText.setSpan(ForegroundColorSpan(Color.parseColor("#FFF70087")), 23, howWeRunTextView.text.length -5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
		howWeRunTextView.text = spannableText
	}

	private fun checkEnableSubmitButton() {
		val nomineeSelected = nomineeList.isNotEmpty() && nomineeSpinner.selectedItemPosition != -1
		val reasonNotEmpty = reasonEditText.text.toString().isNotBlank()
		val processNotEmpty = processText.isNotBlank()
		submitNominationButton.isEnabled = nomineeSelected && reasonNotEmpty && processNotEmpty
	}

}