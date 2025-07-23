package cn.asrobot.demo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.List;

import cn.asrobot.form.components.FormSinglePickerView;
import cn.asrobot.form.components.OptionBottomSheetDialog;

public class MainActivity extends AppCompatActivity {

    private Button button;

    @SuppressLint("MissingInflatedId")
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
        button = findViewById(R.id.button_test);
        button.setOnClickListener(view -> {
            List<String> options = Arrays.asList("腾讯地图", "百度地图", "高德地图");
            new OptionBottomSheetDialog(MainActivity.this)
                    .setOptions(options)
                    .setOnItemClickListener(((position, text) -> {
                        Toast.makeText(MainActivity.this, "点击了：" + text, Toast.LENGTH_SHORT).show();
                    }))
                    .show();
        });

    }
}