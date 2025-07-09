package cn.asrobot.form.model;

import androidx.annotation.NonNull;

public class OptionItem<T> {
    private final String label;
    private final T value;

    public OptionItem(String label, T value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public T getValue() {
        return value;
    }

    @NonNull
    @Override
    public String toString() {
        return label;
    }
}
