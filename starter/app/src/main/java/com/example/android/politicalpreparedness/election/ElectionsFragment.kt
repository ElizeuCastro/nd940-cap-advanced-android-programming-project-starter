package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import kotlinx.android.synthetic.main.fragment_election.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ElectionsFragment : Fragment() {

    private val viewModel: ElectionsViewModel by viewModel()
    private lateinit var binding: FragmentElectionBinding
    private lateinit var electionsAdapter: ElectionListAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
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

        electionsAdapter = ElectionListAdapter(getString(R.string.upcoming_election_header))
        rv_upcoming_election.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rv_upcoming_election.adapter = electionsAdapter

        viewModel.upcomingElections.observe(viewLifecycleOwner, Observer { elections ->
            elections?.let {
                electionsAdapter.updateData(elections)
            }
        })
    }

}