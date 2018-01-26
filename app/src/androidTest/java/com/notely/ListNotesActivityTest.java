package com.notely;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.notely.ui.list.ListNotesActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by yashwant on 27/01/18.
 */

@RunWith(AndroidJUnit4.class)
public class ListNotesActivityTest {
    @Rule
    public ActivityTestRule<ListNotesActivity> activityTestRule =
            new ActivityTestRule<>(ListNotesActivity.class);

    @Test
    public void checkViewsDisplay() {

        onView(withId(R.id.rvNotes))
                .check(matches(isDisplayed()));

    }
}
