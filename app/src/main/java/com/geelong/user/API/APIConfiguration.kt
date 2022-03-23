package com.geelong.user.API


import com.geelong.user.Response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface APIConfiguration {
    @POST("usersignup")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun SignUp(
        @Body stringStringHashMap: HashMap<String, String>,
    ): Call<SignUpResponse>

    @POST("userLoginotp")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun Login(
        @Body stringStringHashMap: HashMap<String, String>,
    ): Call<LoginResponse>

    @POST("privacypolicy")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun privacy(): Call<PrivacyResponse>


    @POST("termsconditions")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun terms(

    ): Call<TermsResponse>

    @POST("vehiclelist")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun vech_details(
        @Body stringStringHashMap: HashMap<String, String>,
    ): Call<Vechail_detailsResponse>


    @POST("booking")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun Driver_details(
        @Body stringStringHashMap: HashMap<String, String>,
    ): Call<DriverDetails_Vch_Response>

    @Multipart
    @POST("edituser")
    fun profileupdate(
        @Part("user_id") driver_id: RequestBody,
        @Part image: MultipartBody.Part?,
    ): Call<EditProfileResponse>


    @POST("usereditprofile")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun Editprofile(
        @Body stringStringHashMap: HashMap<String, String>,
    ): Call<EditPResponse>

    @POST("triphistory")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun TripHistory(
        @Body stringStringHashMap: HashMap<String, String>,
    ): Call<TripHistoryResponse>
}