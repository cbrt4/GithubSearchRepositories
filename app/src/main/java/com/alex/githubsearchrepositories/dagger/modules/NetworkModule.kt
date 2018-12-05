package com.alex.githubsearchrepositories.dagger.modules

import android.content.Context
import com.alex.githubsearchrepositories.network.ApiRequestService
import com.alex.githubsearchrepositories.network.BASE_URL
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttp(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .followRedirects(true)
                .addInterceptor(ChuckInterceptor(context))
                .cache(Cache(File(context.cacheDir, "responses"), 1024 * 1024 * 16))
                .build()
    }

    @Provides
    @Singleton
    fun provideApiRequestService(okHttpClient: OkHttpClient): ApiRequestService {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                        .setLenient()
                        .create()))
                .build().create(ApiRequestService::class.java)
    }
}