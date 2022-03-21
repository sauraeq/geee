package com.geelong.user.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.geelong.user.Fragment.ConfirmPickupAdapter
import com.geelong.user.R

class ConfirmPick_up : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_pick_up)
        supportActionBar?.hide()

        val bundle = Bundle()
        bundle.putString("fragmentName", "Settings Fragment")
        val settingsFragment = ConfirmPickupAdapter()
        settingsFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_main_content_id11, settingsFragment).commit()
    }
    fun inte()
    {
       /* val intent = Intent(this, Confirm::class.java)
        startActivity(intent)*/
        onBackPressed()
    }
}