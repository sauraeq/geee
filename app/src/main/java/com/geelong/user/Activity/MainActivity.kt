package com.geelong.user.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Toast
import com.geelong.user.R
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.SharedPreferenceUtils

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        var status=""
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Handler().postDelayed(Runnable {
            try {
               status= SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils
                    .Status,"")!!.toString()
            } catch (e:Exception)
            {

            }


            if (status.isEmpty())
            {
                val intent = Intent(this, Sign_Up::class.java)
                startActivity(intent)
            }
            else{

                when (status.toInt())
                {
                    0 -> {
                        val intent = Intent(this, Sign_Up::class.java)
                        startActivity(intent)

                    }
                    1 -> {
                        val intent = Intent(this, Search1::class.java)
                        startActivity(intent)

                    }
                    2 -> {
                        val intent = Intent(this, Confirm::class.java)
                        startActivity(intent)

                    }
                    3 -> {
                        val intent = Intent(this, ConfirmPick_up::class.java)
                        startActivity(intent)

                    }
                    4 -> {
                        val intent = Intent(this, DriverDetails::class.java)
                        startActivity(intent)

                    }

                }

            }


           /* if (str_otp.length>=3)
            {
                val intent = Intent(this@MainActivity, Search1::class.java)
                startActivity(intent)
            } else{
                val intent = Intent(this@MainActivity, Sign_Up::class.java)
                startActivity(intent)
            }*/


            finish()
            //code t go new activity

        },3000L)
    }
    }
