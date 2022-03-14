package com.geelong.user.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.geelong.user.R
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.SharedPreferenceUtils

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Handler().postDelayed(Runnable {
          var str:String= SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.USER_ID,"").toString()

            if (str.equals("true"))
            {
                val intent = Intent(this@MainActivity, Search1::class.java)
                startActivity(intent)
            } else{
                val intent = Intent(this@MainActivity, Sign_Up::class.java)
                startActivity(intent)
            }




            finish()
            //code t go new activity

        },3000L)
    }
    }
