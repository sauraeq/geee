package com.geelong.user.Activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geelong.user.API.APIUtils
import com.geelong.user.Adapter.PrivacyAdapter
import com.geelong.user.R
import com.geelong.user.Response.PivacyData
import com.geelong.user.Response.PrivacyResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap


class Privacy_Policy : AppCompatActivity() {

    lateinit var recycler_privacy:RecyclerView
    private var mlist: List<PivacyData> = ArrayList()
    lateinit var privacy_progress:RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)
        supportActionBar?.hide()

        var back_activity_privacy=findViewById<ImageView>(R.id.LeftArrow2)
         privacy_progress=findViewById<RelativeLayout>(R.id.progress_loader_privacy)
        recycler_privacy=findViewById(R.id.recycler_privacy_policy)
        back_activity_privacy.setOnClickListener {
          onBackPressed()
        }

        privacy()

    }

    fun privacy()
    {
        val request = HashMap<String, String>()

        privacy_progress.visibility=View.VISIBLE

        var privacy: Call<PrivacyResponse> = APIUtils.getServiceAPI()!!.privacy()

        privacy.enqueue(object : Callback<PrivacyResponse> {
            override fun onResponse(call: Call<PrivacyResponse>, response: Response<PrivacyResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {

                        mlist= response.body()!!.data
                        recycler_privacy.layoutManager= LinearLayoutManager(this@Privacy_Policy)
                        recycler_privacy.adapter=PrivacyAdapter(this@Privacy_Policy,mlist)

                        privacy_progress.visibility=View.GONE






                    } else {


                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    privacy_progress.visibility=View.GONE


                }

            }

            override fun onFailure(call: Call<PrivacyResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                privacy_progress.visibility=View.GONE


            }

        })
    }
}