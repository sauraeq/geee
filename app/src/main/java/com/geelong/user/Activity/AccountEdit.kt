package com.geelong.user.Activity

import android.Manifest
import android.accounts.Account
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.geelong.user.API.APIUtils
import com.geelong.user.Activity.Acccount
import com.geelong.user.R
import com.geelong.user.Response.EditPResponse
import com.geelong.user.Response.EditProfileResponse
import com.geelong.user.Response.LoginResponse
import com.geelong.user.Util.Camerautils.FileCompressor
import com.geelong.user.Util.Camerautils.PermissionUtils
import com.geelong.user.Util.Camerautils.Utility
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.NetworkUtils
import com.geelong.user.Util.SharedPreferenceUtils
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_acccount.*
import kotlinx.android.synthetic.main.activity_account_edit.*
import kotlinx.android.synthetic.main.activity_search1.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class AccountEdit : AppCompatActivity()  {

    var image=""

    var currentPhotoPath = ""
    var userChoosenTask = ""
    var profileimage = ""
    var REQUEST_CODE: Int = 0
    var filepath_presc: Uri? = null
    var image1path = ""

    private val pickImage = 100
    lateinit var imagepath:String
    lateinit var customprogress:Dialog
    var user_name_et:String=""
    var user_email_et:String=""
    var user_gender_et:String=""
    var user_address_et:String=""
    var img_url:String=""
    var name:String=""
    var email:String=""
    var address:String=""
    var gender:String=""
    var selectedItem:String=""
    var mCompressor: FileCompressor? = null
    private var CAMERA_REQUEST: Int = 1
    private var PICK_IMAGE_REQUEST: Int = 1

  //  lateinit var datePicker: datePickerHelper
    var date:String =""
    private var oneWayTripDate: Date? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_edit)
        var  save=findViewById<TextView>(R.id.edit_profile_save)
        var back_act=findViewById<ImageView>(R.id.back_activity1)
        customprogress= Dialog(this)
        customprogress.setContentView(R.layout.loader_layout)
        name= intent.getStringExtra("name").toString()
        email= intent.getStringExtra("email").toString()
        address= intent.getStringExtra("address").toString()
        gender= intent.getStringExtra("gender").toString()

        user_name_edittext.setText(name)
        user_email_edittext.setText(email)
        user_address_edittext.setText(address)


        mCompressor = FileCompressor(this)
        img_url=SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.Image_Url,"").toString()
        if(img_url.isEmpty())
            {

            }
            else
        {
            var picasso=Picasso.get()
            picasso.load(img_url).into(userProfile_edit_img)
        }


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        var lang=ArrayList<String>()
        lang.add("Select")
        lang.add("Male")
        lang.add("Female")

        if (gender_spinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, lang)
            gender_spinner.adapter = adapter

        }
        gender_spinner.setSelection(lang.indexOf(gender));

        gender_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                 selectedItem = parent?.getItemAtPosition(position).toString()

            }

        }
        val permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE ,
            Manifest.permission.WRITE_EXTERNAL_STORAGE

        )

        save.setOnClickListener {

            user_name_et=user_name_edittext.text.toString()
            user_email_et=user_email_edittext.text.toString()
            user_address_et=user_address_edittext.text.toString()

            if (selectedItem.equals("Select"))
            {
                Toast.makeText(this,"Please Select gender",Toast.LENGTH_LONG).show()
            }
            else if (user_name_et.isEmpty())
            {
                Toast.makeText(this,"Please Enter gender",Toast.LENGTH_LONG).show()
            }
            else if (user_email_et.isEmpty())
            {
                Toast.makeText(this,"Please Enter Email",Toast.LENGTH_LONG).show()
            }
            else if (user_address_et.isEmpty())
            {
                Toast.makeText(this,"Please Enter Address",Toast.LENGTH_LONG).show()
            }
            else
            {
                if (NetworkUtils.checkInternetConnection(this))
                {
                    EditProfile()
                    customprogress.show()
                }


            }

        }
          back_act.setOnClickListener {
            onBackPressed()
              finish()
        }
        change_profile_img.setOnClickListener {
            getMultiplePermission()
           /* Dexter.withContext(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {

                   selectImage()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        Toast.makeText(this@AccountEdit,"Demied....",Toast.LENGTH_LONG).show()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?
                    ) {
                        token!!.continuePermissionRequest()
                    }
                }).check()*/
            //val i = Intent()
           // i.type = "image/*"
            //i.action = Intent.ACTION_GET_CONTENT
          //  startActivityForResult(Intent.createChooser(i, "Select Picture"), pickImage)
        }
    }

   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
        ///Toast.makeText(this,imagepath,Toast.LENGTH_LONG).show()
        Log.d("saurav",imagepath)

        editprofileimg()
        return Uri.parse(file.absolutePath)

    }*/

    fun getPermissions() {
        if (PermissionUtils.checkPermission(this as Activity, Manifest.permission.CAMERA, REQUEST_CODE)) {
            if (PermissionUtils.checkPermission(
                    this as Activity?,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    REQUEST_CODE
                )
            ) {
                if (PermissionUtils.checkPermission(
                        this as Activity?,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        REQUEST_CODE
                    )
                ) {
                    selectImage()
                }
            }
        }
    }

    private fun selectImage() {

        val items = arrayOf<CharSequence>("Take photo", "Choose from gallery")
        val builder = AlertDialog.Builder(this as Activity)
        builder.setTitle("Add Photo")
        builder.setCancelable(true)
        builder.setItems(items) { dialog, item ->
            val result: Boolean = Utility.checkPermission(this)
            if (items[item] == "Take photo") {
                userChoosenTask = "Take photo"
                if (result) {
                    dispatchTakePictureIntent()

                }

            } else if (items[item] == "Choose from gallery") {
                userChoosenTask = "Choose from gallery"
                if (result) {
                    openGallery()
                }

            }
        }
        builder.show()
    }

    fun openGallery() {
        if (Build.VERSION.SDK_INT >= 23) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_REQUEST
            )

        } else {
            if (Build.VERSION.SDK_INT <= 19) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"),
                    PICK_IMAGE_REQUEST
                )
            } else if (Build.VERSION.SDK_INT > 19) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"),
                    PICK_IMAGE_REQUEST
                )
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (takePictureIntent.resolveActivity(packageManager) != null) {

            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                ex.printStackTrace()

            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()

            }

            if (photoFile != null) {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this,
                    getApplicationContext()?.getPackageName() + ".fileprovider",
                    photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, CAMERA_REQUEST)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {

        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File? =getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image: File = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )

        currentPhotoPath = image.getAbsolutePath()
        Log.d("e", currentPhotoPath)

        return image
    }

    fun onselectfromcamera1() {

        var bitmap: Bitmap? = null
        var imgFile = File(currentPhotoPath)
      //  Toast.makeText(this,imgFile.toString()+"",Toast.LENGTH_LONG).show()
        Log.d("e","dfdf")
        if (imgFile.exists()) {
            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgFile = mCompressor!!.compressToFile(imgFile)
            bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            image1path=imgFile.path
            userProfile_edit_img.setImageBitmap(bitmap)
            editprofileimg()

       //     Toast.makeText(this,image1path+"2",Toast.LENGTH_LONG).show()


            profileimage = getStringImage(bitmap)


        }

    }

    fun getStringImage(bmp: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes: ByteArray = baos.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    fun onselectfromgallery(data: Intent) {
        filepath_presc = data.data
        if (filepath_presc.toString() == null) {

        } else {
            try {
                Log.e("e", filepath_presc.toString() + "")
                var bitmap: Bitmap? = null

                var file = File(filepath_presc?.let { getRealPathFromUri(it) })
                try {
                    file = mCompressor!!.compressToFile(file)
                    bitmap = mCompressor!!.compressToBitmap(file)
                    image1path=file.path
                    editprofileimg()
                   // Toast.makeText(this,image1path,Toast.LENGTH_LONG).show()

                    profileimage = bitmap?.let { getStringImage(it) }.toString()
                    userProfile_edit_img.setImageBitmap(bitmap)

                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show()
                    Log.e("e", e.toString())
                }

            } catch (e: java.lang.Exception) {
                Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show()
                Log.e("e", e.toString())
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("Range")
    fun getRealPathFromUri(uri: Uri): String? {
        var result = ""
        val documentID: String
        documentID = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            val pathParts = uri.path!!.split("/".toRegex()).toTypedArray()
            pathParts[pathParts.size - 1]
        } else {
            val pathSegments = uri.lastPathSegment!!.split(":".toRegex()).toTypedArray()
            pathSegments[pathSegments.size - 1]
        }
        val mediaPath = MediaStore.Images.Media.DATA
        val imageCursor: Cursor? =contentResolver.query(
            uri,
            arrayOf(mediaPath),
            MediaStore.Images.Media._ID + "=" + documentID,
            null,
            null
        )
        if (imageCursor!!.moveToFirst()) {
            result = imageCursor.getString(imageCursor.getColumnIndex(mediaPath))
        }
        return result
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == -1 && data != null && data.data != null) {
            Log.e("e", data.toString() + "")
            onselectfromgallery(data)
        }else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            onselectfromcamera1()
        }
    }


    private  fun editprofileimg() {
        val multiPartRepeatString = "application/image"

        var facility_image: MultipartBody.Part? = null
        val file1 = File(image1path)
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
            Toast.makeText(this@AccountEdit,response.body()!!.msg,Toast.LENGTH_LONG).show()

                        } else {

                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@AccountEdit,"Weak Internet Connection",Toast.LENGTH_LONG).show()

                }
            }

            override fun onFailure(call: Call<EditProfileResponse>, t: Throwable) {
                Toast.makeText(this@AccountEdit,"Weak Internet Connection",Toast.LENGTH_LONG).show()
            }

        })


    }

    fun EditProfile()
    {

        var user_id=SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.USER_ID,"").toString()
        val request = HashMap<String, String>()
        request.put("name",user_name_et)
        request.put("email",user_email_et)
        request.put("address",user_address_et)
        request.put("gender",selectedItem)
        request.put("user_id",user_id)



        var edit_profile: Call<EditPResponse> = APIUtils.getServiceAPI()!!.Editprofile(request)

        edit_profile.enqueue(object : Callback<EditPResponse> {
            override fun onResponse(call: Call<EditPResponse>, response: Response<EditPResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {
                        var intent=Intent(this@AccountEdit,Search1::class.java)

                        startActivity(intent)
                        customprogress.hide()
               finishAffinity()

                    } else {

                         Toast.makeText(this@AccountEdit,response.body()!!.msg, Toast.LENGTH_LONG)
                             .show()
                        customprogress.hide()
                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())

                    Toast.makeText(this@AccountEdit,"Weak Internet Connection", Toast.LENGTH_LONG).show()
                    customprogress.hide()

                }

            }

            override fun onFailure(call: Call<EditPResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())

                Toast.makeText(this@AccountEdit,"Weak Internet Connection", Toast.LENGTH_LONG).show()

               customprogress.hide()

            }

        })
    }

    /*private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }
*/
    /*override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>, token: PermissionToken) {
       // Toast.makeText(this,token.toString()+permissions.toString(),Toast.LENGTH_LONG).show()
    }

    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
       //Toast.makeText(this,report.toString(),Toast.LENGTH_LONG).show()
    }*/

    private fun getMultiplePermission() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    report.let {

                        if (report.areAllPermissionsGranted()) {
                            selectImage()
                            // Toast.makeText(this@EditProfile, "Permissions Granted", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@AccountEdit, "Please Grant Permissions to use the app", Toast
                                .LENGTH_SHORT).show()
                        }

                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest?>?, token: PermissionToken?) {
                    token?.continuePermissionRequest()
                }
            }).withErrorListener{
                Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
            }.check()

    }


}