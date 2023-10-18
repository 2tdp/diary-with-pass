package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewshowall;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.ViewContent;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsTheme;

public class ViewItemShowAll extends RelativeLayout {

    static int w;
    ImageView ivTick;
    ViewContent viewContent;
    LayoutParams paramsContent;

    @SuppressLint("ResourceType")
    public ViewItemShowAll(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;

        ivTick = new ImageView(context);
        ivTick.setVisibility(GONE);
        ivTick.setId(1337);
        ivTick.setImageResource(R.drawable.ic_un_tick_del);
        LayoutParams paramsImage = new LayoutParams((int) (6.667f * w / 100), (int) (6.667f * w / 100));
        paramsImage.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

        addView(ivTick, paramsImage);

        viewContent = new ViewContent(context);
        paramsContent = new LayoutParams(-1, -2);
        paramsContent.addRule(RIGHT_OF, ivTick.getId());
        paramsContent.setMargins((int) (5.556f * w / 100), 0, (int) (5.556f * w / 100), (int) (5.556f * w / 100));
        addView(viewContent, paramsContent);
    }

    public void setMarginsViewContent(boolean isMultiDel) {
        if (isMultiDel)
            paramsContent.setMargins((int) (4.44f * w / 100), 0, 0, (int) (5.556f * w / 100));
        else
            paramsContent.setMargins((int) (5.556f * w / 100), 0, (int) (5.556f * w / 100), (int) (5.556f * w / 100));
    }

    public void createTheme(Context context, String nameTheme) {
        if (nameTheme.equals("default")) return;

        String jsonConfig = Utils.readFromFile(context, "theme/theme_app/" + nameTheme + "/config.txt");

        ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);

        if (config == null) return;

        UtilsTheme.changeIconSelectDiary(context, ivTick,
                Color.parseColor(config.getColorMain()),
                Color.parseColor(config.getColorMain()),
                Color.parseColor(config.getColorIconInColor()),
                false);
    }

    public ImageView getIvTick() {
        return ivTick;
    }

    public ViewContent getViewContent() {
        return viewContent;
    }
}
