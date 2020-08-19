package com.yey.ycustomeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yey.ycustomeview.util.KeyboardUtils;

public class YButtomView extends FrameLayout {
    private static String mContentStr;
    private int mContentColor;
    private int mSizeImage;
    private String mErrStr;
    private int mErrColor;
    // 失去焦点时候提示颜色
    private int mLoseFocusColor;
    // 获取焦点时候提示颜色
    private int mGetFocusColor;
    private TextView mTvContent;
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
        mContentStr = typedArray.getString(R.styleable.YButtomView_y_content_desc);
        mContentColor = typedArray.getColor(R.styleable.YButtomView_y_et_content_color, Color.BLACK);
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
        this.setClickable(true);
        LayoutInflater.from(context).inflate(R.layout.layout_y_buttomview, this,true);
        mTvContent = (TextView) findViewById(R.id.tv_y_content);
        mTvContent.setText(mContentStr);
        mTvContent.setTextColor(mContentColor);

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
        this.getChildAt(0).setOnClickListener(new OnClickListener() {
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
    public void setErr(String err) {
        hasErrStatus = true;
        mTvErr.setText(err);
        setErrStatus();
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
     * 清除错误信息
     */
    public void clearErr() {
        hasErrStatus = false;
        mTvErr.setVisibility(View.INVISIBLE);
        if (etHasFocus) {
            mLineView.setBackgroundColor(mGetFocusColor);
        } else {
            mLineView.setBackgroundColor(mLoseFocusColor);
        }
    }

    /**
     * 获取图标控件
     */
    public ImageView getIcon() {
        return mIvImage;
    }

    public TextView getTvContent() {
        return mTvContent;
    }
}
