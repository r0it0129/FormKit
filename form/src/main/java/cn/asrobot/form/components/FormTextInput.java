package cn.asrobot.form.components;

import static cn.asrobot.form.utils.ViewUtils.dpToPx;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import cn.asrobot.form.R;

public class FormTextInput extends FrameLayout {

    private EditText inputView;
    private boolean isRequired = false;

    public FormTextInput(Context context) {
        this(context, null);
    }

    public FormTextInput(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_form_text_input, this, true);
        inputView = findViewById(R.id.edit_input);

        if (attrs != null) {
            try (TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FormTextInput)) {
                // 功能属性
                String hint = a.getString(R.styleable.FormTextInput_hint);
                isRequired = a.getBoolean(R.styleable.FormTextInput_required, false);
                boolean isReadonly = a.getBoolean(R.styleable.FormTextInput_readonly, false);

                // ✅ 读取系统属性 inputType（不能用 TypedArray）
                int inputType = attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android", "inputType", InputType.TYPE_CLASS_TEXT);

                // 样式属性
                int borderColor = a.getColor(R.styleable.FormTextInput_borderColor, 0xFFDDDDDD);
                float borderWidth = a.getDimension(R.styleable.FormTextInput_borderWidth, 1f);
                float cornerRadius = a.getDimension(R.styleable.FormTextInput_cornerRadius, 8f);
                int backgroundColor = a.getColor(R.styleable.FormTextInput_backgroundColor, 0xFFFFFFFF);

                Drawable iconStart = a.getDrawable(R.styleable.FormTextInput_iconStart);
                Drawable iconEnd = a.getDrawable(R.styleable.FormTextInput_iconEnd);
                int padding = (int) a.getDimension(R.styleable.FormTextInput_iconPadding, dpToPx(context, 8)); // 默认 8dp
                int iconWidth = (int) a.getDimension(R.styleable.FormTextInput_iconWidth, dpToPx(context, 20));
                int iconHeight = (int) a.getDimension(R.styleable.FormTextInput_iconHeight, dpToPx(context, 20));

                if (iconStart != null) {
                    iconStart.setBounds(0, 0, iconWidth, iconHeight);
                }
                if (iconEnd != null) {
                    iconEnd.setBounds(0, 0, iconWidth, iconHeight);
                }

                inputView.setCompoundDrawables(iconStart, null, iconEnd, null);
                inputView.setCompoundDrawablePadding(padding);


                // 设置属性
                inputView.setHint(hint);
                inputView.setInputType(inputType);

                GradientDrawable bg = new GradientDrawable();
                bg.setColor(backgroundColor);
                bg.setCornerRadius(cornerRadius);
                bg.setStroke((int) borderWidth, borderColor);
                inputView.setBackground(bg);

                if (isReadonly) {
                    setReadonly(true);
                }
            }
        }
    }


    public String getValue() {
        return inputView.getText().toString().trim();
    }

    public void setValue(String value) {
        inputView.setText(value);
    }

    public boolean validate() {
        if (isRequired && TextUtils.isEmpty(getValue())) {
            inputView.setError("此项为必填");
            return false;
        }
        return true;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public void setReadonly(boolean readonly) {
        inputView.setFocusable(!readonly);
        inputView.setFocusableInTouchMode(!readonly);
        inputView.setClickable(!readonly);
        inputView.setLongClickable(!readonly);
        inputView.setCursorVisible(!readonly);
        if (readonly) {
            inputView.setKeyListener(null);
        }
    }

    public EditText getEditText() {
        return inputView;
    }
}
