package com.bitcodetech.googlemaps

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.CancelableCallback
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MainActivity : AppCompatActivity() {

    private lateinit var polygon: Polygon
    private lateinit var circle: Circle
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
            setupCustomInfoWindow()
            addShapes()

            map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this,
                    R.raw.map_style
                )
            )

        }

        /*mapFragment.getMapAsync(
            object : OnMapReadyCallback {
                override fun onMapReady(googleMap: GoogleMap) {
                    TODO("Not yet implemented")
                }
            }
        )*/

    }

    private fun projection() {

        val latLng = map.projection.fromScreenLocation(Point(300, 300))
        mt("lat lng at 300, 300 is ${latLng.toString()}")

        val point = map.projection.toScreenLocation(latLng)
        mt("scr loc: ${point.x} ${point.y}")

    }

    private fun addShapes() {

        val circleOptions = CircleOptions()
        circleOptions.center(markerPune.position)
        circleOptions.radius(4000.0)
        circleOptions.strokeColor(Color.BLUE)
        circleOptions.fillColor(
            Color.argb(80, 255, 0, 0)
        )
        circle = map.addCircle(circleOptions)

        val polygonOptions = PolygonOptions()
        polygonOptions.strokeColor(Color.BLUE)
        polygonOptions.fillColor(Color.argb(80, 0, 0, 255))
        polygonOptions.add(LatLng(21.1458, 79.0882))
        polygonOptions.add(LatLng(22.7196, 75.8577))
        polygonOptions.add(LatLng( 22.5726, 88.3639 ))
        val list = ArrayList<LatLng>()
        list.add(LatLng(21.1904, 81.2849))
        polygonOptions.addHole(list)

        polygon = map.addPolygon(polygonOptions)


    }

    private fun setupCustomInfoWindow() {
        map.setInfoWindowAdapter(MyInfoWindowAdapter())
    }

    private inner class MyInfoWindowAdapter : InfoWindowAdapter {
        override fun getInfoContents(marker: Marker): View? {
            return null
        }

        override fun getInfoWindow(marker : Marker): View? {
            val view = LayoutInflater.from(this@MainActivity)
                .inflate(R.layout.info_window_view, null)

            view.findViewById<ImageView>(R.id.imgInfoWindow)
                .setImageResource(R.drawable.pune_icon)
            view.findViewById<TextView>(R.id.txtInfoWindow).text = marker.title

            return view
        }

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
                    projection()
                    moveMap()
                    map.addMarker(
                        MarkerOptions()
                            .position(position)
                            .title("Sample Marker")
                    )

                }
            }
        )

        map.setOnMarkerDragListener(
            object : OnMarkerDragListener {
                override fun onMarkerDragStart(marker: Marker) {
                    mt("Drag Start ${marker.position}")
                }

                override fun onMarkerDrag(marker: Marker) {
                    mt("Drag: ${marker.position}")
                }

                override fun onMarkerDragEnd(marker: Marker) {
                    mt("Drag End: ${marker.position}")
                }
            }
        )
    }

    private fun moveMap() {

        /*val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
            markerPune.position,
            18F
        )

        //map.moveCamera(cameraUpdate)
        //map.animateCamera(cameraUpdate)
        map.animateCamera(
            cameraUpdate,
            5000,
            object : CancelableCallback {
                override fun onCancel() {
                   mt("Animation cancelled")
                }

                override fun onFinish() {
                    mt("Animation finished")
                }
            }
        )*/

        val cameraUpdate= CameraUpdateFactory.newCameraPosition(
            CameraPosition(
                markerPune.position,
                20F,
                90F,
                65F
            )
        )

        map.animateCamera(
            cameraUpdate,
            5000,
            object : CancelableCallback {
                override fun onCancel() {
                    mt("Animation cancelled")
                }

                override fun onFinish() {
                    mt("Animation finished")
                }
            }
        )

    }

    private fun mt(text : String) {
        Log.e("tag", text)
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
        //map.mapType = GoogleMap.MAP_TYPE_SATELLITE

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