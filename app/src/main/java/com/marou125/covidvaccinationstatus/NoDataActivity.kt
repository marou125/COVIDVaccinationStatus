package com.marou125.covidvaccinationstatus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class NoDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_data)
        val countryName = intent.getStringExtra("country")
        val country = CountryDataSingleton.findCountry(countryName!!)


        val detailFlagIV = findViewById<ImageView>(R.id.detail_flag_iv)
        val detailCountryNameTV = findViewById<TextView>(R.id.detail_country_name_tv)

        detailFlagIV.setImageResource(country.flag)
        detailCountryNameTV.text = country.name
    }

    override fun onBackPressed() {
        startActivity(Intent(this, CountryListActivity::class.java))
        super.onBackPressed()
    }
}

