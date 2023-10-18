package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewsearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.ViewToolbar;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsTheme;


public class ViewSearchDiary extends LinearLayout {

    int w;
    ViewToolbar viewToolbar;
    RelativeLayout rlSearch;
    EditText etSearch;
    ImageView ivSearch;
    TextView tvNoData;
    RecyclerView rcvSearch;

    public ViewSearchDiary(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;
        setBackgroundColor(getResources().getColor(R.color.white));
        setOrientation(VERTICAL);
        setFocusable(true);
        setClickable(true);

        init(context);
    }

    private void init(Context context) {
        viewToolbar = new ViewToolbar(context);
        viewToolbar.getIvVip().setVisibility(GONE);
        viewToolbar.getTvSave().setVisibility(GONE);
        viewToolbar.getTvTitle().setText(getResources().getString(R.string.search));
        LayoutParams paramsToolbar = new LayoutParams(-1, -2);
        paramsToolbar.setMargins(0, (int) (11.11f * w / 100), 0, 0);
        addView(viewToolbar, paramsToolbar);

        rlSearch = new RelativeLayout(context);
        RelativeLayout.LayoutParams paramsRl = new RelativeLayout.LayoutParams(-1, (int) ((11.11f * w / 100)));
        paramsRl.setMargins((int) (5.556f * w / 100), (int) (4.167f * w / 100), (int) (5.556f * w / 100), 0);

        etSearch = new EditText(context);
        etSearch.setBackgroundResource(R.drawable.border_background_edittext_search);
        etSearch.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
        etSearch.setTextColor(getResources().getColor(R.color.black));
        etSearch.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        etSearch.setHint(getResources().getString(R.string.search_your_memories));
        etSearch.setHintTextColor(getResources().getColor(R.color.gray_2));
        etSearch.setPadding((int) (2.778f * w / 100), 0, 0, 0);
        rlSearch.addView(etSearch, new LayoutParams(-1, -1));

        ivSearch = new ImageView(context);
        ivSearch.setImageResource(R.drawable.ic_search);
        RelativeLayout.LayoutParams paramsImage = new RelativeLayout.LayoutParams((int) (6.667f * w / 100), (int) (6.667f * w / 100));
        paramsImage.setMargins(0, 0, (int) (2.278f * w / 100), 0);
        paramsImage.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsImage.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        rlSearch.addView(ivSearch, paramsImage);

        addView(rlSearch, paramsRl);

        tvNoData = new TextView(context);
        tvNoData.setText(getResources().getString(R.string.nothing_to_search));
        tvNoData.setTextColor(getResources().getColor(R.color.gray_2));
        tvNoData.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        tvNoData.setGravity(Gravity.CENTER);
        addView(tvNoData, new LayoutParams(-1, -1));

        rcvSearch = new RecyclerView(context);
        LayoutParams paramsRcv = new LayoutParams(-1, -1);
        paramsRcv.setMargins((int) (5.556f * w / 100), (int) (5.556f * w / 100), (int) (5.556f * w / 100), (int) (5.556f * w / 100));
        addView(rcvSearch, paramsRcv);
    }

    public void createTheme(Context context, String nameTheme) {
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

        etSearch.setTextColor(Color.parseColor(config.getColorIcon()));
        etSearch.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorMainLight())}, (int) (2.5f * w / 100), -1, -1));

        tvNoData.setTextColor(Color.parseColor(config.getColorUnSelect()));

        UtilsTheme.changeIcon(context,
                "search", 1, R.drawable.ic_search, ivSearch,
                Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));
    }

    public ViewToolbar getViewToolbar() {
        return viewToolbar;
    }

    public RelativeLayout getRlSearch() {
        return rlSearch;
    }

    public EditText getEtSearch() {
        return etSearch;
    }

    public TextView getTvNoData() {
        return tvNoData;
    }

    public RecyclerView getRcvSearch() {
        return rcvSearch;
    }
}
