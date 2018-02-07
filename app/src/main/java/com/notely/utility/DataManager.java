package com.notely.utility;

import com.notely.model.Filter;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by yashwant on 25/01/18.
 */
@Singleton
public class DataManager {

    final ArrayList<Filter> filters = new ArrayList<Filter>();

    @Inject
    DataManager() {

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

}
