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
import com.geelong.user.Activity.Confirm
import com.geelong.user.Activity.ConfirmPick_up
import com.geelong.user.Activity.Otp
import com.geelong.user.R
import com.geelong.user.Activity.Search1
import com.geelong.user.Response.LoginResponse
import com.geelong.user.Response.Vechail_detailsResponse
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.SharedPreferenceUtils
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
class ConfirmFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var lat_user:String=""
    var langi_user:String=""

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
        val rootview= inflater.inflate(R.layout.fragment_confirm, container, false)
        var cardview11=rootview.findViewById<CardView>(R.id.cardview11)
        var back_linera_layoutt=rootview.findViewById<LinearLayout>(R.id.back_linera_layout)
        var pick_up_confirmm=rootview.findViewById<TextView>(R.id.pick_up_confirm)

       lat_user= SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.LATITUDE,"").toString()
        langi_user=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.LONGITUDE,"").toString()

        /*Toast.makeText(requireContext(),lat_user+langi_user,Toast.LENGTH_LONG).show()*/

        vehlist()

        pick_up_confirmm.setOnClickListener {
            val intent = Intent(requireContext(), ConfirmPick_up::class.java)
            startActivity(intent)
        }
        back_linera_layoutt.setOnClickListener {


            (activity as Confirm)?.inte()
        }


       /* var ivMenu1: ImageView =rootview.findViewById(R.id.ivMenu1)
        ivMenu1.setOnClickListener {
            (activity as Search1?)?.click()
        }

        */


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
            val height = 90
            val width = 90
            val bitmapdraw = resources.getDrawable(R.drawable.maparroww) as BitmapDrawable
            val b = bitmapdraw.bitmap
            val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)
            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(28.6201514,77.342835))
                    .title("Spider Man")
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
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
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun vehlist()
    {
        val request = HashMap<String, String>()
        request.put("latitude",lat_user)
        request.put("longitude",langi_user)


        // rlLoader.visibility=View.VISIBLE
        //prgs_loader.visibility=View.VISIBLE

        var veh_list: Call<Vechail_detailsResponse> = APIUtils.getServiceAPI()!!.vech_details(request)

        veh_list.enqueue(object : Callback<Vechail_detailsResponse> {
            override fun onResponse(call: Call<Vechail_detailsResponse>, response: Response<Vechail_detailsResponse>) {
                try {

                    // rlLoader.visibility=View.GONE
                  //  prgs_loader.visibility=View.GONE
                    if (response.body()!!.success.equals("true")) {
                        Toast.makeText(requireContext(),response.body()!!.msg.toString(),Toast.LENGTH_LONG).show()
                      /*  var intent=Intent(requireContext(), Otp::class.java)
                        intent.putExtra("otp",response.body()!!.data.otp.toString())
                        startActivity(intent)*/

                        SharedPreferenceUtils.getInstance(requireContext())?.setStringValue(ConstantUtils.USER_ID,response.body()!!.success)
                        /* SharedPreferenceUtils.getInstance(this@Signup)?.setStringValue(ConstantUtils.EMAIL_ID,response.body()!!.data.email)*/
                        /* SharedPreferenceUtils.getInstance(this@Signup)?.setStringValue(ConstantUtils.IS_LOGIN,"true")
                         var intent = Intent(this@Signup, DashboardActivity::class.java)
                         startActivity(intent)*/
                        //finishAffinity()

                    } else {
                        // Toast.makeText(this@Signup,response.body()!!.msg.toString(), Toast.LENGTH_LONG).show()
                        Toast.makeText(requireContext(),"Error",Toast.LENGTH_LONG).show()
                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    //  rlLoader.visibility=View.GONE
                  //  prgs_loader.visibility=View.GONE
                    Toast.makeText(requireContext(),e.message,Toast.LENGTH_LONG).show()

                }

            }

            override fun onFailure(call: Call<Vechail_detailsResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                // rlLoader.visibility=View.GONE
               // prgs_loader.visibility=View.GONE
                Toast.makeText(requireContext(),t.message,Toast.LENGTH_LONG).show()

            }

        })
    }
}
