package com.example.tokitelist.fragments.edit

import android.app.Application
import android.graphics.SumPathEffect
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.tokitelist.R
import com.example.tokitelist.data.models.KiteItem
import com.example.tokitelist.data.models.Season

class SharedViewModel(application: Application):AndroidViewModel(application) {

    var dbIsEmpty:MutableLiveData<Boolean> = MutableLiveData(true)

    fun checkIfDataIsEmpty(kitems: List<KiteItem>?){
        dbIsEmpty.value = kitems?.isEmpty()
    }

    val listener: AdapterView.OnItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when(position){
                0 -> {(parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application,R.color.cold))}
                1 -> {(parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application,R.color.hot))}
                2 -> {(parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.allYear))}
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}

    }

    fun seasonToInteger(season: Season): Int {
        return when(season){
            Season.winter -> 0
            Season.summer -> 1
            Season.always -> 2
        }
    }

    fun verifyDataFromUser(mName: String): Boolean {
        return mName.isNotEmpty()
    }

    fun strToSeason(string: String?): Season? {
        return when(string){
            "winter" -> Season.winter
            "summer" -> Season.summer
            else -> Season.always
        }
    }


}