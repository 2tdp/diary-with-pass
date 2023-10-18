package com.note.mydiary.diarywithlock.journalwithlock.fragment.pickpicture;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewpickpicture.ViewPreviewPicture;

public class PreviewPictureFragment extends Fragment {

    private static final String URI_IMAGE = "uri_image";
    private static final String ARR_BYTE_IMAGE = "arr_byte_image";

    ViewPreviewPicture viewPreviewPicture;

    private byte[] arrImage;
    private String uriImage;

    public PreviewPictureFragment() {
    }

    public static PreviewPictureFragment newInstance(byte[] arrImage, String uriImage) {
        Bundle args = new Bundle();
        PreviewPictureFragment fragment = new PreviewPictureFragment();
        if (arrImage != null) args.putByteArray(ARR_BYTE_IMAGE, arrImage);
        if (!uriImage.equals("")) args.putString(URI_IMAGE, uriImage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            arrImage = getArguments().getByteArray(ARR_BYTE_IMAGE);
            uriImage = getArguments().getString(URI_IMAGE);
        }
        viewPreviewPicture = new ViewPreviewPicture(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        init();
        return viewPreviewPicture;
    }

    private void init() {
        viewPreviewPicture.getViewToolbar().getIvBack().setOnClickListener(v -> getParentFragmentManager().popBackStack(PreviewPictureFragment.class.getSimpleName(), 1));
        if (uriImage != null && !uriImage.equals("")) {
            Glide.with(requireContext())
                    .load(Uri.parse(uriImage))
                    .into(viewPreviewPicture.getImageView());
        } else if (arrImage != null) {
            Glide.with(requireContext())
                    .load(arrImage)
                    .into(viewPreviewPicture.getImageView());
        }
    }
}
