package com.nk.tokitelist.fragments.session
// TODO: 13.11.20 nightdaytheme 
// TODO: 11.11.20 ratable session
// TODO: 17.11.20 kitem in checlked list antipppen crashes 
// TODO: 17.11.20 sessions in summer/winter farbe 

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.nk.tokitelist.R
import com.nk.tokitelist.data.models.KiteSession
import com.nk.tokitelist.data.models.Season
import com.nk.tokitelist.data.models.Spot
import kotlinx.android.synthetic.main.row_layout.view.*
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
        // TODO: 17.11.20 introduce season for session and colorize accordingly 
        when (dataList[position].spot) {
            dataList[position].spot -> holder.itemView.row_session_background.background.setTint(ContextCompat.getColor(holder.itemView.context, R.color.cold))
        }
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