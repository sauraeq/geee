package com.geelong.user.Activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.NumberPicker
import android.widget.TimePicker
import android.widget.Toast
import com.geelong.user.API.APIUtils
import com.geelong.user.Fragment.SearchActivityNew
import com.geelong.user.R
import com.geelong.user.Response.RideLaterOverViewResponse
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.NetworkUtils
import com.geelong.user.Util.SharedPreferenceUtils
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_ride_later_overview.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class RideLaterOverview : AppCompatActivity() {
    var locType:String=""
    var sourlat = 0.0
    var sourlng:Double = 0.0
    var deslat = 0.0
    var deslng:Double = 0.0
    var no_of_passenger=0
    lateinit var sourcelatLng:LatLng
    lateinit var destlatLng:LatLng
    var destLoc=""
    var cal = Calendar.getInstance()
    var user_id:String=""
    var pick_up:String=""
    var drop_off:String=""
    var no_passenger_overview:String=""
    var date:String=""
    var time_overview:String=""
    var pick_up_value:String=""
    var drop_off_value:String=""
    var no_passenger_overview_value:String=""
    var date_value:String=""
    var time_overview_value:String=""
    var amount:String=""
    var time:String=""
    var distance:String=""
    var booking_id:String=""

    lateinit var customprogress:Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ride_later_overview)
        customprogress= Dialog(this)
        customprogress.setContentView(R.layout.loader_layout)
        user_id=SharedPreferenceUtils.getInstance(this)!!.getStringValue(ConstantUtils.USER_ID,
            "").toString()
        pick_up=intent.getStringExtra("pickup").toString()
        drop_off=intent.getStringExtra("drop").toString()
        no_passenger_overview=intent.getStringExtra("no_pass").toString()
        date=intent.getStringExtra("date").toString()
        time_overview=intent.getStringExtra("time").toString()
        sourlat=intent.getStringExtra("sou_lat")!!.toDouble()
        sourlng=intent.getStringExtra("sou_long")!!.toDouble()
        deslat=intent.getStringExtra("des_lat")!!.toDouble()
        deslng=SharedPreferenceUtils.getInstance(this)!!.getStringValue(ConstantUtils
            .Longitude_Drop,"")!!.toDouble()
        time=SharedPreferenceUtils.getInstance(this)!!.getStringValue(ConstantUtils
            .Pick_up_longitude,"")!!.toString()
        distance=SharedPreferenceUtils.getInstance(this)!!.getStringValue(ConstantUtils
            .Pick_up_longitude,"")!!.toString()
        booking_id=intent.getStringExtra("booking_id").toString()
        amount=SharedPreferenceUtils.getInstance(this)!!.getStringValue(ConstantUtils
            .Pick_up_longitude,"")!!.toString()


        overview_time.setText(time_overview)
        overview_date.setText(date)
        overview_pickup.setText(pick_up)
        overview_drop.setText(drop_off)
        overview_no_passenger.setText(no_passenger_overview)

        overview_pickup.setOnClickListener {
            locType = "pickloc"

            var intent = Intent(this, SearchActivityNew::class.java)
            startActivityForResult(intent, 100)
        }

        overview_drop?.setOnClickListener {
            locType = "droploc"

            var intent = Intent(this, SearchActivityNew::class.java)
            startActivityForResult(intent, 100)
        }

        overview_no_passenger.setOnClickListener {
            numberPickerCustom()
        }
        LeftArrow_ride_later_overview.setOnClickListener {
            onBackPressed()
            finish()
        }
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "yyyy-MM-dd"
                val sdf = SimpleDateFormat(myFormat, Locale.US)
               // overview_pickup = sdf.format(cal.getTime())
                overview_date.setText(sdf.format(cal.getTime()))
            }
        }

        val mTimePicker: TimePickerDialog
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)
        val second=mcurrentTime.get(Calendar.SECOND)


        mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                overview_time!!.setText(String.format("%d:%d:%d", hourOfDay, minute,second))
            }
        }, hour, minute, false)


        overview_time!!.setOnClickListener {
            mTimePicker.show()
        }

        overview_date!!.setOnClickListener {
            DatePickerDialog(this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH))
                .show()



        }
        overview_submit_btn.setOnClickListener {
             pick_up_value=overview_pickup.text.toString()
            drop_off_value=overview_drop.text.toString()
            no_passenger_overview_value =overview_no_passenger.text.toString()
            date_value=overview_date.text.toString()
            time_overview_value=overview_time.text.toString()


            if (pick_up_value.equals("") || drop_off_value.equals("")||
                no_passenger_overview_value.equals("")|| date_value.equals("") ||
                time_overview_value.equals(""))
            {

            }
            else
            {
                if (NetworkUtils.checkInternetConnection(this))
                {
                    Ridelater_Update()
                }
            }


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == 100) {
                val lat: String = data?.getStringExtra("lat").toString()
                val lng: String = data?.getStringExtra("lng").toString()
                val location: String = data?.getStringExtra("location").toString()
                if(locType.equals("pickloc")){
                    sourlat=lat.toDouble()
                    sourlng=lng.toDouble()
                    overview_pickup?.setText(location)

                }else{
                    deslat=lat.toDouble()
                    deslng=lng.toDouble()
                    destLoc=location
                    destlatLng= LatLng(deslat,deslng)
                    overview_drop?.setText(location)

                }



            }

        }catch (e:Exception){

        }

    }

    fun numberPickerCustom() {

        val d = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.date_picker, null)

        d.setView(dialogView)

        val numberPicker = dialogView.findViewById<NumberPicker>(R.id.numberpicker)

        numberPicker.minValue = 1
        numberPicker.maxValue = 4
        numberPicker.value = 1
        numberPicker.wrapSelectorWheel = true


        numberPicker.setOnValueChangedListener { numberPicker, i, i1 ->
            println("onValueChange: ")
        }
        d.setPositiveButton("Done") { dialogInterface, i ->
            println("onClick: " + numberPicker.value)
            var numberpickkk = numberPicker.value.toString()
            overview_no_passenger.setText(numberpickkk)
            no_of_passenger= numberpickkk.toInt()


        }
        d.setNegativeButton("Cancel") { dialogInterface, i -> }
        val alertDialog = d.create()
        alertDialog.show()
    }
    fun Ridelater_Update()
    {
        customprogress.show()
        val request = HashMap<String, String>()
        request.put("pickupAddress",pick_up_value)
        request.put("pickupLatitude", sourlat.toString())
        request.put("pickupLongitude",sourlng.toString())
        request.put("bookingtime",time_overview_value)
        request.put("bookingdate",date_value)
        request.put("booking_id",booking_id)
        request.put("time",time)
        request.put("distance",distance)
        request.put("drop_address",drop_off_value)
        request.put("drop_latitude",deslat.toString())
        request.put("drop_longitude",deslng.toString())
        request.put("passenger",no_passenger_overview_value)


        var tripdetails_his : Call<RideLaterOverViewResponse> = APIUtils.getServiceAPI()!!.RideLaterOverView_update(request)

        tripdetails_his.enqueue(object : Callback<RideLaterOverViewResponse> {
            override fun onResponse(call: Call<RideLaterOverViewResponse>, response: Response<RideLaterOverViewResponse>) {
                try {

                    customprogress.hide()
                    if (response.body()!!.success.equals("true")) {
                        Log.d("response",response.body().toString())
                        if (response.body()!!.data.isEmpty())
                        {
                            Toast.makeText(this@RideLaterOverview,response.body()!!.msg, Toast
                                .LENGTH_LONG)
                                .show()
                            var intent=Intent(this@RideLaterOverview,Search1::class.java)
                            startActivity(intent)
                            finish()
                            customprogress.hide()
                           // finishAffinity()
                        }
                        else
                        {


                        }

                        customprogress.hide()
                    } else {
                        Toast.makeText(this@RideLaterOverview,response.body()!!.msg, Toast
                            .LENGTH_LONG).show()
                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    Toast.makeText(this@RideLaterOverview,"Weak Internet Connection", Toast.LENGTH_LONG).show()
                    customprogress.hide()
                }

            }

            override fun onFailure(call: Call<RideLaterOverViewResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                Toast.makeText(this@RideLaterOverview,"Weak Internet Connection", Toast.LENGTH_LONG).show()
                customprogress.hide()
            }

        })
    }

}