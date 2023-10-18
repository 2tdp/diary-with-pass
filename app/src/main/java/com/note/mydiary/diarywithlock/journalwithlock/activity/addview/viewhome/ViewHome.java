package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewhome;

import android.annotation.SuppressLint;
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

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsTheme;

public class ViewHome extends RelativeLayout {

    static int w;
    ImageView ivCover;
    ViewToolbarHome viewToolbarHome;
    ViewBottomHome viewBottomHome;

    public ViewHome(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;
        setBackgroundColor(getResources().getColor(R.color.white));
        init(context);
    }

    @SuppressLint("ResourceType")
    private void init(Context context) {
        ivCover = new ImageView(context);
        ivCover.setId(2345);
        ivCover.setImageResource(R.drawable.im_home);
        ivCover.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(ivCover, new LayoutParams(-1, 65 * w / 100));

        viewToolbarHome = new ViewToolbarHome(context);
        viewToolbarHome.setId(2346);
        viewToolbarHome.getTvTitle().setVisibility(GONE);
        LayoutParams paramsToolbar = new LayoutParams(-1, -2);
        paramsToolbar.setMargins((int) (5.5f * w / 100), (int) (11.5f * w / 100), (int) (5.5f * w / 100), 0);
        paramsToolbar.addRule(ALIGN_PARENT_TOP, TRUE);
        addView(viewToolbarHome, paramsToolbar);

        viewBottomHome = new ViewBottomHome(context);
        viewBottomHome.setId(2347);
        LayoutParams paramsBottom = new LayoutParams(-1, 25 * w / 100);
        paramsBottom.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, TRUE);
        addView(viewBottomHome, paramsBottom);
    }

    public class ViewToolbarHome extends RelativeLayout {

        TextView tvTitle;
        ImageView ivNavigation, ivSearch, ivSort, ivVip;

        public ViewToolbarHome(Context context) {
            super(context);
            init(context);
        }

        @SuppressLint("ResourceType")
        private void init(Context context) {
            int paddingImage = w / 100;

            ivNavigation = new ImageView(context);
            ivNavigation.setImageResource(R.drawable.ic_navigation);
            ivNavigation.setPadding(paddingImage, paddingImage, paddingImage, paddingImage);
            ivNavigation.setBackgroundResource(Utils.effectPressRectangle(context).resourceId);
            addView(ivNavigation, new LayoutParams(8 * w / 100, 8 * w / 100));

            tvTitle = new TextView(context);
            tvTitle.setText(getResources().getString(R.string.calendar));
            tvTitle.setTextColor(getResources().getColor(R.color.black));
            tvTitle.setTypeface(Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context));
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, 4.44f * w / 100);
            LayoutParams paramsText = new LayoutParams(-2, -2);
            paramsText.addRule(CENTER_HORIZONTAL, TRUE);
            addView(tvTitle, paramsText);

            ivVip = new ImageView(context);
            ivVip.setId(2348);
            ivVip.setImageResource(R.drawable.ic_vip);
            ivVip.setPadding(paddingImage, paddingImage, paddingImage, paddingImage);
            ivVip.setBackgroundResource(Utils.effectPressOval(context).resourceId);
            LayoutParams paramsVip = new LayoutParams(8 * w / 100, 8 * w / 100);
            paramsVip.addRule(RelativeLayout.ALIGN_PARENT_END, TRUE);
            addView(ivVip, paramsVip);

            ivSort = new ImageView(context);
            ivSort.setId(2349);
            ivSort.setImageResource(R.drawable.ic_sort);
            ivSort.setPadding(paddingImage, paddingImage, paddingImage, paddingImage);
            ivSort.setBackgroundResource(Utils.effectPressOval(context).resourceId);
            LayoutParams paramsSort = new LayoutParams(8 * w / 100, 8 * w / 100);
            paramsSort.addRule(LEFT_OF, ivVip.getId());
            paramsSort.setMargins(0, 0, (int) (5.5f * w / 100), 0);
            addView(ivSort, paramsSort);

            ivSearch = new ImageView(context);
            ivSearch.setImageResource(R.drawable.ic_search);
            ivSearch.setPadding(paddingImage, paddingImage, paddingImage, paddingImage);
            ivSearch.setBackgroundResource(Utils.effectPressOval(context).resourceId);
            LayoutParams paramsSearch = new LayoutParams(8 * w / 100, 8 * w / 100);
            paramsSearch.addRule(LEFT_OF, ivSort.getId());
            paramsSearch.setMargins(0, 0, (int) (5.5f * w / 100), 0);
            addView(ivSearch, paramsSearch);
        }

        public TextView getTvTitle() {
            return tvTitle;
        }

        public ImageView getIvNavigation() {
            return ivNavigation;
        }

        public ImageView getIvSearch() {
            return ivSearch;
        }

        public ImageView getIvSort() {
            return ivSort;
        }

        public ImageView getIvVip() {
            return ivVip;
        }
    }

    public class ViewBottomHome extends RelativeLayout {

        ImageView ivAdd;
        ViewOptionHome viewOptionHome;

        public ViewBottomHome(Context context) {
            super(context);

            View view = new View(context);
            view.setBackgroundColor(getResources().getColor(R.color.view_line));
            LayoutParams paramsViewLine = new LayoutParams(-1, (int) (0.2f * w / 100));
            paramsViewLine.setMargins(0, 5 * w / 100, 0, 0);
            addView(view, paramsViewLine);

            viewOptionHome = new ViewOptionHome(context);
            LayoutParams paramsOption = new LayoutParams(-1, -1);
            paramsOption.setMargins(0, 5 * w / 100, 0, 0);
            paramsOption.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, TRUE);
            addView(viewOptionHome, paramsOption);

            ivAdd = new ImageView(context);
            ivAdd.setImageResource(R.drawable.ic_add_home);
            ivAdd.setBackgroundResource(Utils.effectPressOval(context).resourceId);
            LayoutParams paramsAdd = new LayoutParams(18 * w / 100, 18 * w / 100);
            paramsAdd.addRule(RelativeLayout.CENTER_HORIZONTAL, TRUE);
            paramsAdd.addRule(RelativeLayout.ALIGN_PARENT_TOP, TRUE);
            addView(ivAdd, paramsAdd);
        }

        public ImageView getIvAdd() {
            return ivAdd;
        }

        public ViewOptionHome getViewOptionHome() {
            return viewOptionHome;
        }

        public class ViewOptionHome extends LinearLayout {

            ViewClickHome viewClickHome, viewClickCalendar;

            public ViewOptionHome(Context context) {
                super(context);
                setOrientation(LinearLayout.HORIZONTAL);
                setBackgroundColor(Color.parseColor("#00000000"));
                init(context);
            }

            private void init(Context context) {
                viewClickHome = new ViewClickHome(context);
                viewClickHome.getIv().setImageResource(R.drawable.ic_home);
                viewClickHome.getTv().setText(getResources().getString(R.string.home));
                viewClickHome.setBackgroundResource(Utils.effectPressRectangle(context).resourceId);
                addView(viewClickHome, new LayoutParams(0, -1, 1));

                viewClickCalendar = new ViewClickHome(context);
                viewClickCalendar.getIv().setImageResource(R.drawable.ic_calendar);
                viewClickCalendar.getTv().setText(getResources().getString(R.string.calendar));
                viewClickCalendar.setBackgroundResource(Utils.effectPressRectangle(context).resourceId);
                addView(viewClickCalendar, new LayoutParams(0, -1, 1));
            }

            public ViewClickHome getViewClickHome() {
                return viewClickHome;
            }

            public ViewClickHome getViewClickCalendar() {
                return viewClickCalendar;
            }

            public class ViewClickHome extends LinearLayout {

                ImageView iv;
                TextView tv;
                View viewLine;

                public ViewClickHome(Context context) {
                    super(context);
                    setOrientation(LinearLayout.VERTICAL);
                    init(context);
                }

                private void init(Context context) {
                    viewLine = new View(context);
                    viewLine.setBackgroundColor(getResources().getColor(R.color.orange));
                    LayoutParams paramsViewLine = new LayoutParams((int) (7.2f * w / 100), (int) (0.5f * w / 100));
                    paramsViewLine.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
                    addView(viewLine, paramsViewLine);

                    iv = new ImageView(context);
                    iv.setImageResource(R.drawable.ic_home);
                    LayoutParams paramsImage = new LayoutParams(6 * w / 100, 6 * w / 100);
                    paramsImage.setMargins(0, (int) (3.6f * w / 100), 0, 0);
                    paramsImage.gravity = Gravity.CENTER;
                    addView(iv, paramsImage);

                    tv = new TextView(context);
                    tv.setText(getResources().getString(R.string.home));
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 4f * w / 100);
                    tv.setTextColor(getResources().getColor(R.color.orange));
                    LayoutParams paramsText = new LayoutParams(-2, -2);
                    paramsText.setMargins(0, (int) (1.5f * w / 100), 0, 0);
                    paramsText.gravity = Gravity.CENTER;
                    addView(tv, paramsText);
                }

                public ImageView getIv() {
                    return iv;
                }

                public TextView getTv() {
                    return tv;
                }

                public View getViewLine() {
                    return viewLine;
                }
            }
        }
    }

    public void createTheme(Context context, String nameTheme) {
        if (nameTheme.equals("default")) return;

        String jsonConfig = Utils.readFromFile(context, "theme/theme_app/" + nameTheme + "/config.txt");

        ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);

        if (config == null) return;

        if (config.isBackgroundColor())
            setBackgroundColor(Color.parseColor(config.getColorBackground()));
        else if (config.isBackgroundGradient())
            setBackground(Utils.createBackground(
                    new int[]{Color.parseColor(config.getColorBackground()), Color.parseColor(config.getColorBackgroundGradient())},
                    -1, -1, -1));
        else {
            Bitmap bmBg = BitmapFactory.decodeFile(Utils.getStore(context) + "/theme/theme_app/" + nameTheme + "/background.png");
            if (bmBg != null) setBackground(new BitmapDrawable(getResources(), bmBg));
        }

        viewBottomHome.getViewOptionHome().getViewClickHome().setBackgroundColor(Color.parseColor(config.getColorBackgroundBottomHome()));
        viewBottomHome.getViewOptionHome().getViewClickCalendar().setBackgroundColor(Color.parseColor(config.getColorBackgroundBottomHome()));

        if (!config.isBackgroundImage()){
            viewBottomHome.getViewOptionHome().getViewClickHome().getViewLine().setBackgroundColor(Color.parseColor(config.getColorMain()));
            viewBottomHome.getViewOptionHome().getViewClickHome().getTv().setTextColor(Color.parseColor(config.getColorMain()));
            viewBottomHome.getViewOptionHome().getViewClickHome().getIv().setColorFilter(Color.parseColor(config.getColorIcon()));

            viewBottomHome.getViewOptionHome().getViewClickCalendar().getViewLine().setBackgroundColor(Color.parseColor(config.getColorMain()));
            viewBottomHome.getViewOptionHome().getViewClickCalendar().getTv().setTextColor(Color.parseColor(config.getColorMain()));
            viewBottomHome.getViewOptionHome().getViewClickCalendar().getIv().setColorFilter(Color.parseColor(config.getColorIcon()));

            UtilsTheme.changeIcon(context,
                    "add", 2, R.drawable.ic_add_home, viewBottomHome.getIvAdd(),
                    Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorPlusAdd()));
        } else {
            viewBottomHome.getViewOptionHome().getViewClickHome().getViewLine().setBackgroundColor(Color.parseColor(config.getColorIcon()));
            viewBottomHome.getViewOptionHome().getViewClickHome().getTv().setTextColor(Color.parseColor(config.getColorIcon()));
            viewBottomHome.getViewOptionHome().getViewClickHome().getIv().setColorFilter(Color.parseColor(config.getColorIconMore()));

            viewBottomHome.getViewOptionHome().getViewClickCalendar().getViewLine().setBackgroundColor(Color.parseColor(config.getColorIcon()));
            viewBottomHome.getViewOptionHome().getViewClickCalendar().getTv().setTextColor(Color.parseColor(config.getColorIcon()));
            viewBottomHome.getViewOptionHome().getViewClickCalendar().getIv().setColorFilter(Color.parseColor(config.getColorIconMore()));

            UtilsTheme.changeIcon(context,
                    "add", 2, R.drawable.ic_add_home, viewBottomHome.getIvAdd(),
                    Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorPlusAdd()));
        }

        UtilsTheme.changeIcon(context,
                "navigation", 3, R.drawable.ic_navigation, viewToolbarHome.getIvNavigation(),
                Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));
        UtilsTheme.changeIcon(context,
                "search", 1, R.drawable.ic_search, viewToolbarHome.getIvSearch(),
                Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));
        UtilsTheme.changeIcon(context,
                "sort", 1, R.drawable.ic_sort, viewToolbarHome.getIvSort(),
                Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));

        viewToolbarHome.getTvTitle().setTextColor(Color.parseColor(config.getColorIcon()));
        ivCover.setImageBitmap(BitmapFactory.decodeFile(Utils.getStore(context) + "/theme/theme_app/" + nameTheme + "/background_home.png"));
    }

    public ImageView getIvCover() {
        return ivCover;
    }

    public ViewToolbarHome getViewToolbarHome() {
        return viewToolbarHome;
    }

    public ViewBottomHome getViewBottomHome() {
        return viewBottomHome;
    }
}
