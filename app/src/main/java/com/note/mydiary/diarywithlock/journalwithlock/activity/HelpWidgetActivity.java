package com.note.mydiary.diarywithlock.journalwithlock.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager2.widget.ViewPager2;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.base.BaseActivity;
import com.note.mydiary.diarywithlock.journalwithlock.adapter.ViewPagerAddFragmentsAdapter;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.widget.HelpWidgetFragment;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class HelpWidgetActivity extends BaseActivity {

    private ViewPager2 viewPager2;
    private DotsIndicator indicator;
    private ImageView ivBack;
    private TextView tvStep, tvDesHelp;

    private final ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                tvStep.setText("Step " + 1);
                tvDesHelp.setText(R.string.step_1);
            } else if (position == 1) {
                tvStep.setText("Step " + 2);
                tvDesHelp.setText(R.string.step_2);
            } else {
                tvStep.setText("Step " + 3);
                tvDesHelp.setText(R.string.step_3);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_widget_activity);

        init();
    }

    private void init() {
        ivBack = findViewById(R.id.ivBack);
        viewPager2 = findViewById(R.id.vpSplash);
        indicator = findViewById(R.id.dots_indicator);
        tvStep = findViewById(R.id.tvStep);
        tvDesHelp = findViewById(R.id.tvDesHelp);

        ivBack.setOnClickListener(v -> finish());

        setUpViewPager();
    }

    private void setUpViewPager() {
        ViewPagerAddFragmentsAdapter viewPagerAdapter = new ViewPagerAddFragmentsAdapter(getSupportFragmentManager(), getLifecycle());

        HelpWidgetFragment step1 = HelpWidgetFragment.newInstance(R.drawable.im_help_1);
        HelpWidgetFragment step2 = HelpWidgetFragment.newInstance(R.drawable.im_help_2);
        HelpWidgetFragment step3 = HelpWidgetFragment.newInstance(R.drawable.im_help_3);

        viewPagerAdapter.addFrag(step1);
        viewPagerAdapter.addFrag(step2);
        viewPagerAdapter.addFrag(step3);

        viewPager2.unregisterOnPageChangeCallback(onPageChangeCallback);
        viewPager2.registerOnPageChangeCallback(onPageChangeCallback);

        viewPager2.setAdapter(viewPagerAdapter);
        indicator.setViewPager2(viewPager2);
    }
}
