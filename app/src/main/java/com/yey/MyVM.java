package com.yey;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// https://stackoverflow.com/a/48194074/7986616
public class MyVM extends ViewModel {
    public ObservableField<String> mContentMLD1;
    public ObservableField<String> mContentMLD2;
    public ObservableField<Boolean> mErrStatus;
    public ObservableField<String> mLoadImageUrl;
    public ObservableField<Boolean> mNotifyClick;

    public MyVM() {
        mContentMLD1 = new ObservableField<>();
        mContentMLD2 = new ObservableField<>();
        mLoadImageUrl = new ObservableField<>();
        mErrStatus = new ObservableField<>(false);
        mNotifyClick = new ObservableField<>();

    }
}
