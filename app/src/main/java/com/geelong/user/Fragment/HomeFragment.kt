package com.geelong.user.Fragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.geelong.user.Activity.Search1
import com.geelong.user.Adapter.AutoCompleteAdapter
import com.geelong.user.R
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
import java.lang.Math.*
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var locat:String=""
    var lati:String=""
    var longi:String=""
    var pick_up_location:String=""
    var drop_location:String=""
    var n_of_passenger:String=""
    var lati_drop:String="28.6280"
    var langit_drop:String="77.3649"
    var placesClient: PlacesClient? = null
    var autoCompleteTextView: AutoCompleteTextView? = null
    var adapter: AutoCompleteAdapter? = null

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
        pick_up_user=rootview.findViewById(R.id.pickup_location_user)


        pick_up_user.setText(locat)


var toatal_distance=getKilometers(lati.toDouble(),longi.toDouble(),lati_drop.toDouble(),langit_drop.toDouble())
        var toatlkm=toatal_distance.toFloat()
      //  Toast.makeText(context,toatlkm.toString(),Toast.LENGTH_LONG).show()

        val apiKey = getString(R.string.api_key)
        /* if (apiKey.isEmpty()) {
             responseView.setText(getString(R.string.error))
             return
         }*/

        // Setup Places Client

        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), apiKey)
        }

        placesClient = Places.createClient(requireContext())
        autoCompleteTextView = rootview.findViewById<AutoCompleteTextView>(R.id.drop_location_user)


        initAutoCompleteTextView()


      var search_textt:TextView=rootview.findViewById(R.id.search_text)
        search_textt.setOnClickListener {

            pick_up_location=pickup_location_user.text.toString()
            drop_location=drop_location_user.text.toString()
            n_of_passenger=no_passenger.text.toString()





            if (pick_up_location.isEmpty())
            {
                Toast.makeText(requireContext(),"Please select pickup location",Toast.LENGTH_LONG).show()
            }
            else if(drop_location.isEmpty())
            {
                Toast.makeText(requireContext(),"Please select drop location",Toast.LENGTH_LONG).show()
            }

            else if(n_of_passenger.isEmpty())
            {
                Toast.makeText(requireContext(),"Please fill no of passenger",Toast.LENGTH_LONG).show()
            }
            else{
                (activity as Search1?)?.inte()
            }

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

                //                To specify which data types to return, pass an array of Place.Fields in your FetchPlaceRequest
                //                Use only those fields which are required.
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
/*
                        responseView.setText(
                            """
                                    ${task.place.name}
                                    ${task.place.address}
                                    """.trimIndent()
                        )
*/
                    }.addOnFailureListener { e ->
                        e.printStackTrace()
                        //  responseView.setText(e.message)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
}