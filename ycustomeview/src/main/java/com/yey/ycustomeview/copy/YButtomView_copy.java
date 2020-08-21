package com.yey.ycustomeview.copy;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.yey.ycustomeview.R;
import com.yey.ycustomeview.util.KeyboardUtils;

public class YButtomView_copy extends FrameLayout {
    private  String mContentStr;
    private int mContentColor;
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
    private YClickListener mYClickListener;
    // 错误状态记录
    private boolean hasErrStatus;
    private boolean etHasFocus;

    public YButtomView_copy(@NonNull Context context) {
        this(context, null);
    }

    public YButtomView_copy(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YButtomView_copy(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        LayoutInflater.from(context).inflate(R.layout.layout_y_buttom_view, this);
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

    }

    private void initListener() {
        this.getChildAt(0).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mYClickListener != null) {
                    // 点击回调
                    mYClickListener.onClick(false);
                }
                YButtomView_copy.this.requestFocus();
            }
        });
        this.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etHasFocus = hasFocus;
                if (hasFocus) {
                    mLineView.setBackgroundColor(mGetFocusColor);
//                    if (mYClickListener != null) {
//                        // 点击回调
//                        mYClickListener.onClick(true);
//                    }
                    KeyboardUtils.hideSoftInput(YButtomView_copy.this);
                } else {
                    mLineView.setBackgroundColor(mLoseFocusColor);
                }
            }
        });
    }

    // 为YButtomView设置回调监听
    public void setYClickListener(YClickListener yClickListener) {
        mYClickListener = yClickListener;
    }

    public interface YClickListener {
        /**
         * @param isFocus true 通过LiveData通知的点击
         *                false 通过用户手动点击的事件
         */
        void onClick(boolean isFocus);
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

    // 设置控件处于报错状态
    @BindingAdapter("y_err_status")
    public static void setErr(YButtomView_copy ybv, Boolean isErr) {
        if (ybv != null) {
            if (isErr) {
                ybv.setErr();
            } else {
                ybv.clearErr();
            }
        }
    }

    // 为ImageView加载图片
    @BindingAdapter("y_image_url")
    public static void setImageURL(YButtomView_copy ybv, String url) {
        if (ybv != null && url != null && ybv.mYLoadImageListener != null) {
            if (url.startsWith("http")) {
                // 通过网络加载图片
                ybv.mYLoadImageListener.loadHtpp(ybv.mIvImage, url);
            } else {
                // 通过本地资源加载图片
                ybv.mYLoadImageListener.loadLocal(ybv.mIvImage, url);
            }
        }
    }

    public interface YLoadImageListener {
        /**
         * 通过网络添加图片
         */
        void loadHtpp(ImageView imageView, String url);

        /**
         * 通过本地加载图片
         */
        void loadLocal(ImageView imageView, String url);
    }

    private YLoadImageListener mYLoadImageListener;

    /**
     * 设置加载图片监听
     *
     * @param yLoadImageListener
     */
    public void setYLoadImageListener(YLoadImageListener yLoadImageListener) {
        mYLoadImageListener = yLoadImageListener;
    }

    /**
     * 通知YBottomView执行点击回调和是否应该失去焦点
     */
    @BindingAdapter("y_notify_click_focus")
    public static void notifyClickAndFocus(YButtomView_copy ybv, Boolean isClickAndfocus) {
        if (ybv != null && isClickAndfocus != null && ybv.mYClickListener != null) {
            if (isClickAndfocus) {
                ybv.mYClickListener.onClick(true);
                // 获取焦点
                ybv.requestFocus();
            } else {
                // 失去焦点
                ybv.clearFocus();
            }
        }
    }
}
