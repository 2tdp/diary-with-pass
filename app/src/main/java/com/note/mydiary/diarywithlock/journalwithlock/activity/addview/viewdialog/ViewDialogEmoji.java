package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewdialog;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.note.mydiary.diarywithlock.journalwithlock.R;

public class ViewDialogEmoji extends RelativeLayout {

    int w;
    RecyclerView rcvEmoji;

    public ViewDialogEmoji(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;

        ImageView iv = new ImageView(context);
        iv.setImageResource(R.drawable.iv_direct_dialog);
        iv.setColorFilter(getResources().getColor(R.color.background_dialog));
        LayoutParams paramsImage = new LayoutParams((int) (4.5f * w / 100), (int) (4.5f * w / 100));
        paramsImage.setMargins((int) (5.5f * w / 100), 0, 0, 0);
        addView(iv, paramsImage);

        RelativeLayout rlContent = new RelativeLayout(context);
        rlContent.setBackgroundResource(R.drawable.border_dialog_shadow);
        LayoutParams paramsRl = new LayoutParams((int) (88.89f * w / 100), (int) (75f * w / 100));
        paramsRl.setMargins(0, (int) (2f * w / 100), 0, 0);
        rcvEmoji = new RecyclerView(context);
        rcvEmoji.setPadding(w / 100, w / 100, w / 100, w / 100);
        rlContent.addView(rcvEmoji, new LayoutParams(-1, -1));
        addView(rlContent, paramsRl);
    }

    public RecyclerView getRcvEmoji() {
        return rcvEmoji;
    }
}
