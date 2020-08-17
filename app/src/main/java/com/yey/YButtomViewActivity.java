package com.yey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.yey.databinding.ActivityYButtomViewBinding;
import com.yey.vm.YButtomViewVM;
import com.yey.ycustomeview.YButtomView;

public class YButtomViewActivity extends AppCompatActivity {
    private static final String TAG1 = "YButtomViewActivity.class";
    private ActivityYButtomViewBinding binding;
    private YButtomViewVM mVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = (ActivityYButtomViewBinding) DataBindingUtil.setContentView(this, R.layout.activity_y_buttom_view);
        setContentView(binding.getRoot());
        // MutableLiveData 双向绑定
        mVM = new ViewModelProvider(this).get(YButtomViewVM.class);
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
        binding.ybtn1.setYBVListener(new YButtomView.YBVListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(YButtomView yButtomView) {
                Log.e(TAG1, "ybtn1 点击时候调用");
            }

            @SuppressLint("LongLogTag")
            @Override
            public void getFocuse(int count, YButtomView yButtomView) {
                Log.e(TAG1, "ybtn1 获取焦点第" + count + "次回调");
            }
        });
        // 点击回调
        binding.ybtn2.setYBVListener(new YButtomView.YBVListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(YButtomView yButtomView) {
                Log.e(TAG1, "ybtn2 点击时候调用");
            }

            @SuppressLint("LongLogTag")
            @Override
            public void getFocuse(int count, YButtomView yButtomView) {
                Log.e(TAG1, "ybtn2 获取焦点第" + count + "次回调");
            }
        });

    }
}