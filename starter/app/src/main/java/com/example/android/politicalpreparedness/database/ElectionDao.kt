package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Insert
    suspend fun insert(election: Election)

    @Query("select * from election_table")
    suspend fun getSavedElections() : List<Election>

    @Query("select * from election_table where id = :id")
    suspend fun get(id: Int): Election?

    @Delete
    suspend fun delete(election: Election)

    @Query("delete from election_table")
    suspend fun clear()

}