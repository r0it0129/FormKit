package cn.asrobot.demo;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.List;

import cn.asrobot.form.components.FormSinglePickerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FormSinglePickerView pickerGender = findViewById(R.id.picker_gender);

        List<String> genderOptions = Arrays.asList("男", "女", "其他");
        pickerGender.setOptions(genderOptions);

        pickerGender.setValue("女");

        String selected = pickerGender.getValue();

        boolean isValid = pickerGender.validate(); // 如果未选中会显示错误提示

    }
}