package com.note.mydiary.diarywithlock.journalwithlock.fragment.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class OnBoardingFragment extends Fragment {

    public static String ID_RESOURCE = "resource";

    private int idResource;

    public OnBoardingFragment() {
    }

    public static OnBoardingFragment newInstance(int idResource) {
        OnBoardingFragment onBoardingFragment = new OnBoardingFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ID_RESOURCE, idResource);
        onBoardingFragment.setArguments(bundle);
        return onBoardingFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) idResource = getArguments().getInt(ID_RESOURCE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(idResource, container, false);
    }
}