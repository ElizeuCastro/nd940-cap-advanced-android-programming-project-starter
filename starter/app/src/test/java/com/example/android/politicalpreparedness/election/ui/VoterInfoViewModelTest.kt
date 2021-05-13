package com.example.android.politicalpreparedness.election.ui

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.politicalpreparedness.election.dto.VoterInfoDTO
import com.example.android.politicalpreparedness.election.repository.FakeRepository
import com.example.android.politicalpreparedness.getOrAwaitValue
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config
import java.util.*

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.P])
class VoterInfoViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: FakeRepository
    private lateinit var viewModel: VoterInfoViewModel

    @Before()
    fun setup() {
        stopKoin()
        repository = FakeRepository()
    }

    @Test
    fun checkIsFollowing_showNotFollowing() {
        // Give saved election is empty
        repository.savedElections = mutableListOf()

        // When init view model and check is following
        viewModel = VoterInfoViewModel(repository, Election(
                1,
                "Test",
                Date(),
                Division(1.toString(), "us", "os")
        ))

        // Then following show be false
        assertThat(viewModel.isFollow.getOrAwaitValue(), `is`(false))
    }

    @Test
    fun checkIsFollowing_showFollowing() {
        // Give a saved election
        repository.savedElections = mutableListOf(Election(
                1,
                "Test",
                Date(),
                Division(1.toString(), "us", "os")
        ))

        // When init view model check is following
        viewModel = VoterInfoViewModel(repository, Election(
                1,
                "Test",
                Date(),
                Division(1.toString(), "us", "os")
        ))

        // Then following show be true
        assertThat(viewModel.isFollow.getOrAwaitValue(), `is`(true))
    }

    @Test
    fun fetchVoterInfo_showData() {
        // Give a saved election
        repository.savedElections = mutableListOf(Election(
                1,
                "Test",
                Date(),
                Division(1.toString(), "us", "os")
        ))
        repository.voterInfoDTO = VoterInfoDTO(
                name = "voterInfo",
                electionInfoUrl = "http://electionInfoUrl",
                votingLocationFinderUrl = "http://electionInfoUrl",
                ballotInfoUrl = "http://electionInfoUrl",
                correspondenceAddress = "voter info"
        )

        // When init view model fetch voter info
        viewModel = VoterInfoViewModel(repository, Election(
                1,
                "Test",
                Date(),
                Division(1.toString(), "us", "os")
        ))

        // Then voter info should be filled
        val voterInfo = viewModel.voterInfo.getOrAwaitValue()
        assertThat(voterInfo.name, `is`(repository.voterInfoDTO.name))
        assertThat(voterInfo.electionInfoUrl, `is`(repository.voterInfoDTO.electionInfoUrl))
        assertThat(voterInfo.votingLocationFinderUrl, `is`(repository.voterInfoDTO.votingLocationFinderUrl))
        assertThat(voterInfo.ballotInfoUrl, `is`(repository.voterInfoDTO.ballotInfoUrl))
        assertThat(voterInfo.correspondenceAddress, `is`(repository.voterInfoDTO.correspondenceAddress))

    }

    @Test
    fun fetchVoterInfo_showToastVoterInformationError() {
        // Give a server error
        repository.setReturnError(true)

        // When init view model fetch voter info
        viewModel = VoterInfoViewModel(repository, Election(
                1,
                "Test",
                Date(),
                Division(1.toString(), "us", "os")
        ))

        // Then a error toast is shown and voter info is filled with empty value
        val voterInfo = viewModel.voterInfo.getOrAwaitValue()
        assertThat(voterInfo.name, isEmptyString())
        assertThat(voterInfo.electionInfoUrl, isEmptyString())
        assertThat(voterInfo.votingLocationFinderUrl, isEmptyString())
        assertThat(voterInfo.ballotInfoUrl, isEmptyString())
        assertThat(voterInfo.correspondenceAddress, isEmptyString())
        assertThat(viewModel.showToastVoterInfoError.getOrAwaitValue(), `is`(true))

    }

    @Test
    fun toggle_savedElection() = runBlockingTest {
        // Given a election
        val election = Election(
                1,
                "Test",
                Date(),
                Division(1.toString(), "us", "os")
        )
        viewModel = VoterInfoViewModel(repository, Election(
                1,
                "Test",
                Date(),
                Division(1.toString(), "us", "os")
        ))

        // When toggle action
        viewModel.toggle(election)

        // Then isFollow is true and election was saved
        assertThat(repository.getElection(election.id), `is`(election))
        assertThat(viewModel.isFollow.getOrAwaitValue(), `is`(true))

    }

    @Test
    fun toggle_removeElection() = runBlockingTest {
        // Give a saved election
        val election = Election(
                1,
                "Test",
                Date(),
                Division(1.toString(), "us", "os")
        )
        repository.savedElections = mutableListOf(election)
        viewModel = VoterInfoViewModel(repository, Election(
                1,
                "Test",
                Date(),
                Division(1.toString(), "us", "os")
        ))

        // When toggle action
        viewModel.toggle(election)

        // Then isFollow is false and election was removed
        assertThat(repository.getElection(election.id), nullValue())
        assertThat(viewModel.isFollow.getOrAwaitValue(), `is`(false))

    }

    @Test
    fun onURLClick_navigateToWebsite() {
        // Give some URL
        val url = "http://url"
        viewModel = VoterInfoViewModel(repository, Election(
                1,
                "Test",
                Date(),
                Division(1.toString(), "us", "os")
        ))

        // When click on any URL
        viewModel.onURLClick(url)

        // Then the website should be shown
        assertThat(viewModel.url.getOrAwaitValue(), `is`(url))
    }


}