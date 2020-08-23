package com.yey.ycustomeview;

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

import com.yey.ycustomeview.Yinterface.IYInputView;
import com.yey.ycustomeview.util.KeyboardUtils;

// 双向绑定参考: https://www.jianshu.com/p/bd687e5b14c2
// 双向绑定参考: https://blog.chrnie.com/2016/12/02/%E8%87%AA%E5%AE%9A%E4%B9%89-DataBinding-%E5%8F%8C%E5%90%91%E7%BB%91%E5%AE%9A%E5%B1%9E%E6%80%A7/
// https://stackoverflow.com/a/34817565/7986616
public class YEditTextView extends FrameLayout implements IYInputView {
    private static final String TAG = "CustomeEditTextView 日志";
    private EditText mEtContent;
    private TextView mTvHead;
    private TextView mTvErr;
    private TextView mTvHint;
    private String mErrStr;
    // 没有获取焦点时候的提示
    private String mHintStr;
    // 已经获取焦点时候的提示
    private String mFocusHintStr;
    private  String mContentStr;
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

    public YEditTextView(@NonNull Context context) {
        this(context, null);
    }

    public YEditTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YEditTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initXmlParams(context, attrs, defStyleAttr);
        initView(context);
        initListener();
    }


    private void initXmlParams(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.YEditTextView, defStyleAttr, 0);
        mErrStr = typedArray.getString(R.styleable.YEditTextView_y_err_desc);
        mHintStr = typedArray.getString(R.styleable.YEditTextView_y_hint_desc);
        mFocusHintStr = typedArray.getString(R.styleable.YEditTextView_y_focus_hint_desc);
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
        LayoutInflater.from(context).inflate(R.layout.layout_y_edite_text_view, this, true);
        // 设置内容
        mEtContent = (EditText) findViewById(R.id.et_y_content);
        mEtContent.setTextColor(mEtContentColor);
        mEtContent.setText(mContentStr);
        mEtContent.setHintTextColor(mLoseFocusColor);
        if (TextUtils.isEmpty(mContentStr)) {
            mEtContent.setHint(mHintStr);
        }
        mEtContent.setInputType(mInputType);
        if (mMaxLength > 0) {
            mEtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMaxLength)});
        }

        mTvHead = (TextView) findViewById(R.id.tv_y_head);
        mTvHead.setTextColor(mEtContentColor);

        mTvHint = (TextView) this.findViewById(R.id.tv_y_hint);
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
                    mEtContent.setHint(mFocusHintStr);

                    mTvHint.setVisibility(VISIBLE);
                    mTvHint.setTextColor(mGetFocusColor);
                    mTvHint.setText(mHintStr);
                    mLineView.setBackgroundColor(mGetFocusColor);
                    // Log.e(TAG, "onFocusChange: ");
                    KeyboardUtils.showSoftInput(mEtContent, 0);

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
                if ((actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) && mKeyboardNextListener != null) {
                    // 用户点击了键盘的下一步按钮
                    if (mKeyboardNextListener.nextEvent()) {
                        return true; // 保留软键盘
                    } else {
                        return false;// 隐藏软键盘
                    }
                }
                return false;// 隐藏软键盘
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
     * 获取内容头控件
     */
    public TextView getContentHead() {
        return mTvHead;
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
        return IYInputView.TYPE_YETV;
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
     * 清除Err并且清除状态
     */
    @Override
    public void clearErr() {
        hasErrStatus = false;
        this.clearFocus();
        mTvErr.setVisibility(View.INVISIBLE);
        mTvHint.setTextColor(mLoseFocusColor);
        mLineView.setBackgroundColor(mLoseFocusColor);
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

    @Override
    public void requestFocusY() {
        mEtContent.requestFocus();
    }

    @Override
    public void clearFocusY() {
        mEtContent.clearFocus();
    }


    /**
     * 更改控件内容
     */
    @Override
    public void setContent(String content) {
        mEtContent.setText(content);
    }

    /**
     * 获取控件内容
     *
     * @return
     */
    @Override
    public String getContent() {
        String content = mEtContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            return "";
        } else {
            if (content.equals(mContentStr)) {
                return "";
            } else {
                return content;
            }
        }
    }

    /**
     * 清空控件内容
     */
    @Override
    public void clearContent() {
        mEtContent.setText("");
    }


    // SET 方法
    @BindingAdapter("y_change_content")
    public static void setStr(YEditTextView cetv, String content) {
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
    @InverseBindingAdapter(attribute = "y_change_content", event = "contentAttrChanged")
    public static String getStr(YEditTextView cetv) {
        return cetv.mEtContent.getText().toString().trim();
    }

    // 监听,如果有变动就调用listener中的onChange方法
    @BindingAdapter(value = "contentAttrChanged", requireAll = false)
    public static void setChangeListener(YEditTextView cetv, InverseBindingListener listener) {
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


    /**
     * 监听YEditTextView的下一步键盘点击事件
     */
    public interface KeyboardNextListener {
        boolean nextEvent();
    }

    // 下一步键盘点击事件
    private KeyboardNextListener mKeyboardNextListener;

    /**
     * 设置键盘下一步监听
     */
    public void setKeyboardNextListener(KeyboardNextListener keyboardNextListener) {
        mKeyboardNextListener = keyboardNextListener;
    }

    /**
     * 告诉控件是一个ID Number 控件
     */
    private boolean mIsIDNumber;

    public boolean isIDNumber() {
        return mIsIDNumber;
    }

    public void setIDNumber(boolean isIDNumber) {
        mIsIDNumber = isIDNumber;
    }

}
