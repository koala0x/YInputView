## YInputView
> 最近接手的项目输入控件需求较特殊,自己写了两个组合控件,比较有特色一点是用`BindingAdapter`,实现了双向绑定.想了解双向绑定可以看代码中的实现或者这篇[文章](https://blog.csdn.net/MoLiao2046/article/details/107977255)
#### 属性介绍
- 公共属性

|属性和API|解释|
|--------|---|
|y_content_desc|控件的文字内容|
|y_et_content_color|控件的文字内容颜色|
|y_err_desc|控件错误提示文字内容|
|y_tv_err_color|控件错误提示文字内容颜色|
|y_lose_focus|控件失去焦点的颜色|
|y_get_focus|控件获取焦点的颜色|
|setErr()|为控件设置错误信息|
|setErr(String err)|为控件设置错误信息|
|nextYInputView(IYInputView iyInputView)|设置下一个IYInputView控件的地址,这样可以将所有控件组合成一个单链表|
|getNextYInputView()|获取下一个IYInputView控件的地址|
|getType()|获取该控件的类型|
|clearFocusErr()|根据控件状态清除错误信息|
|clearErr()|清除错误信息并清除焦点|
|requestFocusY()|控件请求获取焦点方法|
|clearFocusY()|控件清除焦点方法|

- `YEditTextView`控件特有属性和API

|属性和API|解释|
|--------|-----|
|y_hint_desc|控件正常提示文字内容|
|y_focus_hint_desc|控件获取焦点后提示的文字内容|
|maxLength|控件能够输入的最大内容长度|
|inputType|控件输入内容的类型,如:NUMBER,TEXT,PHONE|
|y_change_content|实现双向绑定数据,动态更新和获取控件数据|
|getEditText()|获取控件中的EditTextView控件|
|getContentHead()|获取EditTextView左侧的TextView,一般做提示用|
|setTextChangedListener()|为EditTextView设置TextWatcher文本改变监听|
|setFocusHintContent()|设置控件获取焦点时候的提示内容|
|setKeyboardNextListener()|监听该控件弹出键盘的下一步按键操作|
|setIDNumber()|设置该控件为证件号输入控件|
|isIDNumber()|判断控件是否是证件号控件|
|getFocusStatus()|得到当前控件是否获取焦点|
|setContent(String content)|为EditTextView设置内容|
|getContent()|获取EditTextView的内容|
|clearContent()|清除EditTextView的内容|

- `YButtomView`控件特有属性和API

|属性和API|解释|
|--------|-----|
|y_image_id|手动设置控件图片资源id|
|image_size|控件图片大小|
|y_progress_loading_color|控件加载图片时候展示出来的Progressbar的颜色|
|y_image_url|实现双向绑定数据,动态更新图片链接|
|setContent(String content)|存储新的图片链接到控件中记录|
|getContent()|获取控件中的图片链接|
|clearContent()|清除控件中的图片链接|
|setYBVListener()|为控件设置点击和获取焦点的监听|
|YBVListener.onClick()|控件被点击时候的回调|
|YBVListener.getFocuse()|控件获取焦点时候的回调|
|getIcon()|获取控件中ImageView图片ID|
|getLoadingIcon()|获取ProgressBar加载动画,当Glide加载图片的时候就可以将ProgressBar显示出来|

- `YButtomSelectView`控件特有属性和API

|属性和API|解释|
|--------|-----|
|y_image_id|手动设置控件图片资源id|
|image_size|控件图片大小|
|y_change_content|实现双向绑定数据,动态更新和获取控件数据|
|y_content_change_color|内容改变之后控件的字体颜色|
|setYBSVListener()|为控件设置点击和获取焦点的监听|
|YBSVListener.onClick()|控件被点击时候的回调|
|YBSVListener.getFocuse()|控件获取焦点时候的回调|
|setContent(String content)|更改控件中的文字内容|
|getContent()|获取控件中的文字内容|
|clearContent()|清除控件中的文字内容|
|getIcon()|获取控件中ImageView图片ID|
|getTvContent()|获取控件中TextView内容控件ID|
#### 依赖
```groovy
implementation 'com.yey:ycustomeview:1.4.0'
```