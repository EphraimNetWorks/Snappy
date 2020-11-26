package com.networks.snappy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.networks.snappy.data.FaceProps
import kotlinx.android.synthetic.main.adapter_face_props.view.*

class FacePropRecyclerAdapter(
    private val filters: List<FaceProps>,
    private val actions: ViewHolderActions
) : RecyclerView.Adapter<FacePropRecyclerAdapter.FacePropViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacePropViewHolder {
        return FacePropViewHolder.create(parent,actions)
    }

    override fun onBindViewHolder(holder: FacePropViewHolder, position: Int) {
        holder.bind(filters[position])
    }

    override fun getItemCount(): Int {
        return filters.size
    }


    class FacePropViewHolder(
        view: View,
        private val actions: ViewHolderActions
    ) : RecyclerView.ViewHolder(view){

        private val propImageView = view.prop_img

        fun bind(props: FaceProps){
            propImageView.setImageResource(props.icon)
            propImageView.setOnClickListener {
                actions.applyFaceProp(props)
            }
        }

        companion object{
            fun create(parent: ViewGroup, actions: ViewHolderActions): FacePropViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.adapter_face_props, parent, false)
                return FacePropViewHolder(view, actions)
            }
        }
    }

    interface ViewHolderActions{
        fun applyFaceProp(props: FaceProps)
    }

}