package com.notely.utility;

import android.content.Context;
import android.text.format.DateUtils;

import java.util.Date;

/**
 * Created by yashwant on 26/01/18.
 */

public class Helper {
    public static String getDate(Context context, long milliSeconds) {
        return DateUtils.getRelativeTimeSpanString(milliSeconds, new Date().getTime(), 0L, DateUtils.FORMAT_ABBREV_ALL).toString();
    }

}
