package com.notely;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.notely.model.Note;
import com.notely.ui.details.DetailsNoteActivity;
import com.notely.ui.list.ListNotesActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by yashwant on 27/01/18.
 */

@RunWith(AndroidJUnit4.class)
public class DetailsNoteActivityTest {
    @Rule
    public ActivityTestRule<DetailsNoteActivity> activityTestRule =
            new ActivityTestRule<>(DetailsNoteActivity.class);


    @Test
    public void checkViewsDisplay() {


    }
}
