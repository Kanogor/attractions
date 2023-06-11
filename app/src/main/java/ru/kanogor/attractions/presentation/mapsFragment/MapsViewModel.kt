package ru.kanogor.attractions.presentation.mapsFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kanogor.attractions.data.opentripmap.AttractionRepository
import ru.kanogor.attractions.entity.Features
import javax.inject.Inject

private const val ARG_TITLE = "argTitleDialog"
private const val ARG_INFO = "argInfoDialog"

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val attractionRepository: AttractionRepository
) : ViewModel() {

    private val _featuresResult = MutableStateFlow<List<Features>>(emptyList())
    private val featuresResult = _featuresResult.asStateFlow()

    fun update(longitude: Double, latitude: Double) {
        viewModelScope.launch {
            val searchAttractions =
                attractionRepository.getAttractions(3000, longitude, latitude).features
            _featuresResult.value = searchAttractions
        }
    }

    @SuppressLint("MissingPermission")
    fun startLocation(
        map: GoogleMap?,
        fusedClient: FusedLocationProviderClient,
        locationCallBack: LocationCallback
    ) {
        map?.isMyLocationEnabled = true
        val request =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1_000).build()

        fusedClient.requestLocationUpdates(
            request,
            locationCallBack,
            Looper.getMainLooper()
        )
    }

    fun createMarker(googleMap: GoogleMap) {
        viewModelScope.launch {
            featuresResult.collect { feature ->
                when (feature) {
                    emptyList<List<Features>>() -> {
                        Log.d("AAA", "Empty list")
                    }
                    else -> {
                        for (i in feature.indices) {
                            googleMap.addMarker(
                                MarkerOptions()
                                    .icon(
                                        BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_AZURE
                                        )
                                    )
                                    .position(
                                        LatLng(
                                            feature[i].geometry.coordinates[1],
                                            feature[i].geometry.coordinates[0]
                                        )
                                    )
                                    .title(feature[i].properties.xid)
                            )
                        }
                    }
                }
            }
        }
    }

    fun onMarkerClickListener(marker: Marker, manager: FragmentManager) {
        viewModelScope.launch {
            val searchInfo =
                attractionRepository.getSingleAttraction(marker.title!!)
            val title = searchInfo.name
            val info = if (searchInfo.wikipedia_extracts == null) {
                "No info"
            } else {
                searchInfo.wikipedia_extracts!!.text
            }
            val bundle = Bundle()
            bundle.putString(ARG_TITLE, title)
            bundle.putString(ARG_INFO, info)
            val dialog = MarkerDialog()
            dialog.arguments = bundle
            dialog.show(manager, "dialog")
        }
    }
}