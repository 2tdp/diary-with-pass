package com.note.mydiary.diarywithlock.journalwithlock.fragment.theme;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewtheme.ViewPickThemeApp;
import com.note.mydiary.diarywithlock.journalwithlock.adapter.ViewPagerAddFragmentsAdapter;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICheckTouch;
import com.note.mydiary.diarywithlock.journalwithlock.data.DataTheme;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.vip.VipOneFragment;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

import java.io.File;
import java.util.ArrayList;

public class ThemeAppFragment extends Fragment {

    ViewPickThemeApp viewPickThemeApp;

    private final ICheckTouch isReset;
    private ViewPagerAddFragmentsAdapter viewPagerAdapter;
    private ArrayList<ThemeModel> lstTheme;
    private int w;

    public ThemeAppFragment(ICheckTouch isReset) {
        this.isReset = isReset;
    }

    public static ThemeAppFragment newInstance(ICheckTouch isReset) {

        Bundle args = new Bundle();

        ThemeAppFragment fragment = new ThemeAppFragment(isReset);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPickThemeApp = new ViewPickThemeApp(requireContext());
        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
            viewPickThemeApp.getViewToolbar().createTheme(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
            viewPickThemeApp.createTheme(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
        }

        w = getResources().getDisplayMetrics().widthPixels;

        lstTheme = new ArrayList<>();

        setUpViewPager();

        new Thread(() -> {
            lstTheme = DataTheme.getThemeApp(requireContext());
            if (!lstTheme.isEmpty()) {
                lstTheme.add(0,
                        new ThemeModel("default",
                                "", "file:///android_asset/theme_app/iv_theme_default.png",
                                "", false,
                                DataLocalManager.getOption(Constant.THEME_APP).equals("")));
                lstTheme.get(0).setColor("#FFAF7D");

                handler.sendEmptyMessage(0);
            } else handler.sendEmptyMessage(1);
        }).start();
    }

    Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 0) {
                viewPickThemeApp.getLl().setVisibility(View.VISIBLE);
                viewPickThemeApp.getViewPager2().setVisibility(View.VISIBLE);
                viewPickThemeApp.getLlNoInternet().setVisibility(View.GONE);

                if (!lstTheme.isEmpty()) {
                    for (ThemeModel theme : lstTheme) {
                        ItemThemeAppFragment itemTheme = ItemThemeAppFragment.newInstance(theme.getPreview());
                        viewPagerAdapter.addFrag(itemTheme);
                    }

                    viewPickThemeApp.getViewPager2().setOffscreenPageLimit(3);
                    viewPickThemeApp.getViewPager2().setAdapter(viewPagerAdapter);
                    viewPickThemeApp.getViewPager2().setPageTransformer((page, position) -> {
                        float pageOffset = w / 10f;
                        float pageMargin = w / 10f;
                        float myOffset = position * -(2 * pageOffset + pageMargin);

                        if (ViewCompat.getLayoutDirection(viewPickThemeApp.getViewPager2()) == ViewCompat.LAYOUT_DIRECTION_RTL)
                            page.setTranslationX(-myOffset);
                        else page.setTranslationX(myOffset);
                        page.setScaleY(1 - (0.15f * Math.abs(position)));
                    });
                }
            } else if (msg.what == 1) {
                viewPickThemeApp.getLl().setVisibility(View.GONE);
                viewPickThemeApp.getViewPager2().setVisibility(View.GONE);
                viewPickThemeApp.getLlNoInternet().setVisibility(View.VISIBLE);
            }
            return true;
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        evenClick();
        return viewPickThemeApp;
    }

    private void evenClick() {
        viewPickThemeApp.getViewToolbar().getIvBack().setOnClickListener(v -> getParentFragmentManager().popBackStack(ThemeAppFragment.class.getSimpleName(), 1));
        viewPickThemeApp.getLl().setOnClickListener(v -> {
            ThemeModel theme = lstTheme.get(viewPickThemeApp.getViewPager2().getCurrentItem());
            if (checkThemeApp(requireContext(), theme.getName())) {
                DataLocalManager.setOption(theme.getName(), Constant.THEME_APP);
                isReset.checkTouch(true);
            } else {
                if (theme.getPrice().equals("vip")) clickVip();
                else
                    startDownloadTemplate(requireContext(), theme, isDone -> {
                        viewPickThemeApp.getIvUpgrade().setVisibility(View.GONE);
                        viewPickThemeApp.getTvClick().setText(getString(R.string.apply));
                    });
            }
        });
    }

    private final ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            ThemeModel theme = lstTheme.get(position);
            if (position == 0 || checkThemeApp(requireContext(), theme.getName())) {
                viewPickThemeApp.getIvUpgrade().setVisibility(View.GONE);
                viewPickThemeApp.getTvClick().setText(getString(R.string.apply));
            } else {
                if (theme.getPrice().equals("vip")) {
                    viewPickThemeApp.getIvUpgrade().setVisibility(View.VISIBLE);
                    viewPickThemeApp.getTvClick().setText(getString(R.string.upgrade_premium));
                } else if (theme.getPrice().equals("free")) {
                    viewPickThemeApp.getIvUpgrade().setVisibility(View.GONE);
                    viewPickThemeApp.getTvClick().setText(getString(R.string.download));
                }
            }
            if (theme.getColor() != null)
                viewPickThemeApp.getLl().setBackground(
                        Utils.createBackground(new int[]{Color.parseColor(theme.getColor())},
                                (int) (2.5f * w / 100), -1, -1));
        }
    };

    private void setUpViewPager() {
        viewPagerAdapter = new ViewPagerAddFragmentsAdapter(getParentFragmentManager(), getLifecycle());

        viewPickThemeApp.getViewPager2().unregisterOnPageChangeCallback(onPageChangeCallback);
        viewPickThemeApp.getViewPager2().registerOnPageChangeCallback(onPageChangeCallback);
    }

    private void startDownloadTemplate(Context context, ThemeModel themeModel, ICheckTouch isDone) {
        //to upper case first letter: story->Story
        String path = Utils.getStore(context) + "/" + Constant.THEME_APP_FOLDER + themeModel.getName().substring(0, 9);
        File directory = new File(path);
        if (!directory.exists()) directory.mkdirs();

        FileDownloadService.DownloadRequest downloadRequest =
                new FileDownloadService.DownloadRequest(themeModel.getUrl(), path + "/" + themeModel.getName() + "1" + ".zip");

        downloadRequest.setRequiresUnzip(true);
        downloadRequest.setDeleteZipAfterExtract(true);
        downloadRequest.setUnzipAtFilePath(path);
        FileDownloadService.OnDownloadStatusListener listener = new FileDownloadService.OnDownloadStatusListener() {
            public void onDownloadStarted() {
                Toast.makeText(context, getString(R.string.downloading), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadCompleted() {
                themeModel.setOnl(false);
                Toast.makeText(context, getString(R.string.done), Toast.LENGTH_SHORT).show();
                isDone.checkTouch(true);
            }

            @Override
            public void onDownloadFailed() {
                Toast.makeText(context, getString(R.string.download_failed), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadProgress(int progress) {

            }
        };
        FileDownloadService.FileDownloader downloader = FileDownloadService.FileDownloader.getInstance(downloadRequest, listener);
        downloader.download(context);
    }

    private void clickVip() {
        VipOneFragment vipOneFragment = VipOneFragment.newInstance();
        Utils.replaceFragment(getParentFragmentManager(), vipOneFragment, true, true, true);
    }

    private boolean checkThemeApp(Context context, String nameTheme) {
        if (nameTheme.equals("default")) return true;
        File file = new File(Utils.getStore(context) + "/" + Constant.THEME_APP_FOLDER + nameTheme.substring(0, 9) + "/" + nameTheme);

        return file.exists();
    }
}
