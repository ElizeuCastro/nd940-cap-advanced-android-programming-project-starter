package com.example.android.politicalpreparedness.election.dto

data class VoterInfoDTO(
        val name: String = "",
        val electionInfoUrl: String = "",
        val votingLocationFinderUrl: String = "",
        val ballotInfoUrl: String = "",
        val correspondenceAddress: String = ""
)