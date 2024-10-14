package com.dicoding.dicodingevent.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

object DateFormatter {
    @SuppressLint("SimpleDateFormat")
    fun convertDateFormat(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val outputFormat = SimpleDateFormat("dd MMMM yyyy HH:mm:ss")
        val date: Date = inputFormat.parse(dateString) ?: return "Invalid date format"
        return outputFormat.format(date)
    }
}