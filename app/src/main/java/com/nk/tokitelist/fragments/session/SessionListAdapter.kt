package com.nk.tokitelist.fragments.session

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.nk.tokitelist.R
import com.nk.tokitelist.data.models.KiteSession
import com.nk.tokitelist.data.models.Season
import kotlinx.android.synthetic.main.session_row_layout.view.*
import kotlinx.android.synthetic.main.session_row_layout.view.row_session_background
import java.text.SimpleDateFormat

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
        var formatter = SimpleDateFormat("EEE, dd-MM-yyyy")
        val date = dataList[position].date
        holder.itemView.txt_session_date.text =  formatter.format(date)
        holder.itemView.row_session_background.setOnClickListener{
            val action = SessionOverviewFragmentDirections.actionSessionsOverviewFragmentToEditSessionFragment(dataList[position])
            holder.itemView.findNavController().navigate(action)
        }
        when (dataList[position].season) {
            Season.summer -> colorizeListItemAccordingToSeason(holder, R.color.hot)
            Season.winter -> colorizeListItemAccordingToSeason(holder, R.color.cold)
        }
        holder.itemView.ratingBar.rating = dataList[position].rating.rat.toFloat()
    }

    private fun colorizeListItemAccordingToSeason(holder: MyViewHolder, color: Int) {
        holder.itemView.row_session_background.background.setTint(ContextCompat.getColor(holder.itemView.context, color))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(data: Array<KiteSession>){
        if (data != null) {
            this.dataList = data.toList()
        }else{
            this.dataList = this.dataList.subList(0,0)
        }
        notifyDataSetChanged()
    }

}