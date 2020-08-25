package com.yey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yey.databinding.ActivityYButtomSelectBinding;
import com.yey.databinding.ActivityYButtomViewBinding;
import com.yey.vm.YButtomSelectViewVM;
import com.yey.vm.YEditTextViewVM;
import com.yey.ycustomeview.YButtomSelectView;

public class YButtomSelectViewActivity extends AppCompatActivity {
    ActivityYButtomSelectBinding binding;
    YButtomSelectViewVM mVM;
    private String TAG = "YButtomSelectViewActivity.class";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = (ActivityYButtomSelectBinding) DataBindingUtil.setContentView(this, R.layout.activity_y_buttom_select);
        setContentView(binding.getRoot());
        // MutableLiveData 双向绑定
        mVM = new ViewModelProvider(this).get(YButtomSelectViewVM.class);
        binding.setMVM(mVM);


        binding.ybsv1.setYBSVListener(new YButtomSelectView.YBSVListener() {

            @Override
            public void onClick(YButtomSelectView yButtomSelectView) {
                Log.e(TAG, "点击获取的回调" );
            }

            @Override
            public void getFocuse(int count, YButtomSelectView yButtomSelectView) {
                Log.e(TAG, "获取焦点时获取的回调" );
            }
        });

        binding.btnRequestFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ybsv1.requestFocus();
            }
        });

        binding.btnContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ybshav3.setContent("QWEQWERTYUIOP{ASDFGHJKLZXCVBNM<WERTYUIOPSDFGHJKLSDFGHJKLSDFGHJKLQWEQWERTYUIOP{ASDFGHJKLZXCVBNM<WERTYUIOPSDFGHJKLSDFGHJKLSDFGHJKLQWEQWERTYUIOP{ASDFGHJKLZXCVBNM<WERTYUIOPSDFGHJKLSDFGHJKLSDFGHJKL水门");
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