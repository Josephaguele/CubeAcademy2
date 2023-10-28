package com.cube.cubeacademy.lib.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cube.cubeacademy.databinding.ViewNominationListItemBinding
import com.cube.cubeacademy.lib.models.Nomination
import com.cube.cubeacademy.lib.models.Nominee

class NominationsRecyclerViewAdapter( private var nomineeList: List<Nominee>) : ListAdapter<Nomination, NominationsRecyclerViewAdapter.ViewHolder>(DIFF_CALLBACK) {
	class ViewHolder(val binding: ViewNominationListItemBinding) : RecyclerView.ViewHolder(binding.root)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val binding = ViewNominationListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return ViewHolder(binding)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val item = getItem(position)
		val nominee = nomineeList.find { it.nomineeId == item.nomineeId }
		/**
		* TODO: This should show the nominee name instead of their id! Where can you get their name from?
		*/
		holder.binding.apply {
			name.text = nominee?.firstName + " " + nominee?.lastName
			reason.text = item.reason
		}
	}

	fun updateNomineeList(nomineeList: List<Nominee>) {
		this.nomineeList = nomineeList
		notifyDataSetChanged()
	}


	companion object {
		val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Nomination>() {
			override fun areItemsTheSame(oldItem: Nomination, newItem: Nomination) = oldItem.nominationId == newItem.nominationId
			override fun areContentsTheSame(oldItem: Nomination, newItem: Nomination) = oldItem == newItem
		}
	}
}