package com.yey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.yey.databinding.ActivityYImageViewBinding;
import com.yey.vm.YImageViewVM;
import com.yey.ycustomeview.YImageView;

public class YImageViewActivity extends AppCompatActivity {
    private static final String TAG1 = "YButtomViewActivity.class";
    private ActivityYImageViewBinding binding;
    private YImageViewVM mVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = (ActivityYImageViewBinding) DataBindingUtil.setContentView(this, R.layout.activity_y_image_view);
        setContentView(binding.getRoot());
        // MutableLiveData 双向绑定
        mVM = new ViewModelProvider(this).get(YImageViewVM.class);
        binding.setMVM(mVM);
        binding.btnShowErr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ybtn1.setErr();
            }
        });

        binding.btnHideErr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ybtn1.clearErr();
            }
        });
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ybtn2.requestFocus();
            }
        });
        binding.btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取焦点相当于被点击
                binding.ybtn1.requestFocus();
            }
        });
        // 点击回调
        binding.ybtn1.setClickFocuseListener(new YImageView.IClickFocuse() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(YImageView yButtomView) {
                Log.e(TAG1, "ybtn1 点击时候调用");
            }

            @SuppressLint("LongLogTag")
            @Override
            public void getFocuse(int count, YImageView yButtomView) {
                Log.e(TAG1, "ybtn1 获取焦点第" + count + "次回调");
            }
        });
        // 点击回调
        binding.ybtn2.setClickFocuseListener(new YImageView.IClickFocuse() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(YImageView yButtomView) {
                Log.e(TAG1, "ybtn2 点击时候调用");
            }

            @SuppressLint("LongLogTag")
            @Override
            public void getFocuse(int count, YImageView yButtomView) {
                Log.e(TAG1, "ybtn2 获取焦点第" + count + "次回调");
            }
        });
        binding.btnShowLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ybtn1.getLoadingIcon().setVisibility(View.VISIBLE);
            }
        });


    }
}