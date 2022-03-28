package com.geelong.user.Fragment

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.geelong.user.Activity.Search1
import com.geelong.user.Adapter.AutoCompleteAdapter
import com.geelong.user.R
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
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.IOException
import java.lang.Math.*
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var locat: String = ""
    var lati_curr: String = ""
    var longi_current: String = ""
    var pick_up_location: String = ""
    var drop_location: String = ""
    var n_of_passenger: String = ""
    var lati_drop: String = ""
    var langit_drop: String = ""
    var placesClient: PlacesClient? = null

    var autoCompleteTextView: AutoCompleteTextView? = null
    var adapter: AutoCompleteAdapter? = null

    lateinit var customprogress: Dialog
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    lateinit var pick_up_user: TextView
    var resultReceiver: ResultReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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
            /* locat=it.getString("Location","")
             lati=it.getString("Late","")
             longi=it.getString("Long","")*/


        }
        customprogress.show()

        resultReceiver = AddressResultReceiver(Handler())
        pick_up_user = rootview.findViewById(R.id.pickup_location_user)



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
        } else {
            currentLocation
        }


        val apiKey = getString(R.string.api_key)



        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), apiKey)
        }

        placesClient = Places.createClient(requireContext())
        autoCompleteTextView = rootview.findViewById<AutoCompleteTextView>(R.id.drop_location_user)


        initAutoCompleteTextView()


        var search_textt: TextView = rootview.findViewById(R.id.search_text)
        search_textt.setOnClickListener {

            pick_up_location = pickup_location_user.text.toString()
            drop_location = drop_location_user.text.toString()
            n_of_passenger = no_passenger.text.toString()


            //  getLocationFromAddress(drop_location)


            if (pick_up_location.isEmpty()) {
                Toast.makeText(requireContext(), "Please select pickup location", Toast.LENGTH_LONG).show()
            } else if (drop_location.isEmpty()) {
                Toast.makeText(requireContext(), "Please select drop location", Toast.LENGTH_LONG).show()
            } else if (n_of_passenger.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill no of passenger", Toast.LENGTH_LONG).show()
            } else {
                var toatal_distance = getKilometers(lati_curr.toDouble(), longi_current.toDouble(), lati_drop.toDouble(), langit_drop.toDouble())
                SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                        ConstantUtils.Distance, toatal_distance.toString())
                Toast.makeText(requireContext(), toatal_distance.toString(), Toast.LENGTH_LONG).show()
                (activity as Search1?)?.inte()
            }

        }

        var ivMenu1: ImageView = rootview.findViewById(R.id.ivMenu_home)
        ivMenu1.setOnClickListener {
            (activity as Search1?)?.click1()
        }
        if (lati_curr.isEmpty() || longi_current.isEmpty() || lati_drop.isEmpty() || langit_drop.isEmpty()) {
// Toast.makeText(context,"empty",Toast.LENGTH_LONG).show()
        } else {

/* var toatal_distance=getKilometers(lati_curr.toDouble(),longi_current.toDouble(),lati_drop.toDouble(),langit_drop.toDouble())
var toatlkm=toatal_distance.toFloat()*/
        }


        customprogress.hide()
        return rootview
    }

    private fun bitmapDescriptorFromVector(context: Context?, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(requireContext(), vectorResId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap =
                Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
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


    private fun initAutoCompleteTextView() {
        autoCompleteTextView?.setThreshold(1)
        autoCompleteTextView?.setOnItemClickListener(autocompleteClickListener)
        adapter = AutoCompleteAdapter(requireContext(), placesClient)
        autoCompleteTextView?.setAdapter(adapter)
    }

    private val autocompleteClickListener =
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


                            // Toast.makeText(requireContext(),,Toast.LENGTH_LONG).show()
                            drop_location = drop_location_user.text.toString()
                            getLocationFromAddress(drop_location)

                        }.addOnFailureListener { e ->
                            e.printStackTrace()

                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
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
                                    lati_curr = lati.toString()
                                    longi_current = longi.toString()

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


                SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                        ConstantUtils.LATITUDE, lati_curr)
                SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                        ConstantUtils.LONGITUDE, longi_current)
                loadMap(lati_curr, longi_current,locat)

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

    fun loadMap(lati_curr1: String, longi_current1: String,loate1:String) {
        try {
            if (lati_curr.isEmpty() || longi_current.isEmpty()) {

            } else {
                customprogress.dismiss()
                pick_up_user.setText(locat)
                val mapFragment =
                        childFragmentManager.findFragmentById(R.id.frg) as SupportMapFragment?  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
                mapFragment!!.getMapAsync { mMap ->
                    mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

                    mMap.clear()


                    val googlePlex = CameraPosition.builder()
                            .target(LatLng(lati_curr1.toDouble(), longi_current1.toDouble()))
                            .zoom(12f)
                            .bearing(0f)
                            .build()

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 1000, null)

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

    fun getLocationFromAddress(strAddress: String?) {

        val coder = Geocoder(requireContext())
        val address: List<Address>?
        try {
//Get latLng from String
            address = coder.getFromLocationName(strAddress, 5)

//check for null
            if (address == null) {
                return
            }

//Lets take first possibility from the all possibilities.
            val location = address[0]
            val latLng = LatLng(location.latitude, location.longitude)
            var la_longArr = latLng.toString().split(",", "(", ")")
            lati_drop = la_longArr[1]
            langit_drop = la_longArr[2]
            Log.d("daad", lati_drop + langit_drop)
            if (strAddress != null) {
                loadMap(lati_drop, langit_drop,strAddress)
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
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getKilometers(lat1: Double, long1: Double, lat2: Double, long2: Double): Double {
        val PI_RAD = Math.PI / 180.0
        val phi1 = lat1 * PI_RAD
        val phi2 = lat2 * PI_RAD
        val lam1 = long1 * PI_RAD
        val lam2 = long2 * PI_RAD
        return 6371.01 * acos(sin(phi1) * sin(phi2) + cos(phi1) * cos(phi2) * cos(lam2 - lam1))
    }


}