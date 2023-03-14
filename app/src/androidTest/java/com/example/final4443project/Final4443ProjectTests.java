package com.example.final4443project;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * The class that holds our automatic instrumented profiling tests (if there is any),
 * which will be executed on our Android devices.
 */
@RunWith(AndroidJUnit4.class)
public class Final4443ProjectTests {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.a4443project", appContext.getPackageName());
    }
}