package com.mikemagss.movingmapmarker

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener {

    private lateinit var mMap: GoogleMap

    override fun onCameraIdle() {
        marker.visibility = View.GONE

        // convert vector asset to bitmap
        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_pin_drop_black_24dp)
        drawable.setTint(ContextCompat.getColor(this, R.color.colorPrimary))
        val size = drawable.intrinsicHeight
        drawable.setBounds(0, 0, size, size)
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.draw(canvas)

        val markerOptions = MarkerOptions().position(mMap.cameraPosition.target)
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
        mMap.addMarker(markerOptions)
    }

    override fun onCameraMove() {
        mMap.clear()
        marker.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap.apply {
            val sf = LatLng(37.7576948, -122.4726192)
            addMarker(MarkerOptions().apply {
                position(sf)
                title("Marker pointed at SF")
                draggable(false)
            })

            animateCamera(CameraUpdateFactory.newLatLngZoom(sf, 18f))

            setOnCameraMoveListener(this@MainActivity)
            setOnCameraIdleListener(this@MainActivity)
        }
    }

}
