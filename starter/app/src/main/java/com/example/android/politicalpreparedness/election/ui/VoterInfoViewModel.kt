package com.example.android.politicalpreparedness.election.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.election.dto.VoterInfoDTO
import com.example.android.politicalpreparedness.election.repository.ElectionDataSource
import com.example.android.politicalpreparedness.network.Result
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch

class VoterInfoViewModel(
        private val repository: ElectionDataSource,
        val election: Election
) : ViewModel() {

    private var _isFollow = MutableLiveData<Boolean>()
    val isFollow: LiveData<Boolean>
        get() = _isFollow

    private var _voterInfo = MutableLiveData<VoterInfoDTO>()
    val voterInfo: LiveData<VoterInfoDTO>
        get() = _voterInfo

    private var _showToastVoterInformationError = MutableLiveData<Boolean>()
    val showToastVoterInfoError: LiveData<Boolean>
        get() = _showToastVoterInformationError

    private var _url = MutableLiveData<String>()
    val url: LiveData<String>
        get() = _url


    init {
        checkIsFollowing()
        fetchVoterInfo()
    }

    private fun checkIsFollowing() {
        viewModelScope.launch {
            _isFollow.value = repository.getElection(election.id) != null
        }
    }

    private fun fetchVoterInfo() {
        viewModelScope.launch {
            if (election.division.state.isNotEmpty()) {
                val address = "${election.division.country},${election.division.state}"
                when (val result = repository.getVoterInfo(address, election.id)) {
                    is Result.Success<*> -> {
                        _voterInfo.value = result.data as VoterInfoDTO
                    }
                    is Result.Error -> {
                        _voterInfo.value = VoterInfoDTO()
                        _showToastVoterInformationError.value = true
                    }
                }
            }
        }
    }

    fun toggle(election: Election) {
        viewModelScope.launch {
            if (isFollow.value == true) {
                repository.delete(election)
            } else {
                repository.insert(election)
            }
            checkIsFollowing()
        }
    }

    fun onURLClick(url: String) {
        _url.value = url
    }

}