package com.yey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yey.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity.class";
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = (ActivityMainBinding) DataBindingUtil.setContentView(this, R.layout.activity_main);
        setContentView(binding.getRoot());
        binding.btnIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, YImageViewActivity.class);
                startActivity(intent);
            }
        });
        binding.btnEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, YEditTextViewActivity.class);
                startActivity(intent);
            }
        });
        binding.btnSelectTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, YSelectTextViewActivity.class);
                startActivity(intent);
            }
        });
        binding.btnImageSelectTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, YImageSelectTextViewActivity.class);
                startActivity(intent);
            }
        });
    }
}