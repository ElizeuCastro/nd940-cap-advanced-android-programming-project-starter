package com.example.android.politicalpreparedness.election.repository

import com.example.android.politicalpreparedness.election.dto.VoterInfoDTO
import com.example.android.politicalpreparedness.network.Result
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse

class FakeRepository : ElectionDataSource {

    private var shouldReturnError = false
    var elections = mutableListOf<Election>()
    var savedElections = mutableListOf<Election>()
    var voterInfoDTO = VoterInfoDTO()

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun getUpcomingElections(): Result<ElectionResponse> {
        return if (shouldReturnError) {
            return Result.Error("Exception getReminder")
        } else {
            Result.Success(ElectionResponse(
                    "elections", elections
            ))
        }
    }

    override suspend fun getSavedElections(): Result<List<Election>> {
        return if (shouldReturnError) {
            return Result.Error("Exception getReminder")
        } else {
            Result.Success(savedElections)
        }
    }

    override suspend fun getElection(id: Int): Election? {
        return savedElections.find { it.id == id}
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

}