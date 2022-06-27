package com.geelong.user.Activity

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.geelong.user.API.APIUtils
import com.geelong.user.FAcebook
import com.geelong.user.R
import com.geelong.user.Response.LoginResponse
import com.geelong.user.Response.SignUpResponse
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.NetworkUtils
import com.geelong.user.Util.SharedPreferenceUtils
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.messaging.FirebaseMessaging
import com.rilixtech.widget.countrycodepicker.CountryCodePicker
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.pick_up_dilaog.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Sign_Up : AppCompatActivity() , GoogleApiClient.OnConnectionFailedListener {

    var radioGroup: RadioGroup ?=null
    lateinit var radioButton: RadioButton
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    var name:String=""
    var email:String=""
    var address:String=""
    var mobile_number:String=""
    var mobile_number_login:String=""
    var gender:String=""
    var token_id:String=""
    var email_matched_string:String=""
    var country_code:String=""
    lateinit var ccp: CountryCodePicker
    lateinit var ccp1:CountryCodePicker
    lateinit var user_name:TextInputEditText
    lateinit var user_email:TextInputEditText
    lateinit var user_address:TextInputEditText
    lateinit var user_mobile:EditText
    lateinit var user_login_mobile:EditText
    var status:String="0"

    lateinit var prgs_loader:RelativeLayout


    private var googleApiClient: GoogleApiClient? = null
    var RC_SIGN_IN=100
    lateinit var btn_gogle:ImageView
    lateinit var custom_progress:Dialog
   var LOCATION_PERMISSION_REQUEST_CODE=1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        radioGroup=findViewById(R.id.radioGroup1)

        user_name=findViewById(R.id.name_edit_text)
        user_email=findViewById(R.id.email_edit_Text)
        user_address=findViewById(R.id.address_edit_text)
        user_mobile=findViewById(R.id.mobile_id_text)
         user_login_mobile=findViewById(R.id.login_edittext_mobile)
        ccp = findViewById(R.id.ccp_signup)
        ccp1=findViewById(R.id.ccp_login)

        /*prgs_loader=findViewById(R.id.progress_loader)*/
        btn_gogle=findViewById(R.id.img_gogle_sign_in)
        custom_progress= Dialog(this)
        custom_progress.setContentView(R.layout.loader_layout)




        var txt_1=findViewById<TextView>(R.id.tvSignUpTab1)
        var txt_2=findViewById<TextView>(R.id.tvSiginTab2)
        var txt_3=findViewById<TextView>(R.id.tvSignUpTab3)
        var txt_4=findViewById<TextView>(R.id.tvSiginTab4)
        var sign_in=findViewById<TextView>(R.id.next_sign_in_btn)
        var sign_up=findViewById<LinearLayout>(R.id.sign_up_linearlayout)




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
        img_facebook_login.setOnClickListener {
            val intent= Intent(this,FAcebook::class.java)
            startActivity(intent)
        }
        external_tc_textview.setOnClickListener {
            val intent=Intent(this,TermsCondition::class.java)
            startActivity(intent)
        }

        SharedPreferenceUtils.getInstance(this)!!.setStringValue(ConstantUtils.Status,status)
        if ((ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
                    != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {

        }
        Generatetoken()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
        btn_gogle.setOnClickListener {
            val intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient!!)
            startActivityForResult(intent, RC_SIGN_IN)
        }



        sign_in.setOnClickListener {
        /* custom_progress.show()*/

            country_code="+"+ccp1.selectedCountryCode.toString()

            mobile_number_login=user_login_mobile.text.toString()

            if (mobile_number_login.isEmpty())

            { user_login_mobile.error="Please Enter Mobile Number"
               // Toast.makeText(this,"Please Enter Mobile Number",Toast.LENGTH_LONG).show()
            }
            else{

                if(NetworkUtils.checkInternetConnection(this))
                {
                    login()
                }

            }




        }
        sign_up.setOnClickListener {

            name=user_name.getText().toString()
            email=user_email.getText().toString()
            address=user_address.getText().toString()
            mobile_number=user_mobile.getText().toString().trim()
            country_code="+"+ccp.selectedCountryCode.toString()

            if (email.matches(emailPattern.toRegex())) {
                email_matched_string="1"
            } else {
                email_matched_string="0"
            }
           /* Toast.makeText(this,country_code,Toast.LENGTH_SHORT).show()*/
            try {
                val intSelectButton: Int = radioGroup!!.checkedRadioButtonId
                radioButton = findViewById(intSelectButton)
                gender=radioButton.text.toString()
            }
            catch (e:Exception)
            {

            }


              if (name.isEmpty())
              {   user_name.error="Please Enter Name"
                 // Toast.makeText(this,"Please Enter Name",Toast.LENGTH_LONG).show()
              }
              else if(name.length<3){
                  user_name.error="Name between 3-30 size"
                //  Toast.makeText(this,"Name between 9-16 size",Toast.LENGTH_LONG)
                     // .show()
              }
              else if(name.length>30){
                  user_name.error="Name between 3-30 size"
                 // Toast.makeText(this,"Name between 9-16 size",Toast.LENGTH_LONG)
                     // .show()
              }
              else if(email.isEmpty())
              { user_email.error="Please Enter Email"
                //  Toast.makeText(this,"Please Enter Email",Toast.LENGTH_LONG).show()
              }
              else if(email_matched_string.equals("0"))
              {user_email.error="Inavlid Email"
                 // Toast.makeText(this,"Inavlid Email",Toast.LENGTH_LONG).show()
              }
              else if(address.isEmpty())
              { user_address.error="Please Enter Address"
                //  Toast.makeText(this,"Please Enter Address",Toast.LENGTH_LONG).show()
              }
              else if(mobile_number.isEmpty() )
              {user_mobile.error="Please Enter Mobile Number"
                 // Toast.makeText(this,"Please Enter Mobile Number",Toast.LENGTH_LONG).show()
              }
              else if(mobile_number.length<9){
                  user_mobile.error="mobile number between 9-15 size"
                  // Toast.makeText(this,"Name between 9-16 size",Toast.LENGTH_LONG)
                  // .show()
              }
              else if(mobile_number.length>15){
                  user_mobile.error="mobile number between 9-15 size"
                  // Toast.makeText(this,"Name between 9-16 size",Toast.LENGTH_LONG)
                  // .show()
              }

              else if(gender.isEmpty())
              {
                  Toast.makeText(this,"Please Enter Gender",Toast.LENGTH_LONG).show()
              }

              else {
                  if(NetworkUtils.checkInternetConnection(this))
                  {
                    //  custom_progress.show()
                      signup()
                  }
                  else{
                      Toast.makeText(this,"dshdg",Toast.LENGTH_LONG).show()
                  }
              }
        }


    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)
            handleSignInResult(result)
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult?) {
        if (result!!.isSuccess) {
            if (result.isSuccess) {
                val account = result.signInAccount
                //   name.setText(account!!.displayName)
                // email.setText(account!!.email)
                // userId!!.text = account.id
                //  ToastUtil.toast_Long(this,account!!.email)
                Toast.makeText(applicationContext, account!!.email, Toast.LENGTH_LONG).show()
                account!!.displayName?.let { account!!.email?.let { it1 -> showDialog(it, it1) } }
            } else {
//                ToastUtil.toast_Long(this,"Please try again later")
                Toast.makeText(applicationContext," Please try again later", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(applicationContext, "Sign in cancel", Toast.LENGTH_LONG).show()
        }
    }



    companion object {
        private const val RC_SIGN_IN = 1
    }

    fun signup()
    {

        custom_progress.show()
        val request = HashMap<String, String>()
        request.put("name",name)
        request.put("email",email)
        request.put("mobile",country_code+mobile_number)
        request.put("address",address)
        request.put("gender",gender)



        var signup: Call<SignUpResponse> = APIUtils.getServiceAPI()!!.SignUp(request)

        signup.enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                try {



                    if (response.body()!!.success.equals("true")) {




                        Toast.makeText(this@Sign_Up,response.body()!!.msg.toString(),Toast.LENGTH_LONG).show()

                         var intent = Intent(this@Sign_Up, Sign_Up::class.java)

                          startActivity(intent)

                        custom_progress.hide()

                    } else {

                        Toast.makeText(this@Sign_Up,response.body()!!.msg,Toast.LENGTH_LONG).show()
                        custom_progress.hide()
                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())

                    Toast.makeText(this@Sign_Up,"Mobile number is already exit",Toast.LENGTH_LONG).show()
                    custom_progress.hide()

                }

            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())

                Toast.makeText(this@Sign_Up,"Mobile number or Email is already exit",Toast
                    .LENGTH_LONG)
                    .show()
                custom_progress.hide()

            }

        })
    }
    fun login()
    {
         custom_progress.show()
        val request = HashMap<String, String>()
        request.put("mobile",country_code+mobile_number_login)
        request.put("device_tokanid",token_id)



        var login_in: Call<LoginResponse> = APIUtils.getServiceAPI()!!.Login(request)

        login_in.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {
                       // Toast.makeText(this@Sign_Up,response.body()!!.msg.toString(),Toast.LENGTH_LONG).show()
                        var intent=Intent(this@Sign_Up, Otp::class.java)
                        intent.putExtra("otp",response.body()!!.data[0].otp)
                        startActivity(intent)

                         SharedPreferenceUtils.getInstance(this@Sign_Up)?.setStringValue(ConstantUtils.USER_ID,response.body()!!.success)
                        SharedPreferenceUtils.getInstance(this@Sign_Up)?.setStringValue(ConstantUtils.USER_ID,response.body()!!.data[0].id)
                        SharedPreferenceUtils.getInstance(this@Sign_Up)?.setStringValue(ConstantUtils.User_Name,response.body()!!.data[0].name)
                        SharedPreferenceUtils.getInstance(this@Sign_Up)?.setStringValue(ConstantUtils.User_Mobile_Number,country_code+mobile_number_login)

                        custom_progress.hide()

                    } else {
                        // Toast.makeText(this@Signup,response.body()!!.msg.toString(), Toast.LENGTH_LONG).show()
                        Toast.makeText(this@Sign_Up,response.body()!!.msg,Toast.LENGTH_LONG).show()
                        custom_progress.hide()
                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())

                    Toast.makeText(this@Sign_Up,"Weak Internet Connection",Toast.LENGTH_LONG).show()
                    custom_progress.hide()

                }

            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())

                Toast.makeText(this@Sign_Up,"Weak Internet Connection",Toast.LENGTH_LONG).show()
                custom_progress.hide()

            }

        })
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    private fun Generatetoken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                //Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            token_id=token
            SharedPreferenceUtils.getInstance(this@Sign_Up)?.setStringValue(ConstantUtils.TokenId,token)
            Log.e("TAG", token)
        })
    }

    fun showDialog(name1:String,email1:String) {
        val dialog = BottomSheetDialog(this)

        dialog.getWindow()!!
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dialog.setCancelable(true)
        dialog.setContentView(R.layout.pick_up_dilaog)
        var radioGroup: RadioGroup ?=null
         var radioButton: RadioButton?=null
       radioGroup=dialog.findViewById(R.id.radioGroup1_popup)

         var signup_btn=dialog.findViewById<TextView>(R.id.sign_up_text_popup)
        var name_etxt=dialog.findViewById<TextInputEditText>(R.id.name_edit_text_popup)
        var email_extx=dialog.findViewById<TextInputEditText>(R.id.email_edit_Text_popup)
        var address_txt=dialog.findViewById<TextInputEditText>(R.id.address_edit_text_popup)
        var mobileno_txt=dialog.findViewById<EditText>(R.id.mobile_id_text_popup)
        var country_cd=dialog.findViewById<CountryCodePicker>(R.id.ccp_signup_popup)

          val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (email.matches(emailPattern.toRegex())) {
            email_matched_string="1"
        } else {
            email_matched_string="0"
        }

        if (name_etxt != null) {
            //  name=name_etxt.getText().toString()
            name_etxt.setText(name1)
        }
        if (email_extx != null) {
            //email=email_extx.getText().toString()
            email_extx.setText(email1)
        }

        if (signup_btn != null) {
            signup_btn.setOnClickListener {

                try {
                    val intSelectButton: Int = radioGroup!!.checkedRadioButtonId
                    radioButton =dialog.findViewById(intSelectButton)
                    gender= radioButton?.text.toString()
                }
                catch (e:Exception)
                {

                }
                if (name_etxt != null) {
                      name=name_etxt.getText().toString()
                  //  name_etxt.setText(name1)
                }
                if (email_extx != null) {
                    email=email_extx.getText().toString()
                   // email_extx.setText(email1)
                }

                if (address_txt != null) {
                    address=address_txt.getText().toString()
                }
                if (mobileno_txt != null) {
                    mobile_number=mobileno_txt.getText().toString().trim()
                }
                if (country_cd!=null)
                {
                    country_code= "+"+country_cd.selectedCountryCode.toString()
                }


                 if(address.isEmpty())
                { address_txt!!.error="Please Enter Address"
                    //  Toast.makeText(this,"Please Enter Address",Toast.LENGTH_LONG).show()
                }
                else if(mobile_number.isEmpty() )
                {mobileno_txt!!.error="Please Enter Mobile Number"
                    // Toast.makeText(this,"Please Enter Mobile Number",Toast.LENGTH_LONG).show()
                }

                else if(gender.isEmpty())
                {
                    Toast.makeText(this,"Please Enter Gender",Toast.LENGTH_LONG).show()
                }

                else {
                    if(NetworkUtils.checkInternetConnection(this))
                    {
                        custom_progress.show()
                        signup()
                    }
                    else{
                        Toast.makeText(this,"dshdg",Toast.LENGTH_LONG).show()
                    }
                }


            }
        }

        dialog.show()

    }

    fun AlertShow(name1:String,email1:String) {
        val dialog = BottomSheetDialog(this)

        dialog.getWindow()!!
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dialog.setCancelable(true)
        dialog.setContentView(R.layout.pick_up_dilaog)



        dialog.show()

    }

}