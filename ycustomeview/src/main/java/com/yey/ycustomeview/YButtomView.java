package com.yey.ycustomeview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class YButtomView extends FrameLayout implements IYInputView {
    private static String mHintStr;
    private int mContentColor;
    private int mSizeImage;
    private String mErrStr;
    private int mErrColor;
    // 失去焦点时候提示颜色
    private int mLoseFocusColor;
    // 获取焦点时候提示颜色
    private int mGetFocusColor;
    private TextView mTvHint;
    // 用来临时保存数据用的
    private TextView mTvTempContent;
    // 用来显示加载图片loading用的
    private ProgressBar mPbLoanding;
    private int mProgressLoadingColor;


    private TextView mTvErr;
    private View mLineView;
    private ImageView mIvImage;
    private int mImageResourceId;
    // 点击回调
    private YBVListener mYBVListener;
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
            if (mYBVListener != null) {
                // 点击回调
                mYBVListener.onClick(YButtomView.this);
            }
            // 是点击事件导致获取焦点
            isClick = true;
            YButtomView.this.requestFocus();
            isClick = false;
        }
    };


    public YButtomView(@NonNull Context context) {
        this(context, null);
    }

    public YButtomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YButtomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initXmlParams(context, attrs, defStyleAttr);
        initView(context);
        initListener();
    }

    private void initXmlParams(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.YButtomView, defStyleAttr, 0);
        mHintStr = typedArray.getString(R.styleable.YButtomView_y_content_desc);
        mContentColor = typedArray.getColor(R.styleable.YButtomView_y_et_content_color, Color.BLACK);
        mProgressLoadingColor = typedArray.getColor(R.styleable.YButtomView_y_progress_loading_color, Color.BLUE);
        mErrStr = typedArray.getString(R.styleable.YButtomView_y_err_desc);
        mErrColor = typedArray.getColor(R.styleable.YButtomView_y_tv_err_color, Color.RED);
        mLoseFocusColor = typedArray.getColor(R.styleable.YButtomView_y_lose_focus, Color.GRAY);
        mGetFocusColor = typedArray.getColor(R.styleable.YButtomView_y_get_focus, Color.BLUE);
        mImageResourceId = typedArray.getResourceId(R.styleable.YButtomView_y_image_id, 0);
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
        LayoutInflater.from(context).inflate(R.layout.layout_y_buttom_view, this, true);
        mTvHint = (TextView) findViewById(R.id.tv_ybv_hint);
        mTvHint.setText(mHintStr);
        mTvHint.setTextColor(mContentColor);

        mTvTempContent = (TextView) findViewById(R.id.tv_temp_content);


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
            mIvImage.setLayoutParams(layoutParams);
            // mPbLoanding.setLayoutParams(layoutParams);
        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            int[][] states = new int[1][];
//            states[0] = new int[android.R.attr.state_window_focused];
//            int[] colors = new int[]{mProgressLoadingColor};
//            mPbLoanding.setIndeterminateTintList(new ColorStateList(states, colors));
//        } else {
//            mPbLoanding.setBackgroundColor(mProgressLoadingColor);
//        }
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
                    if (mYBVListener != null && !isClick) {
                        // 焦点获取
                        mYBVListener.getFocuse(mCountFocus, YButtomView.this);
                        mCountFocus++;
                    }
                    KeyboardUtils.hideSoftInput(YButtomView.this);
                    mLineView.setBackgroundColor(mGetFocusColor);
                } else {
                    mLineView.setBackgroundColor(mLoseFocusColor);
                }
                if (hasErrStatus) {
                    // 如果处于错误状态
                    mLineView.setBackgroundColor(mErrColor);
                }
            }
        });
    }

    // 为YButtomView设置回调监听
    public void setYBVListener(YBVListener ybvListener) {
        mYBVListener = ybvListener;
    }

    public interface YBVListener {
        /**
         * 控件被点击
         *
         * @param yButtomView
         */
        void onClick(YButtomView yButtomView);

        /**
         * 控件第几次获取到焦点
         *
         * @param count
         * @param yButtomView
         */
        void getFocuse(int count, YButtomView yButtomView);
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
    }

    /**
     * 根据状态清楚信息清除错误信息
     */
    @Override
    public void clearFocusErr() {
        hasErrStatus = false;
        mTvErr.setVisibility(View.INVISIBLE);
        if (etHasFocus) {
            mLineView.setBackgroundColor(mGetFocusColor);
        } else {
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
        mTvTempContent.setText(content);
    }

    /**
     * 获取控件内容
     *
     * @return
     */
    @Override
    public String getContent() {
        return mTvTempContent.getText().toString().trim();
    }

    /**
     * 清空控件内容
     */
    @Override
    public void clearContent() {
        mTvTempContent.setText("");
    }

    // SET 方法
    @BindingAdapter("y_change_content")
    public static void setBindingContent(YButtomView ybv, String content) {
        if (ybv != null) {
            String mCurrentStr = ybv.mTvTempContent.getText().toString().trim();
            if (!TextUtils.isEmpty(content) && !content.equalsIgnoreCase(mCurrentStr)) {
                ybv.mTvTempContent.setText(content);
            }
        }
    }

    // GET 方法
    @InverseBindingAdapter(attribute = "y_change_content", event = "contentAttrChanged")
    public static String getBindingContentLD(YButtomView ybv) {
        return ybv.mTvTempContent.getText().toString().trim();
    }

    // 监听,如果有变动就调用listener中的onChange方法
    @BindingAdapter(value = "contentAttrChanged", requireAll = false)
    public static void setContentChangeListener(YButtomView ybv, InverseBindingListener listener) {
        ybv.mTvTempContent.addTextChangedListener(new TextWatcher() {
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
