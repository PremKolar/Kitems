package com.nk.tokitelist

import android.widget.DatePicker
import android.widget.Spinner
import java.util.*


class Tools {


    companion object {
        /**
         *
         * @param datePicker
         * @return a java.util.Date
         */
        fun getDateFromDatePicker(datePicker: DatePicker): Date? {
            val day = datePicker.dayOfMonth
            val month = datePicker.month
            val year = datePicker.year
            val calendar: Calendar = Calendar.getInstance()
            calendar.set(year, month, day)
            return calendar.getTime()
        }

        fun setSpinnerByValue(spinner: Spinner, name: String) {
            if (name == null) return
            spinner.setSelection(Tools.getSpinnerIndex(spinner, name))
        }

        private fun getSpinnerIndex(spinner: Spinner, myString: String): Int {
            for (i in 0 until spinner.count) {
                if (spinner.getItemAtPosition(i).toString().equals(myString, ignoreCase = true)) {
                    return i
                }
            }
            return -1
        }

        fun setDateSetterToDate(datePicker: DatePicker, date: Date) {
            val cal = Calendar.getInstance();
            cal.time = date
            val year = cal[Calendar.YEAR]
            val month = cal[Calendar.MONTH]
            val day = cal[Calendar.DAY_OF_MONTH]
            datePicker.updateDate(year, month, day)
        }
    }

}