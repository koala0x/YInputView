package com.yey.ycustomeview;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

// https://stackoverflow.com/a/34817565/7986616
public class CustomeEditTextView extends FrameLayout {
    private static final String TAG = "CustomeEditTextView 日志";
    // 控件高度
    private EditText mEtContent;
    private TextView mTvErr;
    private TextView mTvHint;
    private String mErrStr;
    private String mHintStr;
    private static String mContentStr;
    private int mErrColor;
    // 失去焦点时候提示颜色
    private int mLoseFocusColor;
    // 获取焦点时候提示颜色
    private int mGetFocusColor;
    private int mEtContentColor;
    private View mLineView;

    // 是否获取焦点
    private static boolean etHasFocus;

    // 是否是错误状态
    private boolean hasErrStatus;


    public CustomeEditTextView(@NonNull Context context) {
        this(context, null);
    }

    public CustomeEditTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomeEditTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initXmlParams(context, attrs, defStyleAttr);
        initView(context);
        initListener();
    }


    private void initXmlParams(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomeEditTextView, defStyleAttr, 0);
        mErrStr = typedArray.getString(R.styleable.CustomeEditTextView_y_err_desc);
        mHintStr = typedArray.getString(R.styleable.CustomeEditTextView_y_hint_desc);
        mContentStr = typedArray.getString(R.styleable.CustomeEditTextView_y_content_desc);
        mErrColor = typedArray.getColor(R.styleable.CustomeEditTextView_y_tv_err_color, Color.RED);
        mLoseFocusColor = typedArray.getColor(R.styleable.CustomeEditTextView_y_lose_focus, Color.GRAY);
        mGetFocusColor = typedArray.getColor(R.styleable.CustomeEditTextView_y_get_focus, Color.BLUE);
        mEtContentColor = typedArray.getColor(R.styleable.CustomeEditTextView_y_et_content_color, Color.BLACK);
        typedArray.recycle();
    }


    // 初始化View
    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_custome_edite_textview, this);
        // 设置内容
        mEtContent = (EditText) findViewById(R.id.et_y_content);
        mEtContent.setText(mContentStr);
        mEtContent.setTextColor(mEtContentColor);
        mEtContent.setHintTextColor(mLoseFocusColor);
        if (TextUtils.isEmpty(mContentStr)) {
            mEtContent.setHint(mHintStr);
        }

        mTvHint = (TextView) findViewById(R.id.tv_y_hint);
        mTvHint.setText(mHintStr);
        mTvHint.setTextColor(mLoseFocusColor);

        mTvErr = (TextView) findViewById(R.id.tv_y_err);
        mTvErr.setText(mErrStr);
        mTvErr.setTextColor(mErrColor);

        mLineView = (View) findViewById(R.id.v_y_line);
        mLineView.setBackgroundColor(mLoseFocusColor);

        // 初始隐藏
        if (TextUtils.isEmpty(mContentStr)) {
            mTvHint.setVisibility(View.INVISIBLE);
        } else {
            mTvHint.setVisibility(View.VISIBLE);
        }
        mTvErr.setVisibility(View.INVISIBLE);

    }


    private void initListener() {
        /**
         * 为EditText设置焦点改变的监听
         */
        mEtContent.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etHasFocus = hasFocus;
                if (hasFocus) {
                    // 获取焦点
                    // 1. EditText hint提示取消
                    // 2. TextView提示控件显示,文字色高亮,内容为XML中设置的提示文字
                    // 3. 分割线背景色高亮
                    mEtContent.setHint("");

                    mTvHint.setVisibility(VISIBLE);
                    mTvHint.setTextColor(mGetFocusColor);
                    mTvHint.setText(mHintStr);
                    mLineView.setBackgroundColor(mGetFocusColor);
                    // Log.e(TAG, "onFocusChange: ");

                } else {
                    /**
                     * 失去焦点时候
                     * 1. EditText中有内容
                     *    a. TextView提示控件不隐藏,文字色置灰
                     *    b. 分割线背景色置灰
                     * 2. EditText中没有内容
                     *    a. EditText hint提示显示
                     *    b. TextView提示控件隐藏,清除该控件中的内容
                     */
                    String mContentStr = mEtContent.getText().toString();
                    mTvHint.setTextColor(mLoseFocusColor);
                    mLineView.setBackgroundColor(mLoseFocusColor);
                    if (TextUtils.isEmpty(mContentStr.trim())) {
                        // 如果没有输入数据
                        mEtContent.setText("");
                        mEtContent.setHint(mHintStr);

                        mTvHint.setVisibility(INVISIBLE);
                        mTvHint.setText("");
                    }
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
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击整个控件为EditText获取焦点
                mEtContent.requestFocus();
            }
        });
        mEtContent.setInputType(InputType.TYPE_CLASS_TEXT);
        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String mStr = s.toString();
                if (!TextUtils.isEmpty(mStr)) {
                    mTvHint.setVisibility(VISIBLE);
                }
            }
        });
    }

    /**
     * 获取EditText控件
     */
    public EditText getEditText() {
        return mEtContent;
    }

    /**
     * 为EditText控件添加内容改变监听
     */
    public void setTextChangedListener(TextWatcher textWatcher) {
        mEtContent.addTextChangedListener(textWatcher);
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

    // SET 方法
    @BindingAdapter("y_content")
    public static void setStr(CustomeEditTextView cetv, String content) {
        if (cetv != null) {
            String mCurrentStr = cetv.mEtContent.getText().toString().trim();
            if (!TextUtils.isEmpty(content)) {
                if (!content.equalsIgnoreCase(mCurrentStr)) {
                    cetv.mEtContent.setText(content);
                    // 设置光标位置
                    cetv.mEtContent.setSelection(content.length());
                }
            }
        }
    }

    // GET 方法
    @InverseBindingAdapter(attribute = "y_content", event = "contentAttrChanged")
    public static String getStr(CustomeEditTextView cetv) {
        return cetv.mEtContent.getText().toString().trim();
    }

    // 监听,如果有变动就调用listener中的onChange方法
    @BindingAdapter(value = "contentAttrChanged")
    public static void setChangeListener(CustomeEditTextView cetv, InverseBindingListener listener) {
        cetv.mEtContent.addTextChangedListener(new TextWatcher() {
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
