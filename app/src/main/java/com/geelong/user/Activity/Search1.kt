package com.geelong.user.Activity

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customnavigationdrawerexample.ClickListener
import com.example.customnavigationdrawerexample.RecyclerTouchListener
import com.geelong.user.API.APIUtils
import com.geelong.user.Adapter.NavigationRVAdapter
import com.geelong.user.Fragment.HomeFragment
import com.geelong.user.Model.NavigationItemModel
import com.geelong.user.R
import com.geelong.user.Response.LoginResponse
import com.geelong.user.Response.NewNotificationResponse
import com.geelong.user.Response.ViewNotificationResponse
import com.geelong.user.Util.*
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.tasks.OnSuccessListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_search1.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class Search1 : AppCompatActivity() {

    lateinit var logout_btn:LinearLayout
   var locat:String=""
    var lan:String=""
    var latii:String=""
    lateinit var drawerLayout: DrawerLayout
    private lateinit var adapter: NavigationRVAdapter
    lateinit var navigation_rv: RecyclerView
    lateinit var ivClose1:ImageView
    var textLatLong: TextView? = null
    lateinit var customProgress:Dialog
    var token_id:String=""
    var REQUEST_CODE=1



    var resultReceiver: ResultReceiver? = null
    private var items = arrayListOf(
        NavigationItemModel(R.drawable.home, "Account"),
        NavigationItemModel(R.drawable.trips, "Trips"),
        NavigationItemModel(R.drawable.noti, "Notifications"),
        NavigationItemModel(R.drawable.tc, "Terms & Conditions"),
        NavigationItemModel(R.drawable.privacy, "Privacy Policy")
       // NavigationItemModel(R.drawable.home, "Profile"),
        // NavigationItemModel(R.drawable.back, "Like us on facebook")
    )
    private var items1 = arrayListOf(
        NavigationItemModel(R.drawable.home, "Account"),
        NavigationItemModel(R.drawable.trips, "Trips"),
        NavigationItemModel(R.drawable.noti, "Notifications"),
        NavigationItemModel(R.drawable.tc, "Terms & Conditions"),
        NavigationItemModel(R.drawable.privacy, "Privacy Policy")
       // NavigationItemModel(R.drawable.home, "Profile"),
        // NavigationItemModel(R.drawable.back, "Like us on facebook")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search1)
        supportActionBar?.hide()
       // window.setStatusBarColor(ContextCompat.getColor(this,android.R.color.transparent))
        drawerLayout = findViewById(R.id.drawer_layout)
        navigation_rv=findViewById(R.id.navigation_rv1)
        var ivMenu=findViewById<ImageView>(R.id.ivMenu1)
        ivClose1=findViewById(R.id.ivClose)
        NewNotification()
        //ViewNotification()

       // resultReceiver = AddressResultReceiver(Handler())




        logout_btn=findViewById(R.id.Logout_Linear_Layout)


    customProgress= Dialog(this)
        customProgress.setContentView(R.layout.loader_layout)
        if (NetworkUtils.checkInternetConnection(this))
        {
            login()
           // NewNotification()
            customProgress.show()
        }


        ivClose1.setOnClickListener() {
            drawerLayout.closeDrawer(GravityCompat.START)
        }


           /* if ((ContextCompat.checkSelfPermission(
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
            }*/
        val bundle = Bundle()
        bundle.putString("fragmentName", "Settings Fragment")
       /* bundle.putString("Location",locat)
        bundle.putString("Late",latii)
        bundle.putString("Long",lan)*/
        val settingsFragment = HomeFragment()
        settingsFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_main_content_id, settingsFragment).commit()
        customProgress.hide()

     //   getLocationFromAddress("noida")

      //  getLocationFromAddress("noida")


        getSupportActionBar()?.setDisplayShowTitleEnabled(false);
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(false);


        navigation_rv.layoutManager = LinearLayoutManager(this)
        navigation_rv.setHasFixedSize(true)
        ivMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
       }

        logout_btn.setOnClickListener {
            SharedPreferenceUtils.getInstance(this)?.clear()
            val intent = Intent(this@Search1, Sign_Up::class.java)
            startActivity(intent)
        }


        navigation_rv.addOnItemTouchListener(RecyclerTouchListener(this, object : ClickListener {
            override fun onClick(view: View, position: Int) {
                when (position) {
                    0 -> {
                        val intent = Intent(this@Search1, Acccount::class.java)
                        startActivity(intent)

                    }
                    1 -> {
                      //  drawerLayout.closeDrawer(GravityCompat.START)
                        val intent = Intent(this@Search1, TripDetails::class.java)
                        startActivity(intent)
                    }
                    2 -> {
                        val intent = Intent(this@Search1, Notification::class.java)
                        startActivity(intent)
                    }
                    3 -> {
                        val intent = Intent(this@Search1, TermsCondition::class.java)
                        startActivity(intent)
                        // # Books Fragment

                    }
                    4 -> {
                        // # Profile Activity
                        val intent = Intent(this@Search1, Privacy_Policy::class.java)
                        startActivity(intent)
                    }
                    5 -> {
                        drawerLayout.closeDrawer(GravityCompat.START)


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


        customProgress.show()


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

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {

            if (supportFragmentManager.backStackEntryCount > 0) {
                // Go to the previous fragment
                supportFragmentManager.popBackStack()
            } else {
                // Exit the app
                super.onBackPressed()
            }
        }
    }

    fun click1(){
        drawerLayout.openDrawer(GravityCompat.START)
    }

    fun inte()
    {
        val intent = Intent(this, Confirm::class.java)
        startActivity(intent)
    }

/*
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
                               *//* textLatLong!!.text =
                                    String.format("Latitude : %s\n Longitude: %s", lati, longi)*//*
                                latii=lati.toString()
                                lan=longi.toString()
                                SharedPreferenceUtils.getInstance(this@Search1)?.setStringValue(
                                    ConstantUtils.LATITUDE,latii)
                                SharedPreferenceUtils.getInstance(this@Search1)?.setStringValue(
                                    ConstantUtils.LONGITUDE,lan)
                               // Toast.makeText(this@Search1,lati.toString()+longi.toString(),Toast.LENGTH_LONG).show()
                                val location = Location("providerNA")
                                location.longitude = longi
                                location.latitude = lati
                                fetchaddressfromlocation(location)
                            } else {
                                *//* progressBar!!.visibility = View.GONE*//*
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
                val settingsFragment = HomeFragment()
                settingsFragment.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .replace(R.id.activity_main_content_id, settingsFragment).commit()
                customProgress.hide()

               // Toast.makeText(this@Search1,address+locaity+state+district+district+country,Toast.LENGTH_LONG).show()

               *//* address!!.text = resultData.getString(Constants.ADDRESS)
                locaity!!.text = resultData.getString(Constants.LOCAITY)
                state!!.text = resultData.getString(Constants.STATE)
                district!!.text = resultData.getString(Constants.DISTRICT)
                country!!.text = resultData.getString(Constants.COUNTRY)
                postcode!!.text = resultData.getString(Constants.POST_CODE)*//*
            } else {
                Toast.makeText(
                    this@Search1,
                    resultData.getString(Constants.RESULT_DATA_KEY),
                    Toast.LENGTH_SHORT
                ).show()
            }
          *//*  progressBar!!.visibility = View.GONE*//*
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
    }*/

    fun login()
    {
      /*  customprogress.show()*/
        var mobi_num=SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.User_Mobile_Number,"").toString()
        token_id=SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.TokenId,"")
                .toString()
        val request = HashMap<String, String>()
        request.put("mobile",mobi_num)
        request.put("device_tokanid",token_id)



        var login_in: Call<LoginResponse> = APIUtils.getServiceAPI()!!.Login(request)

        login_in.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {


                        var img_url=response.body()!!.data[0].profile_photo
                        username_sidebar.text=response.body()!!.data[0].name
                        user_location_sidebar.text=response.body()!!.data[0].address

                        if(img_url.isEmpty())
                        {
                            val picasso=Picasso.get()
                            picasso.load(R.drawable.defaultt).into(navigation_user_pic)
                        }
                        else{
                            val picasso= Picasso.get()
                            picasso.load(img_url).into(navigation_user_pic)
                        }

                        customProgress.hide()


                    } else {

                      //  Toast.makeText(this@Search1,"Error", Toast.LENGTH_LONG).show()
                        customProgress.hide()
                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())

                    Toast.makeText(this@Search1,e.message, Toast.LENGTH_LONG).show()
                    customProgress.hide()

                }

            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())

                Toast.makeText(this@Search1,t.message, Toast.LENGTH_LONG).show()
                customProgress.hide()
            }

        })
    }

   /* fun getLatLongFromGivenAddress(address: String?) {
        var lat = 0.0
        var lng = 0.0
        val geoCoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses: List<Address> = geoCoder.getFromLocationName(address, 1)
            if (addresses.size > 0) {
                val p = GeoPoint(
                    ((addresses[0].getLatitude() * 1E6) as Int).toDouble(),
                    ((addresses[0].getLongitude() * 1E6) as Int).toDouble()
                )
               // lat = p.getLatitudeE6() / 1E6
                lat=p.latitude/1E6
                lng = p.longitude/ 1E6
                Log.d("Latitude", "" + lat)
                Log.d("Longitude", "" + lng)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }*/

  /*  fun getLocationFromAddress(strAddress: String?): GeoPoint? {
        val coder = Geocoder(this)
        val address: List<Address>?
        var p1: GeoPoint? = null
        try {
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null) {
                return null
            }
            val location = address[0]
            location.latitude
            location.longitude
            p1 = GeoPoint(
                (location.latitude * 1E6),
                (location.longitude * 1E6)
            )
            Toast.makeText(this,p1.toString(),Toast.LENGTH_LONG).show()
            return p1
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null



    }*/



    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
            return
        }



                /*   Toast.makeText(
                       applicationContext,
                       currentLocation.latitude.toString() + "" + currentLocation.longitude,
                       Toast.LENGTH_SHORT
                   ).show()*/
                /*val supportMapFragment =
                    (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!
                supportMapFragment.getMapAsync(this)*/
    /*        }
        })*/
    }

    fun NewNotification()
    {

        var user_id=SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils
            .USER_ID,"")
            .toString()

        val request = HashMap<String, String>()
        request.put("user_id",user_id)




        var new_Noti: Call<NewNotificationResponse> = APIUtils.getServiceAPI()!!.NewNotification(request)

        new_Noti.enqueue(object : Callback<NewNotificationResponse> {
            override fun onResponse(call: Call<NewNotificationResponse>, response: Response<NewNotificationResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {
                        /*Toast.makeText(this@Search1,response.body()!!.data[0].count+"seracg1",Toast
                            .LENGTH_LONG)
                            .show()*/
                        SharedPreferenceUtils.getInstance(this@Search1)!!.setStringValue(ConstantUtils.Total_notificat_count,response.body()!!.data[0].count)
                            .toString()


                  /*      customProgress.hide()*/


                    } else {

                        //  Toast.makeText(this@Search1,"Error", Toast.LENGTH_LONG).show()
                        /*customProgress.hide()*/
                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())

                    Toast.makeText(this@Search1,e.message, Toast.LENGTH_LONG).show()
                   /* customProgress.hide()
*/
                }

            }

            override fun onFailure(call: Call<NewNotificationResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())

                Toast.makeText(this@Search1,t.message, Toast.LENGTH_LONG).show()
               /* customProgress.hide()*/
            }

        })
    }






}