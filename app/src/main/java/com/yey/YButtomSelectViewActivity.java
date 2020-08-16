package com.yey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.yey.databinding.ActivityYButtomSelectBinding;
import com.yey.databinding.ActivityYButtomViewBinding;

public class YButtomSelectViewActivity extends AppCompatActivity {
    ActivityYButtomSelectBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = (ActivityYButtomSelectBinding) DataBindingUtil.setContentView(this, R.layout.activity_y_buttom_select);
        setContentView(binding.getRoot());
    }
}