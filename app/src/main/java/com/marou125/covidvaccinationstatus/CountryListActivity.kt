package com.marou125.covidvaccinationstatus

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.marou125.covidvaccinationstatus.database.Country
import java.lang.reflect.Type

class CountryListActivity : AppCompatActivity(), OnItemClickListener {

    val recyclerView: RecyclerView by lazy {
        findViewById<RecyclerView>(R.id.recyclerView)
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(CountryListViewModel::class.java)
    }

    val emptyList by lazy {
        findViewById<TextView>(R.id.emptyList)
    }

    var currentList : List<Country> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_list)

        recyclerView.layoutManager = LinearLayoutManager(this)


        readFavourites()

        currentList = viewModel.favourites

        val toolbarTitle = findViewById<TextView>(R.id.toolbarTitle)


        viewModel.countryListLiveData.observe(
            this,
            Observer { countries ->
                countries?.let {
                    updateUI(currentList)
                }
            }
        )

//        viewModel.favourites.observe(
//                this,
//                {
//                    Log.i("OBSERVER", "LiveData changed")
//                    recyclerView.adapter?.notifyDataSetChanged()
//                }
//        )

        //TODO: this resets the tint but also appears to remove the selection navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        //bottomNav.itemIconTintList=null

        bottomNav.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.favourites -> {
                    toolbarTitle.text = "Favourites"
                    currentList = viewModel.favourites
                }
                R.id.europe -> {
                    toolbarTitle.text = "Europe"
                    currentList = viewModel.europe
                }
                R.id.americas -> {
                    toolbarTitle.text = "Americas"
                    currentList = viewModel.americas
                }
                R.id.asiaPacific -> {
                    toolbarTitle.text = "Asia/Pacific"
                    currentList = viewModel.asiaPacific
                }
                R.id.africa -> {
                    toolbarTitle.text = "Africa"
                    currentList = viewModel.africa
                }
            }
            updateUI(currentList)
            true
        }

        findViewById<Button>(R.id.sort_button).setOnClickListener {
            var mToast = Toast.makeText(this,"",Toast.LENGTH_SHORT)
            if(viewModel.sortedByName){
                mToast.setText(getString(R.string.sorted_by_name))
                viewModel.sortedByName = false
            } else {
                mToast.setText(getString(R.string.sorted_by_population))
                viewModel.sortedByName = true
            }
            updateUI(currentList)
            mToast.show()
        }

        //Test if bugged country is in case data

    }


    //TODO: This skips the view model and accesses the data directly,there should be another solution for this
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
            val sorted = viewModel.sortCountries(continent)
            recyclerView.adapter = CountryListAdapter(sorted, this)
            if(continent.isEmpty()){
                emptyList.visibility = TextView.VISIBLE
            } else {
                emptyList.visibility = TextView.GONE
            }
        }

    override fun onItemClick(position: Int) {
        val sorted = viewModel.sortCountries(currentList)
        val i = Intent(this, CountryDetailActivity::class.java)
        i.putExtra("country", sorted[position].name)
        startActivity(i, null)
    }

    override fun onLongClick(position: Int) {
        val sorted = viewModel.sortCountries(currentList)
        val toast = Toast.makeText(this, "", Toast.LENGTH_SHORT)
        if(CountryDataSingleton.favourites.contains(sorted[position])){
            CountryDataSingleton.favourites.remove(sorted[position])
            toast.setText("Removed from favourites: ${sorted[position].name}")
            toast.show()

        } else {
            CountryDataSingleton.favourites.add(sorted[position])
            toast.setText("Added to favourites: ${sorted[position].name}")
            toast.show()
        }
        updateUI(currentList)

    }


}