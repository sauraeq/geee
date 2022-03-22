package com.geelong.user.Activity

import `in`.aabhasjindal.otptextview.OTPListener
import `in`.aabhasjindal.otptextview.OtpTextView
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.geelong.user.R
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.SharedPreferenceUtils


class Otp : AppCompatActivity() {
    var  get_otp:String=""
    var otp_value:String=""
    lateinit var customProgress:Dialog
    lateinit var prgss_loader: RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        supportActionBar?.hide()
        var verify_txt=findViewById<TextView>(R.id.verify_now)
        var otp_edittext=findViewById<OtpTextView>(R.id.otp_view)

        prgss_loader=findViewById(R.id.progress_loader_otp)
        customProgress= Dialog(this)
        customProgress.setContentView(R.layout.loader_layout)

        get_otp= intent.getStringExtra("otp").toString()
        Toast.makeText(this,get_otp,Toast.LENGTH_LONG).show()

        otp_edittext?.otpListener = object : OTPListener {
            override fun onInteractionListener() {
               // Toast.makeText(this@Otp, "The OTP is ", Toast.LENGTH_SHORT).show()
            }

            override fun onOTPComplete(otp: String) {

                otp_value=otp
              //  Toast.makeText(this@Otp, otp_value, Toast.LENGTH_SHORT).show()
            }
        }


        verify_txt.setOnClickListener {
            customProgress.show()

            if (otp_value==get_otp)
            {


                Toast.makeText(this@Otp,"OTP Verified SUccessfully", Toast.LENGTH_SHORT).show()
                SharedPreferenceUtils.getInstance(this)?.setStringValue(ConstantUtils.OTP,get_otp)
                var intent= Intent(this, Search1::class.java)
                startActivity(intent)
                customProgress.hide()


            }
            else
            {
                customProgress.hide()
                Toast.makeText(this@Otp,"Please Enter Correct OTP", Toast.LENGTH_SHORT).show()
            }

        }




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
