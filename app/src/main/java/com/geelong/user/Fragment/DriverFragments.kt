package com.geelong.user.Fragment

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
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
        // Inflate the layout for this fragment
        val rootview= inflater.inflate(R.layout.fragments_driver_details, container, false)

        var call_to_driver=rootview.findViewById<RelativeLayout>(R.id.driver_call)
        var message_to_driver=rootview.findViewById<RelativeLayout>(R.id.driver_message)

        DriverDetailss()

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


        val mapFragment =
            childFragmentManager.findFragmentById(R.id.frg) as SupportMapFragment?  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment!!.getMapAsync { mMap ->
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

            mMap.clear() //clear old markers




            val googlePlex = CameraPosition.builder()
                .target(LatLng(28.6201514,77.342835))
                .zoom(12f)
                .bearing(0f)
                .build()

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 1000, null)

            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(28.6201514,77.342835))
                    .title("Spider Man")
                    .icon(bitmapDescriptorFromVector(activity, R.drawable.maparroww))
            )


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
        request.put("pickupAddress","noida")
        request.put("pickupLatitude","25.878787")
        request.put("pickupLongitude","77.848475")
        request.put("dropAddress","ghaziabad")
        request.put("dropLatitude","28.9898564")
        request.put("dropLongitude","77.84848")
        request.put("user_id","20" )
        request.put("driver_id","55")
        request.put("amount","55")
        request.put("time","20")




        // rlLoader.visibility=View.VISIBLE
        //prgs_loader.visibility=View.VISIBLE

        var driver_vec_details: Call<DriverDetails_Vch_Response> = APIUtils.getServiceAPI()!!.Driver_details(request)

        driver_vec_details.enqueue(object : Callback<DriverDetails_Vch_Response> {
            override fun onResponse(call: Call<DriverDetails_Vch_Response>, response: Response<DriverDetails_Vch_Response>) {
                try {

                    // rlLoader.visibility=View.GONE
                    //  prgs_loader.visibility=View.GONE
                    if (response.body()!!.success.equals("true")) {

                        Toast.makeText(requireContext(),response.body()!!.msg, Toast.LENGTH_LONG).show()



                    } else {

                        Toast.makeText(requireContext(),"Error", Toast.LENGTH_LONG).show()
                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    //  rlLoader.visibility=View.GONE
                    //  prgs_loader.visibility=View.GONE
                    Toast.makeText(requireContext(),e.message, Toast.LENGTH_LONG).show()

                }

            }

            override fun onFailure(call: Call<DriverDetails_Vch_Response>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                // rlLoader.visibility=View.GONE
                // prgs_loader.visibility=View.GONE
                Toast.makeText(requireContext(),t.message, Toast.LENGTH_LONG).show()

            }

        })
    }


}
