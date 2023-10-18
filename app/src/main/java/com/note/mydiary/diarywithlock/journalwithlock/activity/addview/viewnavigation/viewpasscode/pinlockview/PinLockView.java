package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.pinlockview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.recyclerview.widget.RecyclerView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsBitmap;

import java.util.ArrayList;

/**
 * Represents a numeric lock view which can used to taken numbers as input.
 * The length of the input can be customized using {@link PinLockView#setPinLength(int)}, the default value being 4
 * <p/>
 * It can also be used as dial pad for taking number inputs.
 * Optionally, {@link IndicatorDots} can be attached to this view to indicate the length of the input taken
 * Created by aritraroy on 31/05/16.
 */
public class PinLockView extends RecyclerView {

    static int w;
    private static final int DEFAULT_PIN_LENGTH = 4;
    private static final int[] DEFAULT_KEY_SET = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

    private String mPin = "";
    private int mPinLength;
    private int mHorizontalSpacing, mVerticalSpacing;
    private int mTextColor;
    private float mTextSize;
    private int mButtonSize;
    private Typeface mButtonTypeface;
    private Drawable mButtonBackgroundDrawable;
    private Drawable mButtonBackgroundDel;
    private boolean mShowDeleteButton;

    private IndicatorDots mIndicatorDots;
    private PinLockAdapter mAdapter;
    private PinLockListener mPinLockListener;
    private CustomizationOptionsBundle mCustomizationOptionsBundle;
    private int[] mCustomKeySet;

    private final PinLockAdapter.OnNumberClickListener mOnNumberClickListener = new PinLockAdapter.OnNumberClickListener() {
        @Override
        public void onNumberClicked(int keyValue) {
            if (mPin.length() < getPinLength()) {
                mPin = mPin.concat(String.valueOf(keyValue));

                if (isIndicatorDotsAttached()) mIndicatorDots.updateDot(mPin.length());

                if (mPin.length() == 1) {
                    mAdapter.setPinLength(mPin.length());
                    mAdapter.notifyItemChanged(mAdapter.getItemCount() - 1);
                }

                if (mPinLockListener != null)
                    if (mPin.length() == mPinLength) mPinLockListener.onComplete(mPin);
                    else mPinLockListener.onPinChange(mPin.length(), mPin);
            } else {
                if (!isShowDeleteButton()) {
//                    resetPinLockView();
                    if (mPin.length() < getPinLength())
                        mPin = mPin.concat(String.valueOf(keyValue));

                    if (isIndicatorDotsAttached() && mPin.length() < getPinLength())
                        mIndicatorDots.updateDot(mPin.length());

                    if (mPinLockListener != null) mPinLockListener.onPinChange(mPin.length(), mPin);
                } else {
                    if (mPinLockListener != null) mPinLockListener.onComplete(mPin);
                }
            }
        }
    };

    public PinLockAdapter.OnDeleteClickListener mOnDeleteClickListener = new PinLockAdapter.OnDeleteClickListener() {
        @Override
        public void onDeleteClicked() {
            if (mPin.length() > 0) {
                mPin = mPin.substring(0, mPin.length() - 1);

                if (mPinLockListener != null) {
                    if (mPin.length() == 0) {
                        mPinLockListener.onEmpty();
                        clearInternalPin();
                    } else mPinLockListener.onPinChange(mPin.length(), mPin);
                }

                if (isIndicatorDotsAttached()) mIndicatorDots.updateDot(mPin.length());

                if (mPin.length() == 0) {
                    mAdapter.setPinLength(mPin.length());
                    mAdapter.notifyItemChanged(mAdapter.getItemCount() - 1);
                }
            } else {
                if (mPinLockListener != null) mPinLockListener.onEmpty();
            }
        }

        @Override
        public void onDeleteLongClicked() {
            resetPinLockView();
            if (mPinLockListener != null) mPinLockListener.onEmpty();
        }
    };

