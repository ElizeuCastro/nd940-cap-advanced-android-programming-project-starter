package com.example.android.politicalpreparedness.di

import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.election.repository.ElectionDateSource
import com.example.android.politicalpreparedness.election.repository.ElectionRepository
import com.example.android.politicalpreparedness.network.CivicsApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val apiModule = module {
    single {
        return@single CivicsApi.retrofitService
    }
}

val electionModule = module {
    single { ElectionDatabase.getInstance(get()).electionDao }
    single { ElectionDateSource(get(), get()) as ElectionRepository }
    viewModel { ElectionsViewModel(get()) }
}
