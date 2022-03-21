package com.geelong.user.Fragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.geelong.user.Activity.Search1
import com.geelong.user.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.lang.Math.*



private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var locat:String=""
    var lati:String=""
    var longi:String=""
    var lati_drop:String="28.6280"
    var langit_drop:String="77.3649"

lateinit var pick_up_user:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootview= inflater.inflate(R.layout.fragment_home, container, false)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            locat=it.getString("Location","")
            lati=it.getString("Late","")
            longi=it.getString("Long","")

          /*  Toast.makeText(requireContext(),locat,Toast.LENGTH_LONG).show()*/
        }
        pick_up_user=rootview.findViewById(R.id.pick_up_user)


        pick_up_user.setText(locat)


var toatal_distance=getKilometers(lati.toDouble(),longi.toDouble(),lati_drop.toDouble(),langit_drop.toDouble())
        var toatlkm=toatal_distance.toFloat()
        Toast.makeText(context,toatlkm.toString(),Toast.LENGTH_LONG).show()

      var search_textt:TextView=rootview.findViewById(R.id.search_text)
        search_textt.setOnClickListener {

            (activity as Search1?)?.inte()
        }

        var ivMenu1:ImageView=rootview.findViewById(R.id.ivMenu_home)
        ivMenu1.setOnClickListener {
            (activity as Search1?)?.click1()
        }



        val mapFragment =
            childFragmentManager.findFragmentById(R.id.frg) as SupportMapFragment?  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment!!.getMapAsync { mMap ->
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

            mMap.clear()




            val googlePlex = CameraPosition.builder()
                .target(LatLng(lati.toDouble(),longi.toDouble()))
                .zoom(12f)
                .bearing(0f)
                .build()

           mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 1000, null)

            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(lati.toDouble(),longi.toDouble()))
                    .title(locat)
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

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
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