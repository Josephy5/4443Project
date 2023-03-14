package com.example.final4443project;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.ColorInt;

/**
 * @author Kristiyan Petrov
 */

/**
 * One of the classes/code within the colorpicker package that the painting app's main activity uses
 *
 * Reason why it is here as a java class instead being imported in gradle is because the gradle system
 * couldn't import it from the gradle setting, stating that it is not there. Thus, we had to import the
 * whole package here as project files
 *
 * Code below is based on the github repo for the Kristiyan Petrov's colorpicker package
 * https://github.com/kristiyanP/colorpicker
 */
public class ColorUtils {

    /**
     * Returns true if the text color should be white, given a background color
     *
     * @param color background color
     * @return true if the text should be white, false if the text should be black
     */
    public static boolean isWhiteText(@ColorInt final int color) {
        final int red = Color.red(color);
        final int green = Color.green(color);
        final int blue = Color.blue(color);

        // https://en.wikipedia.org/wiki/YIQ
        // https://24ways.org/2010/calculating-color-contrast/
        final int yiq = ((red * 299) + (green * 587) + (blue * 114)) / 1000;
        return yiq < 192;
    }

    public static int getDimensionDp(int resID, Context context) {
        return (int) (context.getResources().getDimension(resID) / context.getResources().getDisplayMetrics().density);
    }

    public static int dip2px(float dpValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
