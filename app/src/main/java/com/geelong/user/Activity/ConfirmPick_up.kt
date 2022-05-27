package com.geelong.user.Activity

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.geelong.user.API.APIUtils

import com.geelong.user.Fragment.ConfirmPickupFragment
import com.geelong.user.Fragment.HomeFragment
import com.geelong.user.R
import com.geelong.user.Response.DriverDetails_Vch_Response
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.Constants
import com.geelong.user.Util.FetchAddressServices
import com.geelong.user.Util.SharedPreferenceUtils
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragments_driver_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class ConfirmPick_up : AppCompatActivity() {
    var resultReceiver: ResultReceiver? = null
    var locat:String=""
    var lan:String=""
    var latii:String=""
    lateinit var  customprogress:Dialog
    var status="3"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_pick_up)
        customprogress= Dialog(this)
        customprogress.setContentView(R.layout.loader_layout)
        customprogress.show()

        SharedPreferenceUtils.getInstance(this)!!.setStringValue(ConstantUtils.Status,status)

        resultReceiver = AddressResultReceiver(Handler())


        if ((ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
                    != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            currentLocation
        }




      /*  val bundle = Bundle()
        bundle.putString("fragmentName", "Settings Fragment")
        val settingsFragment = ConfirmPickupFragment()
        settingsFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_main_content_id11, settingsFragment).commit()*/
    }
    fun inte()
    {
       /* val intent = Intent(this, Confirm::class.java)
        startActivity(intent)*/
        onBackPressed()
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.size > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                currentLocation
            } else {
                Toast.makeText(this, "Permission is denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private val currentLocation: Unit
        private get() {

            val locationRequest = LocationRequest()
            locationRequest.interval = 10000
            locationRequest.fastestInterval = 3000
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }
            LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        LocationServices.getFusedLocationProviderClient(applicationContext)
                            .removeLocationUpdates(this)
                        if(locationResult.locations !=null) {
                            if (locationResult.locations.size > 0) {
                                val latestlocIndex = locationResult.locations.size - 1
                                val lati = locationResult.locations[latestlocIndex].latitude
                                val longi = locationResult.locations[latestlocIndex].longitude

                                latii=lati.toString()
                                lan=longi.toString()
                               /* SharedPreferenceUtils.getInstance(this@ConfirmPick_up)?.setStringValue(
                                    ConstantUtils.LATITUDE,latii)
                                SharedPreferenceUtils.getInstance(this@ConfirmPick_up)?.setStringValue(
                                    ConstantUtils.LONGITUDE,lan)*/
                                val location = Location("providerNA")
                                location.longitude = longi
                                location.latitude = lati
                                fetchaddressfromlocation(location)
                            } else {
                            }
                        }
                    }
                }, Looper.getMainLooper())
        }

    private inner class AddressResultReceiver(handler: Handler?) : ResultReceiver(handler) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
            super.onReceiveResult(resultCode, resultData)
            if (resultCode == Constants.SUCCESS_RESULT) {
                var  address: String? =resultData.getString(Constants.ADDRESS)
                var  locaity: String? =resultData.getString(Constants.LOCAITY)
                var  state: String? =resultData.getString(Constants.STATE)
                var  district: String? =resultData.getString(Constants.DISTRICT)
                var  country: String? =resultData.getString(Constants.ADDRESS)
                locat=address+","+locaity+","+state
                val bundle = Bundle()
                bundle.putString("fragmentName", "Settings Fragment")
                bundle.putString("Location",locat)
                bundle.putString("Late",latii)
                bundle.putString("Long",lan)
                val settingsFragment = ConfirmPickupFragment()
                settingsFragment.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .replace(R.id.activity_main_content_id11, settingsFragment).commit()
                customprogress.hide()

                /* address!!.text = resultData.getString(Constants.ADDRESS)
                 locaity!!.text = resultData.getString(Constants.LOCAITY)
                 state!!.text = resultData.getString(Constants.STATE)
                 district!!.text = resultData.getString(Constants.DISTRICT)
                 country!!.text = resultData.getString(Constants.COUNTRY)
                 postcode!!.text = resultData.getString(Constants.POST_CODE)*/
            } else {
                Toast.makeText(
                    this@ConfirmPick_up,
                    resultData.getString(Constants.RESULT_DATA_KEY),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private fun fetchaddressfromlocation(location: Location) {
        val intent = Intent(this, FetchAddressServices::class.java)
        intent.putExtra(Constants.RECEVIER, resultReceiver)
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location)
        startService(intent)
    }

    companion object {
        private val LOCATION_PERMISSION_REQUEST_CODE = 1
    }


}