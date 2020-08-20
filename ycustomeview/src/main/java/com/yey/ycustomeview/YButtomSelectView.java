package com.yey.ycustomeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.yey.ycustomeview.Yinterface.IYInputView;
import com.yey.ycustomeview.Yinterface.OnDebouncingClickListener;
import com.yey.ycustomeview.util.KeyboardUtils;

public class YButtomSelectView extends FrameLayout implements IYInputView {
    private static final String TAG1 = "YButtomSelectView";
    private static String mContentStr;
    private int mContentColor;
    private int mContentChangeColor;
    private String mErrStr;
    private int mErrColor;
    // 失去焦点时候提示颜色
    private int mLoseFocusColor;
    // 获取焦点时候提示颜色
    private int mGetFocusColor;
    private TextView mTvContent;
    private TextView mTvHint;
    private TextView mTvErr;
    private View mLineView;
    private ImageView mIvImage;
    private int mImageResourceId;
    // 点击回调
    private YBSVListener mYBSVListener;
    // 错误状态记录
    private boolean hasErrStatus;
    private boolean etHasFocus;
    private int mSizeImage;
    // 记录第几次获取焦点
    private int mCountFocus;
    // 标记是否是点击事件获取到焦点
    private boolean isClick;

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mYBSVListener != null) {
                // 点击回调
                mYBSVListener.onClick(YButtomSelectView.this);
            }
            // 是点击事件导致获取焦点
            isClick = true;
            YButtomSelectView.this.requestFocus();
            isClick = false;
        }
    };

    public YButtomSelectView(@NonNull Context context) {
        this(context, null);
    }

    public YButtomSelectView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YButtomSelectView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initXmlParams(context, attrs, defStyleAttr);
        initView(context);
        initListener();
    }

    private void initXmlParams(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.YButtomSelectView, defStyleAttr, 0);
        mContentStr = typedArray.getString(R.styleable.YButtomSelectView_y_content_desc);
        mContentColor = typedArray.getColor(R.styleable.YButtomSelectView_y_et_content_color, Color.BLACK);
        mContentChangeColor = typedArray.getColor(R.styleable.YButtomSelectView_y_content_change_color, Color.BLACK);
        mErrStr = typedArray.getString(R.styleable.YButtomSelectView_y_err_desc);
        mErrColor = typedArray.getColor(R.styleable.YButtomSelectView_y_tv_err_color, Color.RED);
        mLoseFocusColor = typedArray.getColor(R.styleable.YButtomSelectView_y_lose_focus, Color.GRAY);
        mGetFocusColor = typedArray.getColor(R.styleable.YButtomSelectView_y_get_focus, Color.BLUE);
        mImageResourceId = typedArray.getResourceId(R.styleable.YButtomSelectView_y_image_id, 0);
        mSizeImage = typedArray.getDimensionPixelSize(R.styleable.YButtomSelectView_image_size, 0);
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
        this.setClickable(true);
        LayoutInflater.from(context).inflate(R.layout.layout_y_buttom_select_view, this, true);
        mTvContent = (TextView) findViewById(R.id.tv_y_content);
        mTvContent.setText(mContentStr);
        mTvContent.setTextColor(mContentColor);

        mTvHint = (TextView) findViewById(R.id.tv_y_hint);
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

        mIvImage = (ImageView) findViewById(R.id.iv_image);
        if (mImageResourceId != 0) {
            mIvImage.setImageResource(mImageResourceId);
        }
        if (mSizeImage != 0) {
            ViewGroup.LayoutParams layoutParams = mIvImage.getLayoutParams();
            layoutParams.width = mSizeImage;
            mIvImage.setLayoutParams(layoutParams);
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
                    if (mYBSVListener != null && !isClick) {
                        // 焦点获取
                        mYBSVListener.getFocuse(mCountFocus, YButtomSelectView.this);
                        mCountFocus++;

                    }
                    mTvHint.setTextColor(mGetFocusColor);
                    mLineView.setBackgroundColor(mGetFocusColor);
                    KeyboardUtils.hideSoftInput(YButtomSelectView.this);
                } else {
                    mTvHint.setTextColor(mLoseFocusColor);
                    mLineView.setBackgroundColor(mLoseFocusColor);
                }
                if (hasErrStatus) {
                    // 如果处于错误状态
                    // 1. TextView提示控件字体颜色显示错误色
                    // 2. 分割线背景色显示错误色
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
    public void setYBSVListener(YBSVListener ybsvListener) {
        mYBSVListener = ybsvListener;
    }

    public interface YBSVListener {
        /**
         * 控件被点击
         *
         * @param yButtomSelectView
         */
        void onClick(YButtomSelectView yButtomSelectView);

        /**
         * 控件第几次获取到焦点
         *
         * @param count
         * @param yButtomSelectView
         */
        void getFocuse(int count, YButtomSelectView yButtomSelectView);
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
        return IYInputView.TYPE_YBSV;
    }

    /**
     * 更改控件内容
     */
    public void changeContent(String content) {
        mTvContent.setText(content);
    }

    /**
     * 清空控件内容
     */
    public void clearContent() {
        mTvContent.setText(mContentStr);
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
        // Tv hint
        mTvHint.setTextColor(mErrColor);
    }

    /**
     * 清除错误信息
     */
    public void clearErr() {
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
     * 获取图标控件
     */
    public ImageView getIcon() {
        return mIvImage;
    }

    /**
     * 获取内容控件
     *
     * @return
     */
    public TextView getTvContent() {
        return mTvContent;
    }

    // SET 方法
    @BindingAdapter("y_change_content")
    public static void setContent(YButtomSelectView ybsv, String content) {
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
    public static String getContent(YButtomSelectView ybsv) {
        return ybsv.mTvContent.getText().toString().trim();
    }

    // 监听,如果有变动就调用listener中的onChange方法
    @BindingAdapter(value = "contentAttrChanged", requireAll = false)
    public static void setContentChangeListener(YButtomSelectView ybsv, InverseBindingListener listener) {
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
}