    public PinLockView(Context context) {
        super(context);
        init(context);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void init(Context context) {
        w = getResources().getDisplayMetrics().widthPixels;

        mPinLength = DEFAULT_PIN_LENGTH;
        mHorizontalSpacing = (int) (5.556f * w / 100);
        mVerticalSpacing = (int) (5.556f * w / 100);
        mTextColor = getResources().getColor(R.color.black);
        mTextSize = (int) (8.889f * w / 100);
        mButtonSize = (int) (19.44f * w / 100);
        mButtonTypeface = Utils.getTypeFace("poppins", "sf_pro_text_regular.ttf", context);
        mButtonBackgroundDrawable = getResources().getDrawable(R.drawable.background_button_pinlock);

        String jsonConfig = Utils.readFromFile(context, "theme/theme_app/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP) + "/config.txt");

        ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);

        if (config != null) {
            mTextColor = Color.parseColor(config.getColorIcon());
            BitmapDrawable drawable = new BitmapDrawable(getResources(), UtilsBitmap.getBitmapFromVectorDrawable(context, R.drawable.background_button_pinlock));
            drawable.setTint(Color.parseColor(config.getColorIcon()));
            mButtonBackgroundDrawable = drawable;
            BitmapDrawable drawableDel = new BitmapDrawable(getResources(), UtilsBitmap.getBitmapFromVectorDrawable(context, R.drawable.ic_del_un_text));
            drawableDel.setTint(Color.parseColor(config.getColorIcon()));
            mButtonBackgroundDel = drawableDel;
        }

        mCustomizationOptionsBundle = new CustomizationOptionsBundle();
        mCustomizationOptionsBundle.setTextColor(mTextColor);
        mCustomizationOptionsBundle.setTextSize(mTextSize);
        mCustomizationOptionsBundle.setButtonSize(mButtonSize);
        mCustomizationOptionsBundle.setTypeface(mButtonTypeface);
        mCustomizationOptionsBundle.setButtonBackgroundDrawable(mButtonBackgroundDrawable);
        mCustomizationOptionsBundle.setDrawableDel(mButtonBackgroundDel);
        mCustomizationOptionsBundle.setLstBitmap(new ArrayList<>());

        initView();
    }

    private void initView() {
        setLayoutManager(new LTRGridLayoutManager(getContext(), 3));

        mAdapter = new PinLockAdapter(getContext());
        mAdapter.setOnItemClickListener(mOnNumberClickListener);
        mAdapter.setOnDeleteClickListener(mOnDeleteClickListener);
        mAdapter.setCustomizationOptions(mCustomizationOptionsBundle);
        setAdapter(mAdapter);

        addItemDecoration(new ItemSpaceDecoration(mHorizontalSpacing, mVerticalSpacing, 3, false));
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    /**
     * Sets a {@link PinLockListener} to the to listen to pin update events
     *
     * @param pinLockListener the listener
     */
    public void setPinLockListener(PinLockListener pinLockListener) {
        this.mPinLockListener = pinLockListener;
    }

    public void setDelPinLockListener(PinLockAdapter.OnDeleteClickListener mOnDeleteClickListener) {
        this.mOnDeleteClickListener = mOnDeleteClickListener;
    }

    /**
     * Get the length of the current pin length
     *
     * @return the length of the pin
     */
    public int getPinLength() {
        return mPinLength;
    }

    /**
     * Sets the pin length dynamically
     *
     * @param pinLength the pin length
     */
    public void setPinLength(int pinLength) {
        this.mPinLength = pinLength;

        if (isIndicatorDotsAttached()) mIndicatorDots.setPinLength(pinLength);
    }

    /**
     * Get the text color in the buttons
     *
     * @return the text color
     */
    public int getTextColor() {
        return mTextColor;
    }

    /**
     * Set the text color of the buttons dynamically
     *
     * @param textColor the text color
     */
    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
        mCustomizationOptionsBundle.setTextColor(textColor);
        mAdapter.notifyChange();
    }

    /**
     * Get the size of the text in the buttons
     *
     * @return the size of the text in pixels
     */
    public float getTextSize() {
        return mTextSize;
    }

    /**
     * Set the size of text in pixels
     *
     * @param textSize the text size in pixels
     */
    public void setTextSize(float textSize) {
        this.mTextSize = textSize;
        mCustomizationOptionsBundle.setTextSize(textSize);
        mAdapter.notifyChange();
    }

    /**
     * Get the size of the pin buttons
     *
     * @return the size of the button in pixels
     */
    public int getButtonSize() {
        return mButtonSize;
    }

    /**
     * Set the size of the pin buttons dynamically
     *
     * @param buttonSize the button size
     */
    public void setButtonSize(int buttonSize) {
        this.mButtonSize = buttonSize;
        mCustomizationOptionsBundle.setButtonSize(buttonSize);
        mAdapter.notifyChange();
    }

    public void setButtonTypeface(Typeface typeface) {
        this.mButtonTypeface = typeface;
        mCustomizationOptionsBundle.setTypeface(typeface);
        mAdapter.notifyChange();
    }

    public void setButtonDelete(Drawable drawable) {
        this.mButtonBackgroundDel = drawable;
        mCustomizationOptionsBundle.setDrawableDel(drawable);
        mAdapter.notifyChange();
    }

    /**
     * Get the current background drawable of the buttons, can be null
     *
     * @return the background drawable
     */
    public Drawable getButtonBackgroundDrawable() {
        return mButtonBackgroundDrawable;
    }

    /**
     * Set the background drawable of the buttons dynamically
     *
     * @param buttonBackgroundDrawable the background drawable
     */
    public void setButtonBackgroundDrawable(Drawable buttonBackgroundDrawable) {
        this.mButtonBackgroundDrawable = buttonBackgroundDrawable;
        mCustomizationOptionsBundle.setButtonBackgroundDrawable(buttonBackgroundDrawable);
        mAdapter.notifyChange();
    }

    public void setListBitmap(ArrayList<Bitmap> lstBitmap) {
        mCustomizationOptionsBundle.setLstBitmap(lstBitmap);
        mAdapter.notifyChange();
    }

    public void setHideText(boolean isText) {
        mCustomizationOptionsBundle.setText(isText);
        mAdapter.notifyChange();
    }

    public int[] getCustomKeySet() {
        return mCustomKeySet;
    }

    public void setCustomKeySet(int[] customKeySet) {
        this.mCustomKeySet = customKeySet;

        if (mAdapter != null) mAdapter.setKeyValues(customKeySet);
    }

    public void enableLayoutShuffling() {
        this.mCustomKeySet = ShuffleArrayUtils.shuffle(DEFAULT_KEY_SET);

        if (mAdapter != null) mAdapter.setKeyValues(mCustomKeySet);

    }

    private void clearInternalPin() {
        mPin = "";
    }

    /**
     * Resets the {@link PinLockView}, clearing the entered pin
     * and resetting the {@link IndicatorDots} if attached
     */
    public void resetPinLockView() {

        clearInternalPin();

        mAdapter.setPinLength(mPin.length());
        mAdapter.notifyItemChanged(mAdapter.getItemCount() - 1);

        if (mIndicatorDots != null) mIndicatorDots.updateDot(mPin.length());
    }

    public String getPin() {
        return mPin;
    }

    /**
     * Is the delete button shown
     *
     * @return returns true if shown, false otherwise
     */
    public boolean isShowDeleteButton() {
        return mShowDeleteButton;
    }

    /**
     * Returns true if {@link IndicatorDots} are attached to {@link PinLockView}
     *
     * @return true if attached, false otherwise
     */
    public boolean isIndicatorDotsAttached() {
        return mIndicatorDots != null;
    }

    /**
     * Attaches {@link IndicatorDots} to {@link PinLockView}
     *
     * @param mIndicatorDots the view to attach
     */
    public void attachIndicatorDots(IndicatorDots mIndicatorDots) {
        this.mIndicatorDots = mIndicatorDots;
    }
}
