package com.marou125.covidvaccinationstatus.service

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class VaccinationData (var country: String, var iso_code: String, var data:
    Array<VaccinationTimeStamp>) : Serializable {
}

class VaccinationTimeStamp(var date: String,
                           var total_vaccinations: String,
                           var people_vaccinated: String,
                           var people_fully_vaccinated: String,
var daily_vaccinations: String) : Serializable