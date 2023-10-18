package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewadddiary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.ViewToolbar;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsTheme;
import com.note.remiads.native_ads.ViewNativeAds;

public class ViewAddDiary extends RelativeLayout {

    static int w;
    ViewToolbar viewToolbar;
    ViewContentAddDiary contentAddDiary;
    ImageView ivPic, ivRec, ivLoading;
    ViewOptionPic viewOptionPic;
    LinearLayout llAddFile;

    public ViewAddDiary(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;
        setBackgroundColor(getResources().getColor(R.color.white));
        init(context);
    }

    @SuppressLint("ResourceType")
    private void init(Context context) {
        viewToolbar = new ViewToolbar(context);
        viewToolbar.setId(1234);
        viewToolbar.getIvVip().setVisibility(GONE);
        LayoutParams paramsToolbar = new LayoutParams(-1, -2);
        addView(viewToolbar, paramsToolbar);

        ViewNativeAds viewNativeAds = new ViewNativeAds(context);
        viewNativeAds.setId(6666);
        LayoutParams pAds = new LayoutParams(-1, -2);
        pAds.setMargins(w/30, w/30,w/30,0);
        pAds.addRule(BELOW, viewToolbar.getId());
        addView(viewNativeAds, pAds);
        viewNativeAds.addAds(((Activity) context),true, com.note.remiads.R.string.na_small);

        llAddFile = new LinearLayout(context);
        llAddFile.setId(1235);
        llAddFile.setOrientation(LinearLayout.HORIZONTAL);
        llAddFile.setBackgroundResource(R.drawable.border_orange_2);
        LayoutParams paramsAddFile = new LayoutParams((int) (36.11f * w / 100), (int) (14.167 * w / 100));
        paramsAddFile.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        paramsAddFile.setMargins((int) 5.56f * w / 100, 0, 0, (int) (3.61f * w / 100));

        ivPic = new ImageView(context);
        ivPic.setImageResource(R.drawable.ic_pic);
        LinearLayout.LayoutParams paramsPic = new LinearLayout.LayoutParams(0, (int) (6.67f * w / 100), 1);
        paramsPic.gravity = Gravity.CENTER_VERTICAL;
        llAddFile.addView(ivPic, paramsPic);

        ivRec = new ImageView(context);
        ivRec.setImageResource(R.drawable.ic_rec);
        LinearLayout.LayoutParams paramsRec = new LinearLayout.LayoutParams(0, (int) (6.67f * w / 100), 1);
        paramsRec.gravity = Gravity.CENTER_VERTICAL;
        llAddFile.addView(ivRec, paramsRec);

        addView(llAddFile, paramsAddFile);

        contentAddDiary = new ViewContentAddDiary(context);
        NestedScrollView nestedScrollView = new NestedScrollView(context);
        LayoutParams paramsScroll = new LayoutParams(-1, -1);
        paramsScroll.setMargins((int) (5.5f * w / 100), (int) (5.556f * w / 100), (int) (5.5f * w / 100), (int) (7 * w / 100));
        paramsScroll.addRule(BELOW, viewNativeAds.getId());
        paramsScroll.addRule(ABOVE, llAddFile.getId());
        nestedScrollView.addView(contentAddDiary, new LayoutParams(-1, -1));

        addView(nestedScrollView, paramsScroll);

        viewOptionPic = new ViewOptionPic(context);
        LayoutParams paramsOptionPic = new LayoutParams(-1, (int) (77.78f * w / 100));
        paramsOptionPic.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        addView(viewOptionPic, paramsOptionPic);

        ivLoading = new ImageView(context);
        ivLoading.setVisibility(GONE);
        Glide.with(context)
                .asGif()
                .load(R.drawable.iv_loading)
                .into(ivLoading);
        LayoutParams paramsLoading = new LayoutParams((int) (34f * w / 100), (int) (34f * w / 100));
        paramsLoading.addRule(CENTER_IN_PARENT, TRUE);
        addView(ivLoading, paramsLoading);
    }

    public class ViewOptionPic extends LinearLayout {

        ViewOptionDetail viewOptionDetail;
        ViewToolbar viewToolbar;

        public ViewOptionPic(Context context) {
            super(context);
            setOrientation(VERTICAL);
            setBackgroundColor(getResources().getColor(R.color.white));
            setClickable(true);
            setFocusable(true);

            viewToolbar = new ViewToolbar(context);
            LayoutParams paramsToolbar = new LayoutParams(-1, (int) (11.1f * w / 100));
            addView(viewToolbar, paramsToolbar);

            viewOptionDetail = new ViewOptionDetail(context);
            addView(viewOptionDetail, new LayoutParams(-1, -1));
        }

