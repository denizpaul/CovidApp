package com.dennis.covidapp.domain.models

import com.dennis.covidapp.domain.models.RegionData
import com.google.gson.annotations.SerializedName


data class RegionsResponse (

  @SerializedName("data" ) var data : ArrayList<RegionData> = arrayListOf()

)