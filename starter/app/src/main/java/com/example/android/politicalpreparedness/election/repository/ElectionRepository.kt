package com.example.android.politicalpreparedness.election.repository

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.election.dto.VoterInfoDTO
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.Result
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.asVoterInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionRepository(
        private val service: CivicsApiService,
        private val dao: ElectionDao,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ElectionDataSource {

    override suspend fun getUpcomingElections(): Result<ElectionResponse> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(service.getUpcomingElections())
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }

    override suspend fun getSavedElections(): Result<List<Election>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(dao.getSavedElections())
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }

    override suspend fun getElection(id: Int): Election? = withContext(ioDispatcher) {
        dao.get(id)
    }

    override suspend fun insert(election: Election) {
        withContext(ioDispatcher) {
            dao.insert(election)
        }
    }

    override suspend fun delete(election: Election) {
        withContext(ioDispatcher) {
            dao.delete(election)
        }
    }

    override suspend fun getVoterInfo(address: String, electionId: Int): Result<VoterInfoDTO> {
        return try {
            Result.Success(service.getVoterInfo(address, electionId).asVoterInfo())
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }


}