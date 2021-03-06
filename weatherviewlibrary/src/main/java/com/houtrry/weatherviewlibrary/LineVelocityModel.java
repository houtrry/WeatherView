package com.houtrry.weatherviewlibrary;

import android.util.Pair;

/**
 * @author: houtrry
 * time: 2018/10/15
 * desc:直綫型下落模式。雪花或者雨滴按照直綫下落。當然，可以設置下落角度。
 */
public class LineVelocityModel implements VelocityModelInterface<Float> {

    private double angle = 90;
    private double sinAngle = 0;
    private double cosAngle = 0;

    public LineVelocityModel(double angle) {
        this.angle = angle;
        init();
    }

    private static final int MIN_ANGLE = 0;
    private static final int MAX_ANGLE = 180;

    private void init() {
        if (!(angle % 360 > MIN_ANGLE && angle % 360 < MAX_ANGLE)) {
            throw new IllegalArgumentException(String.format("angle must between %1$d ~ %2$d, and current angle is %3$d", MIN_ANGLE, MAX_ANGLE, angle));
        }
        double radian = angle * Math.PI / 180;
        sinAngle = Math.sin(radian);
        cosAngle = Math.cos(radian);
    }

    @Override
    public Pair<Double, Double> calculateMinAndMax(int width, int height) {
        double minX = 0, maxX = 0;
        double radian = angle * Math.PI / 180;
        double beyond = 0;
        if (angle <= 90) {
            beyond = height / Math.tan(radian);
            minX = -beyond;
            maxX = width;
        } else {
            beyond = height / Math.tan((180 - angle) * Math.PI / 180);
            minX = 0;
            maxX = (width + beyond);
        }
        return new Pair<>(minX, maxX);
    }

    @Override
    public Pair<Float, Float> calculateNext(Float latestX, Float latestY, Float distance) {

        return new Pair<>((float) (latestX + distance * cosAngle), (float) (latestY + distance * sinAngle));
    }
}
