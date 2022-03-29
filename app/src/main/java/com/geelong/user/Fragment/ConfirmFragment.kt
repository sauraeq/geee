package com.geelong.user.Fragment

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
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.geelong.user.API.APIUtils
import com.geelong.user.Activity.Confirm
import com.geelong.user.Activity.ConfirmPick_up
import com.geelong.user.R
import com.geelong.user.Response.Vechail_detailsResponse
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.SharedPreferenceUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_confirm.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class ConfirmFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var lat_user:String=""
    var langi_user:String=""
    var img_url:String=""
    var distance:String=""
    var latitude_drop:String=""
    var longitude_drop:String=""
    lateinit var customprogress:Dialog
    var toatal_time_taken:String=""

    lateinit var pickuplatlang:LatLng
    lateinit var dropuplatlang:LatLng
    lateinit var toatal_distance_txtview:TextView
    lateinit var toatal_time_txtview:TextView



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
        toatal_distance_txtview=rootview.findViewById(R.id.toatl_distance_trip)
        toatal_time_txtview=rootview.findViewById(R.id.total_time_trip)
        customprogress= Dialog(requireContext())
        customprogress.setContentView(R.layout.loader_layout)



        

       lat_user= SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.LATITUDE,"").toString()
        langi_user=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.LONGITUDE,"").toString()
        distance=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Distance,"").toString()
       latitude_drop=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Lati_Drop,"").toString()
        longitude_drop=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Longi_Drop,"").toString()

        /*Toast.makeText(requireContext(),lat_user+langi_user,Toast.LENGTH_LONG).show()*/

        vehlist()

        pickuplatlang = LatLng(lat_user.toDouble(), langi_user.toDouble())
        dropuplatlang = LatLng(latitude_drop.toDouble(), longitude_drop.toDouble())
        loadmap(pickuplatlang,dropuplatlang)
        Totaltimetaken(distance.toDouble())
        if (distance.isEmpty())
        {

        }
        else
        {
            toatal_distance_txtview.text=distance+" "+"KM"
            SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(ConstantUtils
                    .Toatal_dis, distance)

        }

       // total_time_trip.text=toatal_time_taken

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
        customprogress.show()
        val request = HashMap<String, String>()
        request.put("latitude",lat_user)
        request.put("longitude",langi_user)
        request.put("distance",distance)


        // rlLoader.visibility=View.VISIBLE
        //prgs_loader.visibility=View.VISIBLE

        var veh_list: Call<Vechail_detailsResponse> = APIUtils.getServiceAPI()!!.vech_details(request)

        veh_list.enqueue(object : Callback<Vechail_detailsResponse> {
            override fun onResponse(call: Call<Vechail_detailsResponse>, response: Response<Vechail_detailsResponse>) {
                try {

                    // rlLoader.visibility=View.GONE
                  //  prgs_loader.visibility=View.GONE
                    if (response.body()!!.success.equals("true")) {

                        Log.d("response",response.body().toString())
                        img_url=response.body()!!.data[0].image
                        driver_name1.setText(response.body()!!.data[0].name)
                        vch_name.setText(response.body()!!.data[0].vehicle_name)
                        vch_number.setText(response.body()!!.data[0].vehicle_no)
                        total_fare.setText("₹"+response.body()!!.data[0].amount)
                     //   var lat_driver=response.body().data[0].

                        val picasso = Picasso.get()
                        picasso.load(img_url).into(driver_img_confirm)

                        SharedPreferenceUtils.getInstance(requireContext())?.setStringValue(ConstantUtils.Driver_Id,response.body()!!.data[0].driver_id)
                        SharedPreferenceUtils.getInstance(requireContext())?.setStringValue(ConstantUtils.Amount,response.body()!!.data[0].amount)


                        customprogress.hide()

                    } else {

                        Toast.makeText(requireContext(),"Error",Toast.LENGTH_LONG).show()
                        customprogress.hide()
                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    //  rlLoader.visibility=View.GONE
                  //  prgs_loader.visibility=View.GONE
                    Toast.makeText(requireContext(),e.message,Toast.LENGTH_LONG).show()
                    customprogress.hide()

                }

            }

            override fun onFailure(call: Call<Vechail_detailsResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                // rlLoader.visibility=View.GONE
               // prgs_loader.visibility=View.GONE
                Toast.makeText(requireContext(),t.message,Toast.LENGTH_LONG).show()
                customprogress.hide()

            }

        })
    }
    fun  loadmap(pickup_latlang:LatLng,drop_latlang:LatLng)
    {

        customprogress.show()
        val mapFragment =
                childFragmentManager.findFragmentById(R.id.frg) as SupportMapFragment?  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment!!.getMapAsync { mMap ->
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL



            mMap.clear() //clear old markers


            val googlePlex = CameraPosition.builder()
                    .target(LatLng(latitude_drop.toDouble(),longitude_drop.toDouble()))
                    .zoom(8f)
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
                            .position(LatLng(latitude_drop.toDouble(),longitude_drop.toDouble()))
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
            )
            mMap.addMarker(
                    MarkerOptions()
                            .position(LatLng(lat_user.toDouble(),langi_user.toDouble()))
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
            )
            mMap.addPolyline(PolylineOptions().add(pickup_latlang,drop_latlang)
                    .width(12f)
                    .color(Color.RED)
                    .geodesic(true))
            customprogress.hide()


        }
    }
    fun Totaltimetaken(distance_km:Double)
    {
      val km=distance_km.toInt()
        val kms_per_min = 0.5
        val mins_taken = km/kms_per_min
        val totalMinutes = mins_taken.toInt()
        if (totalMinutes < 60) {

            toatal_time_taken=totalMinutes.toString()+" "+"Mins"
            SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(ConstantUtils
                    .Toatal_time, toatal_time_taken)
            toatal_time_txtview.text=toatal_time_taken
           // total_time_trip.setText(toatal_time_taken)

        } else {
            var minutes = Integer.toString(totalMinutes % 60)
            minutes = if (minutes.length == 1) "0$minutes" else minutes
            (totalMinutes / 60).toString() + " hour " + minutes + "mins"
            toatal_time_taken=minutes.toString()
            SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(ConstantUtils
                    .Toatal_time, toatal_time_taken)
            toatal_time_txtview.text=toatal_time_taken
            //total_time_trip.setText(toatal_time_taken)
        }


    }

}


