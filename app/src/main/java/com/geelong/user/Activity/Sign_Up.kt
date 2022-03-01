package com.geelong.user.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.geelong.user.R

class Sign_Up : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()

        var txt_1=findViewById<TextView>(R.id.tvSignUpTab1)
        var txt_2=findViewById<TextView>(R.id.tvSiginTab2)
        var txt_3=findViewById<TextView>(R.id.tvSignUpTab3)
        var txt_4=findViewById<TextView>(R.id.tvSiginTab4)
        var sign_in=findViewById<TextView>(R.id.next_sign_in_btn)
        var sign_up=findViewById<TextView>(R.id.sign_up_text)

        var linear_sign_up=findViewById<LinearLayout>(R.id.singnup_linera)
        var linear_sign_in=findViewById<LinearLayout>(R.id.signIn_linear)
        var linear_sign_up_content=findViewById<LinearLayout>(R.id.signUp_content)
        var Linear_sign_in_content=findViewById<LinearLayout>(R.id.signin_content)
        var Linear_sign_Up_Text=findViewById<LinearLayout>(R.id.Sign_Up_Text_linear)
        var Linear_soical_media_icon=findViewById<LinearLayout>(R.id.Social_media_Linear)
        var Linear_terms_condition=findViewById<LinearLayout>(R.id.term_Linear)

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
            var intent=Intent(this, Otp::class.java)
            startActivity(intent)

        }
        sign_up.setOnClickListener {
            var intent = Intent(this, Sign_Up::class.java)
            startActivity(intent)

        }


    }
}