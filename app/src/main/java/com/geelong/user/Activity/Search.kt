package com.geelong.user.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.geelong.user.Fragment.HomeFragment
import com.geelong.user.R
import com.google.android.gms.maps.GoogleMap


class Search :AppCompatActivity() {


    private var mMap: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.hide()
        val bundle = Bundle()
        bundle.putString("fragmentName", "Settings Fragment")
        val settingsFragment = HomeFragment()
        settingsFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_main_content_id, settingsFragment).commit()

    }
    /*fun mtt()
    {

        val mapFrag=supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFrag.getMapAsync(this)
    }
        override fun onMapReady(googleMap: GoogleMap) {
            mMap = googleMap

            // Add a marker in Sydney and move the camera
            val sydney = LatLng(34.0, 151.0)
            mMap!!.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        }
        */

    }
