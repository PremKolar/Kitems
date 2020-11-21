package com.nk.tokitelist.data.viewmodel

import android.app.Application
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.nk.tokitelist.R
import com.nk.tokitelist.data.models.KiteItem
import com.nk.tokitelist.data.models.Season
import com.nk.tokitelist.data.models.SortMode

class SharedViewModel(application: Application):AndroidViewModel(application) {

    var dbIsEmpty:MutableLiveData<Boolean> = MutableLiveData(true)

    fun checkIfDataIsEmpty(kitems: List<KiteItem>?){
        dbIsEmpty.value = kitems?.isEmpty()
    }

    val listener: AdapterView.OnItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when(position){
                0 -> {colorizeMenuBar(parent, R.color.cold)}
                1 -> {colorizeMenuBar(parent, R.color.hot)}
                2 -> {colorizeMenuBar(parent, R.color.allYear)}
            }
        }

        private fun colorizeMenuBar(parent: AdapterView<*>?, color: Int) {
            (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, color))
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

    fun sortModeToText(sortMode: SortMode): String {
        return when(sortMode){
            SortMode.ALPHAASC -> "alphabetic ↓"
            SortMode.ALPHADESC -> "alphabetic ↑"
            SortMode.SEASONASC -> "season ↓"
            SortMode.SEASONDESC -> "season ↑"
            SortMode.INDEXASC -> "# added ↓"
            SortMode.INDEXDESC -> "# added ↑"
        }
    }
}