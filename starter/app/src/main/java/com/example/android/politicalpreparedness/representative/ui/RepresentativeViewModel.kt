package com.example.android.politicalpreparedness.representative.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.election.repository.ElectionDataSource
import com.example.android.politicalpreparedness.network.Result
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(private val repository: ElectionDataSource) : ViewModel() {

    val addressLine1 = MutableLiveData<String>()
    val addressLine2 = MutableLiveData<String>()
    val city = MutableLiveData<String>()
    val state = MutableLiveData<String>()
    val zip = MutableLiveData<String>()

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    private val _findMyLocation = MutableLiveData<Boolean>()
    val findMyLocation: LiveData<Boolean>
        get() = _findMyLocation

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    fun findMyRepresentativesClick() {
        _showLoading.value = true
        viewModelScope.launch {
            val result = repository.getRepresentatives(getAddress())
            _showLoading.value = false
            when (result) {
                is Result.Success -> {
                    _representatives.value = result.data.offices.flatMap { office ->
                        office.getRepresentatives(result.data.officials)
                    }
                }
                is Result.Error -> {
                    _representatives.value = emptyList()
                }
            }

        }
    }

    private fun getAddress(): String {
        return Address(
                addressLine1.value.toString(),
                addressLine2.value.toString(),
                city.value.toString(),
                state.value.toString(),
                zip.value.toString()
        ).toFormattedString()
    }

    fun useMyLocationClick() {
        _findMyLocation.value = true
    }

    fun useMyLocationComplete() {
        _findMyLocation.value = false
    }

    fun updateState(newState: String) {
        state.value = newState
    }

    fun updateAddress(address: Address) {
        addressLine1.value = address.line1
        addressLine2.value = address.line2 ?: ""
        city.value = address.city
        state.value = address.state
        zip.value = address.zip
    }

}
