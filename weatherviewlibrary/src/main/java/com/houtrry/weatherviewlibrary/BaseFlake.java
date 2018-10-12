package com.houtrry.weatherviewlibrary;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

/**
 * @author: houtrry
 * @date: 2018/10/11 10:48
 * @version: $Rev$
 * @description: ${TODO}
 */

public abstract class BaseFlake {
    public abstract void initData(int width, int height);
    public abstract void initPaint(@NonNull Paint paint);
    public abstract void draw(@NonNull Canvas canvas, @NonNull Paint paint, int count);
}
