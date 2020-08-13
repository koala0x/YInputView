package com.yey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.yey.databinding.ActivityMainBinding;
import com.yey.ycustomeview.YButtomView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity.class";
    ActivityMainBinding mainBinding;
    private MyVM myVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = (ActivityMainBinding) DataBindingUtil.setContentView(this, R.layout.activity_main);
        setContentView(mainBinding.getRoot());

        mainBinding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainBinding.cetv1.setErr();
                mainBinding.cetv2.setErr();
                mainBinding.ybtn1.setErr();
                mainBinding.ybtn2.setErr();
            }
        });

        mainBinding.btnLose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainBinding.cetv1.clearErr();
                mainBinding.cetv2.clearErr();
                mainBinding.ybtn1.clearErr();
                mainBinding.ybtn2.clearErr();
            }
        });

        mainBinding.ybtn1.setYClickListener(new YButtomView.YClickListener() {
            @Override
            public void onClick(boolean isFocus) {
                if (isFocus) {
                    Log.e(TAG, "ybtn1 获取焦点时候调用");
                } else {
                    Log.e(TAG, "ybtn1 点击时候调用");
                }
            }
        });
        mainBinding.ybtn2.setYClickListener(new YButtomView.YClickListener() {
            @Override
            public void onClick(boolean isFocus) {
                if (isFocus) {
                    Log.e(TAG, "ybtn2 获取焦点时候调用");
                } else {
                    Log.e(TAG, "ybtn2 点击时候调用");
                }
            }
        });

        mainBinding.ybtn1.setYLoadImageListener(new YButtomView.YLoadImageListener() {
            @Override
            public void loadHtpp(ImageView imageView, String url) {
                Log.e(TAG, "网络加载"+url);
            }

            @Override
            public void loadLocal(ImageView imageView, String url) {
                Log.e(TAG, "本地加载"+url);
            }
        });


        initVM();
    }


    private void initVM() {
        // MutableLiveData 双向绑定
        myVM = new ViewModelProvider(this).get(MyVM.class);
        mainBinding.setMVM(myVM);
        myVM.mContentMLD1.setValue("1");
        myVM.mContentMLD2.setValue("2");
        // 报错提示
        myVM.mErrStatus.setValue(true);
        // 加载图片
        myVM.mLoadImageUrl.setValue("https://github.com/");
        mainBinding.cetv1.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        // 设置一行
        mainBinding.cetv1.getEditText().setSingleLine();
        // 设置长度
        mainBinding.cetv1.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        // 让下一个控件获取焦点
        mainBinding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainBinding.cetv1.requestFocus();
            }
        });


        mainBinding.btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, myVM.mContentMLD1.getValue() + " " + myVM.mContentMLD2.getValue(), Toast.LENGTH_LONG).show();
            }
        });


    }
}