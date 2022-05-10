package com.geelong.user.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.geelong.user.API.APIUtils
import com.geelong.user.R
import com.geelong.user.Response.PaylaterResponse
import com.geelong.user.Response.ViewNotificationResponse
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.SharedPreferenceUtils
import kotlinx.android.synthetic.main.activity_pay_now.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap


class Pay_Now : AppCompatActivity() {
    var checked_item:String=""
    var paylater="paylater"
    var paynow="paynow"
    lateinit var c1 : CheckBox
    lateinit var c2 : CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_now)
        supportActionBar?.hide()
        c1 = findViewById(R.id.checkbox_paynow)
        c2 = findViewById(R.id.checkbox_paylater)

        var pay_Now_tv=findViewById<TextView>(R.id.pay_now_txt)

        c1.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                c2.setChecked(false)
                checked_item=paynow


            }
        })
        c2.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                c1.setChecked(false)
                checked_item=paylater

            }
        })



        pay_Now_tv.setOnClickListener {
            if(c1.isChecked)
            {
                val intent = Intent(this, FareDetails::class.java)
                startActivity(intent)
            }
            else if(c2.isChecked){
                paylater()
                val intent = Intent(this, DriverDetails::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this,"Please select Payment type",Toast.LENGTH_LONG).show()
            }


            /*val intent = Intent(this, FareDetails::class.java)
            startActivity(intent)*/
        }
        var back_activity_privacy=findViewById<ImageView>(R.id.leftarrow_paynow)
        back_activity_privacy.setOnClickListener {
            /*val intent = Intent(this, ConfirmPick_up::class.java)
            startActivity(intent)*/
            onBackPressed()
        }

    }
    private fun switchCheckedBox(v : View) {
        when (v.id) {
            R.id.checkbox_paynow -> c2.isChecked = false
            R.id.checkbox_paylater -> c1.isSelected = false
        }
    }
    fun paylater()
    {

        var booking_id= SharedPreferenceUtils.getInstance(this)?.getStringValue(
            ConstantUtils
            .Booking_id,"")
            .toString()

        val request = HashMap<String, String>()
        request.put("booking_id",booking_id)




        var paylater: Call<PaylaterResponse> = APIUtils.getServiceAPI()!!.Paylater(request)

        paylater.enqueue(object : Callback<PaylaterResponse> {
            override fun onResponse(call: Call<PaylaterResponse>, response: Response<PaylaterResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {
                      /*  Toast.makeText(this@Pay_Now,response.body()!!.msg,Toast
                            .LENGTH_LONG)
                            .show()
*/

                    } else {


                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())

                    Toast.makeText(this@Pay_Now,"Weak Internet Connection", Toast.LENGTH_LONG).show()

                }

            }

            override fun onFailure(call: Call<PaylaterResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())

                Toast.makeText(this@Pay_Now,"Weak Internet Connection", Toast.LENGTH_LONG).show()

            }

        })
    }
}