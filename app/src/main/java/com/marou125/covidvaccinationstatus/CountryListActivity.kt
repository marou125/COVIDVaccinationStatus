package com.marou125.covidvaccinationstatus

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.marou125.covidvaccinationstatus.database.Country
import java.lang.reflect.Type

class CountryListActivity : AppCompatActivity() {

    val recyclerView by lazy {
        findViewById<RecyclerView>(R.id.recyclerView)
    }

    val viewModel by lazy {
        ViewModelProvider(this).get(CountryListViewModel::class.java)
    }

    val emptyList by lazy {
        findViewById<TextView>(R.id.emptyList)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_list)

        recyclerView.layoutManager = LinearLayoutManager(this)

        readFavourites()

        var currentList: List<Country> = viewModel.favourites


        viewModel.countryListLiveData.observe(
            this,
            Observer { countries ->
                countries?.let {
                    updateUI(currentList)
                }
            }
        )

        //TODO: this resets the tint but also appears to remove the selection navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        //bottomNav.itemIconTintList=null

        bottomNav.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.favourites -> {
                    currentList = viewModel.favourites
                }
                R.id.europe -> {
                    currentList = viewModel.europe
                }
                R.id.americas -> {
                    currentList = viewModel.americas
                }
                R.id.asiaPacific -> {
                    currentList = viewModel.asiaPacific
                }
                R.id.africa -> {
                    currentList = viewModel.africa
                }
            }
            updateUI(currentList)
            true
        }

        //TODO: BUG: When switching lists the sorting gets reset, has to do with the boolean value not being saved properly maybe
        findViewById<Button>(R.id.sort_button).setOnClickListener {
            var mToast = Toast.makeText(this,"",Toast.LENGTH_SHORT)
            currentList = viewModel.sortCountries(currentList)
            if(viewModel.sortedByName){
                mToast.setText(getString(R.string.sorted_by_name))
            } else {
                mToast.setText(getString(R.string.sorted_by_population))
            }
            updateUI(currentList)
            mToast.show()
        }


    }

    override fun onPause() {
        saveFavourites(CountryDataSingleton.favourites)
        super.onPause()
    }

    private fun readFavourites(){
        val prefs = this.getSharedPreferences("ListActivity", Context.MODE_PRIVATE) ?: return
        val gson = Gson()
        val json = prefs.getString("FavouriteList", null)
        val type = object : TypeToken<ArrayList<Country>>() {}.type
        val favouriteList = gson.fromJson<ArrayList<Country>>(json, type)
        if(favouriteList != null){
            CountryDataSingleton.fillFavourites(favouriteList)
        }
    }

    private fun saveFavourites(favourites: ArrayList<Country>){
        val prefs = this.getSharedPreferences("ListActivity", Context.MODE_PRIVATE) ?: return
        with(prefs.edit()){
            val gson = Gson()
            val json = gson.toJson(favourites)
            putString("FavouriteList", json)
            apply()
        }
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, getString(R.string.ExitToast), Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    fun updateUI(continent: List<Country>){
            recyclerView.adapter = CountryListAdapter(continent)
            if(continent.isEmpty()){
                emptyList.visibility = TextView.VISIBLE
            } else {
                emptyList.visibility = TextView.GONE
            }
        }


}