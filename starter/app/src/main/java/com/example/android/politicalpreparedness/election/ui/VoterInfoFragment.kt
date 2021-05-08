package com.example.android.politicalpreparedness.election.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class VoterInfoFragment : Fragment() {

    private lateinit var binding: FragmentVoterInfoBinding
    private val args: VoterInfoFragmentArgs by navArgs()
    private val viewModel: VoterInfoViewModel by viewModel {
        parametersOf(args.argElection)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_voter_info,
                container,
                false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.showToastVoterInfoError.observe(viewLifecycleOwner, Observer {
            if (it) {
                showVoterInformationError()
            }

        })

        viewModel.url.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                openURL(it)
            }
        })

        return binding.root
    }

    private fun openURL(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun showVoterInformationError() {
        Toast.makeText(context, R.string.voter_information_error, Toast.LENGTH_LONG).show()
    }
}