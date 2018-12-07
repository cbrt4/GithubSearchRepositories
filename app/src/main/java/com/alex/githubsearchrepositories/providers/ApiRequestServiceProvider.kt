package com.alex.githubsearchrepositories.providers

import android.content.Context
import com.alex.githubsearchrepositories.model.network.ApiRequestService
import com.alex.githubsearchrepositories.model.network.BASE_URL
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class ApiRequestServiceProvider {
    companion object {

        private var apiRequestService: ApiRequestService? = null

        fun init(context: Context) {

            val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES)
                    .followRedirects(true)
                    .addInterceptor(ChuckInterceptor(context))
                    .cache(Cache(File(context.cacheDir, "responses"), 1024 * 1024 * 16))
                    .build()

            apiRequestService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                            .setLenient()
                            .create()))
                    .build().create(ApiRequestService::class.java)
        }

        fun apiRequestService(): ApiRequestService {
            return apiRequestService as ApiRequestService
        }
    }
}