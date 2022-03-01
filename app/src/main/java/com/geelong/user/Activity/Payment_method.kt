package com.geelong.user.Activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.geelong.user.R

class Payment_method : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_method)
        supportActionBar?.hide()

        var linearGPay=findViewById<LinearLayout>(R.id.linear_gpay)
        var linearPaytm=findViewById<LinearLayout>(R.id.linear_paytm)
        var linearPhonePay=findViewById<LinearLayout>(R.id.linear_phonepay)
        var linearcash=findViewById<LinearLayout>(R.id.linear_cash)
        var relative_pop_up=findViewById<RelativeLayout>(R.id.add_success_popup)

        linearGPay.setOnClickListener {
            showDialog()
          //  relative_pop_up.visibility=View.VISIBLE
        }
        linearPaytm.setOnClickListener {
            showDialog()
        }
        linearPhonePay.setOnClickListener {
            showDialog()
        }
        linearcash.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.getWindow()!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dialog.setCancelable(true)
        dialog.setContentView(R.layout.successalertdialog)
        lateinit var button: LinearLayout


        button = dialog.findViewById(R.id.payment_success)

        button.setOnClickListener {
            dialog.dismiss()
            val intent=Intent(this,DriverDetails::class.java)
            startActivity(intent)
            }




        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()

        dialog.window?.setLayout(700,750)

    }
}