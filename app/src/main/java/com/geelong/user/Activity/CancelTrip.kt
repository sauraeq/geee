package com.geelong.user.Activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.geelong.user.API.APIUtils
import com.geelong.user.Adapter.CancelTripAdapter
import com.geelong.user.R
import com.geelong.user.Response.CancelSubmitResponse
import com.geelong.user.Response.CancelTripReasonResponse
import com.geelong.user.Response.CancelTripResponsedata
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.NetworkUtils
import com.geelong.user.Util.SharedPreferenceUtils
import kotlinx.android.synthetic.main.activity_cancel_trip.*
import kotlinx.android.synthetic.main.canceltrippopup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CancelTrip : AppCompatActivity(),CancelTripAdapter.PractiseInterface {
    lateinit var customprogress:Dialog
    private var mlist: List<CancelTripResponsedata> = ArrayList()
    lateinit var Cancel_trip_submit:TextView
    var reason:String=""
    var bookig_id:String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_trip)
        supportActionBar?.hide()
        Cancel_trip_submit=findViewById(R.id.Cancel_trip_submit_btn)
        customprogress= Dialog(this)
        customprogress.setContentView(R.layout.loader_layout)
        CancelTripReson()
        bookig_id= SharedPreferenceUtils.getInstance(this)?.getStringValue(
            ConstantUtils
            .Booking_id,"")
            .toString()


        cancel_LeftArrow.setOnClickListener {
            onBackPressed()
        }





    }

    override fun onclick(name: String) {
        reason=name
        /*  Toast.makeText(this,reason,Toast.LENGTH_LONG).show()*/
        if (reason.isEmpty()) {

        } else {
            Cancel_trip_submit.setOnClickListener {

                if (NetworkUtils.checkInternetConnection(this)) {
                    CancelTripSubmit(reason)
                }
            }
        }



    }

    fun CancelTripReson()
    {
        customprogress.show()
        val request = HashMap<String, String>()



        var cancel_trip: Call<CancelTripReasonResponse> = APIUtils.getServiceAPI()!!.CancelReason()

        cancel_trip.enqueue(object : Callback<CancelTripReasonResponse> {
            override fun onResponse(call: Call<CancelTripReasonResponse>, response: Response<CancelTripReasonResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {

                        mlist= response.body()!!.data
                        rcyView_Cancel_trip.layoutManager= LinearLayoutManager(this@CancelTrip)
                        rcyView_Cancel_trip.adapter= CancelTripAdapter(this@CancelTrip,this@CancelTrip,mlist)
                       // rcyView_Cancel_trip.findViewHolderForItemId()



                        customprogress.hide()




                    } else {


                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())

                    customprogress.hide()


                }

            }

            override fun onFailure(call: Call<CancelTripReasonResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())

                customprogress.hide()


            }

        })
    }

    fun CancelTripSubmit(var1:String)
    {
        customprogress.show()
        val request = HashMap<String, String>()
        request.put("booking_id",bookig_id)
        request.put("description",var1)



        var cancel_trip: Call<CancelSubmitResponse> = APIUtils.getServiceAPI()!!.CancelResultSubmission(request)

        cancel_trip.enqueue(object : Callback<CancelSubmitResponse> {
            override fun onResponse(call: Call<CancelSubmitResponse>, response: Response<CancelSubmitResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {

                     Toast.makeText(this@CancelTrip,response.body()!!.msg.toString(),Toast.LENGTH_LONG).show()
                        var Inte=Intent(this@CancelTrip,Search1::class.java)
                        startActivity(Inte)
                        finish()


                        customprogress.hide()




                    } else {


                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())

                    customprogress.hide()


                }

            }

            override fun onFailure(call: Call<CancelSubmitResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())

                customprogress.hide()


            }

        })
    }



}