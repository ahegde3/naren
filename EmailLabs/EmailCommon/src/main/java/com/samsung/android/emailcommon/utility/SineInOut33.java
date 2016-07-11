package com.samsung.android.emailcommon.utility;

import android.util.Log;
import android.view.animation.Interpolator;
import android.widget.TextView;

public class SineInOut33 implements Interpolator {

    //private static final float[][] segments = {{0f, 0.000f, 0.428f}, {0.428f, 0.716f, 1.0f }};
    private static final float[][] segments = {{0.0f, 0.050f, 0.495f}, {0.495f, 0.940f, 1.0f}};
    private TextView mtx = null;
    public SineInOut33() {
    }
    public SineInOut33( TextView txt) {
        mtx = txt;
    }

    // b = 0, c = 1, d = 1, t = input
    public float getInterpolation(float input) {
        final float _loc_5 = input / 1;
        final int _loc_6 = segments.length;
        int _loc_9 = (int)(Math.floor(_loc_6 * _loc_5));
        if (_loc_9 >= segments.length) _loc_9 = segments.length - 1;

        final float _loc_7 = (_loc_5 - _loc_9 * (1.0f / _loc_6)) * _loc_6;
        final float[] _loc_8 = segments[_loc_9];
        final float ret = 0 + 1 * (_loc_8[0] + _loc_7 * (2 * (1 - _loc_7) * (_loc_8[1] - _loc_8[0]) + _loc_7 * (_loc_8[2] - _loc_8[0])));

        Log.e("lonnie","getInterpolation "+ ret);
        if( mtx != null ){
            mtx.setText(""+ret);
        }

        return ret;
    }
}
