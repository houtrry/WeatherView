package com.houtrry.weatherviewlibrary;

import android.util.Pair;

/**
 * @author: houtrry
 * time: 2018/10/15
 * desc: 位置計算接口。用於計算下一幀位置。
 */
public interface VelocityModelInterface<T> {
    <E> Pair<E, E> calculateMinAndMax(int width, int height);

    Pair<T, T> calculateNext(T latestX, T latestY, T distance);
}
