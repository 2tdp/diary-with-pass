package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewpickpicture;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsTheme;

public class ViewItemBucket extends RelativeLayout {

    int w;
    RoundedImageView ivThumb;
    ImageView ivRight;
    ViewNamePicture viewNamePicture;

    public ViewItemBucket(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;
        setPadding((int) (1.32f * w / 100), (int) (1.32f * w / 100), (int) (1.32f * w / 100), (int) (1.32f * w / 100));
        setBackgroundColor(getResources().getColor(R.color.trans));
        setFocusable(true);
        setClickable(true);

        init(context);
    }

    @SuppressLint("ResourceType")
    private void init(Context context) {
        ivThumb = new RoundedImageView(context);
        ivThumb.setId(1332);
        ivThumb.setCornerRadius(w / 100f);
        ivThumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(ivThumb, new LayoutParams((int) (18.056f * w / 100), -1));

        viewNamePicture = new ViewNamePicture(context);
        LayoutParams paramsViewName = new LayoutParams(-2, -2);
        paramsViewName.setMargins((int) (5.56f * w / 100), 0, 0, 0);
        paramsViewName.addRule(CENTER_VERTICAL, TRUE);
        paramsViewName.addRule(RIGHT_OF, ivThumb.getId());
        addView(viewNamePicture, paramsViewName);

        ivRight = new ImageView(context);
        ivRight.setImageResource(R.drawable.ic_right);
        ivRight.setPadding((int) (w / 100f), (int) (w / 100f), (int) (w / 100f), (int) (w / 100f));
        LayoutParams paramsRight = new LayoutParams((int) (8.33f * w / 100), (int) (8.33f * w / 100));
        paramsRight.addRule(CENTER_VERTICAL, TRUE);
        paramsRight.addRule(ALIGN_PARENT_END, TRUE);
        addView(ivRight, paramsRight);
    }

    public static class ViewNamePicture extends LinearLayout {

        int w;
        TextView tvName, tvQuantity;

        public ViewNamePicture(Context context) {
            super(context);
            w = getResources().getDisplayMetrics().widthPixels;
            setOrientation(VERTICAL);

            tvName = new TextView(context);
            tvName.setTextColor(getResources().getColor(R.color.text_name_picture));
            tvName.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.89f * w / 100);
            tvName.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
            addView(tvName, new LayoutParams(-2, -2));

            tvQuantity = new TextView(context);
            tvQuantity.setTextColor(getResources().getColor(R.color.text_quantity_picture));
            tvQuantity.setTextSize(TypedValue.COMPLEX_UNIT_PX, 2.78f * w / 100);
            tvQuantity.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
            addView(tvQuantity, new LayoutParams(-2, -2));
        }

        public TextView getTvName() {
            return tvName;
        }

        public TextView getTvQuantity() {
            return tvQuantity;
        }
    }

    public void createThemeApp(Context context, String nameTheme) {
        if (nameTheme.equals("default")) return;

        String jsonConfig = Utils.readFromFile(context, "theme/theme_app/" + nameTheme + "/config.txt");

        ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);

        if (config == null) return;

        viewNamePicture.getTvName().setTextColor(Color.parseColor(config.getColorIcon()));
        UtilsTheme.changeIcon(context, "right", 1, R.drawable.ic_right, ivRight,
                Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));
    }

    public ImageView getIvThumb() {
        return ivThumb;
    }

    public ViewNamePicture getViewNamePicture() {
        return viewNamePicture;
    }
}
