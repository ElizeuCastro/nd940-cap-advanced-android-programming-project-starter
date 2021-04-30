package com.example.android.politicalpreparedness.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("dateText")
fun bindElectionDateText(textView: TextView, date: Date?) {
    textView.text = if (date == null) {
        ""
    } else {
        val format = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US)
        format.format(date)
    }
}