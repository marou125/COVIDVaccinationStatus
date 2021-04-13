package com.marou125.covidvaccinationstatus

import com.marou125.covidvaccinationstatus.service.VaccinationData
import java.util.*

object CountryDataSingleton {

    var countryDataList = emptyList<VaccinationData>()

    fun fillList(countryData: Array<VaccinationData>){
        countryDataList = countryData.toList()
    }
}