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

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.asrobot.form.R;
import cn.asrobot.form.utils.ViewUtils;

public class FormDatePickerView extends FrameLayout {

    private EditText editText;
    private boolean isRequired = false;
    private boolean isReadonly = false;
    private final Calendar selectedCalendar = Calendar.getInstance();

    public FormDatePickerView(Context context) {
        this(context, null);
    }

    public FormDatePickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_form_date_picker, this, true);
        editText = findViewById(R.id.edit_date_picker);

        if (attrs != null) {
            try (TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FormDatePickerView)) {
                String hint = a.getString(R.styleable.FormDatePickerView_hint);
                isRequired = a.getBoolean(R.styleable.FormDatePickerView_required, false);
                isReadonly = a.getBoolean(R.styleable.FormDatePickerView_readonly, false);
                int borderColor = a.getColor(R.styleable.FormDatePickerView_borderColor, 0xFFDDDDDD);
                float borderWidth = a.getDimension(R.styleable.FormDatePickerView_borderWidth, 1f);
                float cornerRadius = a.getDimension(R.styleable.FormDatePickerView_cornerRadius, 8f);
                int backgroundColor = a.getColor(R.styleable.FormDatePickerView_backgroundColor, 0xFFFFFFFF);

                Drawable iconEnd = a.getDrawable(R.styleable.FormDatePickerView_iconEnd);
                if (iconEnd == null) {
                    iconEnd = getResources().getDrawable(R.drawable.ic_calendar); // 默认日历图标
                }
                int iconWidth = (int) a.getDimension(R.styleable.FormDatePickerView_iconWidth, ViewUtils.dpToPx(context, 20));
                int iconHeight = (int) a.getDimension(R.styleable.FormDatePickerView_iconHeight, ViewUtils.dpToPx(context, 20));
                int iconPadding = (int) a.getDimension(R.styleable.FormDatePickerView_iconPadding, ViewUtils.dpToPx(context, 6));

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
                    editText.setOnClickListener(v -> showBottomWheelDialog());
                } else {
                    setReadonly(true);
                }
            }
        }
    }

    private void showBottomWheelDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_bottom_wheel_date_picker, null);

        WheelView yearPicker = view.findViewById(R.id.wheel_year);
        WheelView monthPicker = view.findViewById(R.id.wheel_month);
        WheelView dayPicker = view.findViewById(R.id.wheel_day);
        TextView btnConfirm = view.findViewById(R.id.btn_confirm);
        TextView btnCancel = view.findViewById(R.id.btn_cancel);

        int startYear = 1970;
        int endYear = 2100;
        int currentYear = selectedCalendar.get(Calendar.YEAR);
        int currentMonth = selectedCalendar.get(Calendar.MONTH) + 1;
        int currentDay = selectedCalendar.get(Calendar.DAY_OF_MONTH);

        yearPicker.setItems(generateRange(startYear, endYear));
        yearPicker.setSelectedItem(currentYear - startYear);

        monthPicker.setItems(generateRange(1, 12));
        monthPicker.setSelectedItem(currentMonth - 1);

        dayPicker.setItems(generateRange(1, getMaxDay(currentYear, currentMonth)));
        dayPicker.setSelectedItem(currentDay - 1);

        yearPicker.setOnItemSelectedListener(index -> {
            int year = startYear + index;
            int month = monthPicker.getSelectedItemIndex() + 1;
            updateDayPicker(dayPicker, year, month);
        });

        monthPicker.setOnItemSelectedListener(index -> {
            int year = startYear + yearPicker.getSelectedItemIndex();
            int month = index + 1;
            updateDayPicker(dayPicker, year, month);
        });

        btnConfirm.setOnClickListener(v -> {
            int year = startYear + yearPicker.getSelectedItemIndex();
            int month = monthPicker.getSelectedItemIndex() + 1;
            int day = dayPicker.getSelectedItemIndex() + 1;
            selectedCalendar.set(year, month - 1, day);
            editText.setText(String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, day));
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.setContentView(view);
        dialog.show();
    }

    private void updateDayPicker(WheelView dayPicker, int year, int month) {
        int maxDay = getMaxDay(year, month);
        int selectedDay = Math.min(dayPicker.getSelectedItemIndex() + 1, maxDay);
        dayPicker.setItems(generateRange(1, maxDay));
        dayPicker.setSelectedItem(selectedDay - 1);
    }

    private int getMaxDay(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private List<String> generateRange(int start, int end) {
        List<String> list = new java.util.ArrayList<>();
        for (int i = start; i <= end; i++) {
            list.add(String.format(Locale.getDefault(), "%02d", i));
        }
        return list;
    }

    public String getValue() {
        return editText.getText().toString().trim();
    }

    public void setValue(String dateString) {
        editText.setText(dateString);
    }

    public boolean validate() {
        if (isRequired && TextUtils.isEmpty(getValue())) {
            editText.setError("此项为必填");
            return false;
        }
        return true;
    }

    public void setReadonly(boolean readonly) {
        isReadonly = readonly;
        editText.setFocusable(false);
        editText.setClickable(!readonly);
        if (!readonly) {
            editText.setOnClickListener(v -> showBottomWheelDialog());
        } else {
            editText.setOnClickListener(null);
        }
    }
}
