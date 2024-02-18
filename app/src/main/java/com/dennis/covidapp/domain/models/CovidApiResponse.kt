package com.dennis.covidapp.domain.models
import com.google.gson.annotations.SerializedName


data class CovidApiResponse (

  @SerializedName("data" ) var covidData : CovidData? = CovidData()

)