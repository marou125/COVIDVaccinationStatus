package com.marou125.covidvaccinationstatus

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.marou125.covidvaccinationstatus.database.Country

class CountryListAdapter(private val countryList: List<Country>) : RecyclerView.Adapter<CountryListAdapter.ViewHolder>() {

    class ViewHolder(view: View, countryList: List<Country>) : RecyclerView.ViewHolder(view){
        val countryName : TextView = view.findViewById(R.id.country_name_tv)
        val countryFlag : ImageView = view.findViewById(R.id.flag_iv)

        init {
            view.setOnClickListener {
                val i = Intent(view.context, CountryDetailActivity::class.java)
                i.putExtra("country", countryList.get(adapterPosition).name)
                startActivity(view.context, i, null)
            }


        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

        return ViewHolder(view, countryList)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.countryFlag.setImageResource(countryList[position].flag)
        holder.countryName.text = countryList[position].name
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

}