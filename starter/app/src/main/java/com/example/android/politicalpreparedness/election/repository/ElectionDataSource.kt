package com.example.android.politicalpreparedness.election.repository

import com.example.android.politicalpreparedness.election.dto.VoterInfoDTO
import com.example.android.politicalpreparedness.network.Result
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse

interface ElectionDataSource {

    suspend fun getUpcomingElections(): Result<ElectionResponse>

    suspend fun getSavedElections(): Result<List<Election>>

    suspend fun getElection(id: Int): Election?

    suspend fun insert(election: Election)

    suspend fun delete(election: Election)

    suspend fun getVoterInfo(address: String, electionId: Int): Result<VoterInfoDTO>

}