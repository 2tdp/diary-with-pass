package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewdialog;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

public class ViewDialogMoreDiary extends RelativeLayout {

    static int w;
    ViewItemDialogMoreDiary viewItemPreview, viewItemShare, viewItemDel;

    public ViewDialogMoreDiary(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;
        setFocusable(true);
        setClickable(true);
        init(context);
    }

    private void init(Context context) {
        ImageView iv = new ImageView(context);
        iv.setImageResource(R.drawable.iv_direct_dialog);
        iv.setColorFilter(getResources().getColor(R.color.background_dialog));
        LayoutParams paramsImage = new LayoutParams((int) (4.5f * w / 100), (int) (4.5f * w / 100));
        paramsImage.setMargins(0, 0, (int) (5.56f * w / 100), 0);
        paramsImage.addRule(ALIGN_PARENT_END, TRUE);
        addView(iv, paramsImage);

        LinearLayout llContent = new LinearLayout(context);
        llContent.setOrientation(LinearLayout.VERTICAL);
        llContent.setBackgroundResource(R.drawable.border_dialog_shadow);
        LinearLayout.LayoutParams paramsLl = new LinearLayout.LayoutParams((int) (43.89f * w / 100), (int) (23.333f * w / 100));
        paramsLl.setMargins(0, (int) (2f * w / 100), 0, 0);

        viewItemPreview = new ViewItemDialogMoreDiary(context);
        viewItemPreview.getIv().setImageResource(R.drawable.ic_preview);
        viewItemPreview.getTv().setText(getResources().getString(R.string.preview));
        llContent.addView(viewItemPreview, new LinearLayout.LayoutParams(-1, 0, 1));

        viewItemShare = new ViewItemDialogMoreDiary(context);
        viewItemShare.getIv().setImageResource(R.drawable.ic_share);
        viewItemShare.getTv().setText(getResources().getString(R.string.share));
//        llContent.addView(viewItemShare, new LinearLayout.LayoutParams(-1, 0, 1));

        viewItemDel = new ViewItemDialogMoreDiary(context);
        viewItemDel.getIv().setImageResource(R.drawable.ic_delete);
        viewItemDel.getTv().setText(getResources().getString(R.string.del));
        viewItemDel.getTv().setTextColor(getResources().getColor(R.color.red));
        viewItemDel.getV().setVisibility(GONE);
        llContent.addView(viewItemDel, new LinearLayout.LayoutParams(-1, 0, 1));

        addView(llContent, paramsLl);
    }

    public class ViewItemDialogMoreDiary extends LinearLayout {

        ImageView iv;
        TextView tv;
        View v;

        public ViewItemDialogMoreDiary(Context context) {
            super(context);
            setOrientation(HORIZONTAL);
            setGravity(Gravity.CENTER_VERTICAL);

            iv = new ImageView(context);
            LayoutParams paramsImage = new LayoutParams((int) (5 * w / 100), (int) (5 * w / 100));
            paramsImage.setMargins((int) (3.33f * w / 100), 0, 0, 0);
            addView(iv, paramsImage);

            RelativeLayout rl = new RelativeLayout(context);

            LayoutParams params = new LayoutParams(-1, -1);
            params.setMargins((int) (3.33f * w / 100), 0, 0, 0);

            tv = new TextView(context);
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setTextColor(getResources().getColor(R.color.black));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.389f * w / 100);
            tv.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
            rl.addView(tv, -2, -1);

            v = new View(context);
            v.setBackgroundColor(getResources().getColor(R.color.view_line_popup));
            RelativeLayout.LayoutParams paramsViewLine = new RelativeLayout.LayoutParams(-1, (int) (0.3f * w / 100));
            paramsViewLine.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            rl.addView(v, paramsViewLine);

            addView(rl, params);
        }

        public ImageView getIv() {
            return iv;
        }

        public TextView getTv() {
            return tv;
        }

        public View getV() {
            return v;
        }
    }

    public ViewItemDialogMoreDiary getViewItemPreview() {
        return viewItemPreview;
    }

    public ViewItemDialogMoreDiary getViewItemShare() {
        return viewItemShare;
    }

    public ViewItemDialogMoreDiary getViewItemDel() {
        return viewItemDel;
    }
}
