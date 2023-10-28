package com.cube.cubeacademy.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cube.cubeacademy.databinding.ActivityMainBinding
import com.cube.cubeacademy.lib.adapters.NominationsRecyclerViewAdapter
import com.cube.cubeacademy.lib.di.Repository
import com.cube.cubeacademy.viewmodel.NominationViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
	private lateinit var binding: ActivityMainBinding
	private lateinit var recyclerView: RecyclerView
	private lateinit var adapter: NominationsRecyclerViewAdapter
	private val viewModel: NominationViewModel by viewModels()
	private lateinit var emptyContainer : LinearLayout
	@Inject
	lateinit var repository: Repository

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)
		binding.createButton.setOnClickListener {
			navigateToNominations()
		}
		emptyContainer = binding.emptyContainer


		// Fetch the list of nominees and nominations using the ViewModel
		adapter = NominationsRecyclerViewAdapter(emptyList())
		recyclerView = binding.nominationsList
		recyclerView.layoutManager = LinearLayoutManager(this)
		recyclerView.adapter = adapter

		viewModel.fetchNominations()
		viewModel.fetchCubeNominees()


		viewModel.nomineeList.observe(this, Observer {nominees ->
			// update the dataset that the adapter has to reflect the most recent changes
			adapter.updateNomineeList(nominees)
		})

		viewModel.nominations.observe(this, Observer { nominationsList ->
			recyclerView.layoutManager = LinearLayoutManager(this)
			adapter.submitList(nominationsList)
			if (nominationsList.isEmpty()) {
				recyclerView.visibility = View.GONE
			} else {
				recyclerView.visibility = View.VISIBLE
			}
		})
	}

	private fun navigateToNominations(){
		val intent = Intent(this@MainActivity, CreateNominationActivity::class.java)
		startActivity(intent)
	}
}