package com.example.naren.cardviewproject;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by naren on 1/25/18.
 */

public class CommonUtils {
    public static void doAnimation(Context context, View v, int animId, final AnimationProgressCallback callback) {
        Animation animation = AnimationUtils.loadAnimation(context, animId);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                callback.onAnimationOver();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(animation);
    }
}
