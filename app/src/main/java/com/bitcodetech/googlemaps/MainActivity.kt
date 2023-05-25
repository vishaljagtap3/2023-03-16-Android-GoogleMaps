package com.bitcodetech.googlemaps

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity() {

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var map: GoogleMap
    private lateinit var markerPune: Marker
    private lateinit var markerMum: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment

        mapFragment.getMapAsync {
            map = it

            initSettings()
            addMarkers()
            setListeners()
        }

        /*mapFragment.getMapAsync(
            object : OnMapReadyCallback {
                override fun onMapReady(googleMap: GoogleMap) {
                    TODO("Not yet implemented")
                }
            }
        )*/

    }

    private fun setListeners() {
        map.setOnMarkerClickListener(
            object : GoogleMap.OnMarkerClickListener {
                override fun onMarkerClick(marker: Marker): Boolean {
                    mt("Marker: ${marker.title} clicked")
                    return false
                }
            }
        )

        map.setOnInfoWindowClickListener(
            object : GoogleMap.OnInfoWindowClickListener {
                override fun onInfoWindowClick(marker: Marker) {
                    mt("InfoWindow: ${marker.title}")
                }
            }
        )

        map.setOnMapClickListener(
            object : OnMapClickListener {
                override fun onMapClick(position: LatLng) {

                    map.addMarker(
                        MarkerOptions()
                            .position(position)
                            .title("Sample Marker")
                    )

                }
            }
        )
    }

    private fun mt(text : String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun addMarkers() {

        val puneBitmap = BitmapDescriptorFactory.fromResource(R.drawable.pune_icon)
        val markerOptionsPune = MarkerOptions()
        markerOptionsPune.position(LatLng(18.5204, 73.8567))
            .title("Pune")
            .snippet("This is Pune!")
            //.icon(puneBitmap)
            .draggable(true)
        markerPune = map.addMarker(markerOptionsPune)!!
        //markerPune.remove()

        val markerOptionsMum = MarkerOptions()
        val mumIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
        markerOptionsMum.position(LatLng(19.0760, 72.8777))
            .title("Mumbai")
            .snippet("This is Mumbai!")
            .rotation(45F)
            .icon(mumIcon)


        markerMum = map.addMarker(markerOptionsMum)!!


    }

    @SuppressLint("MissingPermission")
    private fun initSettings() {
        map.isMyLocationEnabled = true
        map.mapType = GoogleMap.MAP_TYPE_SATELLITE

        val uiSettings = map.uiSettings
        uiSettings.isCompassEnabled = true
        uiSettings.isZoomControlsEnabled = true
        uiSettings.isIndoorLevelPickerEnabled = true
        uiSettings.isRotateGesturesEnabled = true
        uiSettings.isScrollGesturesEnabled = true
        uiSettings.isZoomControlsEnabled = true
        uiSettings.isMapToolbarEnabled = true
        uiSettings.isZoomGesturesEnabled = true
        uiSettings.isIndoorLevelPickerEnabled = true
        uiSettings.isTiltGesturesEnabled = true
        uiSettings.isMyLocationButtonEnabled = true
        //uiSettings.setAllGesturesEnabled(true)
    }
}