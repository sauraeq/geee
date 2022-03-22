package com.geelong.user.Fragment

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.geelong.user.Activity.ConfirmPick_up
import com.geelong.user.Activity.Pay_Now
import com.geelong.user.R
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.SharedPreferenceUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.confirm_pickup_fragment.*


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

        Toast.makeText(context,locat+lati+longi,Toast.LENGTH_LONG).show()
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
            val bitmapdraw = resources.getDrawable(R.drawable.maparroww) as BitmapDrawable
            val b = bitmapdraw.bitmap
            val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)
            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(lati.toDouble(),longi.toDouble()))
                    .title(locat)
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

}
