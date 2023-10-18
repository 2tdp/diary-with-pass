package com.note.mydiary.diarywithlock.journalwithlock.fragment.cropcover;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewcutcover.ViewCutCover;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsBitmap;

import java.io.IOException;

public class CropCoverFragment extends Fragment {

    public static String URI_IMAGE = "uriImage";

    ViewCutCover viewCutCover;

    private String uriImage;

    public CropCoverFragment() {
    }

    public static CropCoverFragment newInstance(String uri) {
        CropCoverFragment fragment = new CropCoverFragment();
        Bundle args = new Bundle();
        args.putString(URI_IMAGE, uri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) uriImage = getArguments().getString(URI_IMAGE);
        viewCutCover = new ViewCutCover(requireContext());


        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
            String jsonConfig = Utils.readFromFile(requireContext(), "theme/theme_app/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP) + "/config.txt");

            ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);

            if (config == null) return;

            viewCutCover.getViewToolbar().createTheme(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
            viewCutCover.setBackgroundColor(Color.parseColor(config.getColorBackground()));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        init();

        return viewCutCover;
    }

    private void init() {
        viewCutCover.getViewToolbar().getIvBack().setOnClickListener(v -> getParentFragmentManager().popBackStack());
        try {
            Bitmap bitmap = UtilsBitmap.modifyOrientation(requireContext(), UtilsBitmap.getBitmapFromUri(requireContext(), Uri.parse(uriImage)), Uri.parse(uriImage));
            if (bitmap != null) viewCutCover.getCropRatioView().setData(bitmap);
            else {
                getParentFragmentManager().popBackStack();
                Toast.makeText(requireContext(), getString(R.string.error_crop), Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        viewCutCover.getTv().setOnClickListener(v -> saveBitmap());
    }

    private void saveBitmap() {
        Bitmap bitmap = viewCutCover.getCropRatioView().getCroppedImage();
        if (bitmap != null) {
            DataLocalManager.setOption(UtilsBitmap.saveBitmapToApp(requireContext(),
                    bitmap, Constant.FOLDER_COVER_IMAGE, Constant.NAME_COVER_IMAGE), Constant.NAME_COVER_IMAGE);
            DataLocalManager.setCheck(Constant.IS_COVER, true);
            Utils.clearBackStack(getParentFragmentManager());
        }
    }
}
