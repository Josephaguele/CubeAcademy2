package com.cube.cubeacademy.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import com.cube.cubeacademy.databinding.ActivityCreateNominationBinding
import com.cube.cubeacademy.lib.di.Repository
import com.cube.cubeacademy.lib.models.Nominee
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class CreateNominationActivity : AppCompatActivity() {
	private lateinit var binding: ActivityCreateNominationBinding

	@Inject
	lateinit var repository: Repository
	private lateinit var nomineeList: List<Nominee>
	private lateinit var nomineeSpinner: AppCompatSpinner
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityCreateNominationBinding.inflate(layoutInflater)
		setContentView(binding.root)
		nomineeSpinner = binding.cubeNomineeSpinner
		populateUI()
	}

	private fun populateUI() {
		/**
		 * TODO: Populate the form after having added the views to the xml file (Look for TODO comments in the xml file)
		 * 		 Add the logic for the views and at the end, add the logic to create the new nomination using the api
		 * 		 The nominees drop down list items should come from the api (By fetching the nominee list)
		 */

		// Here we fetch the list of cube nominees from the repository or API
		GlobalScope.launch(Dispatchers.IO) {
			nomineeList = repository.getAllNominees()
			withContext(Dispatchers.Main){
				val nomineeNames = nomineeList.map{"${it.firstName} ${it.lastName}"}
				val adapter = ArrayAdapter(this@CreateNominationActivity, android.R.layout.simple_spinner_item, nomineeNames)
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
				nomineeSpinner.adapter = adapter
			}
		}
	}
}