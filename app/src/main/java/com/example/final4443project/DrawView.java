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

    //private float[] mX = new float[5], mY = new float[5];
    //private Path[] mPath = new Path[5];
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

        /*for (int i = 0; i < 5; ++i) {
            touchPointIDs[i] = INVALID;
            mPath[i] = new Path();
        }*/

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

    // this methods returns the current bitmap
    /*public Bitmap save() {
        return mBitmap;
    }*/

    public void clear() {
        mCanvas.drawColor(Color.WHITE);
        invalidate();
        //for (int i = 0; i < 5; ++i) {
        paths.clear();
        pathsH.clear();
        //}
        //mPath.reset();
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
        //drawManually(mCanvas);
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
        /*for (int size = paths.size(), i = 0; i < size; i++) {
            Path path = paths.get(i);
            mPaint.setColor(currentColor);
            mPaint.setStrokeWidth(strokeWidth);
            if (path != null) {
                mCanvas.drawPath(path, mPaint);
            }
        }*/

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();
    }

    private HashMap<Integer, Float> mX = new HashMap<Integer, Float>();
    private HashMap<Integer, Float> mY = new HashMap<Integer, Float>();
    private HashMap<Integer, Path> paths = new HashMap<Integer, Path>();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int maskedAction = event.getActionMasked();

        //Log.d(TAG, "onTouchEvent");

        switch (maskedAction) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                    Path p = new Path();
                    //Stroke fp = new Stroke(currentColor, strokeWidth, p);
                    //pathsH.add(fp);
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
                        //paths.remove(event.getPointerId(i));
                        //mX.remove(event.getPointerId(i));
                        //mY.remove(event.getPointerId(i));
                    }
                }
                break;
            }
        }

        return true;
    }

    // the below methods manages the touch
    // response of the user on the screen

    // firstly, we create a new Stroke
    // and add it to the paths list
    /*private void touchStart(float x, float y) {
        mPath = new Path();
        Stroke fp = new Stroke(currentColor, strokeWidth, mPath);
        paths.add(fp);

        // finally remove any curve
        // or line from the path
        mPath.reset();

        // this methods sets the starting
        // point of the line being drawn
        mPath.moveTo(x, y);

        // we save the current
        // coordinates of the finger
        mX = x;
        mY = y;
    }

    // in this method we check
    // if the move of finger on the
    // screen is greater than the
    // Tolerance we have previously defined,
    // then we call the quadTo() method which
    // actually smooths the turns we create,
    // by calculating the mean position between
    // the previous position and current position
    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    // at the end, we call the lineTo method
    // which simply draws the line until
    // the end position
    private void touchUp() {
        mPath.lineTo(mX, mY);
    }*/

    // the onTouchEvent() method provides us with
    // the information about the type of motion
    // which has been taken place, and according
    // to that we call our desired methods
    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        // From demo_multitouch, get the index of the pointer (NOTE: will be 0 for ACTION_DOWN or ACTION_UP)
        final int pointerIndex = event.getActionIndex();
        // From demo_multitouch, get the id of the pointer
        final int id = event.getPointerId(pointerIndex);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchPointIDs[touchCount] = id;
                mPath[touchCount] = new Path();
                Stroke fp = new Stroke(currentColor, strokeWidth, mPath[touchCount]);
                paths.add(fp);

                // finally remove any curve
                // or line from the path
                mPath[touchCount].reset();

                // this methods sets the starting
                // point of the line being drawn
                mPath[touchCount].moveTo(x, y);

                // we save the current
                // coordinates of the finger
                mX[touchCount] = x;
                mY[touchCount] = y;
                ++touchCount;
                //touchStart(x, y);
                invalidate();
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                for (int i = 0; i < 5; ++i) {
                    if (touchPointIDs[i] == INVALID) {
                        touchPointIDs[i] = id;
                        mPath[touchCount] = new Path();
                        Stroke fp2 = new Stroke(currentColor, strokeWidth, mPath[i]);
                        paths.add(fp2);

                        // finally remove any curve
                        // or line from the path
                        mPath[i].reset();

                        // this methods sets the starting
                        // point of the line being drawn
                        mPath[i].moveTo(x, y);

                        // we save the current
                        // coordinates of the finger
                        mX[i] = x;
                        mY[i] = y;
                        ++touchCount;
                        break;
                    }
                }
                //touchStart(x, y);
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                //touchMove(x, y);
                for (int i = 0; i < 5; ++i) {
                    if (touchPointIDs[i] != INVALID) {
                        float dx = Math.abs(x - mX[i]);
                        float dy = Math.abs(y - mY[i]);

                        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                            mPath[i].quadTo(mX[i], mY[i], (x + mX[i]) / 2, (y + mY[i]) / 2);
                            mX[i] = x;
                            mY[i] = y;
                        }
                    }
                }
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                //touchUp();
                for (int i = 0; i < 5; ++i) {
                    if (touchPointIDs[i] == id) {
                        mPath[i].lineTo(mX[i], mY[i]);
                        //touchPointIDs[i] = INVALID;
                        //mPath[i].reset();
                        //--touchCount;
                        break;
                    }
                }
                invalidate();
                break;

        }
        return true;*/


}