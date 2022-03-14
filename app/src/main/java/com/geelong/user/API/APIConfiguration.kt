package com.geelong.user.API


import com.geelong.user.Response.LoginResponse
import com.geelong.user.Response.PrivacyResponse
import com.geelong.user.Response.SignUpResponse
import com.geelong.user.Response.TermsResponse
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
    fun privacy(

    ): Call<PrivacyResponse>
    @POST("termsconditions")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun terms(

    ): Call<TermsResponse>



}