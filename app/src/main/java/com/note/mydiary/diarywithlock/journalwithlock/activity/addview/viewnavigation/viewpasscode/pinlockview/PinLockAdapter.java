package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.pinlockview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.note.mydiary.diarywithlock.journalwithlock.R;

/**
 * Created by aritraroy on 31/05/16.
 */
public class PinLockAdapter extends RecyclerView.Adapter<PinLockAdapter.NumberHolder> {

    private final Context mContext;
    private CustomizationOptionsBundle mCustomizationOptionsBundle;
    private OnNumberClickListener mOnNumberClickListener;
    private OnDeleteClickListener mOnDeleteClickListener;
    private int mPinLength;
    private final int w;

    private int[] mKeyValues;

    public PinLockAdapter(Context context) {
        this.mContext = context;
        this.mKeyValues = getAdjustKeyValues(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, -1, 0});
        w = context.getResources().getDisplayMetrics().widthPixels;
    }

    @NonNull
    @Override
    public NumberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NumberHolder(new Button(mContext));
    }

    @Override
    public void onBindViewHolder(@NonNull NumberHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return 12;
    }

    public int getPinLength() {
        return mPinLength;
    }

    public void setPinLength(int pinLength) {
        this.mPinLength = pinLength;
    }

    public int[] getKeyValues() {
        return mKeyValues;
    }

    public void setKeyValues(int[] keyValues) {
        this.mKeyValues = getAdjustKeyValues(keyValues);
        notifyChange();
    }

    private int[] getAdjustKeyValues(int[] keyValues) {
        int[] adjustedKeyValues = new int[keyValues.length + 1];
        for (int i = 0; i < keyValues.length; i++) {
            if (i < 9 || i == 10) adjustedKeyValues[i] = keyValues[i];
            else {
                adjustedKeyValues[i] = -1;
                adjustedKeyValues[i + 1] = keyValues[i];
            }
        }
        return adjustedKeyValues;
    }

    public OnNumberClickListener getOnItemClickListener() {
        return mOnNumberClickListener;
    }

    public void setOnItemClickListener(OnNumberClickListener onNumberClickListener) {
        this.mOnNumberClickListener = onNumberClickListener;
    }

    public OnDeleteClickListener getOnDeleteClickListener() {
        return mOnDeleteClickListener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.mOnDeleteClickListener = onDeleteClickListener;
    }

    public CustomizationOptionsBundle getCustomizationOptions() {
        return mCustomizationOptionsBundle;
    }

    public void setCustomizationOptions(CustomizationOptionsBundle customizationOptionsBundle) {
        this.mCustomizationOptionsBundle = customizationOptionsBundle;
    }

    class NumberHolder extends RecyclerView.ViewHolder {

        Button mNumberButton;

        public NumberHolder(@NonNull View itemView) {
            super(itemView);
            mNumberButton = (Button) itemView;
        }

        public void onBind(int position) {
            if (position == 9) mNumberButton.setVisibility(View.GONE);
            else if (position == 11) {
                LinearLayout.LayoutParams params;
                if (!mCustomizationOptionsBundle.getLstBitmap().isEmpty()) {
                    if (mCustomizationOptionsBundle.getLstBitmap().size() < 12) {
                        if (mCustomizationOptionsBundle.getLstBitmap().size() == 11)
                            mNumberButton.setBackground(new BitmapDrawable(mContext.getResources(), mCustomizationOptionsBundle.getLstBitmap().get(mCustomizationOptionsBundle.getLstBitmap().size() - 1)));
                        else mNumberButton.setBackgroundResource(R.drawable.ic_del_un_text);
                    } else {
                        if (!mCustomizationOptionsBundle.isText())
                            mNumberButton.setBackgroundResource(R.drawable.ic_del_number);
                        else mNumberButton.setBackgroundResource(R.drawable.ic_del_un_text);
                    }
                    params = new LinearLayout.LayoutParams((int) (19.44f * w / 100), (int) (19.44f * w / 100));
                } else {
                    if (mCustomizationOptionsBundle.getDrawableDel() != null) {
                        mNumberButton.setBackground(mCustomizationOptionsBundle.getDrawableDel());
                        params = new LinearLayout.LayoutParams((int) (19.44f * w / 100), (int) (19.44f * w / 100));
                    } else {
                        if (mCustomizationOptionsBundle.isText())
                            mNumberButton.setBackgroundResource(R.drawable.ic_del_number);
                        else mNumberButton.setBackgroundResource(R.drawable.ic_del_un_text);
                        params = new LinearLayout.LayoutParams((int) (17.556f * w / 100), (int) (17.556f * w / 100));
                    }
                }
                mNumberButton.setLayoutParams(params);
            } else {
                if (!mCustomizationOptionsBundle.isText())
                    mNumberButton.setText(String.valueOf(mKeyValues[position]));

                mNumberButton.setVisibility(View.VISIBLE);
                mNumberButton.setTag(mKeyValues[position]);

                if (mCustomizationOptionsBundle != null) {
                    mNumberButton.setTypeface(mCustomizationOptionsBundle.getTypeface());
                    mNumberButton.setTextColor(mCustomizationOptionsBundle.getTextColor());
                    if (!mCustomizationOptionsBundle.getLstBitmap().isEmpty())
                        if (position == 10)
                            mNumberButton.setBackground(new BitmapDrawable(mContext.getResources(), mCustomizationOptionsBundle.getLstBitmap().get(position - 1)));
                        else
                            mNumberButton.setBackground(new BitmapDrawable(mContext.getResources(), mCustomizationOptionsBundle.getLstBitmap().get(position)));
                    else if (mCustomizationOptionsBundle.getButtonBackgroundDrawable() != null)
                        mNumberButton.setBackground(mCustomizationOptionsBundle.getButtonBackgroundDrawable());

                    mNumberButton.setGravity(Gravity.CENTER);
                    mNumberButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, mCustomizationOptionsBundle.getTextSize());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mCustomizationOptionsBundle.getButtonSize(), mCustomizationOptionsBundle.getButtonSize());
                    mNumberButton.setLayoutParams(params);
                }
            }

            mNumberButton.setOnClickListener(v -> {
                if (position == 11) {
                    if (mOnDeleteClickListener != null) mOnDeleteClickListener.onDeleteClicked();
                } else {
                    if (mOnNumberClickListener != null)
                        mOnNumberClickListener.onNumberClicked((Integer) v.getTag());
                }
            });

            mNumberButton.setOnLongClickListener(v -> {
                if (position == 11)
                    if (mOnDeleteClickListener != null)
                        mOnDeleteClickListener.onDeleteLongClicked();
                return true;
            });
        }
    }

    public interface OnNumberClickListener {
        void onNumberClicked(int keyValue);
    }

    public interface OnDeleteClickListener {
        void onDeleteClicked();

        void onDeleteLongClicked();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyChange() {
        notifyDataSetChanged();
    }
}
