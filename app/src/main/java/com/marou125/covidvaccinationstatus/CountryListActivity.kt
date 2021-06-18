package com.marou125.covidvaccinationstatus

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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

    val connectionFailed by lazy {
        intent.getBooleanExtra("connectionFailed", false)
    }

    var currentList: List<Country> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_country_list)
        setSupportActionBar(findViewById(R.id.toolbar_list))

        recyclerView.layoutManager = LinearLayoutManager(this)
        readFavourites()

        currentList = viewModel.favourites
        updateUI(currentList, true)

        val toolbarTitle = findViewById<TextView>(R.id.toolbarTitle)
        val toolbarTopLeft = findViewById<ImageView>(R.id.topleftIcon)


        //TODO: this resets the tint but also appears to remove the selection navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        //bottomNav.itemIconTintList=null

        bottomNav.setOnNavigationItemSelectedListener { item ->
            var isFavourites = false
            when (item.itemId) {
                R.id.favourites -> {
                    toolbarTitle.text = getString(R.string.Favourites)
                    toolbarTopLeft.setImageResource(R.drawable.favourite_topleft)
                    currentList = viewModel.favourites
                    isFavourites = true
                }
                R.id.europe -> {
                    toolbarTitle.text = getString(R.string.Europe)
                    toolbarTopLeft.setImageResource(R.drawable.europe_topleft)
                    currentList = viewModel.europe
                }
                R.id.americas -> {
                    toolbarTitle.text = getString(R.string.Americas)
                    toolbarTopLeft.setImageResource(R.drawable.americas_topleft)
                    currentList = viewModel.americas
                }
                R.id.asiaPacific -> {
                    toolbarTitle.text = getString(R.string.AsiaPacific)
                    toolbarTopLeft.setImageResource(R.drawable.asia_topleft)
                    currentList = viewModel.asiaPacific
                }
                R.id.africa -> {
                    toolbarTitle.text = getString(R.string.Africa)
                    toolbarTopLeft.setImageResource(R.drawable.africa_topleft)
                    currentList = viewModel.africa
                }
            }
            updateUI(currentList, isFavourites)
            true
        }

        if(connectionFailed){
            Toast.makeText(this, R.string.connection_failed, Toast.LENGTH_SHORT).show()
        }

//        findViewById<Button>(R.id.sort_button).setOnClickListener {
//            var mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT)
//            if (viewModel.sortedByName) {
//                mToast.setText(getString(R.string.sorted_by_name))
//                viewModel.sortedByName = false
//            } else {
//                mToast.setText(getString(R.string.sorted_by_population))
//                viewModel.sortedByName = true
//            }
//            var isFavourites = currentList == viewModel.favourites
//            updateUI(currentList, isFavourites)
//            mToast.show()
//        }


    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.sort_button -> { var mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT)
            if (viewModel.sortedByName) {
                mToast.setText(getString(R.string.sorted_by_name))
                viewModel.sortedByName = false
            } else {
                mToast.setText(getString(R.string.sorted_by_population))
                viewModel.sortedByName = true
            }
            var isFavourites = currentList == viewModel.favourites
            updateUI(currentList, isFavourites)
            mToast.show()
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_list_menu, menu)
        return true
    }


    //TODO: This skips the view model and accesses the data directly,there should be another solution for this
    override fun onPause() {
        saveFavourites(CountryDataSingleton.favourites)
        super.onPause()
    }

    private fun readFavourites() {
        val prefs = this.getSharedPreferences("ListActivity", Context.MODE_PRIVATE) ?: return
        val gson = Gson()
        val json = prefs.getString("FavouriteList", null)
        val type = object : TypeToken<ArrayList<Country>>() {}.type
        val favouriteList = gson.fromJson<ArrayList<Country>>(json, type)
        if (favouriteList != null) {
            CountryDataSingleton.fillFavourites(favouriteList)
        }
    }

    private fun saveFavourites(favourites: ArrayList<Country>) {
        val prefs = this.getSharedPreferences("ListActivity", Context.MODE_PRIVATE) ?: return
        with(prefs.edit()) {
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

    fun updateUI(continent: List<Country>, isFavourites: Boolean) {
        val sorted = viewModel.sortCountries(this, continent)
        recyclerView.adapter = CountryListAdapter(this, sorted, this, isFavourites)
        if (continent.isEmpty()) {
            emptyList.visibility = TextView.VISIBLE
        } else {
            emptyList.visibility = TextView.GONE
        }
    }

    override fun onItemClick(position: Int) {
        val sorted = viewModel.sortCountries(this, currentList)
        val i = Intent(this, CountryDetailActivity::class.java)
        i.putExtra("country", sorted[position].name)
        i.putExtra("connectionFailed", connectionFailed)
        startActivity(i, null)
    }

    override fun onLongClick(position: Int) {
        val sorted = viewModel.sortCountries(this, currentList)
        val toast = Toast.makeText(this, "", Toast.LENGTH_SHORT)
        if (CountryDataSingleton.favourites.contains(sorted[position])) {
            CountryDataSingleton.favourites.remove(sorted[position])
            if (CountryDataSingleton.favourites.isEmpty()) {
                emptyList.visibility = TextView.VISIBLE
            }
            toast.setText("${getString(R.string.Removed_from_favourites)} ${getString(sorted[position].name)}")
            toast.show()

        } else {
            emptyList.visibility = TextView.GONE
            CountryDataSingleton.favourites.add(sorted[position])
            toast.setText("${getString(R.string.Added_to_favourites)} ${getString(sorted[position].name)}")
            toast.show()
        }
        //updateUI(currentList)

    }


}