package com.geelong.user.Activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customnavigationdrawerexample.ClickListener
import com.example.customnavigationdrawerexample.RecyclerTouchListener
import com.geelong.user.Adapter.NavigationRVAdapter
import com.geelong.user.Fragment.HomeFragment
import com.geelong.user.Model.NavigationItemModel
import com.geelong.user.R
import com.geelong.user.Util.Constants
import com.geelong.user.Util.FetchAddressServices
import com.geelong.user.Util.SharedPreferenceUtils
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices


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

        resultReceiver = AddressResultReceiver(Handler())



        logout_btn=findViewById(R.id.Logout_Linear_Layout)


       /* img_prfil.setOnClickListener() {
            intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        */



        ivClose1.setOnClickListener() {
            drawerLayout.closeDrawer(GravityCompat.START)
        }


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




        // Set the toolbar
        // setSupportActionBar(activity_main_toolbar)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false);
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(false);

        // Setup Recyclerview's Layout
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

        // Add Item Touch Listener
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
                // Don't highlight the 'Profile' and 'Like us on Facebook' item row
                updateAdapter(position)
                if (position != 6 && position != 4) {

                }
                Handler().postDelayed({
                    drawerLayout.closeDrawer(GravityCompat.START)
                }, 200)
            }
        }))

        // Update Adapter with item data and highlight the default menu item ('Home' Fragment)
        updateAdapter(0)




        // Close the soft keyboard when you open or close the Drawer
        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerClosed(drawerView: View) {
                // Triggered once the drawer closes
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
                // Triggered once the drawer opens
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


        // Set Header Image
        // navigation_header_img.setImageResource(R.drawable.logo)

        // Set background of Drawer
       // navigation_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
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
            // Checking for fragment count on back stack
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

    // TODO: Consider calling
    //    ActivityCompat#requestPermissions
    // here to request the missing permissions, and then overriding
    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
    //                                          int[] grantResults)
    // to handle the case where the user grants the permission. See the documentation
    // for ActivityCompat#requestPermissions for more details.
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
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
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
                               /* textLatLong!!.text =
                                    String.format("Latitude : %s\n Longitude: %s", lati, longi)*/
                                latii=lati.toString()
                                lan=longi.toString()
                               // Toast.makeText(this@Search1,lati.toString()+longi.toString(),Toast.LENGTH_LONG).show()
                                val location = Location("providerNA")
                                location.longitude = longi
                                location.latitude = lati
                                fetchaddressfromlocation(location)
                            } else {
                                /* progressBar!!.visibility = View.GONE*/
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

               // Toast.makeText(this@Search1,address+locaity+state+district+district+country,Toast.LENGTH_LONG).show()

               /* address!!.text = resultData.getString(Constants.ADDRESS)
                locaity!!.text = resultData.getString(Constants.LOCAITY)
                state!!.text = resultData.getString(Constants.STATE)
                district!!.text = resultData.getString(Constants.DISTRICT)
                country!!.text = resultData.getString(Constants.COUNTRY)
                postcode!!.text = resultData.getString(Constants.POST_CODE)*/
            } else {
                Toast.makeText(
                    this@Search1,
                    resultData.getString(Constants.RESULT_DATA_KEY),
                    Toast.LENGTH_SHORT
                ).show()
            }
          /*  progressBar!!.visibility = View.GONE*/
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