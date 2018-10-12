package com.houtrry.weatherviewlibrary;

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

    private int lineWidth;
    private int lineLength;
    private int lineColor;
    private float angle;

    private float currentX;
    private float currentY;

    private float initialX;
    private float initialY;

    private float speed;

    private float maxHeight;
    private float maxWidth;

    private double sinAngle;
    private double cosAngle;

    private float minX;
    private float maxX;

    public RainFlake() {
    }

    @Override
    public void initData(int width, int height) {
        Random random = new Random();
        lineLength = 10 + random.nextInt(20);
        angle = 90;
        double radian = angle * Math.PI / 180;
        lineColor = Color.argb(0 + random.nextInt(120), 255, 255, 255);
        lineWidth = 1 + random.nextInt(4);
        maxWidth = width;
        maxHeight = height;
        speed = random.nextInt(2) + 2;
        double beyond = 0;


        if (angle <= 90) {
            beyond = height / Math.tan(radian);
            minX = (float) -beyond;
            maxX = width;
        } else {
            beyond = height / Math.tan((180 - angle) * Math.PI / 180);
            minX = 0;
            maxX = (float) (width + beyond);
        }
        initialX = currentX = (minX + random.nextFloat() * (maxX - minX));
        initialY = currentY = random.nextInt(50);
        sinAngle = Math.sin(radian);
        cosAngle = Math.cos(radian);
    }

    @Override

    public void initPaint(@NonNull Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull Paint paint, int count) {
        paint.setStrokeWidth(lineWidth);
        paint.setColor(lineColor);

        calculate();
        canvas.drawLine(startX, startY, stopX, stopY, paint);

        Log.d(TAG, "===>>>" + hashCode() + ", startX: " + startX + ", startY: " + startY
                + ", stopX: " + stopX + ", stopY: " + stopY + ", initialX: " + initialX + ", initialY: " + initialY
                + ", cos: " + cosAngle + ", sin: " + sinAngle);
    }


    private float startX;
    private float startY;
    private float stopX;
    private float stopY;

    private void calculate() {
        if (currentY > maxHeight) {
            //超出边界, 从初始点开始重新计算


            Log.d(TAG, "===>>>init, " + hashCode() + ", startX: " + startX + ", startY: " + startY
                    + ", stopX: " + stopX + ", stopY: " + stopY + ", initialX: " + initialX + ", initialY: " + initialY
                    + ", cos: " + cosAngle + ", sin: " + sinAngle);

            currentX = initialX;
            currentY = initialY;
        }

        startX = (float) (currentX + speed * cosAngle);
        startY = (float) (currentY + speed * sinAngle);
        stopX = (float) (startX + lineLength * cosAngle);
        stopY = (float) (startY + lineLength * sinAngle);

        currentX = stopX;
        currentY = stopY;
    }
}
