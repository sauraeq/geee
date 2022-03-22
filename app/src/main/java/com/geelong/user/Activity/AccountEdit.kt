package com.geelong.user.Activity

import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.geelong.user.API.APIUtils
import com.geelong.user.Activity.Acccount
import com.geelong.user.R
import com.geelong.user.Response.EditProfileResponse
import com.geelong.user.Response.LoginResponse
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.SharedPreferenceUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_acccount.*
import kotlinx.android.synthetic.main.activity_account_edit.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class AccountEdit : AppCompatActivity() {

    private val pickImage = 100
    lateinit var imagepath:String
    lateinit var customprogress:Dialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_edit)
        var  save=findViewById<TextView>(R.id.edit_profile_save)

        var back_act=findViewById<ImageView>(R.id.back_activity1)
        customprogress= Dialog(this)
        customprogress.setContentView(R.layout.loader_layout)

        supportActionBar?.hide()
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        save.setOnClickListener {
           /* val intent = Intent(this, Acccount::class.java)
            startActivity(intent)*/

        }
        back_act.setOnClickListener {
           onBackPressed()
        }
        change_profile_img.setOnClickListener {
            val i = Intent()
            i.type = "image/*"
            i.action = Intent.ACTION_GET_CONTENT

            // pass the constant to compare it
            // with the returned requestCode

            // pass the constant to compare it
            // with the returned requestCode
            startActivityForResult(Intent.createChooser(i, "Select Picture"), pickImage)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == pickImage) {

                val selectedImageUri = data?.data
                if (null != selectedImageUri) {

                    userProfile_edit_img.setImageURI(selectedImageUri)
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImageUri)
                    bitmapToFile(bitmap)

                }
            }
        }
    }

    private fun bitmapToFile(bitmap: Bitmap): Uri {



        val wrapper = ContextWrapper(applicationContext)


        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {

            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }


        imagepath = file.absolutePath
        Toast.makeText(this,imagepath,Toast.LENGTH_LONG).show()
        Log.d("saurav",imagepath)
        editprofileimg()

        return Uri.parse(file.absolutePath)



    }

    private  fun editprofileimg() {
       /* progress_loader_edit_profile.visibility= View.VISIBLE*/
        //val dialog: ProgressDialog = ProgressDialog.show(this, null, "Please Wait")
        val multiPartRepeatString = "application/image"

        var facility_image: MultipartBody.Part? = null


        val file1 = File(imagepath)

        val signPicBody1 = file1.asRequestBody(multiPartRepeatString.toMediaTypeOrNull())
        facility_image =
            MultipartBody.Part.createFormData("profile_photo", file1.name, signPicBody1)

        val driver_id: RequestBody =
            SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.USER_ID,"")?.toRequestBody(MultipartBody.FORM)!!


        var AddExerciseCall: Call<EditProfileResponse> = APIUtils.getServiceAPI()!!.profileupdate(
            driver_id,
            facility_image,

            )

        AddExerciseCall.enqueue(object : Callback<EditProfileResponse> {
            override fun onResponse(
                call: Call<EditProfileResponse>,
                response: Response<EditProfileResponse>
            ) {

                try {
                    if (response.code() == 200) {

                        if (response.body()!!.success.equals("true")) {
                            /*  dialog.dismiss()*/
                           /* progress_loader_edit_profile.visibility= View.GONE
                            shrp.setStringPreference("profile_photo",response.body()!!.image)
                            Toast.makeText(this@Profile_Edit, response.body()!!.msg, Toast.LENGTH_SHORT)
                                .show()*/

                        } else {
                            /*  dialog.dismiss()*/
                          /*  progress_loader_edit_profile.visibility= View.GONE
                            Toast.makeText(this@Profile_Edit, response.body()!!.msg, Toast.LENGTH_SHORT)
                                .show()*/
                        }
                    }
                } catch (e: Exception) {


                }
            }

            override fun onFailure(call: Call<EditProfileResponse>, t: Throwable) {
                /* dialog.dismiss()*/
               /* progress_loader_edit_profile.visibility= View.GONE
                *//*
                 Utils.showToast(this@Profile_Edit, t.message.toString())
                 startActivity(Intent(this@Profile_Edit, HomeActivity::class.java))
                 finishAffinity()*//*
                Toast.makeText(this@Profile_Edit, t.toString(), Toast.LENGTH_SHORT).show()*/
            }

        })


    }
 /* fun editProfile()
  {
      customprogress.show()
      val request = HashMap<String, String>()
      request.put("user_id","92")
      request.put("profile_photo",imagepath)



      var edit_profile: Call<EditProfileResponse> = APIUtils.getServiceAPI()!!.Edit_p(request)

      edit_profile.enqueue(object : Callback<EditProfileResponse> {
          override fun onResponse(call: Call<EditProfileResponse>, response: Response<EditProfileResponse>) {
              try {


                  if (response.body()!!.success.equals("true")) {
                      Toast.makeText(this@AccountEdit,response.body()!!.msg.toString(), Toast.LENGTH_LONG).show()
                     *//* User_name.text=response.body()!!.data[0].name
                      User_mobile.text=response.body()!!.data[0].phone
                      User_email.text=response.body()!!.data[0].email
                      User_Address.text=response.body()!!.data[0].address
                      User_gender.text=response.body()!!.data[0].gender
                      var img_url=response.body()!!.data[0].profile_photo
                      val picasso= Picasso.get()
                      picasso.load(img_url).into(User_profile_pic)*//*
                      customprogress.hide()


                  } else {

                      Toast.makeText(this@AccountEdit,"Error", Toast.LENGTH_LONG).show()
                      customprogress.hide()
                  }

              }  catch (e: Exception) {
                  Log.e("saurav", e.toString())

                  Toast.makeText(this@AccountEdit,e.message, Toast.LENGTH_LONG).show()
                  customprogress.hide()

              }

          }

          override fun onFailure(call: Call<EditProfileResponse>, t: Throwable) {
              Log.e("Saurav", t.message.toString())

              Toast.makeText(this@AccountEdit,t.message, Toast.LENGTH_LONG).show()
              customprogress.hide()
          }

      })
  }*/
}