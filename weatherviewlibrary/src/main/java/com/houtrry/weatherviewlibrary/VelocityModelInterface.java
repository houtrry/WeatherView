package com.houtrry.weatherviewlibrary;

import android.util.Pair;

/**
 * @author: houtrry
 * @time: 2018/10/15
 * @desc: ${TODO}
 */
public interface VelocityModelInterface<T> {
    void init();
    <E> Pair<E, E> calculateMinAndMax(T width, T height);

    Pair<T, T> calculateNext(T latestX, T latestY);
}
