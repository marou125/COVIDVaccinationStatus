package com.marou125.covidvaccinationstatus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

/**This activity is displayed if something goes wrong in the try-block of the CountryDetailActivity
 * The catch block starts this activity**/
class NoDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_data)
        val countryName = intent.getIntExtra("country",0)
        val country = CountryDataSingleton.findCountryByName(this, getString(countryName))


        val detailFlagIV = findViewById<ImageView>(R.id.detail_flag_iv)
        val detailCountryNameTV = findViewById<TextView>(R.id.detail_country_name_tv)

        detailFlagIV.setImageResource(country.flag)
        detailCountryNameTV.text = getString(country.name)
    }

    override fun onBackPressed() {
        startActivity(Intent(this, CountryListActivity::class.java))
        super.onBackPressed()
    }
}

