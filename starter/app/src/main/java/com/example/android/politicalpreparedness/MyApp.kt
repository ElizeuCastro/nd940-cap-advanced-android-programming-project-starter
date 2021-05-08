package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.di.apiModule
import com.example.android.politicalpreparedness.di.databaseModule
import com.example.android.politicalpreparedness.di.electionModule
import com.example.android.politicalpreparedness.di.voterInfoModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@MyApp)
            modules(listOf(databaseModule, apiModule, electionModule, voterInfoModule))
        }
    }
}