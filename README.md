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
|y_err_status|配合`MutableLiveData`+`DataBinding`实现单向绑定数据,动态展示错误信息|
- `YEditTextView`控件特有属性
|属性   |   解释   |
|-------|--------|
|y_hint_desc|控件正常提示文字内容|
|y_content|配合`MutableLiveData`+`DataBinding`实现双向绑定数据,动态更新获取控件数据|
- `YButtomView`控件特有属性
|属性   |   解释   |
|-------|--------|
|y_image_id|手动设置控件图片资源id|
|y_image_url|配合`MutableLiveData`+`DataBinding`实现单向绑定数据,动态展示不同Image资源|
#### 依赖
```groovy
implementation 'com.yey:ycustomeview:1.0.2'
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
    app:y_err_desc="错误"
    app:y_get_focus="@android:color/holo_orange_dark"
    app:y_hint_desc="提示" />
```
`app:y_content="@={mVM.mContentMLD1}"`和`app:y_err_status="@{mVM.mErrStatus}"`使用`DataBinding`进行数据绑定.
- 代码使用
```java
// ViewModel.java
public class MyVM extends ViewModel {
    public MutableLiveData<String> mContentMLD1;
    public MutableLiveData<Boolean> mErrStatus;
    public MyVM() {
        mContentMLD1 = new MutableLiveData<>();
        mErrStatus = new MutableLiveData<>();
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
    app:y_err_status="@{mVM.mErrStatus}"
    app:y_image_url="@{mVM.mLoadImageUrl}"
    app:y_image_id="@drawable/kk"/>
```
`app:y_err_status="@{mVM.mErrStatus}"`和`app:y_image_url="@{mVM.mLoadImageUrl}"`使用`DataBinding`进行数据绑定,可以动态的更新`YButtomView`的错误提示和图片.
- 代码使用
```java
// ViewModel.java
public class MyVM extends ViewModel {
    public MutableLiveData<String> mLoadImageUrl;
    public MutableLiveData<Boolean> mErrStatus;
    public MyVM() {
       mLoadImageUrl = new MutableLiveData<>();
       mErrStatus = new MutableLiveData<>();
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
    }
}
```
当`YButtomView`控件获取到焦点时候,回主动的回调`onClick(boolean isFocus)`,主动点击`YButtomView`控件也会回调该方法,通过`isFocus`参数来区分是获取焦点时候的回调还是主动点击的回调.
```java
mainBinding.ybtn1.setYClickListener(new YButtomView.YClickListener() {
    @Override
    public void onClick(boolean isFocus) {
        if (isFocus) {
            Log.e(TAG, "ybtn1 获取焦点时候调用");
        } else {
            Log.e(TAG, "ybtn1 点击时候调用");
        }
    }
});
```

