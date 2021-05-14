package com.example.android.politicalpreparedness.representative.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_representative.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class RepresentativeFragment : Fragment() {

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }

    private val viewModel: RepresentativeViewModel by viewModel()
    private lateinit var binding: FragmentRepresentativeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_representative,
                container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        binding.spinnerStates.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.updateState(binding.spinnerStates.selectedItem as String)
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.updateState(binding.spinnerStates.selectedItem as String)
            }
        }

        val representativeListAdapter = RepresentativeListAdapter()
        rv_representations.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rv_representations.adapter = representativeListAdapter
        viewModel.representatives.observe(viewLifecycleOwner, Observer { representatives ->
            representativeListAdapter.submitList(representatives)
        })

        viewModel.findMyLocation.observe(viewLifecycleOwner, Observer {
            if (it) {
                retrieveMyLocation()
                viewModel.useMyLocationComplete()
            }
        })
    }

    private fun retrieveMyLocation() {
        if (isPermissionGranted()) {
            getLocation()
        } else {
            requestLocationPermission()
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        val address = geoCodeLocation(it)
                        viewModel.updateAddress(address)
                        selectSpinnerState(address)
                    }
                }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(
                            address.thoroughfare ?: "",
                            address.subThoroughfare ?: "",
                            address.locality ?: "",
                            address.adminArea ?: "",
                            address.postalCode ?: ""
                    )
                }
                .first()
    }

    private fun selectSpinnerState(address: Address) {
        val states = resources.getStringArray(R.array.states)
        binding.spinnerStates.setSelection(
                if (states.contains(address.state)) {
                    states.indexOf(address.state)
                } else {
                    0
                }
        )
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (isPermissionGranted(
                            permissions,
                            grantResults,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    )
            ) {
                retrieveMyLocation()
            }
        }
    }

    private fun isPermissionGranted(
            grantPermissions: Array<String>, grantResults: IntArray,
            permission: String
    ): Boolean {
        for (i in grantPermissions.indices) {
            if (permission == grantPermissions[i]) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED
            }
        }
        return false
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

}