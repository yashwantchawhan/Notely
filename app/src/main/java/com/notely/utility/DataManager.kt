package com.notely.utility

import com.notely.model.Filter

import java.util.ArrayList

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by yashwant on 25/01/18.
 */
@Singleton
class DataManager @Inject
internal constructor() {

    internal val filters = ArrayList<Filter>()

    fun getFilters(): ArrayList<Filter> {
        if (filters.isEmpty()) {
            filters.add(Filter(FilterType.Poem, false))
            filters.add(Filter(FilterType.Star, false))
            filters.add(Filter(FilterType.Story, false))
            filters.add(Filter(FilterType.Favourite, false))
        }

        return filters
    }

}
