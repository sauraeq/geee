package com.geelong.user

import `in`.aabhasjindal.otptextview.OTPListener
import `in`.aabhasjindal.otptextview.OtpTextView
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class Otp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        supportActionBar?.hide()
        var verify_txt=findViewById<TextView>(R.id.verify_now)
        verify_txt.setOnClickListener {
            var intent= Intent(this,Search1::class.java)
            startActivity(intent)
        }

        var   otpTextView = findViewById<OtpTextView>(R.id.otp_view)
        otpTextView.setOtpListener(object : OTPListener {
            override fun onInteractionListener() {

            }

            override fun onOTPComplete(otp: String) {

            }
        })


    }
    fun st() {


    }

    }
