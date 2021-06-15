package com.marou125.covidvaccinationstatus

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marou125.covidvaccinationstatus.database.Country
import com.marou125.covidvaccinationstatus.database.CountryRepository
import com.marou125.covidvaccinationstatus.service.VaccinationData
import java.lang.reflect.Executable
import java.security.AccessController.getContext
import java.sql.Date
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class CountryListViewModel : ViewModel() {

    var europe = CountryDataSingleton.europe
    var americas = CountryDataSingleton.americas
    var africa = CountryDataSingleton.africa
    var asiaPacific = CountryDataSingleton.asiaPacific
    val world = CountryDataSingleton.world
    val countryRepository: CountryRepository = CountryRepository.get()
    var countryListLiveData = countryRepository.getCountries()
    val executor = Executors.newSingleThreadExecutor()

    var favourites = CountryDataSingleton.favourites

    //boolean for the sort button
    var sortedByName = true


    init {
        insert(world)
//        favourites.value = CountryDataSingleton.favourites
    }

    //fills database with countries if freshly installed
    private fun insert(world: List<Country>) {
        if(countryListLiveData.value == null){
            executor.execute{
                for (country in world){
                    countryRepository.insertCountry(country)
                }
            }
            countryListLiveData = countryRepository.getCountries()
        }
    }

    //TODO: Sort the other lists too, maybe only the one that is selected
    fun sortCountries(context: Context, continent: List<Country>): List<Country>{
        var sorted = continent
        if(sortedByName) {
            sorted = continent.sortedBy { it.population }.reversed()
        } else {
            sorted = continent.sortedBy{context.resources.getString(it.name)}
        }
        return sorted
    }
}