package com.jlndev.baseservice.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NetworkProviders {

    private val httpClient = OkHttpClient.Builder()

    fun <S> providerRetrofit(serviceClass: Class<S>, baseUrl: String): S =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient.addInterceptorBody().build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(serviceClass)

    private fun OkHttpClient.Builder.addInterceptorBody() : OkHttpClient.Builder {
        if (BuildConfig.DEBUG) {
            addInterceptor(
                HttpLoggingInterceptor().apply {
                    level =  HttpLoggingInterceptor.Level.BODY
                }
            )
        }
        return this
    }
}