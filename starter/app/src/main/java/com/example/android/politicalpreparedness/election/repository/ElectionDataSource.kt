package com.example.android.politicalpreparedness.election.repository

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.election.dto.VoterInfoDTO
import com.example.android.politicalpreparedness.network.Result
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse

interface ElectionDataSource {

    suspend fun getUpcomingElections(): Result<ElectionResponse>

    fun getSavedElections(): LiveData<List<Election>>

    suspend fun getElection(id: Int): Election?

    suspend fun insert(election: Election)

    suspend fun delete(election: Election)

    suspend fun getVoterInfo(address: String, electionId: Int): Result<VoterInfoDTO>

    suspend fun getRepresentatives(address: String): Result<RepresentativeResponse>

}