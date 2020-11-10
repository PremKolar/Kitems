package com.nk.tokitelist.fragments.session

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nk.tokitelist.R
import com.nk.tokitelist.data.models.KiteSession
import kotlinx.android.synthetic.main.session_row_layout.view.*

class SessionListAdapter: RecyclerView.Adapter<SessionListAdapter.MyViewHolder>() {

    var dataList = emptyList<KiteSession>()

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.session_row_layout,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.txt_session_name.text = dataList[position].spot.name
        holder.itemView.txt_session_date.text = dataList[position].date.toString()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(data: Array<KiteSession>){
        if (data != null) {
            this.dataList = data.toList()
        }else{
            this.dataList = this.dataList.subList(0,0) // todo
        }
        notifyDataSetChanged()
    }

}