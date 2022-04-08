package com.geelong.user.Fragment

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.geelong.user.API.APIUtils
import com.geelong.user.Activity.ConfirmPick_up
import com.geelong.user.Activity.Pay_Now
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
import kotlinx.android.synthetic.main.confirm_pickup_fragment.*
import kotlinx.android.synthetic.main.fragments_driver_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ConfirmPickupFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var locat:String=""
    var lati:String=""
    var longi:String=""
    lateinit var current_location:TextView

    var user_id:String=""
    var driver_id:String=""
    var Current_lati:String=""
    var Current_longi:String=""
    var amount:String=""
    var current_loca:String=""
    var drop_lati:String=""
    var drop_longi:String=""
    var drop_location:String=""
    var driver_lati:String=""
    var driver_longi:String=""
    var total_time:String=""
    var total_distance:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootview= inflater.inflate(R.layout.confirm_pickup_fragment, container, false)
        var cardview11=rootview.findViewById<CardView>(R.id.cardview11)
       var  back_go_activityy=rootview.findViewById<LinearLayout>(R.id.back_go_activity)
        var  confirm_pick_up=rootview.findViewById<LinearLayout>(R.id.confirm_pick_Up_layout)
        current_location=rootview.findViewById(R.id.current_loc_textview)


        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            locat=it.getString("Location","")
            lati=it.getString("Late","")
            longi=it.getString("Long","")
        }

       // Toast.makeText(context,locat+lati+longi,Toast.LENGTH_LONG).show()
       // current_loc_textview.setText(locat)
        current_location.text=locat

        SharedPreferenceUtils.getInstance(requireContext())?.setStringValue(ConstantUtils.CurrentL,locat)


        back_go_activityy.setOnClickListener {
           /* val intent = Intent(requireContext(), Confirm::class.java)
            startActivity(intent)*/
            (activity as ConfirmPick_up)?.inte()

        }
        confirm_pick_up.setOnClickListener {
            val intent = Intent(requireContext(), Pay_Now::class.java)
            startActivity(intent)
        }


        /* var ivMenu1: ImageView =rootview.findViewById(R.id.ivMenu1)
         ivMenu1.setOnClickListener {
             (activity as Search1?)?.click()
         }

         */

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
        if (NetworkUtils.checkInternetConnection(requireContext()))
        {
            DriverDetailss()

        }

        loadmap()


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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun loadmap()
    {
        val mapFragment =
                childFragmentManager.findFragmentById(R.id.frg) as SupportMapFragment?  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment!!.getMapAsync { mMap ->
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

            mMap.clear() //clear old markers

            val googlePlex = CameraPosition.builder()
                    .target(LatLng(lati.toDouble(),longi.toDouble()))
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
                            .position(LatLng(lati.toDouble(),longi.toDouble()))
                            .title(locat)
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
            )

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


                    if (response.body()!!.success.equals("true")) {

                        //Toast.makeText(requireContext(),user_id+driver_id+Current_lati+Current_longi+amount+current_loca,Toast.LENGTH_LONG).show()
                        val pro_img_url=response.body()!!.data[0].profile_photo
                        val vch_img_url=response.body()!!.data[0].vehicle_image
                        var booking_id=response.body()!!.data[0].booking_id

                        SharedPreferenceUtils.getInstance(requireContext())?.setStringValue(ConstantUtils
                            .Booking_id,booking_id.toString())




                    } else {

                        Toast.makeText(requireContext(),"Error", Toast.LENGTH_LONG).show()

                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    Toast.makeText(requireContext(),e.message, Toast.LENGTH_LONG).show()


                }

            }

            override fun onFailure(call: Call<DriverDetails_Vch_Response>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                Toast.makeText(requireContext(),t.message, Toast.LENGTH_LONG).show()


            }

        })
    }

}
