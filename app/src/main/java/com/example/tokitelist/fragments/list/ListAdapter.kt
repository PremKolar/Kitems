package com.example.tokitelist.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tokitelist.R
import com.example.tokitelist.data.models.KiteItem
import com.example.tokitelist.data.models.Season
import kotlinx.android.synthetic.main.row_layout.view.*

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    var dataList = emptyList<KiteItem>()

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_layout,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.txt_name.text = dataList[position].name
        holder.itemView.row_background.setOnClickListener{
            holder.itemView.findNavController().navigate(R.id.action_listFragment_to_editFragment)
        }

        when (dataList[position].season) {
            Season.summer -> holder.itemView.season_indicator.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.hot))
            Season.winter -> holder.itemView.season_indicator.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.cold))
            Season.always -> holder.itemView.season_indicator.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.allYear))
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(data: List<KiteItem>){
        this.dataList = data
        notifyDataSetChanged()
    }
}