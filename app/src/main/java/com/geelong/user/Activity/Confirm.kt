package com.geelong.user.Activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.geelong.user.API.APIUtils
import com.geelong.user.Fragment.ConfirmFragment
import com.geelong.user.R
import com.geelong.user.Response.MapData
import com.geelong.user.Response.Vechail_detailsResponse
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.NetworkUtils
import com.geelong.user.Util.SharedPreferenceUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_confirm.*
import kotlinx.android.synthetic.main.fragment_confirm.*
import kotlinx.android.synthetic.main.fragment_confirm.confirm_trip_linearlayout
import kotlinx.android.synthetic.main.fragment_confirm.driver_img_confirm
import kotlinx.android.synthetic.main.fragment_confirm.driver_name1
import kotlinx.android.synthetic.main.fragment_confirm.no_driver_found_txtview
import kotlinx.android.synthetic.main.fragment_confirm.total_fare
import kotlinx.android.synthetic.main.fragment_confirm.vch_name
import kotlinx.android.synthetic.main.fragment_confirm.vch_number
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Confirm : AppCompatActivity() , OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var originLatitude: String = ""
    var originLongitude: String = ""
    var img_url: String = ""
    var distance: String = ""
    var destinationLatitude: String = ""
    var destinationLongitude: String = ""
    lateinit var customprogress:Dialog
     var toatal_time_taken:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)
        supportActionBar?.hide()

        customprogress= Dialog(this)
        customprogress.setContentView(R.layout.loader_layout)

        originLatitude = SharedPreferenceUtils.getInstance(this)
            ?.getStringValue(ConstantUtils.LATITUDE, "").toString()
        originLongitude = SharedPreferenceUtils.getInstance(this)
            ?.getStringValue(ConstantUtils.LONGITUDE, "").toString()
        distance = SharedPreferenceUtils.getInstance(this)
            ?.getStringValue(ConstantUtils.Distance, "").toString()
        destinationLatitude = SharedPreferenceUtils.getInstance(this)
            ?.getStringValue(ConstantUtils.Lati_Drop, "").toString()
        destinationLongitude = SharedPreferenceUtils.getInstance(this)
            ?.getStringValue(ConstantUtils.Longi_Drop, "").toString()

        if(NetworkUtils.checkInternetConnection(this))
        {
            vehlist()
        }

        back_linera_layout_act.setOnClickListener {
            onBackPressed()
        }

        pick_up_confirm_act.setOnClickListener {

            val intent=Intent(this,ConfirmPick_up::class.java)
            startActivity(intent)
        }

        val ai: ApplicationInfo = applicationContext.packageManager
            .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)

        val value=getString(R.string.api_key)
        val apiKey = value.toString()


        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

            mapFragment.getMapAsync {
                mMap = it
                val originLocation = LatLng(originLatitude.toDouble(), originLongitude.toDouble())
                mMap.addMarker(MarkerOptions().position(originLocation))
                val destinationLocation = LatLng(destinationLatitude.toDouble(), destinationLongitude.toDouble())
                mMap.addMarker(MarkerOptions().position(destinationLocation))
                val urll = getDirectionURL(originLocation, destinationLocation, apiKey)
                GetDirection(urll).execute()
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 14F))
            }

    }


    override fun onMapReady(p0: GoogleMap) {
        mMap = p0!!
        val originLocation = LatLng(originLatitude.toDouble(), originLongitude.toDouble())
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(originLocation))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 18F))
    }
    private fun getDirectionURL(origin:LatLng, dest:LatLng, secret: String) : String{
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
                "&destination=${dest.latitude},${dest.longitude}" +
                "&sensor=false" +
                "&mode=driving" +
                "&key=$secret"
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetDirection(val url : String) : AsyncTask<Void, Void, List<List<LatLng>>>(){
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body!!.string()

            val result =  ArrayList<List<LatLng>>()
            try{
                val respObj = Gson().fromJson(data, MapData::class.java)
                val path =  ArrayList<LatLng>()
                for (i in 0 until respObj.routes[0].legs[0].steps.size){
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
            }catch (e:Exception){
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(result: List<List<LatLng>>) {
            val lineoption = PolylineOptions()
            for (i in result.indices){
                lineoption.addAll(result[i])
                lineoption.width(15f)
                lineoption.color(Color.BLACK)
                lineoption.geodesic(true)
            }
            mMap.addPolyline(lineoption)
        }
    }

    fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val latLng = LatLng((lat.toDouble() / 1E5),(lng.toDouble() / 1E5))
            poly.add(latLng)
        }
        return poly
    }

    fun vehlist() {
        Totaltimetaken(distance.toDouble())
        customprogress.show()
        val request = HashMap<String, String>()
        request.put("latitude", originLatitude)
        request.put("longitude", originLongitude)
        request.put("distance", distance)


        var veh_list: Call<Vechail_detailsResponse> =
            APIUtils.getServiceAPI()!!.vech_details(request)

        veh_list.enqueue(object : Callback<Vechail_detailsResponse> {
            override fun onResponse(
                call: Call<Vechail_detailsResponse>,
                response: Response<Vechail_detailsResponse>
            ) {
                try {


                    if (response.body()!!.success.equals("true")) {


                        Log.d("response", response.body().toString())
                        img_url = response.body()!!.data[0].image
                        driver_name1_act.setText(response.body()!!.data[0].name)
                        vch_name_act.setText(response.body()!!.data[0].vehicle_name)
                        vch_number_act.setText(response.body()!!.data[0].vehicle_no)
                        total_fare_act.setText("₹" + response.body()!!.data[0].amount)

                        toatl_distance_trip_act.setText(distance)

                        if (img_url.isEmpty()) {
                            var pica = Picasso.get()
                            pica.load(R.drawable.profilepic).into(driver_img_confirm_act)
                        } else {
                            var pica = Picasso.get()
                            pica.load(img_url).into(driver_img_confirm_act)
                        }



                        SharedPreferenceUtils.getInstance(this@Confirm)?.setStringValue(
                            ConstantUtils.Driver_Id, response.body()!!.data[0].driver_id
                        )
                        SharedPreferenceUtils.getInstance(this@Confirm)
                            ?.setStringValue(ConstantUtils.Amount, response.body()!!.data[0].amount)


                        customprogress.hide()

                    } else {

                        Toast.makeText(
                            this@Confirm, response.body()!!.msg.toString(), Toast
                                .LENGTH_LONG
                        )
                            .show()
                        confirm_trip_linearlayout_act.visibility = View.GONE
                        no_driver_found_txtview1_act.visibility = View.VISIBLE
                        customprogress.hide()
                    }

                } catch (e: Exception) {
                    Log.e("saurav", e.toString())

                    Toast.makeText(this@Confirm, e.message, Toast.LENGTH_LONG).show()
                    customprogress.hide()

                }

            }

            override fun onFailure(call: Call<Vechail_detailsResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                confirm_trip_linearlayout_act.visibility = View.GONE
                no_driver_found_txtview1_act.visibility = View.VISIBLE

                customprogress.hide()

            }

        })
    }

    fun Totaltimetaken(distance_km: Double) {


        val km = distance_km.toInt()
        val kms_per_min = 0.4
        val mins_taken = km / kms_per_min
        val totalMinutes = mins_taken.toInt()
        if (totalMinutes < 60) {

            toatal_time_taken = totalMinutes.toString() + " " + "Mins"
            SharedPreferenceUtils.getInstance(this)!!.setStringValue(
                ConstantUtils
                    .Toatal_time, toatal_time_taken
            )
            total_time_trip_act.text = toatal_time_taken


        } else {
            var minutes = Integer.toString(totalMinutes % 60)
            minutes = if (minutes.length == 1) "0$minutes" else minutes
            (totalMinutes / 60).toString() + " hour " + minutes + "mins"
            toatal_time_taken = minutes.toString()
            SharedPreferenceUtils.getInstance(this)!!.setStringValue(
                ConstantUtils
                    .Toatal_time, toatal_time_taken
            )
            total_time_trip_act.text = toatal_time_taken

        }


    }


    /* fun Totaltimetaken(distance_km: Double) {


         val km = distance_km.toInt()
         val kms_per_min = 0.5
         val mins_taken = km / kms_per_min
         val totalMinutes = mins_taken.toInt()
         if (totalMinutes < 60) {

             toatal_time_taken = totalMinutes.toString() + " " + "Mins"
             SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                 ConstantUtils
                     .Toatal_time, toatal_time_taken
             )
             toatal_time_txtview.text = toatal_time_taken


         } else {
             var minutes = Integer.toString(totalMinutes % 60)
             minutes = if (minutes.length == 1) "0$minutes" else minutes
             (totalMinutes / 60).toString() + " hour " + minutes + "mins"
             toatal_time_taken = minutes.toString()
             SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                 ConstantUtils
                     .Toatal_time, toatal_time_taken
             )
             toatal_time_txtview.text = toatal_time_taken

         }


     }*/


}



        /*customprogress= Dialog(this)
        customprogress.setContentView(R.layout.loader_layout)
        customprogress.show()

        *//* if ((ContextCompat.checkSelfPermission(
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
           }*//*


        val bundle = Bundle()
        bundle.putString("fragmentName", "Settings Fragment")
        val settingsFragment = ConfirmFragment()
        settingsFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_main_content_id1, settingsFragment).commit()
                customprogress.hide()
    }
    fun inte()
    {
        onBackPressed()
    }*/
