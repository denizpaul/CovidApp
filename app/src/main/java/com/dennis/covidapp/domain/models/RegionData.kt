package com.dennis.covidapp.domain.models

import com.google.gson.annotations.SerializedName


data class RegionData (

  @SerializedName("iso"  ) var iso  : String? = null,
  @SerializedName("name" ) var name : String? = null

)