package com.houtrry.weatherviewlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author: houtrry
 * @time: 2018/8/20
 * @desc: ${TODO}
 */
public class RainView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;
    private boolean isDrawing;

    public RainView(Context context) {
        this(context, null);
    }

    public RainView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RainView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private static final int TIME_FRAME = 30;

    @Override
    public void run() {
        while (isDrawing) {
            long startTime = System.currentTimeMillis();
            synchronized (mSurfaceHolder) {
                doDrawTask();
            }
            long diffTime = System.currentTimeMillis() - startTime;
            while (diffTime <= TIME_FRAME) {
                diffTime = System.currentTimeMillis() - startTime;
                Thread.yield();
            }
        }
    }

    private void doDrawTask() {
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
            drawContent(mCanvas);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    private void drawContent(Canvas canvas) {

    }


    public static class RainLine {
        private float lineLength;
        private float lineWith;

        private float angle;

        private int lineColor;

    }
}
