package com.geelong.user.Activity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.geelong.user.API.APIUtils
import com.geelong.user.Adapter.RideLaterAdapter
import com.geelong.user.R
import com.geelong.user.Response.RideLaterHistoryRes
import com.geelong.user.Response.RideLaterHistoryResData
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.SharedPreferenceUtils
import kotlinx.android.synthetic.main.activity_ride_later_history.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class RideLaterHistory : AppCompatActivity() {

    lateinit var customprogress:Dialog
    var user_idd:String=""
    private var mlist: List<RideLaterHistoryResData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ride_later_history)
        customprogress= Dialog(this)
        customprogress.setContentView(R.layout.loader_layout)
        user_idd=SharedPreferenceUtils.getInstance(this)!!.getStringValue(ConstantUtils.USER_ID,
            "").toString()
        TripHistory()

        LeftArrow_ride_later.setOnClickListener {
            onBackPressed()
        }
      /*  rcyView_ride_later.layoutManager = LinearLayoutManager(this)

        val adapter = RideLaterAdapter(this)
        rcyView_ride_later.adapter = adapter*/
    }

    fun TripHistory()
    {
        customprogress.show()
        val request = HashMap<String, String>()
        request.put("user_id",user_idd)



        var tripdetails_his : Call<RideLaterHistoryRes> = APIUtils.getServiceAPI()!!.RideLaterHistory(request)

        tripdetails_his.enqueue(object : Callback<RideLaterHistoryRes> {
            override fun onResponse(call: Call<RideLaterHistoryRes>, response: Response<RideLaterHistoryRes>) {
                try {

                    customprogress.hide()
                    if (response.body()!!.success.equals("true")) {
                        Log.d("response",response.body().toString())
                        if (response.body()!!.data.isEmpty())
                        {
                            Toast.makeText(this@RideLaterHistory,"DATA Not Found", Toast.LENGTH_LONG).show()
                            customprogress.hide()
                        }
                        else
                        {
                            mlist= response.body()!!.data
                            rcyView_ride_later.layoutManager= LinearLayoutManager(this@RideLaterHistory)
                            rcyView_ride_later.adapter= RideLaterAdapter(this@RideLaterHistory,mlist)

                        }




                        customprogress.hide()
                    } else {
                        Toast.makeText(this@RideLaterHistory,response.body()!!.msg,Toast
                            .LENGTH_LONG).show()
                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    Toast.makeText(this@RideLaterHistory,"Weak Internet Connection", Toast.LENGTH_LONG).show()
                    customprogress.hide()
                }

            }

            override fun onFailure(call: Call<RideLaterHistoryRes>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                Toast.makeText(this@RideLaterHistory,"Weak Internet Connection", Toast.LENGTH_LONG).show()
                customprogress.hide()
            }

        })
    }

}