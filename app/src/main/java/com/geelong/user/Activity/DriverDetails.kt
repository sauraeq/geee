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
import android.widget.ImageView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.geelong.user.API.APIUtils
import com.geelong.user.Adapter.NavigationRVAdapter

import com.geelong.user.Model.NavigationItemModel
import com.geelong.user.R
import com.geelong.user.Response.BookingStsResponse
import com.geelong.user.Response.MapData
import com.geelong.user.Util.ConstantUtils
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
import com.kaopiz.kprogresshud.KProgressHUD
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_confirm.*
import kotlinx.android.synthetic.main.activity_driver_details.*
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.HashMap

class DriverDetails : AppCompatActivity(),OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
   var originLatitude: String = ""
    var originLongitude: String = ""
    var destinationLatitude: String = ""
    var destinationLongitude: String = ""
    var booking_id:String=""
    var approx_km:String=""
    var status="4"
     var toatal_time_taken:String=""

    lateinit var drawerLayout: DrawerLayout
    private lateinit var adapter: NavigationRVAdapter
    lateinit var navigation_rv: RecyclerView
    lateinit var ivClose1: ImageView
    lateinit var customprogress:Dialog
    var amount:String=""
    var total_km:String=""



    private var items = arrayListOf(
        NavigationItemModel(R.drawable.home, "Account"),
        NavigationItemModel(R.drawable.trips, "Trips"),
        NavigationItemModel(R.drawable.noti, "Notifications"),
        NavigationItemModel(R.drawable.tc, "Terms & Conditions"),
        NavigationItemModel(R.drawable.privacy, "Privacy Policy")

    )
    private var items1 = arrayListOf(
        NavigationItemModel(R.drawable.home, "Account"),
        NavigationItemModel(R.drawable.trips, "Trips"),
        NavigationItemModel(R.drawable.noti, "Notifications"),
        NavigationItemModel(R.drawable.tc, "Terms & Conditions"),
        NavigationItemModel(R.drawable.privacy, "Privacy Policy")

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_details)
        supportActionBar?.hide()
        SharedPreferenceUtils.getInstance(this)!!.setStringValue(ConstantUtils.Status,status)
        rid_details_linear.visibility=View.GONE
        progress_linear_ride.visibility=View.VISIBLE
        try {
            approx_km=SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.Distance,"")
                .toString()
            toatal_time_taken=SharedPreferenceUtils.getInstance(this)?.
            getStringValue(ConstantUtils.Toatal_time,"").toString()


            amount=SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.Amount,"").toString()
            booking_id=SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.Booking_id,"").toString()
            originLatitude=SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils
                .Pick_up_Latitude,"").toString()
            originLongitude=SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils
                .Pick_up_longitude,"").toString()
            destinationLatitude=SharedPreferenceUtils.getInstance(this)?.
            getStringValue(ConstantUtils.Driver_latitude,"").toString()
            destinationLongitude=SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.Driver_longitude,"").toString()
        }catch (e:Exception)
        {

        }
       /* if(originLatitude.isNotEmpty()|| originLongitude.isNotEmpty()|| destinationLatitude
                .isNotEmpty()|| destinationLongitude.isNotEmpty())
        {
             total_km=getKilometers(originLatitude.toDouble(),originLongitude.toDouble(), destinationLatitude.toDouble(),destinationLongitude.toDouble()).toString()
            approx_km=roundOffDecimal(total_km.toDouble()).toString()
              Totaltimetaken(approx_km.toDouble())
        }
        else
        {

        }*/
        /* drawerLayout = findViewById(R.id.drawer_layout1)
       navigation_rv=findViewById(R.id.navigation_rv11)

       ivClose1=findViewById(R.id.ivClose)*/
        customprogress = Dialog(this)
        customprogress.setContentView(R.layout.dialog_progress)
        Cancel_booking_btn_aty.setOnClickListener {
            val intent = Intent(this,CancelTrip::class.java)
            startActivity(intent)
        }
        pyment_btn_driver_details.setOnClickListener {
            val intent = Intent(this,Pay_Now::class.java)
            startActivity(intent)
        }
        cancel_ride_btn.setOnClickListener {
            val intent=Intent(this,Search1::class.java)
            SharedPreferenceUtils.getInstance(this)!!.removeKey(ConstantUtils.Booking_id)
            SharedPreferenceUtils.getInstance(this)!!.removeKey(ConstantUtils.Distance)
            SharedPreferenceUtils.getInstance(this)!!.removeKey(ConstantUtils.Toatal_time)
            SharedPreferenceUtils.getInstance(this)!!.removeKey(ConstantUtils.Driver_Id)
            startActivity(intent)
        }


        /*  NewNotification()

        val intent = intent
        val message = intent.getStringExtra("message")
        if(!message.isNullOrEmpty()) {
            AlertDialog.Builder(this)
                    .setTitle("Notification")
                    .setMessage(message)
                    .setPositiveButton("Ok", { dialog, which -> }).show()
        }

        logout_driver_details_linear.setOnClickListener {
            SharedPreferenceUtils.getInstance(this)?.clear()
            val intent = Intent(this, Sign_Up::class.java)
            startActivity(intent)
        }

  if (NetworkUtils.checkInternetConnection(this))
  {
      progilepic()
      customprogress.show()
  }

*/
        /*val bundle = Bundle()
    bundle.putString("fragmentName", "Settings Fragment")
    val settingsFragment = DriverFragments()
    settingsFragment.arguments = bundle
    supportFragmentManager.beginTransaction()
    .replace(R.id.activity_main_content_id, settingsFragment).commit()*/
        /*var ivMenu=findViewById<ImageView>(R.id.ivMenu_driver)
    ivClose1.setOnClickListener() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }



    getSupportActionBar()?.setDisplayShowTitleEnabled(false);
    getSupportActionBar()?.setDisplayHomeAsUpEnabled(false);


    navigation_rv.layoutManager = LinearLayoutManager(this)
    navigation_rv.setHasFixedSize(true)
    ivMenu.setOnClickListener {
        drawerLayout.openDrawer(GravityCompat.START)
    }


    navigation_rv.addOnItemTouchListener(RecyclerTouchListener(this, object : ClickListener {
        override fun onClick(view: View, position: Int) {
            when (position) {
                0 -> {
                    val intent = Intent(this@DriverDetails, Acccount::class.java)
                    startActivity(intent)

                }
                1 -> {

                    val intent = Intent(this@DriverDetails, TripDetails::class.java)
                    startActivity(intent)
                }
                2 -> {
                    val intent = Intent(this@DriverDetails, Notification::class.java)
                    startActivity(intent)
                }
                3 -> {
                    val intent = Intent(this@DriverDetails, TermsCondition::class.java)
                    startActivity(intent)

                }
                4 -> {

                    val intent = Intent(this@DriverDetails, Privacy_Policy::class.java)
                    startActivity(intent)
                }
                5 -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    // val intent = Intent(this@MainActivity1, ProfileActivity::class.java)
                    // startActivity(intent)
                    // # Settings Fragment
                    *//* val bundle = Bundle()
                     bundle.putString("fragmentName", "Settings Fragment")
                     val settingsFragment = DemoFragment()
                     settingsFragment.arguments = bundle
                     supportFragmentManager.beginTransaction()
                         .replace(R.id.activity_main_content_id, settingsFragment).commit()*//*

                }
                6 -> {


                }
            }

            updateAdapter(position)
            if (position != 6 && position != 4) {

            }
            Handler().postDelayed({
                drawerLayout.closeDrawer(GravityCompat.START)
            }, 200)
        }
    }))


    updateAdapter(0)

    val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
        this,
        drawerLayout,
        R.string.navigation_drawer_open,
        R.string.navigation_drawer_close
    ) {
        override fun onDrawerClosed(drawerView: View) {

            super.onDrawerClosed(drawerView)
            try {
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            } catch (e: Exception) {
                e.stackTrace
            }
        }

        override fun onDrawerOpened(drawerView: View) {

            super.onDrawerOpened(drawerView)
            try {
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            } catch (e: Exception) {
                e.stackTrace
            }
        }
    }
    drawerLayout.addDrawerListener(toggle)

    toggle.syncState()

}

private fun updateAdapter(highlightItemPos: Int) {
    adapter = NavigationRVAdapter(items,items1, highlightItemPos)
    navigation_rv.adapter = adapter
    adapter.notifyDataSetChanged()
}
*/
        val ai: ApplicationInfo = applicationContext.packageManager
            .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)

        val value=getString(R.string.api_key)
        val apiKey = value.toString()


        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }


        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_aty) as SupportMapFragment
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
        Booking_status()

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


    fun Booking_status()
    {

        val request = HashMap<String, String>()
        request.put("booking_id",booking_id)

        var driver_vec_details: Call<BookingStsResponse> = APIUtils.getServiceAPI()!!.booking_status(request)

        driver_vec_details.enqueue(object : Callback<BookingStsResponse> {
            override fun onResponse(call: Call<BookingStsResponse>, response: Response<BookingStsResponse>) {
                try {

                    /*customprogress.show()*/
                    if (response.body()!!.success.equals("true")) {

                        if (response.body()!!.data[0].status.equals("0")|| response.body()!!
                                .data[0].status.equals("1"))

                        {


                            var handler: Handler? = null
                            handler = Handler()
                            handler.postDelayed(Runnable {
                                Booking_status()

                            }, 5000)
                            // SharedPreferenceUtils.getInstance(requireContext())?.removeKey
                            (ConstantUtils.Booking_id)
                            /*val intent=Intent(requireContext(),Search1::class.java)
                            startActivity(intent)*/

                        }
                        else

                        {

                            rid_details_linear.visibility=View.VISIBLE
                            progress_linear_ride.visibility=View.GONE

                            var otp=response.body()!!.data[0].otp
                            var driver_profile_pic=response.body()!!.data[0].profile_photo
                            var vechile_img=response.body()!!.data[0].vehicle_image
                            var vehicle_no=response.body()!!.data[0].vehicle_no
                            var name=response.body()!!.data[0].name
                            var vehicle_name=response.body()!!.data[0].name
                            var rating=response.body()!!.data[0].rating


                            val picasso = Picasso.get()
                            picasso.load(driver_profile_pic).into(driver_img_drvFrg_aty)
                            picasso.load(vechile_img).into(vch_img_drvFrg_aty)
                            driver_nmae_drvFrg_aty.setText(name)
                            vch_name_drvFrg_aty.setText(vehicle_name)
                            vechile_number_drvFrg_aty.setText(vehicle_no)
                            otp_drvFrg_aty.setOTP(otp)
                            driver_rating_txt_aty.setText(rating)
                            tp_driverdetails.setText("$"+amount)
                            total_distancee_driverdetails.setText(approx_km)
                            total_timee_driverdetails.setText(toatal_time_taken)

                            Ride_status()
                        }

                    }
                    else {

                        Toast.makeText(this@DriverDetails,response.body()!!.msg, Toast.LENGTH_LONG)
                            .show()
                        customprogress.hide()
                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    Toast.makeText(this@DriverDetails,"Weak Internet Connection", Toast.LENGTH_LONG).show()
                    customprogress.hide()

                }

            }

            override fun onFailure(call: Call<BookingStsResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                Toast.makeText(this@DriverDetails,"Weak Internet Connection", Toast.LENGTH_LONG).show()
                customprogress.hide()

            }

        })
    }



    fun Ride_status()
    {

        val request = HashMap<String, String>()
        request.put("booking_id",booking_id)

        var driver_vec_details: Call<BookingStsResponse> = APIUtils.getServiceAPI()!!.booking_status(request)

        driver_vec_details.enqueue(object : Callback<BookingStsResponse> {
            override fun onResponse(call: Call<BookingStsResponse>, response: Response<BookingStsResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {

                        if (response.body()!!.data[0].status.equals("3"))

                        {
                           var intent=Intent(this@DriverDetails,Search1::class.java)
                            startActivity(intent)
                            SharedPreferenceUtils.getInstance(this@DriverDetails)!!.removeKey(ConstantUtils.Booking_id)
                            SharedPreferenceUtils.getInstance(this@DriverDetails)!!.removeKey(ConstantUtils.Driver_Id)
                            SharedPreferenceUtils.getInstance(this@DriverDetails)!!.removeKey(ConstantUtils.Driver_name)
                            SharedPreferenceUtils.getInstance(this@DriverDetails)!!.removeKey(ConstantUtils.Vechile_number)
                            SharedPreferenceUtils.getInstance(this@DriverDetails)!!.removeKey(ConstantUtils.Vechilename)
                            SharedPreferenceUtils.getInstance(this@DriverDetails)!!.removeKey(ConstantUtils.Vechile_image)
                            SharedPreferenceUtils.getInstance(this@DriverDetails)!!.removeKey(ConstantUtils.Distance)
                            SharedPreferenceUtils.getInstance(this@DriverDetails)!!.removeKey(ConstantUtils.Driver_Rating)

                        }
                        else

                        {
                            var handler: Handler? = null
                            handler = Handler()
                            handler.postDelayed(Runnable {
                                Ride_status()

                            }, 5000)

                        }

                    }
                    else {

                        Toast.makeText(this@DriverDetails,response.body()!!.msg, Toast.LENGTH_LONG)
                            .show()
                        customprogress.hide()
                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    Toast.makeText(this@DriverDetails,"Weak Internet Connection", Toast.LENGTH_LONG).show()
                    customprogress.hide()

                }

            }

            override fun onFailure(call: Call<BookingStsResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                Toast.makeText(this@DriverDetails,"Weak Internet Connection", Toast.LENGTH_LONG).show()
                customprogress.hide()

            }

        })
    }
    private fun showDialog() {
        /* val dialog = Dialog(requireContext())
         dialog.getWindow()!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

         dialog.setCancelable(true)
         dialog.setContentView(R.layout.wait_for_trip_popup)
         lateinit var button: LinearLayout*/

        KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setDetailsLabel("Finding Driver")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .show()

        /*button = dialog.findViewById(R.id.payment_success)

        button.setOnClickListener {
            dialog.dismiss()

        }
        dialog.cancal_popup_img.setOnClickListener {
            dialog.dismiss()
            SharedPreferenceUtils.getInstance(requireContext())?.removeKey(ConstantUtils.Booking_id)

        }*/



/*
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()*/

        //dialog.window?.setLayout(700,750)

    }

    fun getKilometers(lat1: Double, long1: Double, lat2: Double, long2: Double): Double {
        val PI_RAD = Math.PI / 180.0
        val phi1 = lat1 * PI_RAD
        val phi2 = lat2 * PI_RAD
        val lam1 = long1 * PI_RAD
        val lam2 = long2 * PI_RAD
        return 6371.01 * Math.acos(
            Math.sin(phi1) * Math.sin(phi2) + Math.cos(phi1) * Math.cos(phi2) * Math.cos(
                lam2 - lam1
            )
        )
    }

    fun roundOffDecimal(number: Double): Double? {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(number).toDouble()
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



        } else {
            var minutes = Integer.toString(totalMinutes % 60)
            minutes = if (minutes.length == 1) "0$minutes" else minutes
            (totalMinutes / 60).toString() + " hour " + minutes + "mins"
            toatal_time_taken = minutes.toString()
            SharedPreferenceUtils.getInstance(this)!!.setStringValue(
                ConstantUtils
                    .Toatal_time, toatal_time_taken
            )


        }


    }
    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }


}


