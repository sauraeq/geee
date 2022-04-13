package com.geelong.user.Fragment

import `in`.aabhasjindal.otptextview.OtpTextView
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
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
import com.geelong.user.Response.BookingStatusResponse
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.NetworkUtils
import com.geelong.user.Util.SharedPreferenceUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.kaopiz.kprogresshud.KProgressHUD
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.canceltrippopup.*
import kotlinx.android.synthetic.main.fragment_confirm.*
import kotlinx.android.synthetic.main.fragments_driver_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
    var booking_id:String=""
    lateinit var pickuplatlang:LatLng
    lateinit var Cancel_booking_btnn:TextView
    lateinit var driver_ratingg:TextView
    lateinit var driver_img_drvFrg:CircleImageView
    lateinit var vch_img_drvFrg:ImageView
    lateinit var driver_nmae_drvFrg:TextView
    lateinit var vechile_number_drvFrg:TextView
     lateinit var vch_name_drvFrg:TextView


    var driver_profile_pic:String=""
    var driver_name:String=""
    var vechile_name:String=""
    var vechile_number:String=""
    var vechile_img:String=""
    var trip_otp:String=""
    var rating_driver=""
    var driver_latitude=""
    var driver_longitude=""
    var user_latitude=""
    var user_lonitude=""
    lateinit var custom_progress:Dialog




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
       /* custom_progress= Dialog(requireContext())
        customprogress.setContentView(R.layout.loader_layout)*/

        var call_to_driver=rootview.findViewById<RelativeLayout>(R.id.driver_call)
        var message_to_driver=rootview.findViewById<RelativeLayout>(R.id.driver_message)
        otp=rootview.findViewById(R.id.otp_drvFrg)
        customprogress= Dialog(requireContext())
        customprogress.setContentView(R.layout.loader_layout)
        driver_ratingg=rootview.findViewById(R.id.driver_rating_txt)
        Cancel_booking_btnn=rootview.findViewById(R.id.Cancel_booking_btn)
        driver_img_drvFrg=rootview.findViewById(R.id.driver_img_drvFrg)
        vch_img_drvFrg=rootview.findViewById(R.id.vch_img_drvFrg)
        driver_nmae_drvFrg=rootview.findViewById(R.id.driver_nmae_drvFrg)
        vechile_number_drvFrg=rootview.findViewById(R.id.vechile_number_drvFrg)
        vch_name_drvFrg=rootview.findViewById(R.id.vch_name_drvFrg)

        try {
            booking_id=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils
                .Booking_id,"").toString()
            total_time=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils
                    .Toatal_time,"").toString()
            total_distance=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Distance,"").toString()

            driver_id=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Driver_Id,"").toString()
            Current_lati=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.LATITUDE,"").toString()
            Current_longi=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.LONGITUDE,"").toString()
            amount=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Amount,"").toString()

            driver_profile_pic=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Driver_profile_photo,"").toString()
            driver_name=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Driver_name,"").toString()
            vechile_name=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Vechilename,"").toString()
            vechile_number=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Vechile_number,"").toString()
            trip_otp=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Trip_Otp,"").toString()
            rating_driver=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Driver_Rating,"").toString()
            driver_latitude=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Driver_latitude,"").toString()
            driver_longitude=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Driver_longitude,"").toString()
            user_latitude=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.LATITUDE,"").toString()
            user_lonitude=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.LONGITUDE,"").toString()
            vechile_img=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Vechile_image,"").toString()
            pickuplatlang= LatLng(user_latitude.toDouble(),user_lonitude.toDouble())


        }
        catch (e:Exception)
        {
Toast.makeText(requireContext(),e.toString(),Toast.LENGTH_LONG).show()
        }


        if (NetworkUtils.checkInternetConnection(requireContext()))
        {
          //  DriverDetailss()
            Booking_status()

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
        Cancel_booking_btnn.setOnClickListener {
            (activity as DriverDetails?)?.GoTo()
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

    fun Booking_status()
    {
        val request = HashMap<String, String>()
        request.put("booking_id",booking_id)







        var driver_vec_details: Call<BookingStatusResponse> = APIUtils.getServiceAPI()!!.booking_status(request)

        driver_vec_details.enqueue(object : Callback<BookingStatusResponse> {
            override fun onResponse(call: Call<BookingStatusResponse>, response: Response<BookingStatusResponse>) {
                try {

                    /*customprogress.show()*/
                    if (response.body()!!.success.equals("true")) {

                        if (response.body()!!.data[0].status.equals("1"))

                        {

                            showDialog()
                            var handler: Handler? = null
                            handler = Handler()
                            handler.postDelayed(Runnable {
                                Booking_status()

                            }, 5000)
                           // SharedPreferenceUtils.getInstance(requireContext())?.removeKey
                            (ConstantUtils.Booking_id)
                            /*val intent=Intent(requireContext(),Search1::class.java)
                            startActivity(intent)*/

                        }
                        else
                        { customprogress.hide()
                            val picasso = Picasso.get()
                            picasso.load(driver_profile_pic).into(driver_img_drvFrg)
                            picasso.load(vechile_img).into(vch_img_drvFrg)
                            driver_nmae_drvFrg.setText(driver_name)
                            vch_name_drvFrg.setText(vechile_name)
                            vechile_number_drvFrg.setText(vechile_number)
                            otp.setOTP(trip_otp)
                            driver_ratingg.setText(rating_driver)
                        }

                    }
                    else {

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

            override fun onFailure(call: Call<BookingStatusResponse>, t: Throwable) {
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
    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.getWindow()!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dialog.setCancelable(true)
        dialog.setContentView(R.layout.wait_for_trip_popup)
        lateinit var button: LinearLayout

     /*   KProgressHUD.create(requireContext())
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setDetailsLabel("Downloading data")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .show();*/

        /*button = dialog.findViewById(R.id.payment_success)

        button.setOnClickListener {
            dialog.dismiss()

        }
        dialog.cancal_popup_img.setOnClickListener {
            dialog.dismiss()
            SharedPreferenceUtils.getInstance(requireContext())?.removeKey(ConstantUtils.Booking_id)

        }*/




        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()

        //dialog.window?.setLayout(700,750)

    }

}
