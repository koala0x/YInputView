package com.yey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Toast;

import com.yey.databinding.ActivityMainBinding;
import com.yey.ycustomeview.CustomeEditTextView;

public class MainActivity extends AppCompatActivity {

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
            }
        });

        mainBinding.btnLose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainBinding.cetv1.clearErr();
                mainBinding.cetv2.clearErr();
            }
        });

        initVM();
    }


    private void initVM() {
        myVM = new ViewModelProvider(this).get(MyVM.class);
        mainBinding.setMVM(myVM);
        myVM.mContentMLD1.setValue("1");
        myVM.mContentMLD2.setValue("2");
        mainBinding.cetv1.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        // 设置一行
        mainBinding.cetv1.getEditText().setSingleLine();
        // 设置长度
        mainBinding.cetv1.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

        mainBinding.btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, myVM.mContentMLD1.getValue() + " " + myVM.mContentMLD2.getValue(), Toast.LENGTH_LONG).show();
            }
        });
    }
}