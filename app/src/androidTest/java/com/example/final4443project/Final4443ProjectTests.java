package com.example.final4443project;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import android.content.Context;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * The class that holds our automatic instrumented profiling tests (if there is any),
 * which will be executed on our Android devices.
 */
@RunWith(AndroidJUnit4.class)
public class Final4443ProjectTests {
    @Rule
    public ActivityTestRule<MainActivity> mActivityScenarioRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);
    @Test
    public void benchmarkCircle() {
        for (int i=0;i<50;++i){
            //draws the circle
            drawCircle(onView(withId(R.id.draw_view)));
            //click on the center of the drawable canvas to update the canvas due to some wacky logic that I don't have time to check
            onView(withId(R.id.draw_view)).perform(click());
            //introduce a slight delay
            onView(isRoot()).perform(waitFor(400));
        }
    }
    public static ViewAction waitFor(long delay) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for " + delay + "milliseconds";
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadForAtLeast(delay);
            }
        };
    }
    public static int drawCircle(ViewInteraction matcher){
        ViewAction va = new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(DrawView.class);
            }

            @Override
            public String getDescription() {
                return "Draw Circle";
            }

            @Override
            public void perform(UiController uiController, View view) {
                DrawView dv= (DrawView) view;
                dv.benchmarkDrawCircle();
            }
        };
        matcher.perform(va);

        return 1;
    }
}