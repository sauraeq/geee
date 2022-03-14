package com.geelong.user.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import com.geelong.user.API.APIUtils
import com.geelong.user.R
import com.geelong.user.Response.LoginResponse
import com.geelong.user.Response.SignUpResponse
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.NetworkUtils
import com.geelong.user.Util.SharedPreferenceUtils
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class Sign_Up : AppCompatActivity() {

    var radioGroup: RadioGroup ?=null
    lateinit var radioButton: RadioButton

    var name:String=""
    var email:String=""
    var address:String=""
    var mobile_number:String=""
    var mobile_number_login:String=""
    var gender:String=""
    lateinit var user_name:TextInputEditText
    lateinit var user_email:TextInputEditText
    lateinit var user_address:TextInputEditText
    lateinit var user_mobile:EditText
    lateinit var user_login_mobile:EditText

    lateinit var prgs_loader:RelativeLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()

        radioGroup=findViewById(R.id.radioGroup1)

        user_name=findViewById(R.id.name_edit_text)
        user_email=findViewById(R.id.email_edit_Text)
        user_address=findViewById(R.id.address_edit_text)
        user_mobile=findViewById(R.id.mobile_id_text)
         user_login_mobile=findViewById(R.id.login_edittext_mobile)

        prgs_loader=findViewById(R.id.progress_loader)




        var txt_1=findViewById<TextView>(R.id.tvSignUpTab1)
        var txt_2=findViewById<TextView>(R.id.tvSiginTab2)
        var txt_3=findViewById<TextView>(R.id.tvSignUpTab3)
        var txt_4=findViewById<TextView>(R.id.tvSiginTab4)
        var sign_in=findViewById<LinearLayout>(R.id.login_linear_next)
        var sign_up1=findViewById<TextView>(R.id.sign_up_text)
        var sign_up=findViewById<LinearLayout>(R.id.sign_up_linearlayout)


        var linear_sign_up=findViewById<LinearLayout>(R.id.singnup_linera)
        var linear_sign_in=findViewById<LinearLayout>(R.id.signIn_linear)
        var linear_sign_up_content=findViewById<LinearLayout>(R.id.signUp_content)
        var Linear_sign_in_content=findViewById<LinearLayout>(R.id.signin_content)
        var Linear_sign_Up_Text=findViewById<LinearLayout>(R.id.Sign_Up_Text_linear)
        var Linear_soical_media_icon=findViewById<LinearLayout>(R.id.Social_media_Linear)
        var Linear_terms_condition=findViewById<LinearLayout>(R.id.term_Linear)

        try {
            val intSelectButton: Int = radioGroup!!.checkedRadioButtonId
            radioButton = findViewById(intSelectButton)
            gender=radioButton.text.toString()
        } catch (e:Exception)
        {

        }

        linear_sign_up.setVisibility(View.GONE)
        linear_sign_up_content.setVisibility(View.GONE)
        Linear_sign_in_content.setVisibility(View.VISIBLE)
        linear_sign_in.setVisibility(View.VISIBLE)
        Linear_soical_media_icon.setVisibility(View.GONE)
        Linear_sign_Up_Text.setVisibility(View.GONE)
        Linear_terms_condition.setVisibility(View.GONE)
        txt_4.setOnClickListener {

            linear_sign_up.setVisibility(View.GONE)
            linear_sign_up_content.setVisibility(View.GONE)
            Linear_sign_in_content.setVisibility(View.VISIBLE)
            linear_sign_in.setVisibility(View.VISIBLE)
            Linear_soical_media_icon.setVisibility(View.GONE)
            Linear_sign_Up_Text.setVisibility(View.GONE)
            Linear_terms_condition.setVisibility(View.GONE)

        }
        txt_1.setOnClickListener {

            linear_sign_in.setVisibility(View.GONE)
            Linear_sign_in_content.setVisibility(View.GONE)
            linear_sign_up.setVisibility(View.VISIBLE)
            linear_sign_up_content.setVisibility(View.VISIBLE)
            Linear_soical_media_icon.setVisibility(View.VISIBLE)
            Linear_sign_Up_Text.setVisibility(View.VISIBLE)
            Linear_terms_condition.setVisibility(View.VISIBLE)
        }
        txt_2.setOnClickListener {
            linear_sign_in.setVisibility(View.VISIBLE)
            linear_sign_up.setVisibility(View.GONE)
            linear_sign_up_content.setVisibility(View.GONE)
            Linear_sign_in_content.setVisibility(View.VISIBLE)
            linear_sign_in.setVisibility(View.VISIBLE)
            Linear_soical_media_icon.setVisibility(View.GONE)
            Linear_sign_Up_Text.setVisibility(View.GONE)
            Linear_terms_condition.setVisibility(View.GONE)


        }
        txt_3.setOnClickListener {
            linear_sign_in.setVisibility(View.GONE)
            linear_sign_up.setVisibility(View.VISIBLE)
            Linear_sign_in_content.setVisibility(View.GONE)
            linear_sign_up.setVisibility(View.VISIBLE)
            linear_sign_up_content.setVisibility(View.VISIBLE)
            Linear_soical_media_icon.setVisibility(View.VISIBLE)
            Linear_sign_Up_Text.setVisibility(View.VISIBLE)
            Linear_terms_condition.setVisibility(View.VISIBLE)
        }
        sign_in.setOnClickListener {
            mobile_number_login=user_login_mobile.text.toString()

            if (mobile_number_login.isEmpty())
            {
                Toast.makeText(this,"Please Enter Mobile Number",Toast.LENGTH_LONG).show()
            }
            else{

                if(NetworkUtils.checkInternetConnection(this))
                {
                    login()
                }

            }




        }
        sign_up.setOnClickListener {



            name=user_name.getText().toString()
            email=user_email.getText().toString()
            address=user_address.getText().toString()
            mobile_number=user_mobile.getText().toString()


            Toast.makeText(this,name+email+address+mobile_number+gender,Toast.LENGTH_LONG).show()


              if (name.isEmpty())
              {
                  Toast.makeText(this,"Please Enter Name",Toast.LENGTH_LONG).show()
              }
              else if(email.isEmpty())
              {
                  Toast.makeText(this,"Please Enter Email",Toast.LENGTH_LONG).show()
              }
              else if(address.isEmpty())
              {
                  Toast.makeText(this,"Please Enter Address",Toast.LENGTH_LONG).show()
              }
              else if(mobile_number.isEmpty())
              {
                  Toast.makeText(this,"Please Enter Mobile Number",Toast.LENGTH_LONG).show()
              }

              else {
                  if(NetworkUtils.checkInternetConnection(this))
                  {
                      signup()
                  }
                  else{
                      Toast.makeText(this,"dshdg",Toast.LENGTH_LONG).show()
                  }
              }





           // var intent = Intent(this, Sign_Up::class.java)
           // startActivity(intent)



        }


    }

    fun signup()
    {
        val request = HashMap<String, String>()
        request.put("name",name)
        request.put("email",email)
        request.put("mobile",mobile_number)
        request.put("address",address)
        request.put("gender","Male")
      // rlLoader.visibility=View.VISIBLE
        prgs_loader.visibility=View.VISIBLE



        var signup: Call<SignUpResponse> = APIUtils.getServiceAPI()!!.SignUp(request)

        signup.enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                try {

                    prgs_loader.visibility=View.GONE
                    if (response.body()!!.success.equals("true")) {




                        Toast.makeText(this@Sign_Up,response.body()!!.msg.toString(),Toast.LENGTH_LONG).show()

                         var intent = Intent(this@Sign_Up, Sign_Up::class.java)

                          startActivity(intent)
                       /* SharedPreferenceUtils.getInstance(this@Signup)?.setStringValue(ConstantUtils.USER_ID,response.body()!!.data.user_id)
                        SharedPreferenceUtils.getInstance(this@Signup)?.setStringValue(ConstantUtils.EMAIL_ID,response.body()!!.data.email)
                        SharedPreferenceUtils.getInstance(this@Signup)?.setStringValue(ConstantUtils.IS_LOGIN,"true")
                        var intent = Intent(this@Signup, DashboardActivity::class.java)
                        startActivity(intent)
                        finishAffinity()*/

                    } else {
                       // Toast.makeText(this@Signup,response.body()!!.msg.toString(), Toast.LENGTH_LONG).show()
                        Toast.makeText(this@Sign_Up,"ERror",Toast.LENGTH_LONG).show()
                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    prgs_loader.visibility=View.GONE
                    Toast.makeText(this@Sign_Up,e.message,Toast.LENGTH_LONG).show()

                }

            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                prgs_loader.visibility=View.GONE
                Toast.makeText(this@Sign_Up,t.message,Toast.LENGTH_LONG).show()

            }

        })
    }
    fun login()
    {
        val request = HashMap<String, String>()
        request.put("mobile",mobile_number_login)


        // rlLoader.visibility=View.VISIBLE
        prgs_loader.visibility=View.VISIBLE

        var login_in: Call<LoginResponse> = APIUtils.getServiceAPI()!!.Login(request)

        login_in.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                try {

                    // rlLoader.visibility=View.GONE
                    prgs_loader.visibility=View.GONE
                    if (response.body()!!.success.equals("true")) {
                        Toast.makeText(this@Sign_Up,response.body()!!.msg.toString(),Toast.LENGTH_LONG).show()
                        var intent=Intent(this@Sign_Up, Otp::class.java)
                        intent.putExtra("otp",response.body()!!.data.otp.toString())
                        startActivity(intent)

                         SharedPreferenceUtils.getInstance(this@Sign_Up)?.setStringValue(ConstantUtils.USER_ID,response.body()!!.success)
                        /* SharedPreferenceUtils.getInstance(this@Signup)?.setStringValue(ConstantUtils.EMAIL_ID,response.body()!!.data.email)*/
                        /* SharedPreferenceUtils.getInstance(this@Signup)?.setStringValue(ConstantUtils.IS_LOGIN,"true")
                         var intent = Intent(this@Signup, DashboardActivity::class.java)
                         startActivity(intent)*/
                         finishAffinity()

                    } else {
                        // Toast.makeText(this@Signup,response.body()!!.msg.toString(), Toast.LENGTH_LONG).show()

                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    //  rlLoader.visibility=View.GONE
                    prgs_loader.visibility=View.GONE

                }

            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                // rlLoader.visibility=View.GONE
                prgs_loader.visibility=View.GONE

            }

        })
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }
}