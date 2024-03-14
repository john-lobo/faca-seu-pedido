package com.jlndev.baseservice.network

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NetworkProviders {

    private val httpClient = OkHttpClient.Builder()

    fun <S> providerRetrofit(serviceClass: Class<S>, baseUrl: String): S =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient.addInterceptorBody().build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setPrettyPrinting().create())) // GsonBuilder for JSON formatting
            .build()
            .create(serviceClass)

    private fun OkHttpClient.Builder.addInterceptorBody(): OkHttpClient.Builder {
            addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            addInterceptor(JsonFormattingInterceptor())
        return this
    }

    class JsonFormattingInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalResponse = chain.proceed(chain.request())
            val bodyString = originalResponse.body?.string() ?: ""
            val formattedJson = try {
                GsonBuilder().setPrettyPrinting().create().toJson(JsonParser.parseString(bodyString))
            } catch (e: JsonParseException) {
                bodyString
            }
            return originalResponse.newBuilder()
                .body(ResponseBody.create(originalResponse.body?.contentType(), formattedJson))
                .build()
        }
    }
}
