package cn.asrobot.form.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import cn.asrobot.form.R;
import cn.asrobot.form.model.OptionItem;
import cn.asrobot.form.utils.ViewUtils;

public class FormSinglePickerView extends FrameLayout {

    private EditText editText;
    private boolean isRequired = false;
    private boolean isReadonly = false;
    private List<OptionItem<?>> options = new ArrayList<>();
    private int selectedIndex = -1;

    public FormSinglePickerView(Context context) {
        this(context, null);
    }

    public FormSinglePickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_form_single_picker, this, true);
        editText = findViewById(R.id.edit_picker);

        if (attrs != null) {
            try (TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FormSinglePickerView)) {
                String hint = a.getString(R.styleable.FormSinglePickerView_hint);
                isRequired = a.getBoolean(R.styleable.FormSinglePickerView_required, false);
                isReadonly = a.getBoolean(R.styleable.FormSinglePickerView_readonly, false);
                int borderColor = a.getColor(R.styleable.FormSinglePickerView_borderColor, 0xFFDDDDDD);
                float borderWidth = a.getDimension(R.styleable.FormSinglePickerView_borderWidth, 1f);
                float cornerRadius = a.getDimension(R.styleable.FormSinglePickerView_cornerRadius, 8f);
                int backgroundColor = a.getColor(R.styleable.FormSinglePickerView_backgroundColor, 0xFFFFFFFF);

                Drawable iconEnd = a.getDrawable(R.styleable.FormSinglePickerView_iconEnd);
                if (iconEnd == null) {
                    iconEnd = getResources().getDrawable(R.drawable.ic_arrow_down);
                }
                int iconWidth = (int) a.getDimension(R.styleable.FormSinglePickerView_iconWidth, ViewUtils.dpToPx(context, 20));
                int iconHeight = (int) a.getDimension(R.styleable.FormSinglePickerView_iconHeight, ViewUtils.dpToPx(context, 20));
                int iconPadding = (int) a.getDimension(R.styleable.FormSinglePickerView_iconPadding, ViewUtils.dpToPx(context, 6));

                if (iconEnd != null) {
                    iconEnd.setBounds(0, 0, iconWidth, iconHeight);
                    editText.setCompoundDrawables(null, null, iconEnd, null);
                    editText.setCompoundDrawablePadding(iconPadding);
                }

                editText.setHint(hint);
                editText.setFocusable(false);

                GradientDrawable bg = new GradientDrawable();
                bg.setColor(backgroundColor);
                bg.setCornerRadius(cornerRadius);
                bg.setStroke((int) borderWidth, borderColor);
                editText.setBackground(bg);

                if (!isReadonly) {
                    editText.setOnClickListener(v -> showPickerDialog());
                }
            }
        }
    }

    private void showPickerDialog() {
        if (options.isEmpty()) return;

        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_bottom_single_picker, null);

        WheelView picker = view.findViewById(R.id.wheel_picker);
        TextView btnConfirm = view.findViewById(R.id.btn_confirm);
        TextView btnCancel = view.findViewById(R.id.btn_cancel);

        List<String> labels = new ArrayList<>();
        for (OptionItem<?> item : options) {
            labels.add(item.getLabel());
        }

        picker.setItems(labels);
        picker.setSelectedItem(Math.max(selectedIndex, 0));

        btnConfirm.setOnClickListener(v -> {
            selectedIndex = picker.getSelectedItemIndex();
            editText.setText(options.get(selectedIndex).getLabel());
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.setContentView(view);
        dialog.show();
    }

    // 设置选项（泛型）
    public void setOptions(List<OptionItem<?>> options) {
        this.options = options;
    }

    // 获取选中值（泛型 Object）
    @Nullable
    public Object getValue() {
        if (selectedIndex >= 0 && selectedIndex < options.size()) {
            return options.get(selectedIndex).getValue();
        }
        return null;
    }

    // 获取 label（用于外部显示等）
    @Nullable
    public String getLabel() {
        if (selectedIndex >= 0 && selectedIndex < options.size()) {
            return options.get(selectedIndex).getLabel();
        }
        return null;
    }

    // 设置选中值
    public void setValue(Object value) {
        for (int i = 0; i < options.size(); i++) {
            Object itemValue = options.get(i).getValue();
            if (itemValue != null && itemValue.equals(value)) {
                selectedIndex = i;
                editText.setText(options.get(i).getLabel());
                return;
            }
        }
    }

    public boolean validate() {
        if (isRequired && TextUtils.isEmpty(editText.getText().toString())) {
            editText.setError("此项为必选");
            return false;
        }
        return true;
    }

    public void setReadonly(boolean readonly) {
        isReadonly = readonly;
        editText.setFocusable(false);
        editText.setClickable(!readonly);
        if (!readonly) {
            editText.setOnClickListener(v -> showPickerDialog());
        } else {
            editText.setOnClickListener(null);
        }
    }
}
