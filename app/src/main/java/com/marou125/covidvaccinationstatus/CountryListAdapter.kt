package com.marou125.covidvaccinationstatus

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.marou125.covidvaccinationstatus.database.Country

interface OnItemClickListener {
    fun onItemClick(position: Int)
    fun onLongClick(position: Int)
}


class CountryListAdapter(context: Context, countryList: List<Country>,
                         val listener: OnItemClickListener, val isFavourites: Boolean = false
) : RecyclerView.Adapter<CountryListAdapter.ViewHolder>() {

    var mCountryList:MutableList<Country> = countryList.toMutableList()
    val mContext = context

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val countryName : TextView = view.findViewById(R.id.country_name_tv)
        val countryFlag : ImageView = view.findViewById(R.id.flag_iv)
        val favStar: ImageView = view.findViewById(R.id.favButton)

        init {
            view.setOnClickListener {
                if(adapterPosition != RecyclerView.NO_POSITION){
                    listener.onItemClick(adapterPosition)
                }
            }
            view.setOnLongClickListener {
                if(adapterPosition != RecyclerView.NO_POSITION){
                    listener.onLongClick(adapterPosition)
                    if(!isFavourites){
                        notifyDataSetChanged()
                    } else {
                        mCountryList.removeAt(adapterPosition)
                        notifyItemRemoved(adapterPosition)
                        notifyItemRangeChanged(adapterPosition, mCountryList.size)
                    }
                }
                true
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.countryFlag.setImageResource(mCountryList[position].flag)
        holder.countryName.text = mContext.resources.getString(mCountryList[position].name)
        if(CountryDataSingleton.favourites.contains(mCountryList[position]) && !isFavourites){
            holder.favStar.visibility = ImageView.VISIBLE
        } else {
            holder.favStar.visibility = ImageView.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return mCountryList.size
    }

}