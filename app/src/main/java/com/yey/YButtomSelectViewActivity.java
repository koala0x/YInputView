package com.yey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.yey.databinding.ActivityYButtomSelectBinding;
import com.yey.databinding.ActivityYButtomViewBinding;

public class YButtomSelectViewActivity extends AppCompatActivity {
    ActivityYButtomSelectBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = (ActivityYButtomSelectBinding) DataBindingUtil.setContentView(this, R.layout.activity_y_buttom_select);
        setContentView(binding.getRoot());
        binding.btnChageContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ybsv1.changeContent("改变了内容");
            }
        });
        binding.btnClearContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ybsv1.clearContent();
            }
        });
        binding.btnShowErr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ybsv1.setErr("错误");
            }
        });
        binding.btnHideErr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ybsv1.clearErr();
            }
        });
    }
}