package com.yey.ycustomeview.copy;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.yey.ycustomeview.R;
import com.yey.ycustomeview.util.KeyboardUtils;

// 双向绑定参考: https://www.jianshu.com/p/bd687e5b14c2
// 双向绑定参考: https://blog.chrnie.com/2016/12/02/%E8%87%AA%E5%AE%9A%E4%B9%89-DataBinding-%E5%8F%8C%E5%90%91%E7%BB%91%E5%AE%9A%E5%B1%9E%E6%80%A7/
// https://stackoverflow.com/a/34817565/7986616
public class YEditTextView_copy extends FrameLayout {
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
    // 输入类型
    private int mInputType;
    // 输入内容长度
    private int mMaxLength;

    public YEditTextView_copy(@NonNull Context context) {
        this(context, null);
    }

    public YEditTextView_copy(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YEditTextView_copy(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initXmlParams(context, attrs, defStyleAttr);
        initView(context);
        initListener();
    }


    private void initXmlParams(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.YEditTextView, defStyleAttr, 0);
        mErrStr = typedArray.getString(R.styleable.YEditTextView_y_err_desc);
        mHintStr = typedArray.getString(R.styleable.YEditTextView_y_hint_desc);
        mContentStr = typedArray.getString(R.styleable.YEditTextView_y_content_desc);
        mErrColor = typedArray.getColor(R.styleable.YEditTextView_y_tv_err_color, Color.RED);
        mLoseFocusColor = typedArray.getColor(R.styleable.YEditTextView_y_lose_focus, Color.GRAY);
        mGetFocusColor = typedArray.getColor(R.styleable.YEditTextView_y_get_focus, Color.BLUE);
        mEtContentColor = typedArray.getColor(R.styleable.YEditTextView_y_et_content_color, Color.BLACK);
        mInputType = typedArray.getInt(R.styleable.YEditTextView_inputType, EditorInfo.TYPE_CLASS_TEXT);
        mMaxLength = typedArray.getInt(R.styleable.YEditTextView_maxLength, 0);
        typedArray.recycle();
    }


    // 初始化View
    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_y_edite_textview, this);
        // 设置内容
        mEtContent = (EditText) findViewById(R.id.et_y_content);
        mEtContent.setText(mContentStr);
        mEtContent.setTextColor(mEtContentColor);
        mEtContent.setHintTextColor(mLoseFocusColor);
        if (TextUtils.isEmpty(mContentStr)) {
            mEtContent.setHint(mHintStr);
        }
        mEtContent.setInputType(mInputType);
        if (mMaxLength > 0) {
            mEtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMaxLength)});
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
                    if (mFocusChangeListener != null) {
                        mFocusChangeListener.change(true);
                    }
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
                    KeyboardUtils.showSoftInput(mEtContent, 0);

                } else {
                    if (mFocusChangeListener != null) {
                        mFocusChangeListener.change(false);
                    }
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
                    KeyboardUtils.hideSoftInput(mEtContent);
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
        mEtContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT && mKeyboardNextListener != null) {
                    // 用户点击了键盘的下一步按钮
                    mKeyboardNextListener.nextEvent();
                }
                return false;
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
     * 控件当前是否获取焦点
     */
    public boolean getFocusStatus() {
        return etHasFocus;
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
    public static void setStr(YEditTextView_copy cetv, String content) {
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
    public static String getStr(YEditTextView_copy cetv) {
        return cetv.mEtContent.getText().toString().trim();
    }

    // 监听,如果有变动就调用listener中的onChange方法
    @BindingAdapter(value = "contentAttrChanged", requireAll = false)
    public static void setChangeListener(YEditTextView_copy cetv, InverseBindingListener listener) {
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

    // 设置控件处于报错状态
    @BindingAdapter("y_err_status")
    public static void setErr(YEditTextView_copy cetv, Boolean isErr) {
        if (cetv != null) {
            if (isErr) {
                cetv.setErr();
            } else {
                cetv.clearErr();
            }
        }
    }

    /**
     * 监听YEditTextView的下一步键盘点击事件
     */
    public interface KeyboardNextListener {
        void nextEvent();
    }

    // 下一步键盘点击事件
    private KeyboardNextListener mKeyboardNextListener;

    /**
     * 设置监听
     */
    public void setKeyboardNextListener(KeyboardNextListener keyboardNextListener) {
        mKeyboardNextListener = keyboardNextListener;
    }

    // 监听当前控件是否获取焦点
    private interface IFocusChangeListener {
        void change(boolean isFocus);
    }

    private IFocusChangeListener mFocusChangeListener;

    public void setIFocusChangeListener(IFocusChangeListener iFocusChangeListener) {
        mFocusChangeListener = iFocusChangeListener;
    }


    /**
     * 通知YEditTextView是否该失去焦点或者主动获取焦点
     */
    @BindingAdapter("y_notify_focus")
    public static void setNotifyFocus(YEditTextView_copy yetv, Boolean isFocus) {
        if (yetv != null && isFocus != null && isFocus != etHasFocus) {
            if (isFocus) {
                // 获取焦点
                yetv.mEtContent.requestFocus();
            } else {
                // 失去焦点
                yetv.mEtContent.clearFocus();
            }
        }
    }

    // GET 方法
    @InverseBindingAdapter(attribute = "y_notify_focus", event = "FocusAttrChanged")
    public static Boolean getNotifyFocus(YEditTextView_copy cetv) {
        boolean focusStatus = cetv.getFocusStatus();
        return focusStatus;
    }

    // 监听,如果有变动就调用listener中的onChange方法
    @BindingAdapter(value = "FocusAttrChanged", requireAll = false)
    public static void NotifyFocus(YEditTextView_copy cetv, InverseBindingListener listener) {
        cetv.setIFocusChangeListener(new IFocusChangeListener() {
            @Override
            public void change(boolean isFocus) {
                listener.onChange();
            }
        });
    }

}
