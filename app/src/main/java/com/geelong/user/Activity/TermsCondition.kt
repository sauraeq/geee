package com.geelong.user.Activity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geelong.user.API.APIUtils
import com.geelong.user.Adapter.TermsAdapetr
import com.geelong.user.R
import com.geelong.user.Response.TermDataRes
import com.geelong.user.Response.TermsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class TermsCondition : AppCompatActivity() {
    lateinit var recycler_privacy: RecyclerView
    private var mlist: List<TermDataRes> = ArrayList()
    lateinit var privacy_progress: RelativeLayout
    lateinit var customprogress: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_condition)

        var back_activity=findViewById<ImageView>(R.id.back_from_privacy)
        privacy_progress=findViewById<RelativeLayout>(R.id.progress_loader_termsCondition)
        recycler_privacy=findViewById(R.id.recycler_privacy_Terms)
        customprogress= Dialog(this)
        customprogress.setContentView(R.layout.loader_layout)

        TermsConditionss()



        back_activity.setOnClickListener {
           onBackPressed()
        }


    }

    fun TermsConditionss()
    {
        customprogress.show()
        val request = HashMap<String, String>()



        var privacy: Call<TermsResponse> = APIUtils.getServiceAPI()!!.terms()

        privacy.enqueue(object : Callback<TermsResponse> {
            override fun onResponse(call: Call<TermsResponse>, response: Response<TermsResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {

                        mlist= response.body()!!.data
                        recycler_privacy.layoutManager= LinearLayoutManager(this@TermsCondition)
                        recycler_privacy.adapter= TermsAdapetr(this@TermsCondition,mlist)



                        customprogress.hide()




                    } else {


                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())

                    customprogress.hide()


                }

            }

            override fun onFailure(call: Call<TermsResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())

                customprogress.hide()


            }

        })
    }
}