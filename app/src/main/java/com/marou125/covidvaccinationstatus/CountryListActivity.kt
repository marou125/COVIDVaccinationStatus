package com.marou125.covidvaccinationstatus

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
import com.marou125.covidvaccinationstatus.database.Country

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

        var currentList = viewModel.favourites

//        if(viewModel.favourites.isEmpty()){
//            emptyList.visibility = TextView.VISIBLE
//        }


        viewModel.countryListLiveData.observe(
            this,
            Observer { countries ->
                countries?.let {
                    updateUI(currentList)
                }
            }
        )

//        val favouriteButton = findViewById<ImageView>(R.id.favButton)
//
//        favouriteButton.setOnClickListener {
//        }

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