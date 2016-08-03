package com.bd.view;

import android.content.Context;

/**
 * Description : <Content><br>
 * CreateTime : 2016/8/1 17:41
 *
 * @author KevinLiu
 * @version <v1.0>
 * @Editor : KevinLiu
 * @ModifyTime : 2016/8/1 17:41
 * @ModifyDescription : <Content>
 */
public class Utils {
    public static float dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Context context, float sp) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

}
