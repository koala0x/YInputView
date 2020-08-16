package com.yey.vm;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

// https://stackoverflow.com/a/48194074/7986616
public class YButtomViewVM extends ViewModel {
    public ObservableField<Boolean> mErrStatus;
    public ObservableField<String> mLoadImageUrl;
    public ObservableField<Boolean> mNotifyClickAndFocus;

    public YButtomViewVM() {
        mLoadImageUrl = new ObservableField<>();
        mErrStatus = new ObservableField<>(false);
        mNotifyClickAndFocus = new ObservableField<>();

    }
}
