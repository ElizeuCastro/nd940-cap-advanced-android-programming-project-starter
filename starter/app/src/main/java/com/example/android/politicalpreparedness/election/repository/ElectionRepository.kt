package com.example.android.politicalpreparedness.election.repository

import com.example.android.politicalpreparedness.network.models.ElectionResponse

interface ElectionRepository {

    suspend fun getUpcomingElections(): ElectionResponse

}