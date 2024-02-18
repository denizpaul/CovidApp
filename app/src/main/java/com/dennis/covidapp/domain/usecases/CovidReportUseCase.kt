package com.dennis.covidapp.domain.usecases

import com.dennis.covidapp.domain.models.CovidApiResponse
import com.dennis.covidapp.data.repository.CovidRepository
import com.dennis.covidapp.domain.models.RegionsResponse
import retrofit2.Response
import javax.inject.Inject

class CovidReportUseCase @Inject constructor(private val covidRepository: CovidRepository) {

    suspend fun getGlobalStatistics(): Response<CovidApiResponse> {
        return covidRepository.getGlobalStatistics()
    }

    suspend fun getRegions(): Response<RegionsResponse> {
        return covidRepository.getRegions()
    }

    suspend fun getRegionSpecificReport(regionCode: String): Response<CovidApiResponse> {
        return covidRepository.getRegionSpecificReport(regionCode)
    }
}
