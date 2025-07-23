package cn.asrobot.form.components;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import cn.asrobot.form.R;

public class OptionBottomSheetDialog {

    public interface OnItemClickListener {
        void onClick(int position, String text);
    }

    private final Context context;
    private List<String> options = new ArrayList<>();
    private OnItemClickListener listener;

    public OptionBottomSheetDialog(Context context) {
        this.context = context;
    }

    public OptionBottomSheetDialog setOptions(List<String> options) {
        this.options = options;
        return this;
    }

    public OptionBottomSheetDialog setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
        return this;
    }

    public void show() {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_option_list, null);
        LinearLayout container = view.findViewById(R.id.option_container);

        for (int i = 0; i < options.size(); i++) {
            final int index = i;
            final String text = options.get(i);

            FrameLayout wrapper = new FrameLayout(context);
            wrapper.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView tv = new TextView(context);
            tv.setText(text);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(16);
            tv.setPadding(0, 32, 0, 32);
            tv.setLayoutParams(new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            // 设置只有第一项圆角背景，其余项白底即可
            if (i == 0) {
                tv.setBackgroundResource(R.drawable.bg_option_top_rounded);
            } else {
                tv.setBackgroundColor(Color.WHITE);
            }

            wrapper.addView(tv);
            container.addView(wrapper);

            tv.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClick(index, text);
                }
                dialog.dismiss();
            });

            // 添加分隔线（最后一项不加）
            if (i < options.size() - 1) {
                View divider = new View(context);
                LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 1);
                divider.setLayoutParams(dividerParams);
                divider.setBackgroundColor(Color.parseColor("#E0E0E0"));
                container.addView(divider);
            }
        }

        // 取消按钮
        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());

        dialog.setContentView(view);
        dialog.show();
    }
}

