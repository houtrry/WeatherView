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
 * date: 2018/10/11 9:51
 * @version: $Rev$
 * description:雨滴實現類。
 */

public class RainFlake extends BaseFlake {

    private static final String TAG = RainFlake.class.getSimpleName();

    private float mLineWidth;
    private float mLineLength;
    private int mLineColor = Color.WHITE;

    private float mMaxHeight;

    private LineVelocityModel mLineVelocityModel;

    private float mMinRainLength = 10;
    private float mMaxRainLength = 20;
    private int mMinLineColorAlpha = 0;
    private int mMaxLineColorAlpha = 120;
    private float mMinLineWidth = 1;
    private float mMaxLineWidth = 5;
    private float mMinSpeed = 2;
    private float mMaxSpeed = 8;
    private float mMinY = 0;
    private float mMaxY = 50;

    public RainFlake() {
    }

    public RainFlake(float minRainLength, float maxRainLength, int minLineColorAlpha, int maxLineColorAlpha, float minLineWidth, float maxLineWidth, float minSpeed, float maxSpeed, float minY, float maxY, int angle, int lineColor) {
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
        mAngle = angle;
        mLineColor = lineColor;
    }

    @Override
    public void initData(@NonNull Context context, int width, int height) {
        Random random = new Random();
        mLineLength = mMinRainLength + random.nextFloat() * (mMaxRainLength - mMinRainLength);
        mAngle = 90;

        mLineVelocityModel = new LineVelocityModel(mAngle);
        mLineColor = Color.argb(mMinLineColorAlpha + random.nextInt(mMaxLineColorAlpha - mMinLineColorAlpha),
                Color.red(mLineColor), Color.green(mLineColor), Color.blue(mLineColor));
        mLineWidth = mMinLineWidth + random.nextFloat() * (mMaxLineWidth - mMinLineWidth);
        mMaxHeight = height;
        mSpeed = mMinSpeed + random.nextFloat() * (mMaxSpeed - mMinSpeed);

        Pair<Double, Double> minAndMax = mLineVelocityModel.calculateMinAndMax(width, height);
        mInitialX = mCurrentX = (float) (minAndMax.first + random.nextFloat() * (minAndMax.second - minAndMax.first));
        mInitialY = mCurrentY = mMinY + random.nextFloat() * mMaxY;
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
