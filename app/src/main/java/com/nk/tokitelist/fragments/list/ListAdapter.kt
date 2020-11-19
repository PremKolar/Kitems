package com.nk.tokitelist.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.nk.tokitelist.R
import com.nk.tokitelist.data.models.KiteItem
import com.nk.tokitelist.data.models.Season
import kotlinx.android.synthetic.main.row_layout.view.*

abstract class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    var dataList = emptyList<KiteItem>()

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val kiteItem = dataList[position]
        holder.itemView.txt_name.text = kiteItem.name
        holder.itemView.row_background.setOnClickListener{
            navigateToEditKitemFragment(kiteItem, holder)
        }

        when (kiteItem.season) {
            Season.summer -> setBackgroundColor(holder,R.color.hot)
            Season.winter -> setBackgroundColor(holder, R.color.cold)
            Season.always -> setBackgroundColor(holder, R.color.allYear)
        }
    }

    abstract fun navigateToEditKitemFragment(kiteItem: KiteItem, holder: MyViewHolder)


    private fun setBackgroundColor(holder: MyViewHolder, color: Int) {
        holder.itemView.row_background.background.setTint(
            ContextCompat.getColor(
                holder.itemView.context,
                color
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(data: List<KiteItem>?){
        if (data != null) {
            this.dataList = data
        }else{
            this.dataList = this.dataList.subList(0,0)
        }
        notifyDataSetChanged()
    }
}