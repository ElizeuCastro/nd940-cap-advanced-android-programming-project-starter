package com.example.android.politicalpreparedness.utils

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.politicalpreparedness.R
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

@BindingAdapter("followText")
fun bindFollowText(button: Button, isFollow: Boolean) {
    button.text = if (isFollow) {
        button.context.getString(R.string.unfollow_button)
    } else {
        button.context.getString(R.string.follow_button)
    }
}

@BindingAdapter("visible")
fun bindContentVisibility(view: View, content: String?) {
    view.visibility = if (content.isNullOrEmpty()) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("fadeVisible")
fun setFadeVisible(view: View, visible: Boolean? = true) {
    if (view.tag == null) {
        view.tag = true
        view.visibility = if (visible == true) View.VISIBLE else View.GONE
    } else {
        view.animate().cancel()
        if (visible == true) {
            if (view.visibility == View.GONE)
                view.fadeIn()
        } else {
            if (view.visibility == View.VISIBLE)
                view.fadeOut()
        }
    }
}
