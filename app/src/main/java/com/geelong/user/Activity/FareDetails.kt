package com.geelong.user.Activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.geelong.user.R
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.SharedPreferenceUtils
import kotlinx.android.synthetic.main.activity_fare_details.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FareDetails : AppCompatActivity() {
     var fare_amount:String=""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fare_details)
        supportActionBar?.hide()
        var pay_Now_fare_tv=findViewById<TextView>(R.id.pay_now_fare_txt)
        pay_Now_fare_tv.setOnClickListener {
            val intent = Intent(this, Payment_method::class.java)
            startActivity(intent)
        }
        var back_activity_privacy=findViewById<ImageView>(R.id.leftarrow_fare)
        back_activity_privacy.setOnClickListener {
       /*     val intent = Intent(this, Pay_Now::class.java)
            startActivity(intent)*/
            onBackPressed()
        }
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formatted = current.format(formatter)

        cuurent_time.setText(formatted.toString())


        fare_amount= SharedPreferenceUtils.getInstance(this)?.getStringValue(
            ConstantUtils
                .Amount,"")
            .toString()

        toatal_fare_txtview.setText("$"+""+fare_amount)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun currenttime()
    {
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formatted = current.format(formatter)
    }
}