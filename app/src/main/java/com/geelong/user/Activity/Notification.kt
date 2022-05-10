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
import com.geelong.user.Adapter.NotificationAdapter
import com.geelong.user.Adapter.TripAdapter

import com.geelong.user.R

import com.geelong.user.Response.NotificationData
import com.geelong.user.Response.NotificationResponse
import com.geelong.user.Response.TripHistoryResponse
import com.geelong.user.Response.ViewNotificationResponse
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.SharedPreferenceUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class Notification : AppCompatActivity() {

    lateinit var customDialog:Dialog
    private var mlist: List<NotificationData> = ArrayList()
    lateinit var recyclervieww:RecyclerView
    var user_id:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        supportActionBar?.hide()
        customDialog= Dialog(this)
        customDialog.setContentView(R.layout.loader_layout)
        user_id= SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.USER_ID,"").toString()

        var back_activity=findViewById<ImageView>(R.id.LeftArrow1)
        Notificat()

         recyclervieww = findViewById<RecyclerView>(R.id.rcyView_trip_notification)

        /*recyclervieww.layoutManager = LinearLayoutManager(this)

        val adapter = NotificationAdapter()
        recyclervieww.adapter = adapter*/

        back_activity.setOnClickListener {

            onBackPressed()
        }

    }

    fun Notificat()
    {
        customDialog.show()
        val request = HashMap<String, String>()
        request.put("user_id",user_id)



        var tripdetails_his : Call<NotificationResponse> = APIUtils.getServiceAPI()!!.Noti(request)

        tripdetails_his.enqueue(object : Callback<NotificationResponse> {
            override fun onResponse(call: Call<NotificationResponse>, response: Response<NotificationResponse>) {
                try {

                    customDialog.hide()
                    if (response.body()!!.success.equals("true")) {
                        Log.d("response",response.body().toString())
                        if (response.body()!!.data.isEmpty())
                        {
                            Toast.makeText(this@Notification,"DATA Not Found", Toast.LENGTH_LONG).show()
                            customDialog.hide()
                        }
                        else
                        {
                            mlist= response.body()!!.data
                            recyclervieww.layoutManager= LinearLayoutManager(this@Notification)
                            recyclervieww.adapter= NotificationAdapter(this@Notification,mlist)
                            ViewNotification()
                        }




                        customDialog.hide()
                    } else {

                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    Toast.makeText(this@Notification,"Weak Internet Connection", Toast.LENGTH_LONG).show()
                    customDialog.hide()
                }

            }

            override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                Toast.makeText(this@Notification,"Weak Internet Connection", Toast.LENGTH_LONG).show()
                customDialog.hide()
            }

        })
    }

    fun ViewNotification()
    {

        var user_id=SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils
            .USER_ID,"")
            .toString()

        val request = HashMap<String, String>()
        request.put("user_id",user_id)




        var view_Noti: Call<ViewNotificationResponse> = APIUtils.getServiceAPI()!!.ViewNotification(request)

        view_Noti.enqueue(object : Callback<ViewNotificationResponse> {
            override fun onResponse(call: Call<ViewNotificationResponse>, response: Response<ViewNotificationResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {
                        Toast.makeText(this@Notification,response.body()!!.msg,Toast
                            .LENGTH_LONG)
                            .show()


                    } else {


                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())

                    Toast.makeText(this@Notification,"Weak Internet Connection", Toast.LENGTH_LONG).show()

                }

            }

            override fun onFailure(call: Call<ViewNotificationResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())

                Toast.makeText(this@Notification,"Weak Internet Connection", Toast.LENGTH_LONG).show()

            }

        })
    }

}