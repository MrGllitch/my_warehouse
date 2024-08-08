package eu.glitch.mywarehouse.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DateUtils {

    companion object {

        private val DATE_FORMAT_CZ = "dd.MM.yyyy"
        private val DATE_FORMAT_EN = "MM/dd/yyyy"


        fun getDateString(date: Long): String {
            // vraci aktualni cas a datum
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = date

            val format: SimpleDateFormat
            if (LanguageUtils.isLanguageCzech()) {
                format = SimpleDateFormat(DATE_FORMAT_CZ, Locale.GERMAN)
            } else {
                format = SimpleDateFormat(DATE_FORMAT_EN, Locale.ENGLISH)
            }
            return format.format(calendar.time)
        }

    }

}
