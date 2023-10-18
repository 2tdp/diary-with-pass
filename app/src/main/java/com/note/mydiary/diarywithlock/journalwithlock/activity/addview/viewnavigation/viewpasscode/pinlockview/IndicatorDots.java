package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.pinlockview;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.IntDef;
import androidx.core.view.ViewCompat;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsBitmap;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * It represents a set of indicator dots which when attached with {@link PinLockView}
 * can be used to indicate the current length of the input
 * <p>
 * Created by aritraroy on 01/06/16.
 */
public class IndicatorDots extends LinearLayout {

    @IntDef({IndicatorType.FIXED, IndicatorType.FILL, IndicatorType.FILL_WITH_ANIMATION})
    @Retention(RetentionPolicy.SOURCE)
    public @interface IndicatorType {
        int FIXED = 0;
        int FILL = 1;
        int FILL_WITH_ANIMATION = 2;
    }

    static int w;
    private static final int DEFAULT_PIN_LENGTH = 4;

    private int mDotDiameter;
    private int mDotSpacing;
    private Drawable mFillDrawable;
    private Drawable mEmptyDrawable;
    private int mPinLength, mIndicatorType;
    private BitmapDrawable bitmapDrawableEmpty, bitmapDrawableFill;

    private int mPreviousLength;

    public IndicatorDots(Context context) {
        this(context, null);
    }

    public IndicatorDots(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorDots(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    private void initView(Context context) {
        w = getResources().getDisplayMetrics().widthPixels;

        mDotDiameter = (int) (3.889f * w / 100);
        mDotSpacing = (int) (2.778f * w / 100);

        mFillDrawable = new BitmapDrawable(getResources(), UtilsBitmap.getBitmapFromVectorDrawable(context, R.drawable.dot_filled_default));
        mEmptyDrawable = new BitmapDrawable(getResources(), UtilsBitmap.getBitmapFromVectorDrawable(context, R.drawable.dot_empty_default));

        String jsonConfig = Utils.readFromFile(context, "theme/theme_app/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP) + "/config.txt");

        ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);

        if (config != null) {
            mFillDrawable.setTint(Color.parseColor(config.getColorIcon()));
            mEmptyDrawable.setTint(Color.parseColor(config.getColorIcon()));
        }

        mPinLength = DEFAULT_PIN_LENGTH;
        mIndicatorType = IndicatorType.FIXED;

        ViewCompat.setLayoutDirection(this, ViewCompat.LAYOUT_DIRECTION_LTR);
        if (mIndicatorType == 0) {
            for (int i = 0; i < mPinLength; i++) {
                View dot = new View(context);
                emptyDot(dot);

                LayoutParams params = new LayoutParams(mDotDiameter, mDotDiameter);
                params.setMargins(mDotSpacing, 0, mDotSpacing, 0);
                dot.setLayoutParams(params);

                addView(dot);
            }
        } else if (mIndicatorType == 2) setLayoutTransition(new LayoutTransition());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // If the indicator type is not fixed
        if (mIndicatorType != 0) {
            ViewGroup.LayoutParams params = this.getLayoutParams();
            params.height = mDotDiameter;
            requestLayout();
        }
    }

    void updateDot(int length) {
        if (mIndicatorType == 0)
            if (length > 0) {
                if (length > mPreviousLength) fillDot(getChildAt(length - 1));
                else emptyDot(getChildAt(length));

                mPreviousLength = length;
            } else {
                // When {@code mPinLength} is 0, we need to reset all the views back to empty
                for (int i = 0; i < getChildCount(); i++) {
                    View v = getChildAt(i);
                    emptyDot(v);
                }
                mPreviousLength = 0;
            }
        else {
            if (length > 0) {
                if (length > mPreviousLength) {
                    View dot = new View(getContext());
                    fillDot(dot);

                    LayoutParams params = new LayoutParams(mDotDiameter, mDotDiameter);
                    params.setMargins(mDotSpacing, 0, mDotSpacing, 0);
                    dot.setLayoutParams(params);

                    addView(dot, length - 1);
                } else removeViewAt(length);
                mPreviousLength = length;
            } else {
                removeAllViews();
                mPreviousLength = 0;
            }
        }
    }

    private void emptyDot(View dot) {
        if (bitmapDrawableEmpty == null) dot.setBackground(mEmptyDrawable);
        else dot.setBackground(bitmapDrawableEmpty);
    }

    private void fillDot(View dot) {
        if (bitmapDrawableFill == null) dot.setBackground(mFillDrawable);
        else dot.setBackground(bitmapDrawableFill);
    }

    public int getPinLength() {
        return mPinLength;
    }

    public void setPinLength(int pinLength) {
        this.mPinLength = pinLength;
        removeAllViews();
        initView(getContext());
    }

    public void setDotDrawable(BitmapDrawable bitmapDrawableEmpty, BitmapDrawable bitmapDrawableFill) {
        this.bitmapDrawableEmpty = bitmapDrawableEmpty;
        this.bitmapDrawableFill = bitmapDrawableFill;
        removeAllViews();
        initView(getContext());
    }

    public
    @IndicatorType
    int getIndicatorType() {
        return mIndicatorType;
    }

    public void setIndicatorType(@IndicatorType int type) {
        this.mIndicatorType = type;
        removeAllViews();
        initView(getContext());
    }
}
