## YInputView
> 最近接手的项目输入控件需求较特殊,我花时间自己写了两个组合控件,比较有特色一点是用`BindingAdapter`,实现了双向绑定.想了解双向绑定可以看代码中的实现或者这篇[文章](https://blog.csdn.net/MoLiao2046/article/details/107977255)
#### 属性介绍
- 公共属性

|属性   |   解释   |
|-------|--------|
|y_content_desc|控件的文字内容|
|y_et_content_color|控件的文字内容颜色|
|y_err_desc|控件错误提示文字内容|
|y_tv_err_color|控件错误提示文字内容颜色|
|y_lose_focus|控件失去焦点的颜色|
|y_get_focus|控件获取焦点的颜色|
|y_err_status|实现单向绑定数据,动态展示错误信息|

- `YEditTextView`控件特有属性

|属性   |   解释   |
|-------|--------|
|y_hint_desc|控件正常提示文字内容|
|y_content|实现双向绑定数据,动态更新获取控件数据|
|y_notify_focus|通过`LiveData`动态实现控件主动获取焦点和失去焦点|
|maxLength|控件能够输入的最大内容长度|
|inputType|控件输入内容的类型,如:NUMBER,TEXT,PHONE|

- `YButtomView`控件特有属性

|属性   |   解释   |
|-------|--------|
|y_image_id|手动设置控件图片资源id|
|y_image_url|实现单向绑定数据,动态展示不同Image资源|
|y_notify_click_focus|通过`LiveData`动态实现控件获取焦点失去焦点,并且获取焦点的时候会进行事件回调|
#### 依赖
```groovy
implementation 'com.yey:ycustomeview:1.0.3'
```
#### `YEditTextView`简单使用
- XML
```xml
<data>
    <variable
        name="mVM"
        type="com.yey.MyVM" />
</data>
<com.yey.ycustomeview.YEditTextView
    android:id="@+id/cetv_1"
    android:layout_width="match_parent"
    android:layout_height="60dp"
    app:y_content="@={mVM.mContentMLD1}"
    app:y_err_status="@{mVM.mErrStatus}"
    app:y_notify_focus="@{mVM.mNotifyClick}"
    app:y_err_desc="错误"
    app:y_get_focus="@android:color/holo_orange_dark"
    app:y_hint_desc="提示" />
```

1. `app:y_content="@={mVM.mContentMLD1}"`和`app:y_err_status="@{mVM.mErrStatus}"`使用`DataBinding`进行数据绑定.
2. `app:y_notify_focus="@{mVM.mNotifyClick}"`通过`ObservableField<Boolean>`动态实现获取焦点失去焦点

- 代码使用
```java
// ViewModel.java
public class MyVM extends ViewModel {
    public MutableLiveData<String> mContentMLD1;
    public MutableLiveData<Boolean> mErrStatus;
    public ObservableField<Boolean> mNotify;
    public MyVM() {
        mContentMLD1 = new MutableLiveData<>();
        mErrStatus = new MutableLiveData<>();
        mNotify = new ObservableField<>(false);
    }
}
// MainActivity.java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myVM = new ViewModelProvider(this).get(MyVM.class);
        mainBinding.setMVM(myVM);
        // 更新YEditTextView内容
        myVM.mContentMLD1.setValue("1");
        // 展示YEditTextView中的错误提示
        myVM.mErrStatus.setValue(true);
        // 获取焦点
        myVM.mNotify.set(!myVM.mNotify.get().booleanValue());
    }
}
```
手动展示错误有两个API可以使用
```java
YEditTextView.setErr()
YEditTextView.setErr(String err)
```
清除错误提示API
```java
YEditTextView.clearErr()
```
#### `YButtomView`简单使用
- XML
```xml
<data>
    <variable
        name="mVM"
        type="com.yey.MyVM" />
</data>
<com.yey.ycustomeview.YButtomView
    android:id="@+id/ybtn_1"
    android:layout_width="match_parent"
    android:layout_height="60dp"
    app:y_content_desc="内容"
    app:y_err_desc="错误提示"
    app:y_notify_focus="@{mVM.mNotifyClick}"
    app:y_err_status="@{mVM.mErrStatus}"
    app:y_image_url="@{mVM.mLoadImageUrl}"
    app:y_image_id="@drawable/kk"/>
```

1. `app:y_err_status="@{mVM.mErrStatus}"`和`app:y_image_url="@{mVM.mLoadImageUrl}"`使用`DataBinding`进行数据绑定,可以动态的更新`YButtomView`的错误提示和图片.
2. app:y_notify_focus="@{mVM.mNotifyClick}"通过`ObservableField<Boolean>`动态实现获取焦点失去焦点,获取焦点时候能发出一个回调事件.

- 代码使用
```java
// ViewModel.java
public class MyVM extends ViewModel {
    public MutableLiveData<String> mLoadImageUrl;
    public MutableLiveData<Boolean> mErrStatus;
    public ObservableField<Boolean> mNotifyClick;
    public MyVM() {
       mLoadImageUrl = new MutableLiveData<>();
       mErrStatus = new MutableLiveData<>();
       mNotifyClick = new ObservableField<>(false);
    }
}
// MainActivity.java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myVM = new ViewModelProvider(this).get(MyVM.class);
        mainBinding.setMVM(myVM);
        // 更新YEditTextView内容
        myVM.mContentMLD1.setValue("1");
        // 更新YButtomView中的目标图片
        myVM.mLoadImageUrl.setValue("https://xxx.com/image...");
        // 因为加载网络图片有很多框架,所以将加载图片的操作暴露出来,这样可以使用不同的加载图片框架了.
        mainBinding.ybtn1.setYLoadImageListener(new YButtomView.YLoadImageListener() {
            @Override
            public void loadHtpp(ImageView imageView, String url) {
                Log.e(TAG, "网络加载图片"+url);
            }
            @Override
            public void loadLocal(ImageView imageView, String url) {
                Log.e(TAG, "本地加载图片 比如Cache缓存中的图片 file//xxx.png"+url);
            }
        });
        // 通过ObservableField来让控件主动获取焦点
        myVM.mNotifyClick.set(!myVM.mNotifyClick.get().booleanValue());
    }
}
```
当`YButtomView`控件获取到焦点时候,回主动的回调`onClick(boolean isFocus)`,主动点击`YButtomView`控件也会回调该方法,通过`isFocus`参数来区分回调是点击事件还是通过`ObservableField<Boolean>`发出的.
```java
mainBinding.ybtn2.setYClickListener(new YButtomView.YClickListener() {
    @Override
    public void onClick(boolean isFocus) {
        if (isFocus) {
            Log.e(TAG, "ybtn2 LiveData通知的回调");
        } else {
            Log.e(TAG, "ybtn2 点击时候调用");
        }
    }
});
```

