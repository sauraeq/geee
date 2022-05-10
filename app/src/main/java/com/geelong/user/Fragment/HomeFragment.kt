package com.geelong.user.Fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.geelong.user.API.APIUtils
import com.geelong.user.Activity.Confirm
import com.geelong.user.Activity.Pay_Now
import com.geelong.user.Activity.Search1
import com.geelong.user.Adapter.AutoCompleteAdapter
import com.geelong.user.Adapter.AutoCompleteAdapter_pickup
import com.geelong.user.R
import com.geelong.user.Response.BookingResponse
import com.geelong.user.Response.DriverDetails_Vch_Response
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.Constants
import com.geelong.user.Util.FetchAddressServices
import com.geelong.user.Util.SharedPreferenceUtils
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.android.synthetic.main.activity_confirm.*
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.Math.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class HomeFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    var locat: String = ""

    var pick_up_location: String = ""
    var drop_location: String = ""
    var n_of_passenger: String = ""

    var placesClient: PlacesClient? = null
    var pickUp_address: String = ""
    var pick_up_latitude: String = ""
    var pick_up_longitude: String = ""
    var user_id: String = ""
    var toatal_time_taken: String = ""
    var time_count:String=""
    var no_of_passenger=0
    lateinit var passenger_edittext:EditText

    var autoCompleteTextView_drop: AutoCompleteTextView? = null
    var adapter: AutoCompleteAdapter? = null
    var adapter_1: AutoCompleteAdapter_pickup? = null

    lateinit var customprogress: Dialog
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    lateinit var pick_up_user: AutoCompleteTextView
    var resultReceiver: ResultReceiver? = null
    lateinit var no_passengerr: EditText
    var total_distance_apprx: String = ""
    var locType:String=""
    var sourlat = 0.0
    var sourlng:Double = 0.0
    var deslat = 0.0
    var deslng:Double = 0.0
    lateinit var sourcelatLng:LatLng
    lateinit var destlatLng:LatLng
    var destLoc=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootview = inflater.inflate(R.layout.fragment_home, container, false)

        customprogress = Dialog(requireContext())
        customprogress.setContentView(R.layout.loader_layout)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }

        resultReceiver = AddressResultReceiver(Handler())
        user_id = SharedPreferenceUtils.getInstance(requireContext())
            ?.getStringValue(ConstantUtils.USER_ID, "").toString()
        pick_up_user = rootview.findViewById(R.id.pickup_location_user)

        no_passengerr = rootview.findViewById(R.id.no_passenger)
        no_passengerr.setOnClickListener {
            numberPickerCustom()
        }


        if ((ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
                    != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )

            Toast.makeText(requireContext(), "Permission", Toast.LENGTH_LONG).show()
        } else {
            currentLocation
        }


        val apiKey = getString(R.string.api_key)



        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), apiKey)
        }

        placesClient = Places.createClient(requireContext())
        autoCompleteTextView_drop =
            rootview.findViewById<AutoCompleteTextView>(R.id.drop_location_user)


        //initAutoCompleteTextView_drop()
     //   initAutoCompleteTextView_pickup()

        pick_up_user.setOnClickListener {
            locType = "pickloc"

            var intent = Intent(requireContext(), SearchActivityNew::class.java)
            startActivityForResult(intent, 100)
        }

        autoCompleteTextView_drop?.setOnClickListener {
            locType = "droploc"

            var intent = Intent(requireContext(), SearchActivityNew::class.java)
            startActivityForResult(intent, 100)
        }


        var search_textt: TextView = rootview.findViewById(R.id.search_text_tv)
        search_textt.setOnClickListener {

            pick_up_location = pickup_location_user.text.toString()
            drop_location = drop_location_user.text.toString()
            n_of_passenger = no_passenger.text.toString()

            //  getLocationFromAddress(drop_location)

            if (pick_up_location.isEmpty()) {
                Toast.makeText(requireContext(), "Please select pickup location", Toast.LENGTH_LONG)
                    .show()
            } else if (drop_location.isEmpty()) {
                Toast.makeText(requireContext(), "Please select drop location", Toast.LENGTH_LONG)
                    .show()
            } else if (n_of_passenger.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill no of passenger", Toast.LENGTH_LONG)
                    .show()
            } else {
                if (sourlat.toString().isEmpty() || sourlng.toString().isEmpty() || deslat.toString
                        ().isEmpty()
                    || deslng.toString().isEmpty()
                ) {

                } else {
                    var toatal_distance = getKilometers(
                        sourlat.toDouble(),
                        sourlng.toDouble(),
                        deslat.toDouble(),
                        deslng.toDouble()
                    )
                    total_distance_apprx = roundOffDecimal(toatal_distance.toDouble()).toString()
                    time_count=Totaltimetaken(total_distance_apprx.toDouble())
                    SharedPreferenceUtils.getInstance(requireContext())!!
                        .setStringValue(ConstantUtils.Distance, total_distance_apprx.toString())

                    DriverDetailss()

                }

            }

        }

        var ivMenu1: ImageView = rootview.findViewById(R.id.ivMenu_home)
        ivMenu1.setOnClickListener {
            (activity as Search1?)?.click1()
        }

        customprogress.hide()
        return rootview
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == 100) {
                val lat: String = data?.getStringExtra("lat").toString()
                val lng: String = data?.getStringExtra("lng").toString()
                val location: String = data?.getStringExtra("location").toString()
                if(locType.equals("pickloc")){
                    sourlat=lat.toDouble()
                    sourlng=lng.toDouble()
                    pick_up_user?.setText(location)
                    pick_up_user?.setText(location)
                    SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(ConstantUtils
                        .Pick_up_Latitude,sourlat.toString()).toString()
                    SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(ConstantUtils
                        .Pick_up_longitude,sourlng.toString()).toString()
                    SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(ConstantUtils
                        .Current_Location,location).toString()
                    sourcelatLng= LatLng(sourlat,sourlng)
                }else{
                    deslat=lat.toDouble()
                    deslng=lng.toDouble()
                    destLoc=location
                    destlatLng= LatLng(deslat,deslng)
                    SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(ConstantUtils
                        .Latitude_Drop,deslat.toString()).toString()
                    SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(ConstantUtils
                        .Longitude_Drop,deslng.toString()).toString()
                    SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(ConstantUtils
                        .Drop_location,location).toString()
                    autoCompleteTextView_drop?.setText(location)
                    autoCompleteTextView_drop?.setText(location)
                }



            }

        }catch (e:Exception){

        }

    }
    private fun bitmapDescriptorFromVector(context: Context?, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(requireContext(), vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


   /* private fun initAutoCompleteTextView_drop() {
        autoCompleteTextView_drop?.setThreshold(1)
        autoCompleteTextView_drop?.setOnItemClickListener(autocompleteClickListener_drop)
        adapter = AutoCompleteAdapter(requireContext(), placesClient)
        autoCompleteTextView_drop?.setAdapter(adapter)
    }

    private fun initAutoCompleteTextView_pickup() {
        pick_up_user?.setThreshold(1)
        pick_up_user?.setOnItemClickListener(autocompleteClickListener_pickup)
        adapter_1 = AutoCompleteAdapter_pickup(requireContext(), placesClient)
        pick_up_user?.setAdapter(adapter_1)
    }
*/
   /* private val autocompleteClickListener_drop =
        AdapterView.OnItemClickListener { adapterView, view, i, l ->
            try {
                val item: AutocompletePrediction = adapter?.getItem(i)!!
                var placeID: String? = null
                if (item != null) {
                    placeID = item.placeId
                }

                val placeFields = Arrays.asList(
                    Place.Field.ID,
                    Place.Field.NAME,
                    Place.Field.ADDRESS,
                    Place.Field.LAT_LNG
                )
                var request: FetchPlaceRequest? = null
                if (placeID != null) {
                    request = FetchPlaceRequest.builder(placeID, placeFields)
                        .build()
                }
                if (request != null) {
                    placesClient!!.fetchPlace(request).addOnSuccessListener { task ->
                        val inputMethodManager =
                            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(
                            drop_location_user.getWindowToken(),
                            0
                        )

                        drop_location = drop_location_user.text.toString()
                        getLocationFromAddress_drop(drop_location)

                    }.addOnFailureListener { e ->
                        e.printStackTrace()

                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    private val autocompleteClickListener_pickup =
        AdapterView.OnItemClickListener { adapterView, view, i, l ->
            try {
                val item: AutocompletePrediction = adapter_1?.getItem(i)!!
                var placeID: String? = null
                if (item != null) {
                    placeID = item.placeId
                }
                val placeFields = Arrays.asList(
                    Place.Field.ID,
                    Place.Field.NAME,
                    Place.Field.ADDRESS,
                    Place.Field.LAT_LNG
                )
                var request: FetchPlaceRequest? = null
                if (placeID != null) {
                    request = FetchPlaceRequest.builder(placeID, placeFields)
                        .build()
                }
                if (request != null) {
                    placesClient!!.fetchPlace(request).addOnSuccessListener { task ->


                        val inputMethodManager =
                            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(pick_up_user.getWindowToken(), 0)

                        pick_up_location = pick_up_user.text.toString()
                        SharedPreferenceUtils.getInstance(requireContext())
                            ?.setStringValue(ConstantUtils.Current_Location, pick_up_location)
                        getLocationFromAddress_pickup(pick_up_location)

                    }.addOnFailureListener { e ->
                        e.printStackTrace()
                        Toast.makeText(
                            requireContext(),
                            e.printStackTrace().toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), e.printStackTrace().toString(), Toast.LENGTH_LONG)
                    .show()
            }
        }
*/

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
                Toast.makeText(requireContext(), "Permission is denied!", Toast.LENGTH_SHORT).show()
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
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }
            LocationServices.getFusedLocationProviderClient(requireContext())
                .requestLocationUpdates(locationRequest, object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        LocationServices.getFusedLocationProviderClient(requireContext())
                            .removeLocationUpdates(this)
                        if (locationResult.locations != null) {
                            if (locationResult.locations.size > 0) {
                                val latestlocIndex = locationResult.locations.size - 1
                                val lati = locationResult.locations[latestlocIndex].latitude
                                val longi = locationResult.locations[latestlocIndex].longitude
                                sourlat = lati
                                sourlng = longi
                                SharedPreferenceUtils.getInstance(requireContext())!!
                                    .setStringValue(
                                        ConstantUtils.Pick_up_Latitude,sourlat.toString()
                                    )
                                SharedPreferenceUtils.getInstance(requireContext())!!
                                    .setStringValue(
                                        ConstantUtils.Pick_up_longitude, sourlng.toString()
                                    )

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
                var address: String? = resultData.getString(Constants.ADDRESS)
                var locaity: String? = resultData.getString(Constants.LOCAITY)
                var state: String? = resultData.getString(Constants.STATE)
                var district: String? = resultData.getString(Constants.DISTRICT)
                var country: String? = resultData.getString(Constants.ADDRESS)
                locat = address + "," + locaity + "," + state
                pick_up_user.setText(locat)

                SharedPreferenceUtils.getInstance(requireContext())
                    ?.setStringValue(ConstantUtils.Current_Location, locat)

                loadMap(sourlat.toString(), sourlng.toString(), locat)

            } else {
                Toast.makeText(
                    requireContext(),
                    resultData.getString(Constants.RESULT_DATA_KEY),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private fun fetchaddressfromlocation(location: Location) {
        val intent = Intent(requireContext(), FetchAddressServices::class.java)
        intent.putExtra(Constants.RECEVIER, resultReceiver)
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location)
        requireContext().startService(intent)
    }

    fun loadMap(lati_curr1: String, longi_current1: String, loate1: String) {
        try {
            if (sourlat.toString().isEmpty() || sourlng.toString().isEmpty()) {

            } else {
                customprogress.dismiss()
                val mapFragment =
                    childFragmentManager.findFragmentById(R.id.frg) as SupportMapFragment?
                mapFragment!!.getMapAsync { mMap ->
                    mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

                    mMap.clear()


                    val googlePlex = CameraPosition.builder()
                        .target(LatLng(lati_curr1.toDouble(), longi_current1.toDouble()))
                        .zoom(12f)
                        .bearing(0f)
                        .build()

                    mMap.animateCamera(
                        CameraUpdateFactory.newCameraPosition(googlePlex),
                        1000,
                        null
                    )

                    mMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(lati_curr1.toDouble(), longi_current1.toDouble()))
                            .title(loate1)
                            .icon(bitmapDescriptorFromVector(activity, R.drawable.maparroww))
                    )
                }


            }

        } catch (e: Exception) {

        }


    }
/*

    fun getLocationFromAddress_drop(strAddress: String?) {

        val coder = Geocoder(requireContext())
        val address: List<Address>?
        try {

            address = coder.getFromLocationName(strAddress, 5)


            if (address == null) {
                return
            }


            val location = address[0]
            val latLng = LatLng(location.latitude, location.longitude)
            var la_longArr = latLng.toString().split(",", "(", ")")
            lati_drop = la_longArr[1]
            longi_drop = la_longArr[2]
            SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                ConstantUtils.Latitude_Drop, lati_drop
            )
            SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                ConstantUtils.Longitude_Drop, longi_drop
            )

            Log.d("daad", lati_drop + longi_drop)
            if (strAddress != null) {
                loadMap(lati_drop, longi_drop, strAddress)
            }


        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getLocationFromAddress_pickup(strAddress: String?) {

        val coder = Geocoder(requireContext())
        val address: List<Address>?
        try {
            address = coder.getFromLocationName(strAddress, 5)

            if (address == null) {
                return
            }

            val location = address[0]
            val latLng = LatLng(location.latitude, location.longitude)
            var la_longArr = latLng.toString().split(",", "(", ")")
            lati_curr = la_longArr[1]
            longi_current = la_longArr[2]
            Toast.makeText(requireContext(), lati_curr + longi_current, Toast.LENGTH_LONG).show()
            SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                ConstantUtils.Pick_up_Latitude, lati_curr
            )
            SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                ConstantUtils.Pick_up_longitude, longi_current
            )

            Log.d("daad", lati_curr + longi_current)
            if (strAddress != null) {
                loadMap(lati_curr, longi_current, strAddress)
            }


        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
*/

    fun getKilometers(lat1: Double, long1: Double, lat2: Double, long2: Double): Double {
        val PI_RAD = Math.PI / 180.0
        val phi1 = lat1 * PI_RAD
        val phi2 = lat2 * PI_RAD
        val lam1 = long1 * PI_RAD
        val lam2 = long2 * PI_RAD
        return 6371.01 * acos(sin(phi1) * sin(phi2) + cos(phi1) * cos(phi2) * cos(lam2 - lam1))
    }

    fun roundOffDecimal(number: Double): Double? {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(number).toDouble()
    }

    fun numberPickerCustom() {

        val d = AlertDialog.Builder(requireContext())
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.date_picker, null)

        d.setView(dialogView)

        val numberPicker = dialogView.findViewById<NumberPicker>(R.id.numberpicker)

        numberPicker.minValue = 1
        numberPicker.maxValue = 4
        numberPicker.value = 1
        numberPicker.wrapSelectorWheel = true


        numberPicker.setOnValueChangedListener { numberPicker, i, i1 ->
            println("onValueChange: ")
        }
        d.setPositiveButton("Done") { dialogInterface, i ->
            println("onClick: " + numberPicker.value)
            var numberpickkk = numberPicker.value.toString()
            no_passenger.setText(numberpickkk)
            no_of_passenger=numberpickkk.toInt()


        }
        d.setNegativeButton("Cancel") { dialogInterface, i -> }
        val alertDialog = d.create()
        alertDialog.show()
    }

    fun DriverDetailss() {

        customprogress.show()
        val request = HashMap<String, String>()
        request.put("pickupAddress", pick_up_location)
        request.put("pickupLatitude", sourlat.toString())
        request.put("pickupLongitude", sourlng.toString())
        request.put("dropAddress", drop_location)
        request.put("dropLatitude", deslat.toString())
        request.put("dropLongitude", deslng.toString())
        request.put("user_id", user_id)
        request.put("time", time_count)
        request.put("distance", total_distance_apprx)
        request.put("passenger", no_of_passenger.toString())



        var driver_vec_details: Call<BookingResponse> =
            APIUtils.getServiceAPI()!!.Driver_details(request)

        driver_vec_details.enqueue(object : Callback<BookingResponse> {
            override fun onResponse(
                call: Call<BookingResponse>,
                response: Response<BookingResponse>
            ) {
                try {


                    if (response.body()!!.success.equals("true")) {

                        //Toast.makeText(requireContext(),user_id+driver_id+Current_lati+Current_longi+amount+current_loca,Toast.LENGTH_LONG).show()
                     //   Toast.makeText(requireContext(), response.body()!!.msg, Toast.LENGTH_LONG)
                         //   .show()

                       SharedPreferenceUtils.getInstance(requireContext())!!.
                       setStringValue(ConstantUtils.Booking_id,response.body()!!.data.toString())

                        val intent = Intent(requireContext(), Confirm::class.java)
                        startActivity(intent)
                        customprogress.hide()


                    } else {

                        Toast.makeText(requireContext(), response.body()!!.msg, Toast.LENGTH_LONG)
                            .show()
                        customprogress.hide()
                    }

                } catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    Toast.makeText(requireContext(),"Weak Internet Connection",Toast.LENGTH_LONG).show()
                    customprogress.hide()

                }

            }

            override fun onFailure(call: Call<BookingResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                Toast.makeText(requireContext(),"Weak Internet Connection",Toast.LENGTH_LONG).show()
                customprogress.hide()


            }

        })
    }

    fun Totaltimetaken(distance_km: Double): String {


        val km = distance_km.toInt()
        val kms_per_min = 0.4
        val mins_taken = km / kms_per_min
        val totalMinutes = mins_taken.toInt()
        if (totalMinutes < 60) {

            toatal_time_taken = totalMinutes.toString() + " " + "Mins"
            SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                ConstantUtils
                    .Toatal_time, toatal_time_taken
            )

        } else {
            var minutes = Integer.toString(totalMinutes % 60)
            minutes = if (minutes.length == 1) "0$minutes" else minutes
            (totalMinutes / 60).toString() + " hour " + minutes + "mins"
            toatal_time_taken = minutes.toString()
            SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                ConstantUtils
                    .Toatal_time, toatal_time_taken
            )

        }
      return toatal_time_taken

    }
}
// Toast.makeText(requireContext(), latitude1+longitude1, Toast.LENGTH_SHORT).show()

/*  //Put marker on map on that LatLng
val srchMarker: Marker = mMap.addMarker(
MarkerOptions().position(latLng).title("Destination")
  .icon(BitmapDescriptorFactory.fromResource(R.drawable.bb))
)

//Animate and Zoon on that map location
mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
mMap.animateCamera(CameraUpdateFactory.zoomTo(15f))*/