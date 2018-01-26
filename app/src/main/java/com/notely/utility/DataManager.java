package com.notely.utility;

import com.notely.model.Filter;

import java.util.ArrayList;

/**
 * Created by yashwant on 25/01/18.
 */

public class DataManager {

    private static DataManager dataManager;
    final ArrayList<Filter> filters = new ArrayList<Filter>();

    public static DataManager getInstance() {
        if (dataManager == null) {
            dataManager = new DataManager();

        }
        return dataManager;
    }

    public ArrayList<Filter> getFilters() {
        if (filters.isEmpty()) {
            filters.add(new Filter(FilterType.Poem, false));
            filters.add(new Filter(FilterType.Star, false));
            filters.add(new Filter(FilterType.Story, false));
            filters.add(new Filter(FilterType.Favourite, false));
        }

        return filters;
    }

    public void clearFilter() {
        filters.clear();
    }
}
