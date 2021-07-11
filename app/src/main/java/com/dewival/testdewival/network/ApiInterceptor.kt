package com.dewival.testdewival.network

import android.util.Log
import com.dewival.testdewival.BuildConfig
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody

class ApiInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

     /*   val url = chain.request().url
        val requestBuilder = url.newBuilder().build()*/
        val response = chain.proceed(chain.request())
        Log.d("TAG", "interceptURL: "+chain.request().url.toString())
        if (isAuthFailed(response.code) && chain.request().url.toString() != "${BuildConfig.BASE_URL}api/login/"){
            Log.d("TAG", "intercept222: ")
            return Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(401)
                .message(AUTH_FAILED)
                .body(ResponseBody.create(null, AUTH_FAILED)).build()
        }else{
            Log.d("TAG", "intercept: ")
            return response
        }
    }

    private fun isAuthFailed(code: Int): Boolean {
        Log.d("TAG", "isAuthFailed: "+code)
        return code == 401
    }

    companion object{
        const val AUTH_FAILED = "Auth failed"
        const val HTTP_401 = "HTTP 401"
    }
}