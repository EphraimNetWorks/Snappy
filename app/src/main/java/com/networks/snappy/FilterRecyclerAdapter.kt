package com.networks.snappy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.networks.snappy.data.CameraFilter
import kotlinx.android.synthetic.main.adapter_filter.view.*

class FilterRecyclerAdapter(
    private val filters: List<CameraFilter>,
    private val actions: ViewHolderActions
) : RecyclerView.Adapter<FilterRecyclerAdapter.FilterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        return FilterViewHolder.create(parent,actions)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(filters[position])
    }

    override fun getItemCount(): Int {
        return filters.size
    }


    class FilterViewHolder(
        view: View,
        private val actions: ViewHolderActions
    ) : RecyclerView.ViewHolder(view){

        private val nameTextview = view.filter_name_textview

        fun bind(filter: CameraFilter){
            nameTextview.text = filter.name
            nameTextview.setOnClickListener {
                actions.applyFilter(filter)
            }
        }

        companion object{
            fun create(parent: ViewGroup, actions: ViewHolderActions): FilterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.adapter_filter, parent, false)
                return FilterViewHolder(view, actions)
            }
        }
    }

    interface ViewHolderActions{
        fun applyFilter(cameraFilter: CameraFilter)
    }
}