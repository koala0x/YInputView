package com.yey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yey.databinding.ActivityYEditTextViewBinding;
import com.yey.vm.YButtomViewVM;
import com.yey.vm.YEditTextViewVM;
import com.yey.ycustomeview.YEditTextView;

public class YEditTextViewActivity extends AppCompatActivity {
    private static final String TAG = "YEditTextViewActivity.class";
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
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                Log.e(TAG, mVM.mNotifyFocus.get().booleanValue() + "");
                // 失去焦点
                // mVM.mNotifyFocus.set(false);
                // binding.cetv1.getEditText().clearFocus();
                binding.cetv2.requestFocus();
            }
        });
        binding.btnGetFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取焦点
                mVM.mNotifyFocus.set(!mVM.mNotifyFocus.get().booleanValue());
            }
        });
        // 打印数据
        binding.btnPrintData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(YEditTextViewActivity.this, mVM.mContentMLD1.get() + " " + mVM.mContentMLD2.get(), Toast.LENGTH_LONG).show();
            }
        });
        // 监听nex回调
        binding.cetv1.setKeyboardNextListener(new YEditTextView.KeyboardNextListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void nextEvent() {
                Log.e(TAG, "cetv1 用户点击了下一步按钮");
            }
        });

    }
}