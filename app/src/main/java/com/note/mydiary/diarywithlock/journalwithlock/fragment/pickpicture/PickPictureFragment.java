package com.note.mydiary.diarywithlock.journalwithlock.fragment.pickpicture;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewpickpicture.ViewPickPictureFragment;
import com.note.mydiary.diarywithlock.journalwithlock.adapter.BucketAdapter;
import com.note.mydiary.diarywithlock.journalwithlock.adapter.DetailPictureAdapter;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICallBackItem;
import com.note.mydiary.diarywithlock.journalwithlock.data.DataPic;
import com.note.mydiary.diarywithlock.journalwithlock.model.picture.BucketPicModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

import java.util.ArrayList;

public class PickPictureFragment extends Fragment {

    private static final String IS_COVER = "isCover";

    ViewPickPictureFragment viewPickPictureFragment;
    DetailPictureAdapter detailPictureAdapter;

    private BucketAdapter bucketAdapter;
    private final ICallBackItem callBack;

    private boolean isCover;
    private Animation animation;

    public PickPictureFragment(ICallBackItem callBack) {
        this.callBack = callBack;
    }

    public static PickPictureFragment newInstance(boolean isCover, ICallBackItem callBack) {
        PickPictureFragment pictureFragment = new PickPictureFragment(callBack);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_COVER, isCover);
        pictureFragment.setArguments(bundle);
        return pictureFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) isCover = getArguments().getBoolean(IS_COVER);

        viewPickPictureFragment = new ViewPickPictureFragment(requireContext());
        viewPickPictureFragment.getRcvPicture().setVisibility(View.GONE);

        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
            viewPickPictureFragment.getViewToolbar().createTheme(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
            viewPickPictureFragment.createThemeApp(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
        }

        if (DataLocalManager.getListBucket(Constant.LIST_ALL_PIC).isEmpty()) {
            new Thread(() -> {
                DataPic.getBucketPictureList(requireContext());
                handler.sendEmptyMessage(0);
            }).start();
        } else handler.sendEmptyMessage(0);
    }

    Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 0) {
                ArrayList<BucketPicModel> lstBucket = DataLocalManager.getListBucket(Constant.LIST_ALL_PIC);

                if (!lstBucket.isEmpty()) bucketAdapter.setData(lstBucket);

                if (isCover)
                    if (DataLocalManager.getCheck(Constant.IS_COVER)) {
                        viewPickPictureFragment.getSwitchCompat().setChecked(true);
                        onOffCover(1);
                    } else {
                        viewPickPictureFragment.getSwitchCompat().setChecked(false);
                        onOffCover(0);
                    }
            }
            return true;
        }
    });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init();
        evenClick();

        return viewPickPictureFragment;
    }

    private void init() {
        if (isCover)
            viewPickPictureFragment.getRlSwitch().setVisibility(View.VISIBLE);
        else {
            viewPickPictureFragment.getRlSwitch().setVisibility(View.GONE);
            viewPickPictureFragment.getRcvBucket().setVisibility(View.VISIBLE);
        }

        bucketAdapter = new BucketAdapter(requireContext(), (o, pos) -> {
            BucketPicModel bucketPicModel = (BucketPicModel) o;

            switchLayoutPicture(1);

            detailPictureAdapter = new DetailPictureAdapter(requireContext(), false, isCover, (ob, position) -> {
                callBack.callBackItem(ob, pos);
                if (!isCover)
                    Utils.clearBackStack(requireActivity().getSupportFragmentManager());
            });

            detailPictureAdapter.setData(bucketPicModel.getLstPic());
            GridLayoutManager manager = new GridLayoutManager(requireContext(), 4);
            viewPickPictureFragment.getRcvPicture().setLayoutManager(manager);
            viewPickPictureFragment.getRcvPicture().setAdapter(detailPictureAdapter);
        });

        LinearLayoutManager manager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        viewPickPictureFragment.getRcvBucket().setLayoutManager(manager);
        viewPickPictureFragment.getRcvBucket().setAdapter(bucketAdapter);
    }

    private void evenClick() {
        viewPickPictureFragment.getViewToolbar().getIvBack().setOnClickListener(v -> {
            if (viewPickPictureFragment.getRcvPicture().getVisibility() == View.VISIBLE)
                switchLayoutPicture(0);
            else Utils.clearBackStack(requireActivity().getSupportFragmentManager());
        });

        viewPickPictureFragment.getViewToolbar().getIvVip().setOnClickListener(v -> {
            callBack.callBackItem(detailPictureAdapter.getSelected(), -1);
            Utils.clearBackStack(requireActivity().getSupportFragmentManager());
        });

        viewPickPictureFragment.getSwitchCompat().setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) onOffCover(1);
            else onOffCover(0);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewPickPictureFragment != null) switchLayoutPicture(0);
    }

    public void onOffCover(int position) {
        switch (position) {
            case 0:
                viewPickPictureFragment.getSwitchCompat().setChecked(false);
                viewPickPictureFragment.getRcvBucket().setVisibility(View.GONE);
                viewPickPictureFragment.getRcvPicture().setVisibility(View.GONE);

                DataLocalManager.setCheck(Constant.IS_COVER, false);
                break;
            case 1:
                viewPickPictureFragment.getSwitchCompat().setChecked(true);
                viewPickPictureFragment.getRcvBucket().setVisibility(View.VISIBLE);

                DataLocalManager.setCheck(Constant.IS_COVER, true);
                break;
        }
    }

    public void switchLayoutPicture(int pos) {
        switch (pos) {
            case 0:
                //gone rcv Picture
                animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_right);
                viewPickPictureFragment.getRcvPicture().setAnimation(animation);
                viewPickPictureFragment.getRcvPicture().setVisibility(View.GONE);
                viewPickPictureFragment.getViewToolbar().getIvVip().setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_left_small);
                viewPickPictureFragment.getRcvBucket().setAnimation(animation);
                viewPickPictureFragment.getRcvBucket().setVisibility(View.VISIBLE);
                break;
            case 1:
                //gone rcv Bucket
                animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_left);
                viewPickPictureFragment.getRcvBucket().setAnimation(animation);
                viewPickPictureFragment.getRcvBucket().setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right);
                viewPickPictureFragment.getRcvPicture().setAnimation(animation);
                viewPickPictureFragment.getRcvPicture().setVisibility(View.VISIBLE);
                if (!isCover)
                    viewPickPictureFragment.getViewToolbar().getIvVip().setVisibility(View.VISIBLE);
                break;
        }

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                viewPickPictureFragment.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public ViewPickPictureFragment getViewPickPictureFragment() {
        return viewPickPictureFragment;
    }

    public void popBackStack() {
        getParentFragmentManager().popBackStack();
    }
}