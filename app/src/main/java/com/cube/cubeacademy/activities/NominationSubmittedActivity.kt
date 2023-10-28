package com.cube.cubeacademy.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.cube.cubeacademy.databinding.ActivityNominationSubmittedBinding

class NominationSubmittedActivity : AppCompatActivity() {
	private lateinit var binding: ActivityNominationSubmittedBinding
	private lateinit var createNewNominationButton : Button
	private lateinit var backToHomeButton : Button

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityNominationSubmittedBinding.inflate(layoutInflater)
		setContentView(binding.root)
		createNewNominationButton = binding.submitButton
		backToHomeButton = binding.backButton
		buttonsFunctionality()
	}

	private fun buttonsFunctionality() {
		createNewNominationButton.setOnClickListener{
			val intent = Intent(this@NominationSubmittedActivity, CreateNominationActivity::class.java)
			startActivity(intent)
		}

		backToHomeButton.setOnClickListener{
			val intent = Intent(this@NominationSubmittedActivity, MainActivity::class.java)
			startActivity(intent)
		}
	}
}