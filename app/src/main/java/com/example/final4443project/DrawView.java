package com.example.final4443project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Custom drawview class for the painting app
 * <p>
 * Code below is based on a paint application tutorial from geekforgeeks website
 * (https://www.geeksforgeeks.org/how-to-create-a-paint-application-in-android/)
 * and a multitouch code implementation from the user Divers on the website, stackOverflow
 * (https://stackoverflow.com/questions/11966692/android-smooth-multi-touch-drawing)
 * <p>
 * despite being mostly based on the tutorial, the code is modified a bit to satisfy the
 * needs of our final project
 */
public class DrawView extends View {

    private static final float TOUCH_TOLERANCE = 4;
    final static int INVALID = -1;
    //private float mX, mY;
    //private Path mPath;
    private HashMap<Integer, Float> mX = new HashMap<Integer, Float>();
    private HashMap<Integer, Float> mY = new HashMap<Integer, Float>();
    private HashMap<Integer, Path> paths = new HashMap<Integer, Path>();

    private int touchCount = 0;
    private int[] touchPointIDs = new int[5];

    // the Paint class encapsulates the color
    // and style information about
    // how to draw the geometries,text and bitmaps
    private Paint mPaint;

    // ArrayList to store all the strokes
    // drawn by the user on the Canvas
    private ArrayList<Stroke> pathsH = new ArrayList<>();
    private int currentColor;
    private int strokeWidth;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    private float benchmarkCircleSize=300f;

    // Constructors to initialise all the attributes
    public DrawView(Context context) {
        this(context, null);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();

        // the below methods smoothens
        // the drawings of the user
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        // 0xff=255 in decimal
        mPaint.setAlpha(0xff);

    }

    // this method instantiate the bitmap and object
    public void init(int height, int width) {

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        // set an initial color of the brush
        currentColor = Color.GREEN;

        // set an initial brush size
        strokeWidth = 20;
    }

    // sets the current color of stroke
    public void setColor(int color) {
        currentColor = color;
    }

    // sets the stroke width
    public void setStrokeWidth(int width) {
        strokeWidth = width;
    }

    public void undo() {
        // check whether the List is empty or not
        // if empty, the remove method will return an error
        if (paths.size() != 0) {
            paths.remove(paths.size() - 1);
            //invalidate();
        }
        if (pathsH.size() != 0) {
            pathsH.remove(pathsH.size() - 1);
            invalidate();
        }
    }

    public void clear() {
        mCanvas.drawColor(Color.WHITE);
        invalidate();
        paths.clear();
        pathsH.clear();
    }

    public void benchmarkDrawCircle() {
        int centerX = mCanvas.getWidth() / 2, centerY = mCanvas.getHeight() / 2;

        //Log.d("SEND HELP", String.valueOf(pathsH.size()));
        Path pathHold = new Path();
        pathHold.addCircle(centerX, centerY, benchmarkCircleSize, Path.Direction.CCW);
        benchmarkCircleSize+=Math.floor(Math.random() *(10 - (-10) + 1) + (-10));;

        Stroke stroke = new Stroke(currentColor, strokeWidth, pathHold);
        pathsH.add(stroke);

        //Log.d("SEND HELP", String.valueOf(pathsH.size()));
    }

    // this is the main method where
    // the actual drawing takes place
    @Override
    protected void onDraw(Canvas canvas) {
        // save the current state of the canvas before,
        // to draw the background of the canvas
        canvas.save();

        // DEFAULT color of the canvas
        int backgroundColor = Color.WHITE;
        mCanvas.drawColor(backgroundColor);

        // now, we iterate over the list of paths
        // and draw each path on the canvas
        for (Stroke fp : pathsH) {
            mPaint.setColor(fp.color);
            mPaint.setStrokeWidth(fp.strokeWidth);
            mCanvas.drawPath(fp.path, mPaint);
        }

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();
    }
    /*
     * Multitouch solution by Divers on stackoverflow, https://stackoverflow.com/questions/11966692/android-smooth-multi-touch-drawing
     *
     * modified Divers' solution a bit to satisfy the needs of the project and remove
     * some small unnecessary things that we don't need for this project
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int maskedAction = event.getActionMasked();
        //Log.d(TAG, "onTouchEvent");

        switch (maskedAction) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                    Path p = new Path();
                    p.moveTo(event.getX(i), event.getY(i));
                    paths.put(event.getPointerId(i), p);
                    mX.put(event.getPointerId(i), event.getX(i));
                    mY.put(event.getPointerId(i), event.getY(i));
                    Stroke fp = new Stroke(currentColor, strokeWidth, p);
                    pathsH.add(fp);
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                    Path p = paths.get(event.getPointerId(i));
                    if (p != null) {
                        float x = event.getX(i);
                        float y = event.getY(i);
                        p.quadTo(mX.get(event.getPointerId(i)), mY.get(event.getPointerId(i)), (x + mX.get(event.getPointerId(i))) / 2,
                                (y + mY.get(event.getPointerId(i))) / 2);
                        mX.put(event.getPointerId(i), event.getX(i));
                        mY.put(event.getPointerId(i), event.getY(i));
                    }
                }
                invalidate();
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP: {
                for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                    Path p = paths.get(event.getPointerId(i));
                    if (p != null) {
                        p.lineTo(event.getX(i), event.getY(i));
                        invalidate();
                    }
                }
                break;
            }
        }
        return true;
    }
}