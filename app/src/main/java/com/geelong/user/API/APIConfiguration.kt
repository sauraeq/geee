package com.geelong.user.API


import com.geelong.user.Response.*
import com.geelong.user.ReveiwResponse
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
    ): Call<BookingResponse>

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

    @POST("addreview")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun Review(
        @Body stringStringHashMap: HashMap<String, String>,
    ): Call<ReveiwResponse>

    @POST("notification")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun Noti(
        @Body stringStringHashMap: HashMap<String, String>,
    ): Call<NotificationResponse>

    @POST("cancelreasondata")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun CancelReason(): Call<CancelTripReasonResponse>

    @POST("cancelride")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun CancelResultSubmission(
        @Body stringStringHashMap: HashMap<String, String>,
    ): Call<CancelSubmitResponse>


    @POST("subscription")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun payment(
        @Body stringStringHashMap: HashMap<String, String>,
    ): Call<PyMentResp>

    @POST("newnotification")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun NewNotification(
        @Body stringStringHashMap: HashMap<String, String>,
    ): Call<NewNotificationResponse>

    @POST("viewnotification")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun ViewNotification(
        @Body stringStringHashMap: HashMap<String, String>,
    ): Call<ViewNotificationResponse>

    @POST("paylater")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun Paylater(
        @Body stringStringHashMap: HashMap<String, String>,
    ): Call<PaylaterResponse>

    @POST("bookingstatus")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun booking_status(
        @Body stringStringHashMap: HashMap<String, String>,
    ): Call<BookingStsResponse>

    @POST("bookingstatus")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun booking_status_first(
        @Body stringStringHashMap: HashMap<String, String>,
    ): Call<BookingStatusResponse>

    @POST("cofirmbooking")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun confirm_booking(
        @Body stringStringHashMap: HashMap<String, String>,
    ): Call<CnfBookingResponse>

}