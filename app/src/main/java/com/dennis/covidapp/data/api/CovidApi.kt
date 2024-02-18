package com.dennis.covidapp.data.api

import com.dennis.covidapp.domain.models.CovidApiResponse
import com.dennis.covidapp.domain.models.RegionsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CovidApi {

    @GET("reports/total")
    suspend fun getGlobalStatistics(
    ):Response<CovidApiResponse>

    @GET("regions")
    suspend fun getRegions(
    ):Response<RegionsResponse>

    @GET("reports/total")
    suspend fun getRegionSpecificReport(
        @Query("iso")
        country: String = "AUS"
    ):Response<CovidApiResponse>

}