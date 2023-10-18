package com.note.mydiary.diarywithlock.journalwithlock.activity.onboarding;

import android.os.Bundle;
import android.widget.TextView;

import androidx.viewpager2.widget.ViewPager2;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.RequestPermissionActivity;
import com.note.mydiary.diarywithlock.journalwithlock.activity.base.BaseActivity;
import com.note.mydiary.diarywithlock.journalwithlock.adapter.ViewPagerAddFragmentsAdapter;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.onboarding.OnBoardingFragment;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class OnBoardingActivity extends BaseActivity {

    private ViewPager2 viewPager2;
    private DotsIndicator indicator;
    private TextView tvSplash;

    private final ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            if (position == 1)
                tvSplash.setText(getResources().getString(R.string.splash_get_start));
            else tvSplash.setText(getResources().getString(R.string.splash_continue));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        if (DataLocalManager.getFirstInstall("first"))
            setIntent(RequestPermissionActivity.class.getName(), true);

        init();
    }

    private void init() {
        viewPager2 = findViewById(R.id.vpSplash);
        indicator = findViewById(R.id.dots_indicator);
        tvSplash = findViewById(R.id.tvSplash);

        setUpViewPager();

//        DataLocalManager.setOption("#FFAF7D", Constant.COLOR_MAIN);
//        checkTheme();

        tvSplash.setOnClickListener(view -> {
            if (viewPager2.getCurrentItem() == 1) {
                DataLocalManager.setFirstInstall("first", true);
                setIntent(RequestPermissionActivity.class.getName(), true);
            } else viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1, true);
        });


    }

    private void setUpViewPager() {
        ViewPagerAddFragmentsAdapter viewPagerAdapter = new ViewPagerAddFragmentsAdapter(getSupportFragmentManager(), getLifecycle());

        OnBoardingFragment onBoardingOneFragment = OnBoardingFragment.newInstance(R.layout.fragment_on_boarding_one);
        OnBoardingFragment onBoardingTwoFragment = OnBoardingFragment.newInstance(R.layout.fragment_on_boarding_two);

        viewPagerAdapter.addFrag(onBoardingOneFragment);
        viewPagerAdapter.addFrag(onBoardingTwoFragment);

        viewPager2.unregisterOnPageChangeCallback(onPageChangeCallback);
        viewPager2.registerOnPageChangeCallback(onPageChangeCallback);

        viewPager2.setAdapter(viewPagerAdapter);
        indicator.setViewPager2(viewPager2);
    }
}