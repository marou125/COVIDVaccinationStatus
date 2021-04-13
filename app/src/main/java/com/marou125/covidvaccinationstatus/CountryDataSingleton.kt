package com.marou125.covidvaccinationstatus

import com.marou125.covidvaccinationstatus.service.JsonCaseData
import com.marou125.covidvaccinationstatus.service.VaccinationData
import java.util.*

object CountryDataSingleton {

    var countryDataList = emptyList<VaccinationData>()
    var caseData:JsonCaseData? = null

    fun fillList(countryData: Array<VaccinationData>){
        countryDataList = countryData.toList()
    }

    fun fillCaseData(jsonData: JsonCaseData){
        caseData = jsonData
    }
}