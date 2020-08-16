package com.yey.vm;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

// https://stackoverflow.com/a/48194074/7986616
public class YEditTextViewVM extends ViewModel {
    public ObservableField<String> mContentMLD1;
    public ObservableField<String> mContentMLD2;
    public ObservableField<Boolean> mErrStatus;
    public ObservableField<Boolean> mNotifyFocus;

    public YEditTextViewVM() {
        mContentMLD1 = new ObservableField<>();
        mContentMLD2 = new ObservableField<>();
        mErrStatus = new ObservableField<>(false);
        mNotifyFocus = new ObservableField<>(false);

    }
}
