package com.yey.vm;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

// https://stackoverflow.com/a/48194074/7986616
public class YEditTextViewVM extends ViewModel {
    public ObservableField<String> mContentMLD1;
    public ObservableField<String> mContentMLD2;


    public YEditTextViewVM() {
        mContentMLD1 = new ObservableField<>();
        mContentMLD2 = new ObservableField<>();
    }
}
