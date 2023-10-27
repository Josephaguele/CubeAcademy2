package com.cube.cubeacademy.activities

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.Observer
import com.cube.cubeacademy.databinding.ActivityCreateNominationBinding
import com.cube.cubeacademy.lib.di.Repository
import com.cube.cubeacademy.viewmodel.NominationViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.activity.viewModels
@AndroidEntryPoint
class CreateNominationActivity : AppCompatActivity() {
	private lateinit var binding: ActivityCreateNominationBinding

	@Inject
	lateinit var repository: Repository
	private val viewModel: NominationViewModel by viewModels()
	private lateinit var nomineeSpinner: AppCompatSpinner
	private lateinit var howWeRunTextView : TextView
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityCreateNominationBinding.inflate(layoutInflater)
		setContentView(binding.root)
		nomineeSpinner = binding.cubeNomineeSpinner


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
		})

		viewModel.fetchCubeNominees()
	}

	private fun multiColourTextView(){
		howWeRunTextView = binding.howwerunTextview
		val spannableText = SpannableString(howWeRunTextView.text)
		spannableText.setSpan(ForegroundColorSpan(Color.parseColor("#FFF70087")), 23, howWeRunTextView.text.length -5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
		howWeRunTextView.text = spannableText
	}

}