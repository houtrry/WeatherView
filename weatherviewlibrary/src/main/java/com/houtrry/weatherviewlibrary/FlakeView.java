package com.houtrry.weatherviewlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
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

    private int mBitmapMinAlpha = 20;
    private int mBitmapMaxAlpha = 220;
    private float mBitmapMinScale = 0.4f;
    private float mBitmapMaxScale = 1.0f;
    private BitmapDrawable mSnowBitmap = null;


    private float mMinSpeed = 2;
    private float mMaxSpeed = 8;
    private float mMinY = 0;
    private float mMaxY = 50;
    private int mAngle = 90;

    private float mMinRainLength = 10;
    private float mMaxRainLength = 20;
    private int mMinLineColorAlpha = 0;
    private int mMaxLineColorAlpha = 120;
    private float mMinLineWidth = 1;
    private float mMaxLineWidth = 5;
    private int mLineBaseColor = Color.WHITE;

    private int mFlakeCount = 100;

    private static final int TYPE_RAIN = 0;
    private static final int TYPE_SNOW = 1;

    private @WeatherType int mWeatherType = TYPE_RAIN;


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
            for (int i = 0; i < mFlakeCount; i++) {
                BaseFlake flake = new RainFlake(mMinRainLength, mMaxRainLength, mMinLineColorAlpha, mMaxLineColorAlpha, mMinLineWidth, mMaxLineWidth, mMinSpeed, mMaxSpeed, mMinY, mMaxY, mAngle, mLineBaseColor);
                flake = new SnowFlake(mSnowBitmap.getBitmap(), mAngle, mBitmapMinAlpha, mBitmapMaxAlpha, mMinSpeed, mMaxSpeed, mMinY, mMaxY, mBitmapMinScale, mBitmapMaxScale);
//                BaseFlake flake = new SnowFlake();
                flake.initData(getContext(), mWidth, mHeight);
                flake.initPaint(mPaint);
                mFlakes.add(flake);
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
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlakeView);

        mSnowBitmap = (BitmapDrawable) typedArray.getDrawable(R.styleable.FlakeView_snow_bitmap);
        mBitmapMinAlpha = typedArray.getInt(R.styleable.FlakeView_snow_min_alpha, 20);
        mBitmapMaxAlpha = typedArray.getInt(R.styleable.FlakeView_snow_max_alpha, 220);
        mBitmapMinScale = typedArray.getFloat(R.styleable.FlakeView_snow_min_scale, 0.4f);
        mBitmapMaxScale = typedArray.getFloat(R.styleable.FlakeView_snow_max_scale, 1.0f);

        mMinSpeed = typedArray.getDimension(R.styleable.FlakeView_flake_min_speed, 2);
        mMaxSpeed = typedArray.getDimension(R.styleable.FlakeView_flake_max_speed, 8);
        mMinY = typedArray.getDimension(R.styleable.FlakeView_flake_min_speed, 0);
        mMaxY = typedArray.getDimension(R.styleable.FlakeView_flake_max_speed, 50);

        mAngle = typedArray.getInt(R.styleable.FlakeView_flake_angle, 90);
        mFlakeCount = typedArray.getInt(R.styleable.FlakeView_flake_count, 100);
        mWeatherType = typedArray.getInt(R.styleable.FlakeView_weather_type, TYPE_RAIN);

        mMinRainLength = typedArray.getDimension(R.styleable.FlakeView_rain_min_length, 10);
        mMaxRainLength = typedArray.getDimension(R.styleable.FlakeView_rain_max_length, 20);
        mMinLineWidth = typedArray.getDimension(R.styleable.FlakeView_rain_min_width, 1);
        mMaxLineWidth = typedArray.getDimension(R.styleable.FlakeView_rain_max_width, 5);

        mMinLineColorAlpha = typedArray.getInt(R.styleable.FlakeView_rain_min_line_color_alpha, 0);
        mMaxLineColorAlpha = typedArray.getInt(R.styleable.FlakeView_rain_max_line_color_alpha, 120);

        typedArray.recycle();
    }

    private List<BaseFlake> mFlakes = new ArrayList<>();

    private void initData(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

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

    @IntDef({TYPE_RAIN, TYPE_SNOW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface WeatherType {

    }
}
