package com.geelong.user.API


import com.geelong.user.Response.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

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


}