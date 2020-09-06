package com.yey.vm;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

// https://stackoverflow.com/a/48194074/7986616
public class YImageSelectTextViewVM extends ViewModel {
    public ObservableField<String> mChangeContent;

    public YImageSelectTextViewVM() {
        mChangeContent = new ObservableField<>();

    }
}
