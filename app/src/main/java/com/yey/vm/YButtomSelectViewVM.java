package com.yey.vm;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

// https://stackoverflow.com/a/48194074/7986616
public class YButtomSelectViewVM extends ViewModel {
    public ObservableField<String> mChangeContent;

    public YButtomSelectViewVM() {
        mChangeContent = new ObservableField<>();

    }
}
