package com.geelong.user.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geelong.user.API.APIUtils
import com.geelong.user.Adapter.TermsAdapetr
import com.geelong.user.Adapter.TripAdapter
import com.geelong.user.R
import com.geelong.user.Response.TripHistoryData
import com.geelong.user.Response.TripHistoryResponse
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.SharedPreferenceUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class TripDetails : AppCompatActivity() {
    private var mlist: List<TripHistoryData> = ArrayList()
    lateinit var recyclerview: RecyclerView
    var user_id:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)
        supportActionBar?.hide()
        var back_activity=findViewById<ImageView>(R.id.LeftArrow)

       user_id= SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.USER_ID,"").toString()
         recyclerview = findViewById<RecyclerView>(R.id.rcyView_trip_details)
       /* recyclerview.layoutManager = LinearLayoutManager(this)

        val adapter = TripAdapter(this)
        recyclerview.adapter = adapter*/
        back_activity.setOnClickListener {

            onBackPressed()
        }
        TripHistory()

    }


    fun TripHistory()
    {

        val request = HashMap<String, String>()
        request.put("user_id",user_id)



        var tripdetails_his : Call<TripHistoryResponse> = APIUtils.getServiceAPI()!!.TripHistory(request)

        tripdetails_his.enqueue(object : Callback<TripHistoryResponse> {
            override fun onResponse(call: Call<TripHistoryResponse>, response: Response<TripHistoryResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {


                        mlist= response.body()!!.data
                        recyclerview.layoutManager= LinearLayoutManager(this@TripDetails)
                        recyclerview.adapter= TripAdapter(this@TripDetails,mlist)








                    } else {


                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())



                }

            }

            override fun onFailure(call: Call<TripHistoryResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())




            }

        })
    }
}