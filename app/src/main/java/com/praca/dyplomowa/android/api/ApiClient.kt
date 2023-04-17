package com.praca.dyplomowa.android.api

import android.content.Context
import com.praca.dyplomowa.android.utils.Constants
import com.praca.dyplomowa.android.utils.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    fun getInstance(context: Context): Retrofit {



        var httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        var okHttpClient = OkHttpClient
            .Builder()
            .authenticator { route, response ->
                if (response.code == 401 && response.request.url.toString() != "http://localhost:8080/auth/login") {
                    SessionManager.refreshToken(token = SessionManager.getRefreshToken(context)!!, context = context)
                    response.request.newBuilder().removeHeader("Authorization").addHeader("Authorization", "Bearer ${SessionManager.getAccessToken(context)}").build()
                } else if(response.code == 401 && response.request.url.toString() == "http://localhost:8080/auth/login"){
                    null
                } else {
                    response.request
                }
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor { chain ->
                if (SessionManager.getAccessToken(context).isNullOrBlank()){
                    chain.proceed(
                        chain.request()
                             .newBuilder()
                             .addHeader("Authorization", "Bearer ${SessionManager.getAccessToken(context)}")
                             .build())
                }else{
                    chain.proceed(
                        chain.request()
                             .newBuilder()
                             .addHeader("Authorization", "Bearer ${SessionManager.getAccessToken(context)}")
                             .build())
                }

            }.build()

        var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        return retrofit
    }

}