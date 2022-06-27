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
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.geelong.user.API.APIUtils
import com.geelong.user.Fragment.ConfirmFragment
import com.geelong.user.R
import com.geelong.user.Response.*
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
import kotlinx.android.synthetic.main.activity_driver_details.*
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
import java.util.HashMap

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
    var booking_id:String=""
    var amount:String=""
    var approx_km:String=""
    var status="2"
    var count=0
    var cmt=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)

        confirm_linear.visibility=View.GONE
        progress_linear.visibility=View.VISIBLE

        SharedPreferenceUtils.getInstance(this)!!.setStringValue(ConstantUtils.Status,status)


        customprogress= Dialog(this)
        customprogress.setContentView(R.layout.dialog_progress)

        approx_km=SharedPreferenceUtils.getInstance(this)
            ?.getStringValue(ConstantUtils.Distance, "").toString()
        amount=SharedPreferenceUtils.getInstance(this)
            ?.getStringValue(ConstantUtils.Amount, "").toString()
        booking_id=SharedPreferenceUtils.getInstance(this)
            ?.getStringValue(ConstantUtils.Booking_id, "").toString()

        originLatitude = SharedPreferenceUtils.getInstance(this)
            ?.getStringValue(ConstantUtils.Pick_up_Latitude, "").toString()
        originLongitude = SharedPreferenceUtils.getInstance(this)
            ?.getStringValue(ConstantUtils.Pick_up_longitude, "").toString()
        distance = SharedPreferenceUtils.getInstance(this)
            ?.getStringValue(ConstantUtils.Distance, "").toString()
        destinationLatitude = SharedPreferenceUtils.getInstance(this)
            ?.getStringValue(ConstantUtils.Latitude_Drop, "").toString()
        destinationLongitude = SharedPreferenceUtils.getInstance(this)
            ?.getStringValue(ConstantUtils.Longitude_Drop, "").toString()

        if(NetworkUtils.checkInternetConnection(this))
        {
           Booking_status()
        }
        cancel_requestt_btn.setOnClickListener {
            val intent=Intent(this,Search1::class.java)
            SharedPreferenceUtils.getInstance(this)!!.removeKey(ConstantUtils.Booking_id)
            SharedPreferenceUtils.getInstance(this)!!.removeKey(ConstantUtils.Distance)
            SharedPreferenceUtils.getInstance(this)!!.removeKey(ConstantUtils.Toatal_time)
            startActivity(intent)
            finish()
        }

        back_linera_layout_act.setOnClickListener {
            onBackPressed()
            finish()
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
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 10F))
            }
       /* var handler: Handler? = null
        handler = Handler()
       handler.postDelayed(Runnable {
           cmt++
            CancelTripSubmit()
           finish()
           

        },400000L)
        if(cmt==1)
        {
            handler.removeCallbacksAndMessages(null)
        }
*/

    }


    override fun onMapReady(p0: GoogleMap) {

        try {
            mMap = p0!!
            val originLocation = LatLng(originLatitude.toDouble(), originLongitude.toDouble())
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(originLocation))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 5F))
        }catch (e:Exception){

        }

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

    fun Booking_status()

    {


        val request = HashMap<String, String>()
        request.put("booking_id",booking_id)

        var driver_vec_details: Call<BookingStatusResponse> = APIUtils.getServiceAPI()!!.booking_status_first(request)

        driver_vec_details.enqueue(object : Callback<BookingStatusResponse> {
            override fun onResponse(call: Call<BookingStatusResponse>, response: Response<BookingStatusResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {

                        if (response.body()!!.data[0].status.equals("0"))

                        {
                            customprogress.hide()
                            //cardview_driverDetails.visibility=View.GONE


                            var handler: Handler? = null
                            handler = Handler()
                            handler.postDelayed(Runnable {
                                Booking_status()
                                count++

                            }, 5000)

                            if(count==15)
                            {
                                //Toast.makeText(this@Confirm,count.toString(),Toast.LENGTH_LONG)
                                //.show()
                                handler.removeCallbacksAndMessages(null)
                            }


                            // SharedPreferenceUtils.getInstance(requireContext())?.removeKey
                          //  (ConstantUtils.Booking_id)
                            /*val intent=Intent(requireContext(),Search1::class.java)
                            startActivity(intent)*/

                        }
                        else

                        {
                            confirm_linear.visibility=View.VISIBLE
                            progress_linear.visibility=View.GONE
                           // cardview_driverDetails.visibility=View.VISIBLE
                            customprogress.hide()
                            var otp=response.body()!!.data[0].otp
                            var driver_profile_pic=response.body()!!.data[0].profile_photo
                            var vechile_img=response.body()!!.data[0].vehicle_image
                            var vehicle_no=response.body()!!.data[0].vehicle_no.toString()
                            var name=response.body()!!.data[0].name
                            SharedPreferenceUtils.getInstance(this@Confirm)!!.
                            setStringValue(ConstantUtils.Driver_Id,response.body()!!.data[0].id)
                            var vehicle_name=response.body()!!.data[0].vehicle_name.toString()
                            var rating=response.body()!!.data[0].rating
                            SharedPreferenceUtils.getInstance(this@Confirm)!!.
                            setStringValue(ConstantUtils.Driver_latitude,response.body()!!
                                .data[0].latitude)
                            SharedPreferenceUtils.getInstance(this@Confirm)!!.
                            setStringValue(ConstantUtils.Driver_longitude,response.body()!!
                                .data[0].longitude)

                    try {
                        val picasso = Picasso.get()
                        picasso.load(driver_profile_pic).into(driver_img_confirm_act)
                        driver_name1_act.setText(name)
                        if(vehicle_name.isNotEmpty() || vehicle_no.isNotEmpty())
                        {
                            vch_name_act.setText(vehicle_name)
                            vch_number_act.setText(vehicle_no.toString())
                        }


                        //driver_rating_txt_aty.setText(rating)
                      total_fare_act.setText("$"+response.body()!!.data[0].amount)
                        SharedPreferenceUtils.getInstance(this@Confirm)!!.
                        setStringValue(ConstantUtils.Amount,response.body()!!.data[0].amount)
                        SharedPreferenceUtils.getInstance(this@Confirm)!!.
                        setStringValue(ConstantUtils.Distance,response.body()!!.data[0].distance)
                        SharedPreferenceUtils.getInstance(this@Confirm)!!.
                        setStringValue(ConstantUtils.Toatal_time,response.body()!!.data[0].time)
                        toatl_distance_trip_act.setText(response.body()!!.data[0].distance+" "+"KM")
                        total_time_trip_act.setText(response.body()!!.data[0].time)
                    }catch (e:Exception)
                    {
                        /*//Toast.makeText(this@Confirm,e.toString(), Toast.LENGTH_LONG)
                            .show()*/
                    }


                        }

                    }
                    else {

                        Toast.makeText(this@Confirm,response.body()!!.msg, Toast.LENGTH_LONG)
                            .show()
                        customprogress.hide()
                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    Toast.makeText(this@Confirm,e.message, Toast.LENGTH_LONG).show()
                    customprogress.hide()

                }

            }

            override fun onFailure(call: Call<BookingStatusResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                Toast.makeText(this@Confirm,t.message, Toast.LENGTH_LONG).show()
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


    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity()

            var handler: Handler? = null
            handler = Handler()
            SharedPreferenceUtils.getInstance(this)!!.setStringValue(ConstantUtils.Status,"1")
        /*  handler.removeCallbacks(Runnable {  },1000)*/
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    fun CancelTripSubmit()
    {
        customprogress.show()
        val request = HashMap<String, String>()
        request.put("booking_id",booking_id)
        request.put("description","dfdf")



        var cancel_trip: Call<AfterminCancelRideResponse> = APIUtils.getServiceAPI()!!.AfterminCancel(request)

        cancel_trip.enqueue(object : Callback<AfterminCancelRideResponse> {
            override fun onResponse(call: Call<AfterminCancelRideResponse>, response: Response<AfterminCancelRideResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {
                        SharedPreferenceUtils.getInstance(this@Confirm)!!.setStringValue(ConstantUtils.Status,"1").toString()
                              SharedPreferenceUtils.getInstance(this@Confirm)!!.removeKey(ConstantUtils.Booking_id)
                        Toast.makeText(this@Confirm,response.body()!!.msg.toString(),Toast.LENGTH_LONG).show()
                       /* var Inte=Intent(this@Confirm,Search1::class.java)
                        startActivity(Inte)
*/


                        customprogress.hide()




                    } else {


                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())

                    customprogress.hide()


                }

            }

            override fun onFailure(call: Call<AfterminCancelRideResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())

                customprogress.hide()


            }

        })
    }




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

