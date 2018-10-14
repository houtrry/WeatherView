package com.houtrry.weatherviewlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Random;

/**
 * @author: houtrry
 * @date: 2018/10/11 9:51
 * @version: $Rev$
 * @description: ${TODO}
 */

public class RainFlake extends BaseFlake {

    private static final String TAG = RainFlake.class.getSimpleName();

    private int mLineWidth;
    private int mLineLength;
    private int mLineColor;

    private float mMaxHeight;
    private float mMaxWidth;

    private double mSinAngle;
    private double mCosAngle;

    private double mMinX;
    private double mMaxX;

    public RainFlake() {
    }

    @Override
    public void initData(@NonNull Context context, int width, int height) {
        Random random = new Random();
        mLineLength = 10 + random.nextInt(20);
        mAngle = 135;
        double radian = mAngle * Math.PI / 180;
        mLineColor = Color.argb(0 + random.nextInt(120), 255, 255, 255);
        mLineWidth = 1 + random.nextInt(4);
        mMaxWidth = width;
        mMaxHeight = height;
        mSpeed = random.nextInt(2) + 6;
        double beyond = 0;


        if (mAngle <= 90) {
            beyond = height / Math.tan(radian);
            mMinX = -beyond;
            mMaxX = width;
        } else {
            beyond = height / Math.tan((180 - mAngle) * Math.PI / 180);
            mMinX = 0;
            mMaxX = (width + beyond);
        }
        mInitialX = mCurrentX = (float) (mMinX + random.nextFloat() * (mMaxX - mMinX));
        mInitialY = mCurrentY = random.nextInt(50);
        mSinAngle = Math.sin(radian);
        mCosAngle = Math.cos(radian);
    }

    @Override

    public void initPaint(@NonNull Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull Paint paint, int count) {
        paint.setStrokeWidth(mLineWidth);
        paint.setColor(mLineColor);

        calculate();
        canvas.drawLine(startX, startY, stopX, stopY, paint);

        Log.d(TAG, "===>>>" + hashCode() + ", startX: " + startX + ", startY: " + startY
                + ", stopX: " + stopX + ", stopY: " + stopY + ", mInitialX: " + mInitialX + ", mInitialY: " + mInitialY
                + ", cos: " + mCosAngle + ", sin: " + mSinAngle);
    }


    private float startX;
    private float startY;
    private float stopX;
    private float stopY;

    private void calculate() {
        if (mCurrentY > mMaxHeight) {
            //超出边界, 从初始点开始重新计算


            Log.d(TAG, "===>>>init, " + hashCode() + ", startX: " + startX + ", startY: " + startY
                    + ", stopX: " + stopX + ", stopY: " + stopY + ", mInitialX: " + mInitialX + ", mInitialY: " + mInitialY
                    + ", cos: " + mCosAngle + ", sin: " + mSinAngle);

            mCurrentX = mInitialX;
            mCurrentY = mInitialY;
        }

        startX = (float) (mCurrentX + mSpeed * mCosAngle);
        startY = (float) (mCurrentY + mSpeed * mSinAngle);
        stopX = (float) (startX + mLineLength * mCosAngle);
        stopY = (float) (startY + mLineLength * mSinAngle);

        mCurrentX = stopX;
        mCurrentY = stopY;
    }
}
