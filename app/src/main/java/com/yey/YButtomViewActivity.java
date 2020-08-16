package com.yey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
                // 使用LiveData 通知显示错误
                mVM.mErrStatus.set(true);
            }
        });

        binding.btnHideErr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用LiveData 通知隐藏错误
                mVM.mErrStatus.set(false);
            }
        });
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 失去焦点
                mVM.mNotifyClickAndFocus.set(false);
            }
        });
        binding.btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 用LiveData 通知YButtomView 点击事件
                mVM.mNotifyClickAndFocus.set(true);
            }
        });
        // 点击回调
        binding.ybtn1.setYClickListener(new YButtomView.YClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(boolean isFocus) {
                if (isFocus) {
                    Log.e(TAG1, "ybtn1 LiveData通知的回调");
                } else {
                    Log.e(TAG1, "ybtn1 点击时候调用");
                }
            }
        });
        // 点击回调
        binding.ybtn2.setYClickListener(new YButtomView.YClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(boolean isFocus) {
                if (isFocus) {
                    Log.e(TAG1, "ybtn2 LiveData通知的回调");
                } else {
                    Log.e(TAG1, "ybtn2 点击时候调用");
                }
            }
        });
        // 通知更新图片
        binding.btnLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVM.mLoadImageUrl.set("https://www.gravatar.com/avatar/4ebebbf67121594bf3fb678295ce39c2?s=32&d=identicon&r=PG");
            }
        });
    }
}