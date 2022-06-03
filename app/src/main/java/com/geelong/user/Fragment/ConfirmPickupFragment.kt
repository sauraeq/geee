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
import com.geelong.user.Activity.*
import com.geelong.user.Adapter.AutoCompleteAdapter
import com.geelong.user.R
import com.geelong.user.Response.BookingResponse
import com.geelong.user.Response.CnfBookingResponse
import com.geelong.user.Response.ConfirmBookingResponse
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
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ConfirmPickupFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    var locat:String=""
    var lati:String=""
    var longi:String=""
    var toatal_time_taken:String=""
    lateinit var current_location:TextView
    var placesClient: PlacesClient? = null
    var user_id:String=""
    var driver_id:String=""
    var Current_lati:String=""
    var Current_longi:String=""
    var amount:String=""
    var current_loca:String=""
    var booking_id:String=""
    var drop_lati:String=""
    var drop_longi:String=""
    var drop_location:String=""
    var total_time:String=""
    var total_distance:String=""
    var adapter: AutoCompleteAdapter? = null
   lateinit var customprogress:Dialog
   lateinit var confirm_search_btn:TextView
   lateinit var pick_up_confirm_texview:TextView
   var Confirm_pickup_total_distance:String=""
    var sourlat = 0.0
    var sourlng:Double = 0.0
   lateinit var remarks:EditText
     var remarks_string:String="..."

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
        remarks=rootview.findViewById(R.id.edittext_remarks)

        customprogress= Dialog(requireContext())
        customprogress.setContentView(R.layout.loader_layout)


        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }


        locat=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils
            .Current_Location,"").toString()
        current_location.setText(locat)



        val apiKey = getString(R.string.api_key)



        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), apiKey)
        }

        placesClient = Places.createClient(requireContext())

       /* initAutoCompleteTextView()*/
        back_go_activityy.setOnClickListener {

            (activity as ConfirmPick_up)?.inte()

        }
        confirm_pick_up.setOnClickListener {

        }



        try {
            driver_id=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils
                .Driver_Id,"").toString()
            total_time=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils
                .Toatal_time,"").toString()
            total_distance=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Distance,"").toString()
            user_id=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.USER_ID,"").toString()
            driver_id=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Driver_Id,"").toString()
            Current_lati=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Pick_up_Latitude,"").toString()
            Current_longi=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Pick_up_longitude,"").toString()
            amount=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Amount,"").toString()
            current_loca=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Current_Location,"").toString()
            drop_lati=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Latitude_Drop,"").toString()
            drop_longi=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils
                .Longitude_Drop,"").toString()
            drop_location=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils
                .Drop_location,"").toString()
            booking_id=SharedPreferenceUtils.getInstance(requireContext())!!.
            getStringValue(ConstantUtils.Booking_id,"").toString()
            loadmap(current_loca,Current_lati,Current_longi)

        }
        catch (e:Exception)
        {

        }

        current_location.setOnClickListener {
            var intent = Intent(requireContext(), SearchActivityNew::class.java)
            startActivityForResult(intent, 100)
        }

        pick_up_confirm_texview.setOnClickListener {

                remarks_string=remarks.text.toString()

            if (drop_location.isEmpty())
            {
                Toast.makeText(requireContext(),"drop is empty",Toast.LENGTH_LONG).show()
            }
            else if (Current_lati.isEmpty()  )
            {
                Toast.makeText(requireContext(),"confirm pickup latitude empty",Toast.LENGTH_LONG)
                    .show()
            }
            else if(Current_longi.isEmpty())
            {
                Toast.makeText(requireContext(),"confirm pickup longitude empty",Toast.LENGTH_LONG)
                    .show()
            }
            else if(current_location.text.toString().isNullOrEmpty())
            {
                Toast.makeText(requireContext(),"confirm pickup  empty",Toast.LENGTH_LONG)
                    .show()
            }
            else
            {
                Confirm_Booking()
               // loadMap(confirm_pickup_latitude,confirm_pickup_longitude,drop_location)
            }
        }
       /* pick_up_confirm_texview.setOnClickListener {
            if (NetworkUtils.checkInternetConnection(requireContext()))
            {
            //    DriverDetailss()
                val intent=Intent(requireContext(),DriverDetails::class.java)
                startActivity(intent)

            }

        }*/

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

    /*private fun initAutoCompleteTextView() {
        current_location?.setThreshold(1)
        current_location?.setOnItemClickListener(autocompleteClickListener_drop)
        adapter = AutoCompleteAdapter(requireContext(), placesClient)
        current_location?.setAdapter(adapter)
    }*/

    fun loadmap(var1:String,var_2:String,var_3:String)
    {

        try {
            val mapFragment =
                childFragmentManager.findFragmentById(R.id.frg) as SupportMapFragment?  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
            mapFragment!!.getMapAsync { mMap ->
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

                mMap.clear()

                val googlePlex = CameraPosition.builder()
                    .target(LatLng(var_2.toDouble(),var_3.toDouble()))
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
                        .position(LatLng(var_2.toDouble(),var_3.toDouble()))
                        .title(var1)
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                )

            }
        }catch (e:Exception){

        }

    }


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


                        // Toast.makeText(requireContext(),,Toast.LENGTH_LONG).show()
                        val inputMethodManager = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(current_loc_textview.getWindowToken(), 0)

                        pickup_cnf_location = current_loc_textview.text.toString()
                        SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                            ConstantUtils.Current_Location, pickup_cnf_location)
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
            Current_lati = la_longArr[1]
            Current_longi = la_longArr[2]
       var dis_confirm_pick_up=getKilometers(Current_lati.toDouble(),
           Current_longi.toDouble(),drop_lati.toDouble(),drop_longi.toDouble())
            total_distance=roundOffDecimal(dis_confirm_pick_up.toDouble()).toString()

            Totaltimetaken(total_distance.toDouble())


            SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(ConstantUtils
                .Distance,total_distance)



            Toast.makeText(requireContext(),Current_lati+Current_longi
                ,Toast.LENGTH_LONG).show()
            SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                ConstantUtils.Confirm_PickUp_Latitude, Current_lati)
            SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                ConstantUtils.Confirm_Pick_Up_Longitude, Current_longi)

            Log.d("daad", Current_lati + Current_longi)
            if (strAddress != null) {
                loadMap(Current_lati, Current_longi,strAddress)
            }

          //  Confirm_Booking(pickup_cnf_location,confirm_pickup_latitude,confirm_pickup_longitude)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }*/

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == 100) {
                val lat: String = data?.getStringExtra("lat").toString()
                val lng: String = data?.getStringExtra("lng").toString()
                val location: String = data?.getStringExtra("location").toString()
                 var location_pincode=""
                if(location.equals("null"))
                {
                    current_location?.setText(locat)

                }else{
                    Current_lati=lat
                    Current_longi=lng
                    location_pincode=getlocation(Current_lati.toDouble(),Current_longi.toDouble())
                    current_location?.setText(location+","+location_pincode)
                   // current_location?.setText(location)
                    SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(ConstantUtils
                        .Pick_up_Latitude,Current_lati.toString()).toString()
                    SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(ConstantUtils
                        .Pick_up_longitude,Current_longi.toString()).toString()
                    SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(ConstantUtils
                        .Current_Location,location+","+location_pincode).toString()

                    var dist=getKilometers(Current_lati.toDouble(),Current_longi.toDouble(),
                        drop_lati.toDouble(),drop_longi.toDouble())
                    total_distance=roundOffDecimal(dist.toDouble()).toString()
                    SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(ConstantUtils
                        .Distance,total_distance).toString()
                    Totaltimetaken(total_distance.toDouble())
                }




            }

        }catch (e:Exception){

        }

    }


    fun Confirm_Booking(){

        customprogress.show()
        val request = HashMap<String, String>()
        request.put("pickupAddress", current_loca)
        request.put("pickupLatitude", Current_lati)
        request.put("pickupLongitude", Current_longi)
        request.put("dropAddress", drop_location)
        request.put("dropLatitude", drop_lati)
        request.put("dropLongitude", drop_longi)
        request.put("user_id", user_id)
        request.put("time", total_time)
        request.put("distance", total_distance)
        request.put("driver_id", user_id)
        request.put("booking_id", booking_id)
        request.put("driver_id", driver_id)
        request.put("remark",remarks_string)




        var driver_vec_details: Call<CnfBookingResponse> =
            APIUtils.getServiceAPI()!!.confirm_booking(request)

        driver_vec_details.enqueue(object : Callback<CnfBookingResponse> {
            override fun onResponse(
                call: Call<CnfBookingResponse>,
                response: Response<CnfBookingResponse>
            ) {
                try {


                    if (response.body()!!.success.equals("true")) {
val intent=Intent(requireContext(),DriverDetails::class.java)
                        startActivity(intent)
                        //Toast.makeText(requireContext(),user_id+driver_id+Current_lati+Current_longi+amount+current_loca,Toast.LENGTH_LONG).show()
                       /* Toast.makeText(requireContext(), response.body()!!.msg, Toast.LENGTH_LONG)
                            .show()
*/
                        customprogress.hide()


                    } else {

                        Toast.makeText(requireContext(), response.body()!!.msg, Toast.LENGTH_LONG)
                            .show()
                        customprogress.hide()
                    }

                } catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    Toast.makeText(requireContext(),"Weak Internet Connection", Toast.LENGTH_LONG).show()
                    customprogress.hide()

                }

            }

            override fun onFailure(call: Call<CnfBookingResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                Toast.makeText(requireContext(),"Weak Internet Connection", Toast.LENGTH_LONG).show()
                customprogress.hide()


            }

        })
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

    fun Totaltimetaken(distance_km: Double): String {


        val km = distance_km.toInt()
        val kms_per_min = 0.4
        val mins_taken = km / kms_per_min
        val totalMinutes = mins_taken.toInt()
        if (totalMinutes < 60) {

            total_time = totalMinutes.toString() + " " + "Mins"
            SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                ConstantUtils
                    .Toatal_time, total_time
            )
            /*   total_time_trip_act.text = toatal_time_taken*/


        } else {
            var minutes = Integer.toString(totalMinutes % 60)
            minutes = if (minutes.length == 1) "0$minutes" else minutes
            (totalMinutes / 60).toString() + " hour " + minutes + "mins"
            total_time = minutes.toString()
            SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                ConstantUtils
                    .Toatal_time, total_time
            )
            /*total_time_trip_act.text = toatal_time_taken*/

        }
        return total_time

    }
    fun getlocation(lat:Double,long:Double): String {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address>?
        val address: Address?
        var fulladdress = ""
        var postalCode=""
        addresses = geocoder.getFromLocation(lat,long, 1)

        if (addresses.isNotEmpty()) {
            address = addresses[0]
            fulladdress = address.getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex
            var city = address.getLocality();
            var state = address.getAdminArea();
            var country = address.getCountryName();
            postalCode = address.getPostalCode();
            var knownName = address.getFeatureName();

            /* locat = fulladdress
             pick_up_user.setText(locat)

             SharedPreferenceUtils.getInstance(requireContext())
                 ?.setStringValue(ConstantUtils.Current_Location, locat)

             loadMap(sourlat.toString(), sourlng.toString(), locat)
             // Only if available else return NULL*/
        } else{
            fulladdress = "Location not found"
        }
        return postalCode

    }

}


/*   fun DriverDetailss()
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
    }*/
