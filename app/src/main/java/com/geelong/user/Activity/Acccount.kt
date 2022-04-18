package com.geelong.user.Activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import com.geelong.user.API.APIUtils
import com.geelong.user.R
import com.geelong.user.Response.LoginResponse
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.NetworkUtils
import com.geelong.user.Util.SharedPreferenceUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_acccount.*
import kotlinx.android.synthetic.main.activity_driver_details.*
import kotlinx.android.synthetic.main.fragment_confirm.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class Acccount : AppCompatActivity() {
     var mobile_number_account:String=""
    lateinit var customprogress:Dialog
    var token_id:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acccount)
        var edit_profile=findViewById<ImageView>(R.id.edit_profile_imageview)
        var back_act=findViewById<ImageView>(R.id.back_activity)
        customprogress= Dialog(this)
        customprogress.setContentView(R.layout.loader_layout)


        mobile_number_account=SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.User_Mobile_Number,"").toString()
        token_id=SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.TokenId,"")
                .toString()

        edit_profile.setOnClickListener {
            val intent = Intent(this, AccountEdit::class.java)
            startActivity(intent)
        }
        if (NetworkUtils.checkInternetConnection(this))
        {
            profile()
        }

        supportActionBar?.hide()
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        back_act.setOnClickListener {
           onBackPressed()
        }

    }

    fun profile()
    {
        customprogress.show()
        val request = HashMap<String, String>()
        request.put("mobile",mobile_number_account)
        request.put("device_tokanid",token_id)



        var login_in: Call<LoginResponse> = APIUtils.getServiceAPI()!!.Login(request)

        login_in.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {

                        if (response.body()!!.data.isEmpty())
                        {
                            Toast.makeText(this@Acccount,"DATA Not Found",Toast.LENGTH_LONG).show()

                        }
                        else
                        {
                            User_name.text=response.body()!!.data[0].name
                            User_mobile.text=response.body()!!.data[0].phone
                            User_email.text=response.body()!!.data[0].email
                            User_Address.text=response.body()!!.data[0].address
                            User_gender.text=response.body()!!.data[0].gender
                            var img_url=response.body()!!.data[0].profile_photo
                            SharedPreferenceUtils.getInstance(this@Acccount)?.setStringValue(ConstantUtils.Image_Url,img_url)
                            if(img_url.isEmpty())
                            {
                                val picasso=Picasso.get()
                                picasso.load(R.drawable.driverimg).into(User_profile_pic)
                            }
                            else{
                                val picasso=Picasso.get()
                                picasso.load(img_url).into(User_profile_pic)
                            }

                        }



                        customprogress.hide()


                    } else {

                        Toast.makeText(this@Acccount,response.body()!!.msg, Toast.LENGTH_LONG)
                            .show()
                        customprogress.hide()
                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    Toast.makeText(this@Acccount,e.message, Toast.LENGTH_LONG).show()
                    customprogress.hide()

                }

            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                Toast.makeText(this@Acccount,t.message, Toast.LENGTH_LONG).show()
                customprogress.hide()
            }

        })
    }


}