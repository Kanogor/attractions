package ru.kanogor.attractions.presentation.mapsFragment

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import ru.kanogor.attractions.R
import ru.kanogor.attractions.databinding.FragmentMapsBinding

@AndroidEntryPoint
class MapsFragment : Fragment() {

    private var locationListener: LocationSource.OnLocationChangedListener? = null
    private var mMap: GoogleMap? = null
    private lateinit var fusedClient: FusedLocationProviderClient

    private var needAnimateCamera = false
    private var needMoveCamera = true
    private val handler = Handler(Looper.getMainLooper())
    private val cameraMoveRunnable = Runnable {
        needMoveCamera = true
    }

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MapsViewModel by viewModels()

    private val launcher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { map ->
        if (map.values.isEmpty() && map.values.all { it }) {
            viewModel.startLocation(
                map = mMap,
                fusedClient = fusedClient,
                locationCallBack = locationCallBack
            )
        }
    }

    private val locationCallBack: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let { location ->
                locationListener?.onLocationChanged(location)
                viewModel.update(location.longitude, location.latitude)
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    LatLng(location.latitude, location.longitude),
                    18f
                )
                if (needMoveCamera) {
                    if (needAnimateCamera) {
                        mMap?.animateCamera(cameraUpdate)
                    } else {
                        needAnimateCamera = true
                        mMap?.moveCamera(cameraUpdate)
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback())

        binding.mapOverlay.setOnTouchListener { _, _ ->
            handler.removeCallbacks(cameraMoveRunnable)
            needMoveCamera = false
            handler.postDelayed(cameraMoveRunnable, 8_000)
            false
        }

        fusedClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private fun callback() = OnMapReadyCallback { googleMap ->

        mMap = googleMap
        checkPermission()

        with(googleMap.uiSettings) {
            isMyLocationButtonEnabled = true
            isZoomControlsEnabled = true
        }
        googleMap.setLocationSource(object : LocationSource {
            override fun activate(p0: LocationSource.OnLocationChangedListener) {
                locationListener = p0
            }

            override fun deactivate() {
                locationListener = null
            }
        })

        viewModel.createMarker(
            googleMap = googleMap
        )

        googleMap.setOnMarkerClickListener { marker ->
            viewModel.onMarkerClickListener(marker, requireActivity().supportFragmentManager)
            true
        }

        googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
    }

    override fun onStart() {
        super.onStart()
        checkPermission()
    }

    override fun onStop() {
        super.onStop()
        fusedClient.removeLocationUpdates(locationCallBack)
        needAnimateCamera = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkPermission() {
        if (REQUIRED_PERMISSION.all { permission ->
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            }) viewModel.startLocation(
            map = mMap,
            fusedClient = fusedClient,
            locationCallBack = locationCallBack
        )
        else {
            launcher.launch(REQUIRED_PERMISSION)
        }
    }

    companion object {
        private val REQUIRED_PERMISSION: Array<String> = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}