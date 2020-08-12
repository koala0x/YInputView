package com.yey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
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
        mainBinding.btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, myVM.mContentMLD1.getValue() + " " + myVM.mContentMLD2.getValue(), Toast.LENGTH_LONG).show();
            }
        });
    }
}