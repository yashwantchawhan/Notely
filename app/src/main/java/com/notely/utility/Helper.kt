package com.notely.utility

import android.content.Context
import android.text.format.DateUtils

import java.util.Date

/**
 * Created by yashwant on 26/01/18.
 */

object Helper {
    fun getDate(context: Context, milliSeconds: Long): String {
        return DateUtils.getRelativeTimeSpanString(milliSeconds, Date().time, 0L, DateUtils.FORMAT_ABBREV_ALL).toString()
    }

}
