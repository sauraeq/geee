package com.geelong.user.API

class APIUtils {
    companion object {

        private val BASE_URL = "http://demo.equalinfotech.com/geelong/api/users/"

        fun getServiceAPI(): APIConfiguration? {
            return APIClient.getApiClient(BASE_URL)!!.create(APIConfiguration::class.java)


        }

    }
}
