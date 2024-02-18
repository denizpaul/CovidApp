package com.dennis.covidapp.domain.models
import com.google.gson.annotations.SerializedName


data class CovidData (
  @SerializedName("date"           ) var date          : String? = null,
  @SerializedName("last_update"    ) var lastUpdate    : String? = null,
  @SerializedName("confirmed"      ) var confirmed     : Int?    = null,
  @SerializedName("confirmed_diff" ) var confirmedDiff : Int?    = null,
  @SerializedName("deaths"         ) var deaths        : Int?    = null,
  @SerializedName("deaths_diff"    ) var deathsDiff    : Int?    = null,
  @SerializedName("recovered"      ) var recovered     : Int?    = null,
  @SerializedName("recovered_diff" ) var recoveredDiff : Int?    = null,
  @SerializedName("active"         ) var active        : Int?    = null,
  @SerializedName("active_diff"    ) var activeDiff    : Int?    = null,
  @SerializedName("fatality_rate"  ) var fatalityRate  : Double? = null

)