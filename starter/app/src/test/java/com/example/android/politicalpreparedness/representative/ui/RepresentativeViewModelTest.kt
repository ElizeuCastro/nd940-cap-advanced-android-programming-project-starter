package com.example.android.politicalpreparedness.representative.ui

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.politicalpreparedness.MainCoroutineRule
import com.example.android.politicalpreparedness.election.repository.FakeRepository
import com.example.android.politicalpreparedness.election.ui.ElectionsViewModel
import com.example.android.politicalpreparedness.getOrAwaitValue
import com.example.android.politicalpreparedness.network.models.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.P])
class RepresentativeViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: RepresentativeViewModel
    private lateinit var repository: FakeRepository

    @Before
    fun setup() {
        stopKoin()
        repository = FakeRepository()
        viewModel = RepresentativeViewModel(repository)
        repository.representativeResponse = RepresentativeResponse(
                offices = listOf(
                        Office(
                                name = "President of the United States",
                                division = Division("ocd-division/country:us/state:ca", "us", "ca"),
                                officials = listOf(0)
                        )
                ),
                officials = listOf(
                        Official(
                                address = listOf(
                                        Address("1600 Pennsylvania Avenue Northwest", null, "Washington", "DC", "20500")
                                ),
                                channels = listOf(
                                        Channel("Twitter", "potus")
                                ),
                                name = "Joseph R. Biden",
                                party = "Democratic Party",
                                phones = listOf("(202) 456-1111"),
                                photoUrl = null,
                                urls = listOf("https://www.whitehouse.gov/")
                        )
                )
        )
    }

    @Test
    fun findMyRepresentativesClick_withData() {
        // Given some address
        viewModel.addressLine1.value = "Amphitheatre Parkway"
        viewModel.addressLine2.value = "1600"
        viewModel.city.value = "Mountain View"
        viewModel.state.value = "California"
        viewModel.zip.value = "94043"

        // When find representatives
        viewModel.findMyRepresentativesClick()

        // Then should see 1 representatives
        val representatives = viewModel.representatives.getOrAwaitValue()
        assertThat(representatives.size, (`is`(1)))
        assertThat(representatives[0].office.name, (`is`("President of the United States")))
        assertThat(representatives[0].official.name, (`is`("Joseph R. Biden")))
    }

    @Test
    fun findMyRepresentativesClick_returnError() {
        // Given some address
        viewModel.addressLine1.value = "Amphitheatre Parkway"
        viewModel.addressLine2.value = "1600"
        viewModel.city.value = "Mountain View"
        viewModel.state.value = "California"
        viewModel.zip.value = "94043"
        repository.setReturnError(true)

        // When find representatives
        viewModel.findMyRepresentativesClick()

        // Then should an empty list
        val representatives = viewModel.representatives.getOrAwaitValue()
        assertThat(representatives.size, (`is`(0)))
    }

    @Test
    fun findMyRepresentativesClick_CheckLoading() = mainCoroutineRule.runBlockingTest {

        // Given some address
        viewModel.addressLine1.value = "Amphitheatre Parkway"
        viewModel.addressLine2.value = "1600"
        viewModel.city.value = "Mountain View"
        viewModel.state.value = "California"
        viewModel.zip.value = "94043"

        // Pause dispatcher
        mainCoroutineRule.pauseDispatcher()

        // When find representatives
        viewModel.findMyRepresentativesClick()

        // Then - show loading
        assertThat(viewModel.showLoading.getOrAwaitValue(), (`is`(true)))

        // Execute pending coroutines
        mainCoroutineRule.resumeDispatcher()

        // Then - hide loading
        assertThat(viewModel.showLoading.getOrAwaitValue(), (`is`(false)))

    }

    @Test
    fun useMyLocationClick() {
        // When click user my location
        viewModel.useMyLocationClick()

        // Then try get location
        assertThat(viewModel.findMyLocation.getOrAwaitValue(), `is`(true))
    }

    @Test
    fun useMyLocationComplete() {
        // When click user my location
        viewModel.useMyLocationComplete()

        // Then find my location should be complete
        assertThat(viewModel.findMyLocation.getOrAwaitValue(), `is`(false))
    }

    @Test
    fun updateState() {
        // When trigger to update stated selected
        viewModel.updateState("California")

        // Then state should be updated correctly
        assertThat(viewModel.state.value, `is`("California"))
    }

    @Test
    fun updateAddress() {
        // Give some address
        val address = Address(
                "Amphitheatre Parkway",
                "1600",
                "Mountain View",
                "California",
                "94043"
        )

        // When trigger to update state from geo location
        viewModel.updateAddress(address)

        // Then address should be updated correctly
        assertThat(viewModel.addressLine1.value, `is`("Amphitheatre Parkway"))
        assertThat(viewModel.addressLine2.value, `is`("1600"))
        assertThat(viewModel.city.value, `is`("Mountain View"))
        assertThat(viewModel.state.value, `is`("California"))
        assertThat(viewModel.zip.value, `is`("94043"))
    }

}