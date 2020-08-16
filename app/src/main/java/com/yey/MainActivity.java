package com.yey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.yey.databinding.ActivityMainBinding;
import com.yey.vm.MyVM;
import com.yey.ycustomeview.YButtomView;
import com.yey.ycustomeview.YEditTextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity.class";
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = (ActivityMainBinding) DataBindingUtil.setContentView(this, R.layout.activity_main);
        setContentView(binding.getRoot());
        binding.btnYbuttomview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, YButtomViewActivity.class);
                startActivity(intent);
            }
        });
        binding.btnYedittextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, YEditTextViewActivity.class);
                startActivity(intent);
            }
        });
        binding.btnYbuttomselectview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, YButtomSelectViewActivity.class);
                startActivity(intent);
            }
        });
    }
}