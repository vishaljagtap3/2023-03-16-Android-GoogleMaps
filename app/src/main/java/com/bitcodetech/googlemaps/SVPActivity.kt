package com.bitcodetech.googlemaps

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback
import com.google.android.gms.maps.StreetViewPanorama
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment
import com.google.android.gms.maps.model.LatLng

class SVPActivity : AppCompatActivity() {

    private lateinit var svpFragment: SupportStreetViewPanoramaFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.svp_activity)

        svpFragment =
            supportFragmentManager.findFragmentById(R.id.svpFragment) as SupportStreetViewPanoramaFragment

        svpFragment.getStreetViewPanoramaAsync(
            object : OnStreetViewPanoramaReadyCallback {
                override fun onStreetViewPanoramaReady(svp : StreetViewPanorama) {
                    svp.isZoomGesturesEnabled = true
                    svp.isStreetNamesEnabled = true
                    svp.isPanningGesturesEnabled = true
                    svp.isUserNavigationEnabled = true
                    svp.setPosition(LatLng(40.7580, 73.9855))
                }
            }
        )
    }
}