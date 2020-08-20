package com.yey.ycustomeview.Yinterface;

public interface IYInputView {
    int TYPE_YBV = 0;
    int TYPE_YBSV = 1;
    int TYPE_YETV = 2;

    void setErr();

    void setErr(String desc);

    void nextYInputView(IYInputView iyInputView);

    IYInputView getNextYInputView();

    int getType();
}
