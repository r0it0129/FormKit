package cn.asrobot.form.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import cn.asrobot.form.R;

public class FormTextAreaView extends FrameLayout {

    private EditText inputView;
    private TextView counterView;
    private boolean isRequired = false;
    private int maxLength = 100;
    private boolean showCounter = false;

    public FormTextAreaView(Context context) {
        this(context, null);
    }

    public FormTextAreaView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_form_text_area, this, true);
        inputView = findViewById(R.id.edit_text_area);
        counterView = findViewById(R.id.text_counter);

        if (attrs != null) {
            try (TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FormTextAreaView)) {
                String hint = a.getString(R.styleable.FormTextAreaView_hint);
                isRequired = a.getBoolean(R.styleable.FormTextAreaView_required, false);
                boolean isReadonly = a.getBoolean(R.styleable.FormTextAreaView_readonly, false);
                int borderColor = a.getColor(R.styleable.FormTextAreaView_borderColor, 0xFFDDDDDD);
                float borderWidth = a.getDimension(R.styleable.FormTextAreaView_borderWidth, 1f);
                float cornerRadius = a.getDimension(R.styleable.FormTextAreaView_cornerRadius, 8f);
                int backgroundColor = a.getColor(R.styleable.FormTextAreaView_backgroundColor, 0xFFFFFFFF);
                maxLength = a.getInt(R.styleable.FormTextAreaView_maxLength, 100);
                showCounter = a.getBoolean(R.styleable.FormTextAreaView_showCounter, false);

                inputView.setHint(hint);
                inputView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

                GradientDrawable bg = new GradientDrawable();
                bg.setColor(backgroundColor);
                bg.setCornerRadius(cornerRadius);
                bg.setStroke((int) borderWidth, borderColor);
                inputView.setBackground(bg);

                if (isReadonly) {
                    setReadonly(true);
                }

                if (showCounter) {
                    counterView.setVisibility(VISIBLE);
                    updateCounter(inputView.getText().toString().length(), maxLength);
                    inputView.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            updateCounter(s.length(), maxLength);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                } else {
                    counterView.setVisibility(GONE);
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateCounter(int currentLength, int maxLength) {
        counterView.setText(currentLength + " / " + maxLength);
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
