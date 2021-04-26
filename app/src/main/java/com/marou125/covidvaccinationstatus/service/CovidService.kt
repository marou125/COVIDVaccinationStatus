package com.marou125.covidvaccinationstatus.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface CovidService {
    @GET("owid/covid-19-data/master/public/data/vaccinations/vaccinations.json")
    fun getVaccinationData(): Call<Array<VaccinationData>>


    //https://covid-api.mmediagroup.fr/v1/cases
    @GET
    fun getCaseData(@Url url: String): Call<JsonCaseData>
}