package com.cube.cubeacademy.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
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

		setupUI()
		viewModel.fetchNominations()
		viewModel.fetchCubeNominees()




		viewModel.nomineeList.observe(this, Observer { nominees ->
			binding.nominationsList.adapter?.let { adapter ->
				(adapter as NominationsRecyclerViewAdapter).updateNomineeList(nominees)
			}
		})

		viewModel.nominations.observe(this, Observer { nominationsList ->
			binding.nominationsList.adapter?.let { adapter ->
				(adapter as NominationsRecyclerViewAdapter).submitList(nominationsList)
				if (nominationsList.isEmpty()) {
					binding.nominationsList.visibility = View.GONE
				} else {
					binding.nominationsList.visibility = View.VISIBLE
				}
			}
		})
	}

	private fun setupUI(){
		val adapter = createAdapter()
		setupRecyclerView(adapter)
	}

	private fun setupRecyclerView(nominationsRecyclerViewAdapter: NominationsRecyclerViewAdapter) {

		binding.nominationsList.apply {
			adapter = nominationsRecyclerViewAdapter
			layoutManager = LinearLayoutManager(context)
			setHasFixedSize(true)
		}
	}

	private fun createAdapter(): NominationsRecyclerViewAdapter {
		return NominationsRecyclerViewAdapter(emptyList())
	}

	private fun navigateToNominations(){
		val intent = Intent(this@MainActivity, CreateNominationActivity::class.java)
		startActivity(intent)
	}
}