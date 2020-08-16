package com.yey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.yey.databinding.ActivityYEditTextViewBinding;
import com.yey.vm.YButtomViewVM;
import com.yey.vm.YEditTextViewVM;

public class YEditTextViewActivity extends AppCompatActivity {
    ActivityYEditTextViewBinding binding;
    YEditTextViewVM mVM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = (ActivityYEditTextViewBinding) DataBindingUtil.setContentView(this, R.layout.activity_y_edit_text_view);
        setContentView(binding.getRoot());

        // MutableLiveData 双向绑定
        mVM = new ViewModelProvider(this).get(YEditTextViewVM.class);
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
                mVM.mNotifyFocus.set(false);
            }
        });
        binding.btnGetFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 失去焦点
                mVM.mNotifyFocus.set(true);
            }
        });

        binding.btnPrintData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(YEditTextViewActivity.this, mVM.mContentMLD1.get() + " " + mVM.mContentMLD2.get(), Toast.LENGTH_LONG).show();
            }
        });

    }
}