package com.cube.cubeacademy.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cube.cubeacademy.lib.di.Repository
import com.cube.cubeacademy.lib.models.Nomination
import com.cube.cubeacademy.lib.models.Nominee
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NominationViewModel @Inject constructor(private val repository: Repository,
                                              private val dispatcher: CoroutineDispatcher = Dispatchers.IO) : ViewModel() {

    private val _nomineeList = MutableLiveData<List<Nominee>>()
    val nomineeList: LiveData<List<Nominee>> = _nomineeList


    private val _nominationResult = MutableLiveData<Nomination?>()
    val nominationResult: MutableLiveData<Nomination?> = _nominationResult

    fun fetchCubeNominees(){
        viewModelScope.launch(dispatcher){
            val nominees = repository.getAllNominees()
            _nomineeList.postValue(nominees)
        }
    }


    fun submitNomination(nomineeId: String, reason: String, process: String) {
        viewModelScope.launch(dispatcher) {
            try {
                val nomination = repository.createNomination(nomineeId, reason, process)
                _nominationResult.postValue(nomination)
            } catch (e: Exception) {

               Log.i("Nomination", e.toString())
            }
        }
    }
}