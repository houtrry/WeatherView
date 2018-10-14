package com.houtrry.weatherviewlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import java.util.Random;

/**
 * @author: houtrry
 * @time: 2018/10/14
 * @desc: ${TODO}
 */
public class SnowFlake extends BaseFlake {

    private Bitmap mSnowBitmap = null;
    private float mScale = 0;
    private int mAlpha = 0;

    @Override
    public void initData(@NonNull Context context, int width, int height) {
        mSnowBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_snow);
        mSpeed = 3;
        Random random = new Random();
        mCurrentX = mInitialX = random.nextFloat() * width;
        mCurrentY = mInitialY = random.nextFloat() * 50;
        mScale = (float) (0.4 + random.nextFloat() * 0.6);
        mAlpha = 20 + random.nextInt(200);
        mMaxY = height;
    }

    @Override
    public void initPaint(@NonNull Paint paint) {

    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull Paint paint, int count) {
        if (mCurrentY > mMaxY) {
            mCurrentY = mInitialY;
        }
        canvas.save();
        canvas.scale(mScale, mScale, mCurrentX + mSnowBitmap.getWidth() * 0.5f, mCurrentY + mSnowBitmap.getHeight() * 0.5f);
        paint.setAlpha(mAlpha);
        canvas.drawBitmap(mSnowBitmap, mCurrentX, mCurrentY, paint);
        canvas.restore();
        mCurrentY += mSpeed;
    }

}
