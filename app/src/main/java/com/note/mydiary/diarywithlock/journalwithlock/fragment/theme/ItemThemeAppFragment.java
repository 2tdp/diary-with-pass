package com.note.mydiary.diarywithlock.journalwithlock.fragment.theme;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.note.mydiary.diarywithlock.journalwithlock.R;

public class ItemThemeAppFragment extends Fragment {

    private static final String URI_THEME = "uriTheme";

    ImageView imageView;

    private String uriTheme;

    public ItemThemeAppFragment() {
    }

    public static ItemThemeAppFragment newInstance(String uriTheme) {

        Bundle args = new Bundle();

        ItemThemeAppFragment fragment = new ItemThemeAppFragment();
        args.putString(URI_THEME, uriTheme);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageView = new ImageView(requireContext());

        if (getArguments() != null) uriTheme = getArguments().getString(URI_THEME);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Glide.with(requireContext())
                .load(uriTheme)
                .placeholder(R.drawable.ic_err_theme_app)
                .into(imageView);

        return imageView;
    }
}
