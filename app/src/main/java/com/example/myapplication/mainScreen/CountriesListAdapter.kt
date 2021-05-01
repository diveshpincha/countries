package com.example.myapplication.mainScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.database.CountriesData
import com.example.myapplication.databinding.CountryDataBinding

class CountriesListAdapter (val onClickListener:OnClickListener):
    ListAdapter<CountriesData,CountriesListAdapter.CountriesViewHolder>(DiffCallBack) {


    class CountriesViewHolder(private var binding: CountryDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(country: CountriesData) {
            binding.property=country
            binding.executePendingBindings()
        }
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<CountriesData>() {
        override fun areItemsTheSame(oldItem: CountriesData, newItem: CountriesData): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: CountriesData, newItem: CountriesData): Boolean {
            return oldItem.name == newItem.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesViewHolder {
        return CountriesViewHolder(CountryDataBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        val country = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(country)
        }
        holder.bind(country)
    }


    class OnClickListener(val clickListener: (Countries: CountriesData) -> Unit) {
        fun onClick(Countries: CountriesData) = clickListener(Countries)
    }
}