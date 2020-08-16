package com.yey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.yey.databinding.ActivityYButtomSelectBinding;
import com.yey.databinding.ActivityYButtomViewBinding;
import com.yey.vm.YButtomSelectViewVM;
import com.yey.vm.YEditTextViewVM;

public class YButtomSelectViewActivity extends AppCompatActivity {
    ActivityYButtomSelectBinding binding;
    YButtomSelectViewVM mVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = (ActivityYButtomSelectBinding) DataBindingUtil.setContentView(this, R.layout.activity_y_buttom_select);
        setContentView(binding.getRoot());
        // MutableLiveData 双向绑定
        mVM = new ViewModelProvider(this).get(YButtomSelectViewVM.class);
        binding.setMVM(mVM);
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
        // 打印数据
        binding.btnPrintData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(YButtomSelectViewActivity.this, mVM.mChangeContent.get(), Toast.LENGTH_LONG).show();
            }
        });
    }
}