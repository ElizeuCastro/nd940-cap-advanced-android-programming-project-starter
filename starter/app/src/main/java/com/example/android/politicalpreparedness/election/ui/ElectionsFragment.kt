package com.example.android.politicalpreparedness.election.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.android.synthetic.main.fragment_election.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ElectionsFragment : Fragment() {

    private val viewModel: ElectionsViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentElectionBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_election,
                container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val electionsAdapter = ElectionListAdapter(getString(R.string.upcoming_election_header)) {
            viewModel.displayVoterInfo(it)
        }
        rv_upcoming_election.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rv_upcoming_election.adapter = electionsAdapter
        viewModel.upcomingElections.observe(viewLifecycleOwner, Observer { elections ->
            elections?.let {
                electionsAdapter.updateData(elections)
            }
        })

        val savedElectionsAdapter = ElectionListAdapter(getString(R.string.saved_election_header)) {
            viewModel.displayVoterInfo(it)
        }
        rv_saved_election.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rv_saved_election.adapter = savedElectionsAdapter
        viewModel.savedElections.observe(viewLifecycleOwner, Observer { elections ->
            elections?.let {
                savedElectionsAdapter.updateData(elections)
            }
        })

        viewModel.showUpcomingElectionError.observe(viewLifecycleOwner, Observer {
            if (it) {
                showUpcomingElectionError()
            }

        })

        viewModel.navigateToVoterInfo.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                openVoterInfoFragment(it)
                viewModel.displayVoterInfoComplete()
            }
        })
    }

    private fun openVoterInfoFragment(election: Election) {
        findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election)
        )
    }

    private fun showUpcomingElectionError() {
        Toast.makeText(context, R.string.upcoming_election_error, Toast.LENGTH_LONG).show()
    }

}