package com.dennis.covidapp.data.repository

import com.dennis.covidapp.data.api.CovidApi
import javax.inject.Inject

open class CovidRepository @Inject constructor(private val covidApi: CovidApi) {
    suspend fun getGlobalStatistics() =
        covidApi.getGlobalStatistics()

    suspend fun getRegions() =
        covidApi.getRegions()

    suspend fun getRegionSpecificReport(regionCode: String) =
        covidApi.getRegionSpecificReport(regionCode)


}