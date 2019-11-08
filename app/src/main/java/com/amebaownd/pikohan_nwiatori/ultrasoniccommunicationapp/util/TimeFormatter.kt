package com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.util

import java.text.SimpleDateFormat

object TimeFormatter {
    const val TIME ="HH:mm"
    private val timePattern = SimpleDateFormat(TIME)

    fun timeToStr(time:Long) = timePattern.format(time).toString()
}