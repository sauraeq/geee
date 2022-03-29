package com.geelong.user.Fragment

import `in`.aabhasjindal.otptextview.OtpTextView
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.geelong.user.API.APIUtils
import com.geelong.user.Activity.CallActivity
import com.geelong.user.Activity.Chat
import com.geelong.user.Activity.DriverDetails
import com.geelong.user.Activity.Search1
import com.geelong.user.R
import com.geelong.user.Response.DriverDetails_Vch_Response
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.NetworkUtils
import com.geelong.user.Util.SharedPreferenceUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_confirm.*
import kotlinx.android.synthetic.main.fragments_driver_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DriverFragments : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var otp:OtpTextView

    var user_id:String=""
    var driver_id:String=""
    var Current_lati:String=""
    var Current_longi:String=""
    var amount:String=""
    var current_loca:String=""
    lateinit var customprogress:Dialog
    var drop_lati:String=""
    var drop_longi:String=""
    var drop_location:String=""
    var driver_lati:String=""
    var driver_longi:String=""
    var total_time:String=""
    var total_distance:String=""
    lateinit var pickuplatlang:LatLng



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootview= inflater.inflate(R.layout.fragments_driver_details, container, false)

        var call_to_driver=rootview.findViewById<RelativeLayout>(R.id.driver_call)
        var message_to_driver=rootview.findViewById<RelativeLayout>(R.id.driver_message)
        otp=rootview.findViewById(R.id.otp_drvFrg)
        customprogress= Dialog(requireContext())
        customprogress.setContentView(R.layout.loader_layout)
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

            pickuplatlang= LatLng(Current_lati.toDouble(),Current_longi.toDouble())


        }
        catch (e:Exception)
        {

        }


        if (NetworkUtils.checkInternetConnection(requireContext()))
        {
            DriverDetailss()

        }





        call_to_driver.setOnClickListener {
            val intent= Intent(context, CallActivity::class.java)
            startActivity(intent)
        }

        message_to_driver.setOnClickListener {
            val intent= Intent(context, Chat::class.java)
            startActivity(intent)
        }




        var ivMenu_driver: LinearLayout =rootview.findViewById(R.id.linear_menu)
        ivMenu_driver.setOnClickListener {
            (activity as DriverDetails?)?.click()
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

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
           DriverFragments().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun DriverDetailss()
    {
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
        request.put("time",total_time)
        request.put("distance",total_distance)






        var driver_vec_details: Call<DriverDetails_Vch_Response> = APIUtils.getServiceAPI()!!.Driver_details(request)

        driver_vec_details.enqueue(object : Callback<DriverDetails_Vch_Response> {
            override fun onResponse(call: Call<DriverDetails_Vch_Response>, response: Response<DriverDetails_Vch_Response>) {
                try {

                    customprogress.show()
                    if (response.body()!!.success.equals("true")) {

                        //Toast.makeText(requireContext(),user_id+driver_id+Current_lati+Current_longi+amount+current_loca,Toast.LENGTH_LONG).show()
                        val pro_img_url=response.body()!!.data[0].profile_photo
                        val vch_img_url=response.body()!!.data[0].vehicle_image

                        val picasso = Picasso.get()
                        picasso.load(pro_img_url).into(driver_img_drvFrg)
                        picasso.load(vch_img_url).into(vch_img_drvFrg)
                        driver_nmae_drvFrg.text=response.body()!!.data[0].name
                        vch_name_drvFrg.text=response.body()!!.data[0].vehicle_name
                        otp.setOTP(response.body()!!.data[0].otp.toString())

                        val booking_id=response.body()!!.data[0].booking_id.toString()
                        val driver_rating=response.body()!!.data[0].rating
                        driver_lati=response.body()!!.data[0].latitude
                        driver_longi=response.body()!!.data[0].longitude

                      loadmap()

                        SharedPreferenceUtils.getInstance(requireContext())?.setStringValue(ConstantUtils.Booking_id,booking_id)
                        SharedPreferenceUtils.getInstance(requireContext())?.setStringValue(ConstantUtils.Driver_Rating,driver_rating)

                        customprogress.hide()


                    } else {

                        Toast.makeText(requireContext(),"Error", Toast.LENGTH_LONG).show()
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

    fun loadmap()
    {
        Current_lati=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.LATITUDE,"").toString()
        Current_longi=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.LONGITUDE,"").toString()
        customprogress.show()
    var drivrlatlong=LatLng(driver_lati.toDouble(),driver_longi.toDouble())

        val mapFragment =
                childFragmentManager.findFragmentById(R.id.frg) as SupportMapFragment?  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment!!.getMapAsync { mMap ->
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL



            mMap.clear() //clear old markers


            val googlePlex = CameraPosition.builder()
                    .target(LatLng(driver_lati.toDouble(),driver_longi.toDouble()))
                    .zoom(22f)
                    .bearing(0f)
                    .build()

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 1000, null)
            val height = 90
            val width = 90
            val bitmapdraw = resources.getDrawable(R.drawable.placeholder) as BitmapDrawable
            val b = bitmapdraw.bitmap
            val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)
           // Toast.makeText(requireContext(),driver_lati+driver_longi,Toast.LENGTH_LONG).show()
            Log.d("loc",driver_lati+driver_longi+Current_lati+Current_longi)
            mMap.addMarker(
                    MarkerOptions()
                            .position(LatLng(driver_lati.toDouble(),driver_longi.toDouble()))
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
            )
          //  Toast.makeText(requireContext(),Current_lati+Current_longi,Toast.LENGTH_LONG).show()
            mMap.addMarker(
                    MarkerOptions()
                            .position(LatLng(Current_lati.toDouble(),Current_longi.toDouble()))
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
            )
            mMap.addPolyline(PolylineOptions().add(drivrlatlong,pickuplatlang)
                    .width(12f)
                    .color(Color.RED)
                    .geodesic(true))
            customprogress.hide()


        }
    }


}
