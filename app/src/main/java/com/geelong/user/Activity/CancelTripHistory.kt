package com.geelong.user.Activity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geelong.user.API.APIUtils
import com.geelong.user.Adapter.CancelTripHistoryAdapter
import com.geelong.user.R
import com.geelong.user.Response.CancelTripHistoryResData
import com.geelong.user.Response.CancelTripHistoryResponse
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.NetworkUtils
import com.geelong.user.Util.SharedPreferenceUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class CancelTripHistory : AppCompatActivity() {

    private var mlist: List<CancelTripHistoryResData> = ArrayList()
    lateinit var recyclerview: RecyclerView
    var user_id:String=""
    lateinit var customprogress: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_trip_history)
        supportActionBar?.hide()
        var back_activity=findViewById<ImageView>(R.id.LeftArrow_canceltrip)

        user_id= SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.USER_ID,"").toString()
        recyclerview = findViewById<RecyclerView>(R.id.rcyView_cancel_trip_details_)
        customprogress= Dialog(this)
        customprogress.setContentView(R.layout.loader_layout)

        back_activity.setOnClickListener {

            onBackPressed()
        }
        if (NetworkUtils.checkInternetConnection(this))
        {
            TripHistory()
        }

    }

    fun TripHistory()
    {
        customprogress.show()
        val request = HashMap<String, String>()
        request.put("user_id",user_id)



        var tripdetails_his : Call<CancelTripHistoryResponse> = APIUtils.getServiceAPI()!!.CancelTripHistoryy(request)

        tripdetails_his.enqueue(object : Callback<CancelTripHistoryResponse> {
            override fun onResponse(call: Call<CancelTripHistoryResponse>, response: Response<CancelTripHistoryResponse>) {
                try {

                    customprogress.hide()
                    if (response.body()!!.success.equals("true")) {
                        Log.d("response",response.body().toString())
                        if (response.body()!!.data.isEmpty())
                        {
                            Toast.makeText(this@CancelTripHistory,"DATA Not Found", Toast.LENGTH_LONG).show()
                            customprogress.hide()
                        }
                        else
                        {
                            mlist= response.body()!!.data
                            recyclerview.layoutManager= LinearLayoutManager(this@CancelTripHistory)
                            recyclerview.adapter= CancelTripHistoryAdapter(this@CancelTripHistory,mlist)
                        }




                        customprogress.hide()
                    } else {

                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    Toast.makeText(this@CancelTripHistory,"Weak Internet Connection", Toast.LENGTH_LONG).show()
                    customprogress.hide()
                }

            }

            override fun onFailure(call: Call<CancelTripHistoryResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                Toast.makeText(this@CancelTripHistory,"Weak Internet Connection", Toast.LENGTH_LONG).show()
                customprogress.hide()
            }

        })
    }
}