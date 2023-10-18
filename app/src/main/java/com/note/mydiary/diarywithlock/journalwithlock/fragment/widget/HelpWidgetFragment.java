package com.note.mydiary.diarywithlock.journalwithlock.fragment.widget;

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

public class HelpWidgetFragment extends Fragment {

    public static String IMAGE = "image";

    private int pic;

    public HelpWidgetFragment() {
    }

    public static HelpWidgetFragment newInstance(int pic) {
        HelpWidgetFragment fragment = new HelpWidgetFragment();
        Bundle args = new Bundle();
        args.putInt(IMAGE, pic);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) pic = getArguments().getInt(IMAGE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_help_widget, container, false);
        ImageView ivPic = v.findViewById(R.id.ivPic);

        Glide.with(requireContext())
                .load(pic)
                .into(ivPic);

        return v;
    }
}
