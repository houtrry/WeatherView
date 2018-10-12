package com.houtrry.weatherviewlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: houtrry
 * @date: 2018/10/10 15:25
 * @version: $Rev$
 * @description: ${TODO}
 */

public class FlakeView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder mSurfaceHolder;
    private boolean mIsDrawing;
    private Canvas mCanvas;
    private int mWidth;
    private int mHeight;

    public FlakeView(Context context) {
        this(context, null);
    }

    public FlakeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlakeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            for (int i = 0; i < 100; i++) {
                RainFlake rainFlake = new RainFlake();
                rainFlake.initData(mWidth, mHeight);
                rainFlake.initPaint(mPaint);
                mFlakes.add(rainFlake);
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
    }

    @Override
    public void run() {
        while (mIsDrawing) {
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

    private void init(Context context, AttributeSet attrs) {
        initView(context, attrs);
        initData(context);
    }

    private Paint mPaint;

    private void initView(Context context, AttributeSet attrs) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(10);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    private List<BaseFlake> mFlakes = new ArrayList<>();

    private void initData(Context context) {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
        setZOrderOnTop(true);
        mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
    }

    private static final int TIME_FRAME = 30;

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

    private int count = 0;

    private void drawContent(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC);
        for (BaseFlake flake : mFlakes) {
            flake.draw(canvas, mPaint, count);
        }
        count++;
    }
}
