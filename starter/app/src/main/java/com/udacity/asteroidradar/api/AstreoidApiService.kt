package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit_scalar = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

private val retrofit_moshi = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

interface NasaApiService {

    @GET("neo/rest/v1/feed")
    suspend fun getNearAstreoids(@Query("api_key") api_key: String = Constants.API_KEY): String

    @GET("planetary/apod")
    suspend fun getPictureOfDay(@Query("api_key") api_key: String = Constants.API_KEY): PictureOfDay

}

object Network {

    val asteroidService: NasaApiService by lazy { retrofit_scalar.create(NasaApiService::class.java) }

    val pictureOfDayApiService: NasaApiService by lazy { retrofit_moshi.create(NasaApiService::class.java) }
}

