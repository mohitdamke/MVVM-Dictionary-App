package com.example.mvvmnotesapp.di

import com.example.mvvmnotesapp.data.api.DictionaryApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    //HERWRSDSDSADDDD Might be error in the code
    @Provides
    @Singleton
    fun provideDictionaryApi(): DictionaryApi {

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(DictionaryApi.BASE_URL)
            .client(OkHttpClient.Builder().build())
            .build()
            .create(DictionaryApi::class.java)
    }
}