package com.example.android.politicalpreparedness.election.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.election.repository.ElectionDataSource
import com.example.android.politicalpreparedness.network.Result
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import kotlinx.coroutines.launch

class ElectionsViewModel(private val repository: ElectionDataSource) : ViewModel() {

    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upcomingElections

    private val _savedElections = MutableLiveData<List<Election>>()
    val savedElections: LiveData<List<Election>>
        get() = _savedElections

    private val _navigateToVoterInfo = MutableLiveData<Election?>()
    val navigateToVoterInfo: LiveData<Election?>
        get() = _navigateToVoterInfo

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    private var _showUpcomingElectionError = MutableLiveData<Boolean>()
    val showUpcomingElectionError: LiveData<Boolean>
        get() = _showUpcomingElectionError

    init {
        fetchUpcomingElections()
        fetchSavedElections()
    }

    private fun fetchUpcomingElections() {
        _showLoading.value = true
        viewModelScope.launch {
            val result = repository.getUpcomingElections()
            _showLoading.value = false
            when (result) {
                is Result.Success<*> -> {
                    _upcomingElections.value = (result.data as ElectionResponse).elections
                    _showUpcomingElectionError.value = false
                }
                is Result.Error -> {
                    _upcomingElections.value = emptyList()
                    _showUpcomingElectionError.value = true
                }
            }
        }
    }

    private fun fetchSavedElections() {
        viewModelScope.launch {
            when (val result = repository.getSavedElections()) {
                is Result.Success<*> -> {
                    _savedElections.value = (result.data as List<Election>)
                }
                is Result.Error -> {
                    _savedElections.value = emptyList()
                }
            }
        }
    }

    fun displayVoterInfo(election: Election) {
        _navigateToVoterInfo.value = election
    }

    fun displayVoterInfoComplete() {
        _navigateToVoterInfo.value = null
    }

}