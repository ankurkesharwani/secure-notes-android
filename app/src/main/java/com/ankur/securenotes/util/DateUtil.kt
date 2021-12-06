package com.ankur.securenotes.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
  @SuppressLint("SimpleDateFormat")
  fun getDisplayableDate(date: Date?, format: String = "dd MMM, yyyy"): String? {
    if (date == null) {
      return null
    }

    val dateFormat = SimpleDateFormat(format)
    dateFormat.timeZone = TimeZone.getDefault()
    return dateFormat.format(date)
  }
}