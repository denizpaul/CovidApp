package com.dennis.covidapp.ui.viewmodel

import android.app.Application
import android.content.res.Resources
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dennis.covidapp.R
import com.dennis.covidapp.domain.models.CovidApiResponse
import com.dennis.covidapp.domain.usecases.CovidReportUseCase
import com.dennis.covidapp.util.ScreenState
import com.dennis.covidapp.domain.models.RegionsResponse
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class CovidViewModel @Inject constructor(
    application: Application,
    private val covidReportUseCase: CovidReportUseCase,
    private val resources: Resources
) : AndroidViewModel(application) {

    private var globalReportMutableLiveData: MutableLiveData<ScreenState<CovidApiResponse>> = MutableLiveData()
    val globalReportLiveData : LiveData<ScreenState<CovidApiResponse>> = globalReportMutableLiveData;

    private var regionsMutableLiveData: MutableLiveData<ScreenState<RegionsResponse>> = MutableLiveData()
    val regionsLiveData : LiveData<ScreenState<RegionsResponse>> = regionsMutableLiveData;

    private var regionReportMutableLiveData: MutableLiveData<ScreenState<CovidApiResponse>> = MutableLiveData()
    val regionReportLiveData : LiveData<ScreenState<CovidApiResponse>> = regionReportMutableLiveData;

    fun getGlobalReport() = viewModelScope.launch {
        getGlobalStatisticsData()
    }

    fun getRegions() = viewModelScope.launch {
        getRegionData()
    }

    fun getRegionSpecificReport(regionCode: String) = viewModelScope.launch {
        getRegionSpecificData(regionCode)
    }

    private suspend fun getGlobalStatisticsData() {
        globalReportMutableLiveData.postValue(ScreenState.Loading())
        try {
            val response: Response<CovidApiResponse> = covidReportUseCase.getGlobalStatistics()
            globalReportMutableLiveData.postValue(handleReportResponse(response))
        } catch (t: Throwable) {
            when(t) {
                is IOException -> globalReportMutableLiveData.postValue(ScreenState.Error(resources.getString(
                    R.string.error_could_not_connect)))
                else -> globalReportMutableLiveData.postValue(ScreenState.Error(resources.getString(
                    R.string.error_no_signal)))
            }
        }

    }

    private fun handleReportResponse(response: Response<CovidApiResponse>): ScreenState<CovidApiResponse> {
        if(response.isSuccessful) {
            response.body().let { covidApiResponse ->
                return ScreenState.Success(covidApiResponse!!)
            }
        }
        return ScreenState.Error(response.message())
    }

    private suspend fun getRegionData() {
        regionsMutableLiveData.postValue(ScreenState.Loading())
        try {
            val response: Response<RegionsResponse> = covidReportUseCase.getRegions()
            regionsMutableLiveData.postValue(handleRegionsResponse(response))
        } catch (t: Throwable) {
            when(t) {
                is IOException -> regionsMutableLiveData.postValue(ScreenState.Error(resources.getString(
                    R.string.error_could_not_connect)))
                else -> regionsMutableLiveData.postValue(ScreenState.Error(resources.getString(
                    R.string.error_no_signal)))
            }
        }

    }

    private fun handleRegionsResponse(response: Response<RegionsResponse>): ScreenState<RegionsResponse> {
        if(response.isSuccessful) {
            response.body().let { regionsResponse ->
                return ScreenState.Success(regionsResponse!!)
            }
        }
        return ScreenState.Error(response.message())
    }

    private suspend fun getRegionSpecificData(regionCode: String) {
        regionReportMutableLiveData.postValue(ScreenState.Loading())
        try {
            val response: Response<CovidApiResponse> = covidReportUseCase.getRegionSpecificReport(regionCode)
            regionReportMutableLiveData.postValue(handleReportResponse(response))
        } catch (t: Throwable) {
            when(t) {
                is IOException -> regionReportMutableLiveData.postValue(ScreenState.Error(resources.getString(
                    R.string.error_could_not_connect)))
                is JsonSyntaxException -> regionReportMutableLiveData.postValue(ScreenState.Error(resources.getString(
                    R.string.error_no_data)))
                else -> regionReportMutableLiveData.postValue(ScreenState.Error(resources.getString(
                    R.string.error_no_signal)))
            }
        }

    }

}