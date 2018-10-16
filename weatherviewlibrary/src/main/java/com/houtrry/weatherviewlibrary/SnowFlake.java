package com.houtrry.weatherviewlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.Random;

/**
 * @author: houtrry
 * @time: 2018/10/14
 * @desc: ${TODO}
 */
public class SnowFlake extends BaseFlake {

    private static final String TAG = SnowFlake.class.getSimpleName();

    private Bitmap mSnowBitmap = null;
    private float mScale = 0;
    private int mAlpha = 0;
    private int mAngle = 45;

    private LineVelocityModel mLineVelocityModel;
    private int mMinAlpha = 20;
    private int mMaxAlpha = 220;
    private int mMinSpeed = 2;
    private int mMaxSpeed = 8;
    private int mMinY = 0;
    private int mMaxY = 50;
    private float mMinScale = 0.4f;
    private float mMaxScale = 1.0f;

    public SnowFlake() {
    }

    public SnowFlake(Bitmap snowBitmap, int angle, int minAlpha, int maxAlpha, int minSpeed, int maxSpeed, int minY, int maxY, float minScale, float maxScale) {
        mSnowBitmap = snowBitmap;
        mAngle = angle;
        mMinAlpha = minAlpha;
        mMaxAlpha = maxAlpha;
        mMinSpeed = minSpeed;
        mMaxSpeed = maxSpeed;
        mMinY = minY;
        mMaxY = maxY;
        mMinScale = minScale;
        mMaxScale = maxScale;
    }

    @Override
    public void initData(@NonNull Context context, int width, int height) {
        mSnowBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_snow);
        Random random = new Random();
        mSpeed = random.nextInt(mMaxSpeed - mMinSpeed) + mMinSpeed;

        mLineVelocityModel = new LineVelocityModel(mAngle);

        Pair<Double, Double> minAndMax = mLineVelocityModel.calculateMinAndMax(width, height);

        mCurrentX = mInitialX = (float) (minAndMax.first + random.nextFloat() * (minAndMax.second - minAndMax.first));
        mCurrentY = mInitialY = mMinY + random.nextFloat() * (mMaxY - mMinY);
        mScale = mMinScale + random.nextFloat() * (mMaxScale - mMinScale);
        mAlpha = mMinAlpha + random.nextInt(mMaxAlpha - mMinAlpha);
        mMaxY = height;
    }

    @Override
    public void initPaint(@NonNull Paint paint) {

    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull Paint paint, int count) {
        Pair<Float, Float> next = mLineVelocityModel.calculateNext(mCurrentX, mCurrentY, mSpeed);
        mCurrentX = next.first;
        mCurrentY = next.second;
        if (mCurrentY > mMaxY) {
            mCurrentX = mInitialX;
            mCurrentY = mInitialY;
        }

        canvas.save();
        canvas.scale(mScale, mScale, mCurrentX , mCurrentY );
        paint.setAlpha(mAlpha);
        canvas.drawBitmap(mSnowBitmap, mCurrentX -  mSnowBitmap.getWidth() * 0.5f, mCurrentY - mSnowBitmap.getHeight() * 0.5f, paint);
        canvas.restore();
    }

}
