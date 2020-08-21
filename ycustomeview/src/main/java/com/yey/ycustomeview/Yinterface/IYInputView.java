package com.yey.ycustomeview.Yinterface;

public interface IYInputView {
    int TYPE_YBV = 0;
    int TYPE_YBSV = 1;
    int TYPE_YETV = 2;

    // 设置错误提示
    void setErr();

    // 设置错误提示
    void setErr(String desc);

    // 设置下一个控件
    void nextYInputView(IYInputView iyInputView);

    // 获取下一个控件
    IYInputView getNextYInputView();

    // 获取控件类型
    int getType();

    // 根据焦点来清除错误状态
    void clearFocusErr();

    // 清除错误状态
    void clearErr();

    // 请求获取焦点
    void requestFocusY();

    // 清空焦点
    void clearFocusY();

    // 更改控件内容
    void setContent(String content);

    // 获取控件内容
    String getContent();

    // 清除控件内容
    void clearContent();
}
