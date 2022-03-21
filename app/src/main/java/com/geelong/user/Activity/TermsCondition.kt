package com.geelong.user.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geelong.user.API.APIUtils
import com.geelong.user.Adapter.TermsAdapetr
import com.geelong.user.R
import com.geelong.user.Response.DataXX
import com.geelong.user.Response.TermsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class TermsCondition : AppCompatActivity() {
    lateinit var recycler_privacy: RecyclerView
    private var mlist: List<DataXX> = ArrayList()
    lateinit var privacy_progress: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_condition)

        var back_activity=findViewById<ImageView>(R.id.back_from_privacy)
        privacy_progress=findViewById<RelativeLayout>(R.id.progress_loader_termsCondition)
        recycler_privacy=findViewById(R.id.recycler_privacy_Terms)

        TermsConditionss()

        supportActionBar?.hide()

        back_activity.setOnClickListener {
           onBackPressed()
        }


    }

    fun TermsConditionss()
    {
        val request = HashMap<String, String>()

        privacy_progress.visibility= View.VISIBLE

        var privacy: Call<TermsResponse> = APIUtils.getServiceAPI()!!.terms()

        privacy.enqueue(object : Callback<TermsResponse> {
            override fun onResponse(call: Call<TermsResponse>, response: Response<TermsResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {

                        mlist= response.body()!!.data
                        recycler_privacy.layoutManager= LinearLayoutManager(this@TermsCondition)
                        recycler_privacy.adapter= TermsAdapetr(this@TermsCondition,mlist)

                        privacy_progress.visibility= View.GONE






                    } else {


                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    privacy_progress.visibility= View.GONE


                }

            }

            override fun onFailure(call: Call<TermsResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                privacy_progress.visibility= View.GONE


            }

        })
    }
}