        public ViewToolbar getViewToolbar() {
            return viewToolbar;
        }

        public class ViewToolbar extends RelativeLayout {

            TextView tv;
            ImageView iv;

            public ViewToolbar(Context context) {
                super(context);
                setBackgroundResource(R.drawable.border_top_left_right);
                init(context);
            }

            private void init(Context context) {
                tv = new TextView(context);
                tv.setText(getResources().getString(R.string.add_photo));
                tv.setTextColor(getResources().getColor(R.color.black));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.89f * w / 100);
                tv.setTypeface(Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context));
                LayoutParams paramsText = new LayoutParams(-2, -2);
                paramsText.addRule(CENTER_IN_PARENT, TRUE);
                addView(tv, paramsText);

                iv = new ImageView(context);
                iv.setImageResource(R.drawable.ic_hide_view);
                LayoutParams paramsImage = new LayoutParams((int) (5.56f * w / 100), (int) (5.56f * w / 100));
                paramsImage.addRule(ALIGN_PARENT_END, TRUE);
                paramsImage.addRule(CENTER_VERTICAL, TRUE);
                paramsImage.setMargins(0, 0, (int) (5.56f * w / 100), 0);
                addView(iv, paramsImage);
            }

            public TextView getTv() {
                return tv;
            }

            public ImageView getIv() {
                return iv;
            }
        }

        public class ViewOptionDetail extends LinearLayout {

            ViewOption optionGallery, optionCamera;

            public ViewOptionDetail(Context context) {
                super(context);
                setOrientation(HORIZONTAL);

                optionGallery = new ViewOption(context);
                optionGallery.setMarginLeft();
                optionGallery.getViewChild().getIv().setImageResource(R.drawable.ic_pic);
                optionGallery.getViewChild().getTv().setText(getResources().getString(R.string.gallery));
                addView(optionGallery, new LayoutParams(0, -1, 1));

                optionCamera = new ViewOption(context);
                optionCamera.setMarginRight();
                optionCamera.getViewChild().getIv().setImageResource(R.drawable.ic_camera_2);
                optionCamera.getViewChild().getTv().setText(getResources().getString(R.string.camera));
                addView(optionCamera, new LayoutParams(0, -1, 1));
            }

            public class ViewOption extends RelativeLayout {

                View v;
                ViewOptionDetailChild viewChild;
                LayoutParams paramsBackground, paramsChild;

                public ViewOption(Context context) {
                    super(context);

                    v = new View(context);
                    v.setBackgroundResource(R.drawable.border_gray_1);
                    paramsBackground = new LayoutParams((int) (27.78f * w / 100), (int) (27.78f * w / 100));
                    paramsBackground.addRule(CENTER_VERTICAL, TRUE);
                    addView(v, paramsBackground);

                    viewChild = new ViewOptionDetailChild(context);
                    paramsChild = new LayoutParams(-2, -2);
                    paramsChild.addRule(CENTER_VERTICAL, TRUE);
                    addView(viewChild, paramsChild);
                }

                public void setMarginLeft() {
                    paramsBackground.addRule(ALIGN_PARENT_END, TRUE);
                    paramsBackground.setMargins(0, 0, (int) (5.56f * w / 100), 0);

                    paramsChild.addRule(ALIGN_PARENT_END, TRUE);
                    paramsChild.setMargins(0, 0, (int) (13.89f * w / 100), 0);
                }

                public void setMarginRight() {
                    paramsBackground.setMargins((int) (5.56f * w / 100), 0, 0, 0);

                    paramsChild.setMargins((int) (13.89f * w / 100), 0, 0, 0);
                }

                public class ViewOptionDetailChild extends LinearLayout {

                    ImageView iv;
                    TextView tv;

                    public ViewOptionDetailChild(Context context) {
                        super(context);
                        setGravity(Gravity.CENTER);
                        setOrientation(VERTICAL);

                        iv = new ImageView(context);
                        LayoutParams paramsImage = new LayoutParams((int) (6.67f * w / 100), (int) (6.67f * w / 100));
                        addView(iv, paramsImage);

                        tv = new TextView(context);
                        tv.setTextColor(getResources().getColor(R.color.black));
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
                        tv.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
                        LayoutParams paramsText = new LayoutParams(-2, -2);
                        paramsText.setMargins(0, (int) (1.389f * w / 100), 0, 0);
                        addView(tv, paramsText);
                    }

                    public ImageView getIv() {
                        return iv;
                    }

                    public TextView getTv() {
                        return tv;
                    }
                }

                public View getV() {
                    return v;
                }

                public ViewOptionDetailChild getViewChild() {
                    return viewChild;
                }
            }

            public ViewOption getOptionGallery() {
                return optionGallery;
            }

            public ViewOption getOptionCamera() {
                return optionCamera;
            }
        }

        public ViewOptionDetail getViewOptionDetail() {
            return viewOptionDetail;
        }
    }

    public void createThemeApp(Context context, String nameTheme) {
        if (nameTheme.equals("default")) return;

        String jsonConfig = Utils.readFromFile(context, "theme/theme_app/" + nameTheme + "/config.txt");

        ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);

        if (config == null) return;

        if (config.isBackgroundColor())
            setBackgroundColor(Color.parseColor(config.getColorBackground()));
        else if (config.isBackgroundGradient())
            setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorBackground()),
                    Color.parseColor(config.getColorBackgroundGradient())}, -1, -1, -1));
        else {
            Bitmap bmBg = BitmapFactory.decodeFile(Utils.getStore(context) + "/theme/theme_app/" + nameTheme + "/background.png");
            if (bmBg != null)
                setBackground(new BitmapDrawable(getResources(), bmBg));
        }

        llAddFile.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorBackgroundOptionAddDiary())}, (int) (2.5 * w / 100), -1, -1));
        UtilsTheme.changeIcon(context, "pic", 1, R.drawable.ic_pic, ivPic,
                Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));
        UtilsTheme.changeIcon(context, "rec", 2, R.drawable.ic_rec, ivRec,
                Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));

        if (config.isBackgroundColor())
            viewOptionPic.setBackgroundColor(Color.parseColor(config.getColorBackground()));
        else if (config.isBackgroundGradient())
            viewOptionPic.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorBackground()),
                    Color.parseColor(config.getColorBackgroundGradient())}, -1, -1, -1));
        else {
            Bitmap bmBg = BitmapFactory.decodeFile(Utils.getStore(context) + "/theme/theme_app/" + nameTheme + "/background.png");
            if (bmBg != null)
                viewOptionPic.setBackground(new BitmapDrawable(getResources(), bmBg));
        }

        viewOptionPic.getViewToolbar().setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorBackgroundOptionAddDiary())}, (int) (2.5 * w / 100), -1, -1));
        viewOptionPic.getViewToolbar().getTv().setTextColor(Color.parseColor(config.getColorIcon()));
        UtilsTheme.changeIcon(context, "hideview", 2, R.drawable.ic_hide_view, viewOptionPic.getViewToolbar().getIv(),
                Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorIconInColor()));

        viewOptionPic.getViewOptionDetail().getOptionGallery().getV().setBackground(
                Utils.createBackground(new int[]{Color.parseColor(config.getColorBackgroundOptionAddDiary())}, (int) (2.5 * w / 100), -1, -1));
        UtilsTheme.changeIcon(context, "pic", 1, R.drawable.ic_pic,
                viewOptionPic.getViewOptionDetail().getOptionGallery().getViewChild().getIv(),
                Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));
        viewOptionPic.getViewOptionDetail().getOptionGallery().getViewChild().getTv().setTextColor(Color.parseColor(config.getColorIcon()));

        viewOptionPic.getViewOptionDetail().getOptionCamera().getV().setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorBackgroundOptionAddDiary())}, (int) (2.5 * w / 100), -1, -1));
        UtilsTheme.changeIcon(context, "cam", 3, R.drawable.ic_camera_2,
                viewOptionPic.getViewOptionDetail().getOptionCamera().getViewChild().getIv(),
                Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));
        viewOptionPic.getViewOptionDetail().getOptionCamera().getViewChild().getTv().setTextColor(Color.parseColor(config.getColorIcon()));

        contentAddDiary.createTheme(context, nameTheme);
    }

    public ImageView getIvLoading() {
        return ivLoading;
    }

    public ImageView getIvPic() {
        return ivPic;
    }

    public ImageView getIvRec() {
        return ivRec;
    }

    public ViewOptionPic getViewOptionPic() {
        return viewOptionPic;
    }

    public ViewToolbar getViewToolbar() {
        return viewToolbar;
    }

    public ViewContentAddDiary getContentAddDiary() {
        return contentAddDiary;
    }
}
