package com.notely.model;

import com.notely.utility.FilterType;

/**
 * Created by yashwant on 25/01/18.
 */

public class Filter {
    boolean isChecked;
    FilterType filterType;

    public Filter(FilterType filterType, boolean b) {
        this.filterType = filterType;
        this.isChecked = b;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public FilterType getFilterType() {
        return filterType;
    }

    public void setFilterType(FilterType filterType) {
        this.filterType = filterType;
    }
}
