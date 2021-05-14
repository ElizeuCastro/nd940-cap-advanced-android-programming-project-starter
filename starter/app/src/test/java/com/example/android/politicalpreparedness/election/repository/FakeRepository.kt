package com.example.android.politicalpreparedness.election.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.election.dto.VoterInfoDTO
import com.example.android.politicalpreparedness.network.Result
import com.example.android.politicalpreparedness.network.models.*

class FakeRepository : ElectionDataSource {

    private var shouldReturnError = false
    var elections = mutableListOf<Election>()
    var savedElections = mutableListOf<Election>()
    var voterInfoDTO = VoterInfoDTO()
    var representativeResponse = RepresentativeResponse(
            offices = emptyList(), officials = emptyList()
    )

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun getUpcomingElections(): Result<ElectionResponse> {
        return if (shouldReturnError) {
            return Result.Error("Exception getUpcomingElections")
        } else {
            Result.Success(ElectionResponse(
                    "elections", elections
            ))
        }
    }

    override fun getSavedElections(): LiveData<List<Election>> {
        val observableTasks = MutableLiveData<List<Election>>()
        observableTasks.value = savedElections
        return observableTasks
    }

    override suspend fun getElection(id: Int): Election? {
        return savedElections.find { it.id == id }
    }

    override suspend fun insert(election: Election) {
        savedElections.add(election)
    }

    override suspend fun delete(election: Election) {
        savedElections.remove(election)
    }

    override suspend fun getVoterInfo(address: String, electionId: Int): Result<VoterInfoDTO> {
        return if (shouldReturnError) {
            return Result.Error("Exception getVoterInfo")
        } else {
            Result.Success(voterInfoDTO)
        }
    }

    override suspend fun getRepresentatives(address: String): Result<RepresentativeResponse> {
        return if (shouldReturnError) {
            return Result.Error("Exception getReminder")
        } else {
            Result.Success(representativeResponse)
        }
    }

}