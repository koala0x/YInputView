package com.yey.ycustomeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.yey.ycustomeview.Yinterface.IYInputView;
import com.yey.ycustomeview.Yinterface.OnDebouncingClickListener;
import com.yey.ycustomeview.util.KeyboardUtils;

public class YImageSelectTextView extends FrameLayout implements IYInputView {
    // 提示
    private static String mHintStr;
    // 内容
    private static String mContentStr;
    private int mContentColor;
    private int mContentChangeColor;
    private int mSizeImage;
    private String mErrStr;
    private int mErrColor;
    // 失去焦点时候提示颜色
    private int mLoseFocusColor;
    // 获取焦点时候提示颜色
    private int mGetFocusColor;
    private TextView mTvContent;
    // 用来临时保存数据用的
    private TextView mTvHint;
    // 用来显示加载图片loading用的
    private ProgressBar mPbLoanding;
    private int mProgressLoadingColor;


    private TextView mTvErr;
    private View mLineView;
    private ImageView mIvImage;
    private int mImageResourceId;
    // 点击回调
    private IClickFocuse mIClickFocuse;
    // 错误状态记录
    private boolean hasErrStatus;
    private boolean etHasFocus;

    // 记录第几次获取焦点
    private int mCountFocus;

    // 标记是否是点击事件获取到焦点
    private boolean isClick;

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mIClickFocuse != null) {
                // 点击回调
                mIClickFocuse.onClick(YImageSelectTextView.this);
            }
            // 是点击事件导致获取焦点
            isClick = true;
            YImageSelectTextView.this.requestFocus();
            isClick = false;
        }
    };
    private TextView mTvUrl;


    public YImageSelectTextView(@NonNull Context context) {
        this(context, null);
    }

    public YImageSelectTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YImageSelectTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initXmlParams(context, attrs, defStyleAttr);
        initView(context);
        initListener();
    }

    private void initXmlParams(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.YImageSelectTextView, defStyleAttr, 0);
        mContentStr = typedArray.getString(R.styleable.YEditTextView_y_content_desc);
        mHintStr = typedArray.getString(R.styleable.YImageSelectTextView_y_content_desc);
        mContentColor = typedArray.getColor(R.styleable.YImageSelectTextView_y_et_content_color, Color.BLACK);
        mContentChangeColor = typedArray.getColor(R.styleable.YImageSelectTextView_y_content_change_color, Color.BLACK);
        mProgressLoadingColor = typedArray.getColor(R.styleable.YImageSelectTextView_y_progress_loading_color, Color.BLUE);
        mErrStr = typedArray.getString(R.styleable.YImageSelectTextView_y_err_desc);
        mErrColor = typedArray.getColor(R.styleable.YImageSelectTextView_y_tv_err_color, Color.RED);
        mLoseFocusColor = typedArray.getColor(R.styleable.YImageSelectTextView_y_lose_focus, Color.GRAY);
        mGetFocusColor = typedArray.getColor(R.styleable.YImageSelectTextView_y_get_focus, Color.BLUE);
        mImageResourceId = typedArray.getResourceId(R.styleable.YImageSelectTextView_y_image_id, 0);
        mSizeImage = typedArray.getDimensionPixelSize(R.styleable.YImageSelectTextView_image_size, 0);
        typedArray.recycle();
    }


    /**
     * 设置资源
     *
     * @param context
     */
    private void initView(Context context) {
        this.setClickable(true);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        LayoutInflater.from(context).inflate(R.layout.layout_y_image_select_text_view, this, true);

        mTvUrl = new TextView(context);

        mTvContent = (TextView) findViewById(R.id.tv_yistv_content);
        mTvContent.setText(mContentStr);
        mTvContent.setTextColor(mContentColor);

        mTvHint = (TextView) findViewById(R.id.tv_yistv_hint);
        mTvHint.setText(mContentStr);
        mTvHint.setTextColor(mLoseFocusColor);


        // 初始内容和当前内容不相等, 那么就将提示展示出来
        if (mTvContent.getText().toString().trim().equalsIgnoreCase(mContentStr)) {
            mTvHint.setVisibility(INVISIBLE);
            mTvContent.setTextColor(mContentColor);
        } else {
            mTvHint.setVisibility(VISIBLE);
            mTvContent.setTextColor(mContentChangeColor);
        }


        mTvErr = (TextView) findViewById(R.id.tv_y_err);
        mTvErr.setText(mErrStr);
        mTvErr.setTextColor(mErrColor);
        mTvErr.setVisibility(View.INVISIBLE);


        mLineView = (View) findViewById(R.id.v_y_line);
        mLineView.setBackgroundColor(mLoseFocusColor);

        mPbLoanding = (ProgressBar) findViewById(R.id.pb_loading);
        // https://www.jianshu.com/p/857929af1403
        mPbLoanding.getIndeterminateDrawable().setColorFilter(mProgressLoadingColor, PorterDuff.Mode.SRC_IN);

        mIvImage = (ImageView) findViewById(R.id.iv_image);
        if (mImageResourceId != 0) {
            mIvImage.setImageResource(mImageResourceId);
        }
        if (mSizeImage != 0) {
            ViewGroup.LayoutParams layoutParams = mIvImage.getLayoutParams();
            layoutParams.width = mSizeImage;
            layoutParams.height = mSizeImage;
            mIvImage.setLayoutParams(layoutParams);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
        int hMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY);
        //使子View显示
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).measure(wMeasureSpec, hMeasureSpec);
        }
    }

    private void initListener() {
        this.getChildAt(0).setOnClickListener(new OnDebouncingClickListener(false, 500) {
            @Override
            public void onDebouncingClick(View v) {
                mOnClickListener.onClick(v);
            }
        });
        this.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etHasFocus = hasFocus;
                if (hasFocus) {
                    if (mIClickFocuse != null && !isClick) {
                        // 焦点获取
                        mIClickFocuse.getFocuse(mCountFocus, YImageSelectTextView.this);
                        mCountFocus++;
                    }
                    mTvHint.setTextColor(mGetFocusColor);
                    mLineView.setBackgroundColor(mGetFocusColor);
                    KeyboardUtils.hideSoftInput(YImageSelectTextView.this);
                } else {
                    mTvHint.setTextColor(mLoseFocusColor);
                    mLineView.setBackgroundColor(mLoseFocusColor);
                }
                if (hasErrStatus) {
                    // 如果处于错误状态
                    mTvHint.setTextColor(mErrColor);
                    mLineView.setBackgroundColor(mErrColor);
                }
            }
        });
        mTvContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mContentStr.equals(s.toString())) {
                    mTvHint.setVisibility(INVISIBLE);
                    mTvContent.setTextColor(mContentColor);
                } else {
                    mTvHint.setVisibility(VISIBLE);
                    mTvContent.setTextColor(mContentChangeColor);
                }
            }
        });

    }

    // 为YButtomView设置回调监听
    public void setClickFocuseListener(IClickFocuse iClickFocuse) {
        this.mIClickFocuse = iClickFocuse;
    }

    public interface IClickFocuse {
        /**
         * 控件被点击
         *
         * @param imageSelectTextView
         */
        void onClick(YImageSelectTextView imageSelectTextView);

        /**
         * 控件第几次获取到焦点
         *
         * @param count
         * @param yImageSelectTextView
         */
        void getFocuse(int count, YImageSelectTextView yImageSelectTextView);
    }


    /**
     * 显示错误信息
     */
    @Override
    public void setErr() {
        if (!TextUtils.isEmpty(mErrStr)) {
            hasErrStatus = true;
            mTvErr.setText(mErrStr);
            setErrStatus();
        }
    }

    /**
     * 显示错误信息
     */
    @Override
    public void setErr(String err) {
        hasErrStatus = true;
        mTvErr.setText(err);
        setErrStatus();
    }

    private IYInputView mNextIYInputView;

    /**
     * 设置下一个控件
     *
     * @param iyInputView
     */
    @Override
    public void nextYInputView(IYInputView iyInputView) {
        mNextIYInputView = iyInputView;
    }

    /**
     * 获取下一个控件
     *
     * @return
     */
    @Override
    public IYInputView getNextYInputView() {
        return mNextIYInputView;
    }

    @Override
    public int getType() {
        return IYInputView.TYPE_YBV;
    }


    /**
     * 设置控件为错误状态
     */
    private void setErrStatus() {
        // Tv Err
        mTvErr.setTextColor(mErrColor);
        mTvErr.setVisibility(View.VISIBLE);
        // line
        mLineView.setBackgroundColor(mErrColor);
        // hint
        mTvHint.setTextColor(mErrColor);
    }

    /**
     * 根据状态清楚信息清除错误信息
     */
    @Override
    public void clearFocusErr() {
        hasErrStatus = false;
        mTvErr.setVisibility(View.INVISIBLE);
        if (etHasFocus) {
            mTvHint.setTextColor(mGetFocusColor);
            mLineView.setBackgroundColor(mGetFocusColor);
        } else {
            mTvHint.setTextColor(mLoseFocusColor);
            mLineView.setBackgroundColor(mLoseFocusColor);
        }
    }

    /**
     * 清除错误信息
     */
    public void clearErr() {
        hasErrStatus = false;
        this.clearFocus();
        mTvErr.setVisibility(View.INVISIBLE);
        mTvHint.setTextColor(mLoseFocusColor);
        mLineView.setBackgroundColor(mLoseFocusColor);
    }

    @Override
    public void requestFocusY() {
        this.requestFocus();
    }

    @Override
    public void clearFocusY() {
        this.clearFocus();
    }

    /**
     * 更改控件内容
     */
    @Override
    public void setContent(String content) {
        mTvContent.setText(content);
    }

    /**
     * 更改图片Url链接
     *
     * @param content
     */
    public void setUrlContent(String content) {
        mTvUrl.setText(content);
    }

    /**
     * 获取控件内容
     *
     * @return
     */
    @Override
    public String getContent() {
        return mTvContent.getText().toString().trim();
    }

    /**
     * 获取图片Url链接
     *
     * @return
     */
    public String getUrlContent() {
        return mTvUrl.getText().toString().trim();
    }

    /**
     * 清空控件内容
     */
    @Override
    public void clearContent() {
        mTvContent.setText("");
    }

    /**
     * 清除图片Url链接以及图片
     */
    public void clearImageUrlContent() {
        mTvUrl.setText("");
        if (mImageResourceId != 0) {
            getIcon().setImageResource(mImageResourceId);
        }
    }

    // SET 方法
    @BindingAdapter("y_image_url")
    public static void setImageUrl(YImageSelectTextView ybv, String content) {
        if (ybv != null) {
            String mCurrentStr = ybv.mTvUrl.getText().toString().trim();
            if (!TextUtils.isEmpty(content) && !content.equalsIgnoreCase(mCurrentStr)) {
                ybv.mTvUrl.setText(content);
            }
        }
    }

    // GET 方法
    @InverseBindingAdapter(attribute = "y_image_url", event = "ImageUrlAttrChanged")
    public static String getImageUrlLD(YImageSelectTextView ybv) {
        return ybv.mTvUrl.getText().toString().trim();
    }

    // 监听,如果有变动就调用listener中的onChange方法
    @BindingAdapter(value = "ImageUrlAttrChanged", requireAll = false)
    public static void setImageUrlListener(YImageSelectTextView ybv, InverseBindingListener listener) {
        ybv.mTvUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listener.onChange();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    // SET 方法
    @BindingAdapter("y_change_content")
    public static void setBindingContent(YImageSelectTextView ybsv, String content) {
        if (ybsv != null) {
            String mCurrentStr = ybsv.mTvContent.getText().toString().trim();
            if (!TextUtils.isEmpty(content)) {
                if (!content.equalsIgnoreCase(mCurrentStr)) {
                    ybsv.mTvContent.setText(content);
                }
            }
        }
    }

    // GET 方法
    @InverseBindingAdapter(attribute = "y_change_content", event = "contentAttrChanged")
    public static String getBindingContentLD(YImageSelectTextView ybsv) {
        return ybsv.mTvContent.getText().toString().trim();
    }

    // 监听,如果有变动就调用listener中的onChange方法
    @BindingAdapter(value = "contentAttrChanged", requireAll = false)
    public static void setContentChangeListener(YImageSelectTextView ybsv, InverseBindingListener listener) {
        ybsv.mTvContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listener.onChange();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    /**
     * 获取图标控件
     */
    public ImageView getIcon() {
        return mIvImage;
    }

    /**
     * 获取加载loading 控件
     */
    public ProgressBar getLoadingIcon() {
        return mPbLoanding;
    }

}


//        this.getViewTreeObserver()
//                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        int width = mIvImage.getWidth();
//                        if (mSizeImage != 0) {
//                            ViewGroup.LayoutParams layoutParams = mIvImage.getLayoutParams();
//                            layoutParams.width = mSizeImage;
//                            mIvImage.setLayoutParams(layoutParams);
//                        }
//                        YImageSelectTextView.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                    }
//                });