package cn.asrobot.form.utils;

import android.content.Context;
import android.util.TypedValue;

public class ViewUtils {

    /**
     * 将 dp 转换为 px
     */
    public static int dpToPx(Context context, float dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics()
        );
    }

    /**
     * 将 sp 转换为 px（用于字体）
     */
    public static int spToPx(Context context, float sp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp,
                context.getResources().getDisplayMetrics()
        );
    }
}
