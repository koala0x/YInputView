package com.yey.ycustomeview.Yinterface;

import android.view.View;

import androidx.annotation.NonNull;
// https://github.com/Blankj/AndroidUtilCode/blob/master/lib/utilcode/src/main/java/com/blankj/utilcode/util/ClickUtils.java
public abstract class OnDebouncingClickListener implements View.OnClickListener {
    private static final int DEBOUNCING_TAG = -7;
    private static final long DEBOUNCING_DEFAULT_VALUE = 200;
    private static boolean mEnabled = true;

    private static final Runnable ENABLE_AGAIN = new Runnable() {
        @Override
        public void run() {
            mEnabled = true;
        }
    };

    private static boolean isValid(@NonNull final View view, final long duration) {
        long curTime = System.currentTimeMillis();
        Object tag = view.getTag(DEBOUNCING_TAG);
        if (!(tag instanceof Long)) {
            view.setTag(DEBOUNCING_TAG, curTime);
            return true;
        }
        long preTime = (Long) tag;
        if (curTime - preTime < 0) {
            view.setTag(DEBOUNCING_TAG, curTime);
            return false;
        } else if (curTime - preTime <= duration) {
            return false;
        }
        view.setTag(DEBOUNCING_TAG, curTime);
        return true;
    }

    private long mDuration;
    private boolean mIsGlobal;

    public OnDebouncingClickListener() {
        this(true, DEBOUNCING_DEFAULT_VALUE);
    }

    public OnDebouncingClickListener(final boolean isGlobal) {
        this(isGlobal, DEBOUNCING_DEFAULT_VALUE);
    }

    public OnDebouncingClickListener(final long duration) {
        this(true, duration);
    }

    public OnDebouncingClickListener(final boolean isGlobal, final long duration) {
        mIsGlobal = isGlobal;
        mDuration = duration;
    }

    public abstract void onDebouncingClick(View v);

    @Override
    public final void onClick(View v) {
        if (mIsGlobal) {
            if (mEnabled) {
                mEnabled = false;
                v.postDelayed(ENABLE_AGAIN, mDuration);
                onDebouncingClick(v);
            }
        } else {
            if (isValid(v, mDuration)) {
                onDebouncingClick(v);
            }
        }
    }
}
