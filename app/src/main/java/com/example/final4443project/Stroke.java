package com.example.final4443project;

import android.graphics.Path;

/**
 * Stroke class for the main activity
 *
 * Code below is based on a paint application tutorial from geekforgeeks website
 * https://www.geeksforgeeks.org/how-to-create-a-paint-application-in-android/
 */
public class Stroke {

    // color of the stroke
    public int color;

    // width of the stroke
    public int strokeWidth;

    // a Path object to
    // represent the path drawn
    public Path path;

    // constructor to initialise the attributes
    public Stroke(int color, int strokeWidth, Path path) {
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.path = path;
    }
}