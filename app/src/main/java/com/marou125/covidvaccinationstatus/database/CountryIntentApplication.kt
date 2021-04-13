package com.marou125.covidvaccinationstatus.database

import android.app.Application

class CountryIntentApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CountryRepository.initialize(this)
    }
}