package com.geelong.user.Activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.geelong.user.API.APIUtils
import com.geelong.user.R
import com.geelong.user.Response.PyMentResp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.SharedPreferenceUtils
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_payment_method.*
import org.json.JSONException
import org.json.JSONObject


import kotlin.math.log


class Payment_method : AppCompatActivity() {

    var userid:String=""
    var booking_id:String="5"
    var payment_method:String="PayPal"
    lateinit var webview_paymentt:WebView
    lateinit var customprogress:Dialog
    private var requestQueue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_method)
        customprogress= Dialog(this)
        customprogress.setContentView(R.layout.loader_layout)
        requestQueue = Volley.newRequestQueue(this)

        userid= SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.USER_ID,"").toString()
       // booking_id=SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils
           // .Booking_id,"").toString()

        webview_paymentt=findViewById(R.id.webview_payement)
        webview_paymentt.getSettings().setJavaScriptEnabled(true)
        webview_paymentt.getSettings().setJavaScriptCanOpenWindowsAutomatically(true)
        payment()
       // webview_paymentt.loadUrl("https://www.sandbox.paypal" +
               // ".com/cgi-bin/webscr?cmd=_express-checkout&token=EC-07G461549L633371J")
        /*var linearGPay=findViewById<LinearLayout>(R.id.linear_gpay)
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
        var back_activity_privacy=findViewById<ImageView>(R.id.Left_paymentmethod)*/
        Left_paymentmethod.setOnClickListener {
           /* val intent = Intent(this, FareDetails::class.java)
            startActivity(intent)*/
            onBackPressed()
        }

    }


    fun payment()
    {
        customprogress.show()
        val request = HashMap<String, String>()
        request.put("user_id",userid)
        request.put("payment_method","PayPal")
        request.put("booking_id","4")
        request.put("amount","100")
        request.put("driver_id","4")



        var payment: Call<PyMentResp> = APIUtils.getServiceAPI()!!.payment(request)

        payment.enqueue(object : Callback<PyMentResp> {
            override fun onResponse(call: Call<PyMentResp>, response: Response<PyMentResp>) {
                try {


                    if (response.body()!!.success.equals("true")) {
                        var payment_link_url=response.body()!!.data.payment_url
                        //webview_paymentt.loadUrl(payment_link_url)
                        startWebView(payment_link_url)


                            Toast.makeText(this@Payment_method,response.body()!!.msg,Toast.LENGTH_LONG).show()




                        customprogress.hide()


                    } else {

                        Toast.makeText(this@Payment_method,response.body()!!.msg, Toast
                            .LENGTH_LONG).show()
                        customprogress.hide()
                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    Toast.makeText(this@Payment_method,"Weak Internet Connection", Toast.LENGTH_LONG).show()
                    customprogress.hide()

                }

            }

            override fun onFailure(call: Call<PyMentResp>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                Toast.makeText(this@Payment_method,"Weak Internet Connection", Toast.LENGTH_LONG).show()
                customprogress.hide()
            }

        })
    }

    private fun startWebView(url: String) {
        webview_paymentt.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }


            override fun onLoadResource(view: WebView, url: String) {
               /* Toast.makeText(this@Payment_method,"payment under process",Toast.LENGTH_LONG).show()*/

            }
            override fun onPageFinished(view: WebView, url: String) {
                try {

                    Log.d("sasa",url)
                    jsonParse(url)


                } catch (exception: java.lang.Exception) {
                    exception.printStackTrace()
                }
            }
        })


        webview_paymentt.loadUrl(url)
    }

    private fun jsonParse(url: String) {
        val Url = url
        val request = JsonObjectRequest(Request.Method.GET, Url, null, { response
            ->try {
                Log.d("res",response.toString())

            val jsonobject:JSONObject= JSONObject(response.toString())
            var user:String=jsonobject.getString("success")
            //Toast.makeText(this,user,Toast.LENGTH_LONG).show()
            if (user.equals("true"))
            {
                showDialog()
            }
            else
            {
                val intent=Intent(this,Payment_method::class.java)
                startActivity(intent)
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        }, { error -> error.printStackTrace() })
        requestQueue?.add(request)



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

        //dialog.window?.setLayout(700,750)

    }

}