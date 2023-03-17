package com.example.benchmark;

import androidx.benchmark.macro.CompilationMode;
import androidx.benchmark.macro.FrameTimingMetric;
import androidx.benchmark.macro.StartupMode;
import androidx.benchmark.macro.StartupTimingMetric;
import androidx.benchmark.macro.junit4.MacrobenchmarkRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;

/**
 * The class that holds our automatic instrumented benchmarking tests,
 * which will be executed on our Android devices.
 *
 * For this class to work you will need a android device that is on API Level 33,
 * the reason is why because of the androidx dependencies it is using
 *
 * Unlike the profiling tests, it doesn't replicate the multi touch things and does not
 * draw circles. This is due to the limitations of the UI automator system. The reason
 * why is because macrobenchmark is not competitable with espresso, thus we have to
 * use UI automator for the benchmarking
 */
@RunWith(AndroidJUnit4.class)
public class Final4443ProjectBenchmarks {

    @Rule
    public MacrobenchmarkRule mBenchmarkRule = new MacrobenchmarkRule();

    //Default macro benchmark method for reference
    @Test
    public void startup() {
        mBenchmarkRule.measureRepeated(
                "com.example.final4443project",
                Collections.singletonList(new StartupTimingMetric()),
                CompilationMode.DEFAULT,
                StartupMode.COLD,
                5,
                scope -> {
                    scope.pressHome();
                    scope.startActivityAndWait();
                    return null;
                });
    }
    @Test
    public void drawLines(){
        mBenchmarkRule.measureRepeated(
                "com.example.final4443project",
                Collections.singletonList(new FrameTimingMetric()),
                CompilationMode.DEFAULT,
                StartupMode.COLD,
                2,
                scope -> {
                    scope.pressHome();
                    scope.startActivityAndWait();
                    benchmarkLine();
                    return null;
                });
    }
    public void benchmarkLine() {
        UiObject draw = new UiObject(new UiSelector().descriptionContains("draw_view"));

        for (int i=0;i<5;++i) {
            try {
                draw.click();
                draw.dragTo(1,1,10);
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}