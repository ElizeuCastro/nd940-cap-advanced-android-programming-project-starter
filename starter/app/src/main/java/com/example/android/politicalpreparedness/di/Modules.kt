package com.example.android.politicalpreparedness.di

import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.election.ui.ElectionsViewModel
import com.example.android.politicalpreparedness.election.ui.VoterInfoViewModel
import com.example.android.politicalpreparedness.election.repository.ElectionRepository
import com.example.android.politicalpreparedness.election.repository.ElectionDataSource
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.representative.RepresentativeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {
    single { ElectionDatabase.getInstance(get()).electionDao }
}

val apiModule = module {
    single {
        return@single CivicsApi.retrofitService
    }
}

val repositoryModule = module {
    single { ElectionRepository(get(), get()) as ElectionDataSource }
}

val electionModule = module {
    viewModel { ElectionsViewModel(get()) }
}

val voterInfoModule = module {
    viewModel { (election: Election) -> VoterInfoViewModel(get(), election) }
}

val representativeModule = module {
    viewModel { RepresentativeViewModel(get()) }
}

