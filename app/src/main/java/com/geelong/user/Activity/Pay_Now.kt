package com.geelong.user.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.geelong.user.R

class Pay_Now : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_now)
        supportActionBar?.hide()

        var pay_Now_tv=findViewById<TextView>(R.id.pay_now_txt)

        pay_Now_tv.setOnClickListener {
            val intent = Intent(this, FareDetails::class.java)
            startActivity(intent)
        }
        var back_activity_privacy=findViewById<ImageView>(R.id.leftarrow_paynow)
        back_activity_privacy.setOnClickListener {
            val intent = Intent(this, ConfirmPick_up::class.java)
            startActivity(intent)
        }

    }
}