package com.houtrry.weatherviewlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author: houtrry
 * @time: 2018/8/20
 * @desc: ${TODO}
 */
public class RainView extends View {

    public RainView(Context context) {
        this(context, null);
    }

    public RainView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RainView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

    }
}
