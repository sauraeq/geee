package com.geelong.user.Fragment

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.geelong.user.API.APIUtils
import com.geelong.user.Activity.ConfirmPick_up
import com.geelong.user.Activity.Pay_Now
import com.geelong.user.Adapter.AutoCompleteAdapter
import com.geelong.user.R
import com.geelong.user.Response.DriverDetails_Vch_Response
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.NetworkUtils
import com.geelong.user.Util.SharedPreferenceUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.confirm_pickup_fragment.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragments_driver_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ConfirmPickupFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    var locat:String=""
    var lati:String=""
    var longi:String=""
    lateinit var current_location:AutoCompleteTextView
    var placesClient: PlacesClient? = null
    var user_id:String=""
    var driver_id:String=""
    var Current_lati:String=""
    var Current_longi:String=""
    var amount:String=""
    var current_loca:String=""
    var drop_lati:String=""
    var drop_longi:String=""
    var drop_location:String=""
    var pickup_cnf_location:String=""
    var driver_lati:String=""
    var driver_longi:String=""
    var total_time:String=""
    var total_distance:String=""
    var confirm_pickup_latitude:String=""
    var confirm_pickup_longitude:String=""
    var adapter: AutoCompleteAdapter? = null
   lateinit var customprogress:Dialog
   lateinit var confirm_search_btn:TextView
   lateinit var pick_up_confirm_texview:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootview= inflater.inflate(R.layout.confirm_pickup_fragment, container, false)
        confirm_search_btn=rootview.findViewById(R.id.confirm_search_pickup)
        var cardview11=rootview.findViewById<CardView>(R.id.cardview11)
       var  back_go_activityy=rootview.findViewById<LinearLayout>(R.id.back_go_activity)
        var  confirm_pick_up=rootview.findViewById<LinearLayout>(R.id.confirm_pick_Up_layout)
        current_location=rootview.findViewById(R.id.current_loc_textview)
        pick_up_confirm_texview =rootview.findViewById(R.id.pick_up_confirm_txt1)

        customprogress= Dialog(requireContext())
        customprogress.setContentView(R.layout.loader_layout)


        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }


        locat=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.CurrentL,"").toString()
        current_location.setText(locat)



        val apiKey = getString(R.string.api_key)



        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), apiKey)
        }

        placesClient = Places.createClient(requireContext())


        back_go_activityy.setOnClickListener {

            (activity as ConfirmPick_up)?.inte()

        }
        confirm_pick_up.setOnClickListener {

        }
        initAutoCompleteTextView()


        try {
            total_time=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils
                .Toatal_time,"").toString()
            total_distance=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Distance,"").toString()
            user_id=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.USER_ID,"").toString()
            driver_id=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Driver_Id,"").toString()
            Current_lati=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.LATITUDE,"").toString()
            Current_longi=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.LONGITUDE,"").toString()
            amount=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Amount,"").toString()
            current_loca=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.CurrentL,"").toString()
            drop_lati=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Lati_Drop,"").toString()
            drop_longi=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils
                .Longi_Drop,"").toString()
            drop_location=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils
                .Drop_location,"").toString()




        }
        catch (e:Exception)
        {

        }


        loadmap()




        confirm_search_btn.setOnClickListener {
            if (drop_location.isEmpty())
            {
                Toast.makeText(requireContext(),"drop is empty",Toast.LENGTH_LONG).show()
            }
            else if (confirm_pickup_latitude.isEmpty())
            {
                Toast.makeText(requireContext(),"confirm pickup latitude empty",Toast.LENGTH_LONG)
                    .show()
            }
            else if(confirm_pickup_longitude.isEmpty())
            {
                Toast.makeText(requireContext(),"confirm pickup longitude empty",Toast.LENGTH_LONG)
                    .show()
            }
            else
            {
               // loadMap(confirm_pickup_latitude,confirm_pickup_longitude,drop_location)
            }
        }
        pick_up_confirm_texview.setOnClickListener {
            if (NetworkUtils.checkInternetConnection(requireContext()))
            {
                DriverDetailss()

            }

        }

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
        current_location?.setThreshold(1)
        current_location?.setOnItemClickListener(autocompleteClickListener_drop)
        adapter = AutoCompleteAdapter(requireContext(), placesClient)
        current_location?.setAdapter(adapter)
    }

    fun loadmap()
    {
        val mapFragment =
                childFragmentManager.findFragmentById(R.id.frg) as SupportMapFragment?  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment!!.getMapAsync { mMap ->
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

            mMap.clear()

            val googlePlex = CameraPosition.builder()
                    .target(LatLng(Current_lati.toDouble(),Current_longi.toDouble()))
                    .zoom(20f)
                    .bearing(0f)
                    .build()

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 1000, null)
            val height = 90
            val width = 90
            val bitmapdraw = resources.getDrawable(R.drawable.placeholder) as BitmapDrawable
            val b = bitmapdraw.bitmap
            val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)
            mMap.addMarker(
                    MarkerOptions()
                            .position(LatLng(Current_lati.toDouble(),Current_longi.toDouble()))
                           .title(current_loca)
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
            )

        }
    }


    private val autocompleteClickListener_drop =
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
                        val inputMethodManager = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(current_loc_textview.getWindowToken(), 0)

                        pickup_cnf_location = current_loc_textview.text.toString()
                        SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                            ConstantUtils.CurrentL, pickup_cnf_location)
                        getLocationFromAddress(pickup_cnf_location)

                    }.addOnFailureListener { e ->
                        e.printStackTrace()

                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    fun getLocationFromAddress(strAddress: String?) {

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
            confirm_pickup_latitude = la_longArr[1]
            confirm_pickup_longitude = la_longArr[2]
            Toast.makeText(requireContext(),confirm_pickup_latitude+confirm_pickup_longitude
                ,Toast.LENGTH_LONG).show()
            SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                ConstantUtils.LATITUDE, confirm_pickup_latitude)
            SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                ConstantUtils.LONGITUDE, confirm_pickup_longitude)

            Log.d("daad", confirm_pickup_latitude + confirm_pickup_longitude)
            if (strAddress != null) {
                loadMap(confirm_pickup_latitude, confirm_pickup_longitude,strAddress)
            }


        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadMap(lati_curr1: String, longi_current1: String,loate1:String) {
        try {
            if (lati_curr1.isEmpty() || longi_current1.isEmpty()) {

            } else {

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
    fun DriverDetailss()
    {

        customprogress.show()
        val request = HashMap<String, String>()
        request.put("pickupAddress",current_loca)
        request.put("pickupLatitude",Current_lati)
        request.put("pickupLongitude",Current_longi)
        request.put("dropAddress",drop_location)
        request.put("dropLatitude",drop_lati)
        request.put("dropLongitude",drop_longi)
        request.put("user_id",user_id )
        request.put("driver_id",driver_id)
        request.put("amount",amount)
        request.put("time","25")
        request.put("distance",total_distance)






        var driver_vec_details: Call<DriverDetails_Vch_Response> = APIUtils.getServiceAPI()!!.Driver_details(request)

        driver_vec_details.enqueue(object : Callback<DriverDetails_Vch_Response> {
            override fun onResponse(call: Call<DriverDetails_Vch_Response>, response: Response<DriverDetails_Vch_Response>) {
                try {


                    if (response.body()!!.success.equals("true")) {

                        //Toast.makeText(requireContext(),user_id+driver_id+Current_lati+Current_longi+amount+current_loca,Toast.LENGTH_LONG).show()
                        val pro_img_url=response.body()!!.data[0].profile_photo
                        val vch_img_url=response.body()!!.data[0].vehicle_image
                        var booking_id=response.body()!!.data[0].booking_id

                        SharedPreferenceUtils.getInstance(requireContext())?.setStringValue(ConstantUtils
                            .Booking_id,booking_id.toString())
                        SharedPreferenceUtils.getInstance(requireContext())?.setStringValue(ConstantUtils
                            .Driver_name,response.body()!!.data[0].name)
                        SharedPreferenceUtils.getInstance(requireContext())?.setStringValue(ConstantUtils
                            .Driver_latitude,response.body()!!.data[0].latitude)
                        SharedPreferenceUtils.getInstance(requireContext())?.setStringValue(ConstantUtils
                            .Driver_longitude,response.body()!!.data[0].longitude)
                        SharedPreferenceUtils.getInstance(requireContext())?.setStringValue(ConstantUtils
                            .Driver_profile_photo,response.body()!!.data[0].profile_photo)
                        SharedPreferenceUtils.getInstance(requireContext())?.setStringValue(ConstantUtils
                            .Vechile_image,response.body()!!.data[0].vehicle_image)
                        SharedPreferenceUtils.getInstance(requireContext())?.setStringValue(ConstantUtils
                            .Vechilename,response.body()!!.data[0].vehicle_name)
                        SharedPreferenceUtils.getInstance(requireContext())?.setStringValue(ConstantUtils
                            .Vechile_number,response.body()!!.data[0].vehicle_no)
                        SharedPreferenceUtils.getInstance(requireContext())?.setStringValue(ConstantUtils
                            .Trip_Otp,response.body()!!.data[0].otp.toString())
                        SharedPreferenceUtils.getInstance(requireContext())?.setStringValue(ConstantUtils
                            .Driver_Rating,response.body()!!.data[0].rating.toString())

                        val intent = Intent(requireContext(), Pay_Now::class.java)
                        startActivity(intent)
                        customprogress.hide()


                    } else {

                        Toast.makeText(requireContext(),response.body()!!.msg, Toast.LENGTH_LONG)
                            .show()
                        customprogress.hide()
                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    Toast.makeText(requireContext(),e.message, Toast.LENGTH_LONG).show()
                    customprogress.hide()

                }

            }

            override fun onFailure(call: Call<DriverDetails_Vch_Response>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                Toast.makeText(requireContext(),t.message, Toast.LENGTH_LONG).show()
                customprogress.hide()


            }

        })
    }




}
