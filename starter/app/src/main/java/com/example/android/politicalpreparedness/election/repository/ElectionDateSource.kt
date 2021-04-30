package com.example.android.politicalpreparedness.election.repository

import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionDateSource(private val service: CivicsApiService, val dao: ElectionDao) : ElectionRepository {

    override suspend fun getUpcomingElections(): ElectionResponse {
        return withContext(Dispatchers.IO) {
            service.getUpcomingElections()
        }
    }


}