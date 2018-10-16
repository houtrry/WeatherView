package com.houtrry.weatherviewlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

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

    private LineVelocityModel mLineVelocityModel;

    private int mMinRainLength = 10;
    private int mMaxRainLength = 20;
    private int mMinLineColorAlpha = 0;
    private int mMaxLineColorAlpha = 120;
    private int mMinLineWidth = 1;
    private int mMaxLineWidth = 5;
    private int mMinSpeed = 2;
    private int mMaxSpeed = 8;
    private int mMinY = 0;
    private int mMaxY = 50;

    public RainFlake() {
    }

    public RainFlake(int minRainLength, int maxRainLength, int minLineColorAlpha, int maxLineColorAlpha, int minLineWidth, int maxLineWidth, int minSpeed, int maxSpeed, int minY, int maxY) {
        mMinRainLength = minRainLength;
        mMaxRainLength = maxRainLength;
        mMinLineColorAlpha = minLineColorAlpha;
        mMaxLineColorAlpha = maxLineColorAlpha;
        mMinLineWidth = minLineWidth;
        mMaxLineWidth = maxLineWidth;
        mMinSpeed = minSpeed;
        mMaxSpeed = maxSpeed;
        mMinY = minY;
        mMaxY = maxY;
    }

    @Override
    public void initData(@NonNull Context context, int width, int height) {
        Random random = new Random();
        mLineLength = mMinRainLength + random.nextInt(mMaxRainLength - mMinRainLength);
        mAngle = 90;

        mLineVelocityModel = new LineVelocityModel(mAngle);
        mLineColor = Color.argb(mMinLineColorAlpha + random.nextInt(mMaxLineColorAlpha - mMinLineColorAlpha), Color.red(mLineColor), Color.green(mLineColor), Color.blue(mLineColor));
        mLineWidth = mMinLineWidth + random.nextInt(mMaxLineWidth - mMinLineWidth);
        mMaxHeight = height;
        mSpeed = mMinSpeed + random.nextInt(mMaxSpeed - mMinSpeed);

        Pair<Double, Double> minAndMax = mLineVelocityModel.calculateMinAndMax(width, height);
        mInitialX = mCurrentX = (float) (minAndMax.first + random.nextFloat() * (minAndMax.second - minAndMax.first));
        mInitialY = mCurrentY = mMinY + random.nextInt(mMaxY);
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
                + ", stopX: " + stopX + ", stopY: " + stopY + ", mInitialX: " + mInitialX + ", mInitialY: " + mInitialY);
    }


    private float startX;
    private float startY;
    private float stopX;
    private float stopY;

    private void calculate() {
        if (mCurrentY > mMaxHeight) {
            //超出边界, 从初始点开始重新计算

            Log.d(TAG, "===>>>init, " + hashCode() + ", startX: " + startX + ", startY: " + startY
                    + ", stopX: " + stopX + ", stopY: " + stopY + ", mInitialX: " + mInitialX + ", mInitialY: " + mInitialY);

            mCurrentX = mInitialX;
            mCurrentY = mInitialY;
        }

        Pair<Float, Float> nextStart = mLineVelocityModel.calculateNext(mCurrentX, mCurrentY, mSpeed);
        Pair<Float, Float> nextEnd = mLineVelocityModel.calculateNext(mCurrentX, mCurrentY, mSpeed + mLineLength);
        startX = nextStart.first;
        startY = nextStart.second;
        stopX = nextEnd.first;
        stopY = nextEnd.second;

        mCurrentX = stopX;
        mCurrentY = stopY;
    }
}
