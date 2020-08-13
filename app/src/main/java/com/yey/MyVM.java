package com.yey;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyVM extends ViewModel {
    public MutableLiveData<String> mContentMLD1;
    public MutableLiveData<String> mContentMLD2;
    public MutableLiveData<Boolean> mErrStatus;

    public MyVM() {
         mContentMLD1 = new MutableLiveData<>();
         mContentMLD2 = new MutableLiveData<>();
         mErrStatus = new MutableLiveData<>();

    }
}
