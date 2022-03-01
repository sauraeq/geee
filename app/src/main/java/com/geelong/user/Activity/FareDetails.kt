package com.geelong.user.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.geelong.user.R

class FareDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fare_details)
        supportActionBar?.hide()
        var pay_Now_fare_tv=findViewById<TextView>(R.id.pay_now_fare_txt)
        pay_Now_fare_tv.setOnClickListener {
            val intent = Intent(this, Payment_method::class.java)
            startActivity(intent)
        }

    }
}