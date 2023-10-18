package com.note.mydiary.diarywithlock.journalwithlock.fragment.theme;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewdialog.ViewDialogLoadTheme;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewdialog.ViewDialogTextDiary;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewtheme.ViewPickThemeLock;
import com.note.mydiary.diarywithlock.journalwithlock.adapter.ThemeAdapter;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICheckTouch;
import com.note.mydiary.diarywithlock.journalwithlock.data.DataTheme;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.passcode.LockScreenFragment;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.passcode.PasscodeFragment;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.vip.VipOneFragment;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

import java.io.File;
import java.util.ArrayList;

public class ThemeLockFragment extends Fragment {

    ViewPickThemeLock viewTheme;
    ViewDialogLoadTheme viewDialogLoadTheme;

    private LockScreenFragment lockScreenFragment;
    private ArrayList<ThemeModel> lstTheme;
    private int w;
    private ThemeAdapter themeAdapter;

    public ThemeLockFragment() {
    }

    public static ThemeLockFragment newInstance() {

        Bundle args = new Bundle();

        ThemeLockFragment fragment = new ThemeLockFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewTheme = new ViewPickThemeLock(requireContext());
        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
            viewTheme.getViewToolbar().createTheme(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
            viewTheme.createTheme(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
        }

        w = getResources().getDisplayMetrics().widthPixels;

        lstTheme = new ArrayList<>();
        new Thread(() -> {
            lstTheme = DataTheme.getThemeLock(requireContext());
            if (!lstTheme.isEmpty()) {
                lstTheme.add(0,
                        new ThemeModel("default",
                                "", "file:///android_asset/theme_lock_screen/iv_theme_default.png",
                                "", false,
                                DataLocalManager.getOption(Constant.THEME_LOCK).equals("default")));

                handler.sendEmptyMessage(0);
            } else handler.sendEmptyMessage(1);
        }).start();
    }

    Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 0) {
                themeAdapter.setData(lstTheme);
                viewTheme.getRcv().setVisibility(View.VISIBLE);
                viewTheme.getLlNoInternet().setVisibility(View.GONE);
            } else if (msg.what == 1) {
                viewTheme.getRcv().setVisibility(View.GONE);
                viewTheme.getLlNoInternet().setVisibility(View.VISIBLE);
            }
            return true;
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        init();
        return viewTheme;
    }

    private void init() {
        viewTheme.getViewToolbar().getIvBack().setOnClickListener(v -> Utils.clearBackStack(getParentFragmentManager()));
        themeAdapter = new ThemeAdapter(requireContext(), (o, pos) -> {
            ThemeModel theme = (ThemeModel) o;

            if (theme.getName().equals("default")) {
                Toast.makeText(requireContext(), getString(R.string.done), Toast.LENGTH_SHORT).show();
                DataLocalManager.setOption(theme.getName(), Constant.THEME_LOCK);
                themeAdapter.setSelected(theme);
            } else {
                if (DataLocalManager.getOption(Constant.PASS_QUESTION_ONE).equals("") || DataLocalManager.getOption(Constant.PASS_QUESTION_TWO).equals(""))
                    showDialogQuestionPass(theme, 3);
                else if ((DataLocalManager.getInt(Constant.TYPE_LOCK) == 0 && theme.getName().contains("pincode"))
                        || DataLocalManager.getInt(Constant.TYPE_LOCK) == 2 && theme.getName().contains("pattern"))
                    showDialog(theme);
                else {
                    if (DataLocalManager.getInt(Constant.TYPE_LOCK) == 0 && theme.getName().contains("pattern"))
                        showDialogQuestionPass(theme, 1);
                    else if (DataLocalManager.getInt(Constant.TYPE_LOCK) == 2 && theme.getName().contains("pincode"))
                        showDialogQuestionPass(theme, 0);
                    else showDialogQuestionPass(theme, 2);
                }
            }
        });

        GridLayoutManager manager = new GridLayoutManager(requireContext(), 3);

        viewTheme.getRcv().setLayoutManager(manager);
        viewTheme.getRcv().setAdapter(themeAdapter);
    }

    private void showDialog(ThemeModel theme) {
        viewDialogLoadTheme = new ViewDialogLoadTheme(requireContext());

        AlertDialog dialog = new AlertDialog.Builder(requireContext(), R.style.SheetDialog).create();
        dialog.setView(viewDialogLoadTheme);
        viewDialogLoadTheme.setLayoutParams(new LinearLayout.LayoutParams((int) (83.33f * w / 100), 125 * w / 100));
        dialog.show();
        if (theme.getName().equals("default"))
            Glide.with(requireContext())
                    .load(Uri.parse(theme.getPreview()))
                    .into(viewDialogLoadTheme.getIv());
        else
            Glide.with(requireContext())
                    .load(theme.getPreview())
                    .placeholder(R.drawable.ic_err_theme_lock)
                    .into(viewDialogLoadTheme.getIv());

        if (checkTheme(theme)) {
            viewDialogLoadTheme.getTv().setText(getString(R.string.apply));
            viewDialogLoadTheme.getTv().setBackgroundResource(R.drawable.border_green);
        }

        viewDialogLoadTheme.getTv().setOnClickListener(v -> {
            if (!viewDialogLoadTheme.getTv().getText().equals(getString(R.string.download))) {
                themeAdapter.setSelected(theme);

                if (theme.getName().contains("theme_pincode")) {
                    if (DataLocalManager.getInt(Constant.TYPE_LOCK) != 0) {
                        Toast.makeText(requireContext(), getString(R.string.you_need_set_a_pin_code), Toast.LENGTH_SHORT).show();
                        PasscodeFragment passcodeFragment = PasscodeFragment.newInstance(theme.getName(), (o, p) -> {
                            if (DataLocalManager.getInt(Constant.TYPE_LOCK) == 0 || DataLocalManager.getInt(Constant.TYPE_LOCK) == 2)
                                startDownloadTemplate(requireContext(), theme, isDone -> {
                                    Toast.makeText(requireContext(), getString(R.string.done), Toast.LENGTH_SHORT).show();
                                    DataLocalManager.setOption(theme.getName(), Constant.THEME_LOCK);
                                });
                        }, isTouch -> {

                        }, false);
                        Utils.replaceFragment(getParentFragmentManager(), passcodeFragment, true, true, true);
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.done), Toast.LENGTH_SHORT).show();
                        DataLocalManager.setOption(theme.getName(), Constant.THEME_LOCK);
                    }
                } else if (theme.getName().contains("theme_pattern")) {
                    Log.d(Constant.TAG, "showDialog: " + DataLocalManager.getInt(Constant.TYPE_LOCK));
                    if (DataLocalManager.getInt(Constant.TYPE_LOCK) != 2) {
                        Toast.makeText(requireContext(), getString(R.string.you_need_set_a_pattern), Toast.LENGTH_SHORT).show();
                        PasscodeFragment passcodeFragment = PasscodeFragment.newInstance(theme.getName(), (o, p) -> {
                            if (DataLocalManager.getInt(Constant.TYPE_LOCK) == 0 || DataLocalManager.getInt(Constant.TYPE_LOCK) == 2)
                                startDownloadTemplate(requireContext(), theme, isDone -> {
                                    Toast.makeText(requireContext(), getString(R.string.done), Toast.LENGTH_SHORT).show();
                                    DataLocalManager.setOption(theme.getName(), Constant.THEME_LOCK);
                                });
                        }, isTouch -> {
                        }, false);
                        Utils.replaceFragment(getParentFragmentManager(), passcodeFragment, true, true, true);
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.done), Toast.LENGTH_SHORT).show();
                        DataLocalManager.setOption(theme.getName(), Constant.THEME_LOCK);
                    }
                }

                dialog.cancel();
            } else {
                if (theme.getPrice().equals("vip")) {
                    dialog.cancel();
                    clickVip();
                } else {
                    viewDialogLoadTheme.getCustomSeekbarDownload().setVisibility(View.VISIBLE);
                    viewDialogLoadTheme.getTv().setVisibility(View.GONE);
                    startDownloadTemplate(requireContext(), theme, isDone -> {
                    });
                    dialog.setCancelable(false);
                }
            }
        });
    }

    private void showDialogQuestionPass(ThemeModel theme, int pos) {
        switch (pos) {
            case 0:
                ViewDialogTextDiary viewDialogPincode = new ViewDialogTextDiary(requireContext());

                viewDialogPincode.getTvTitle().setText(getString(R.string.change_theme));
                viewDialogPincode.getTvContent().setText(getString(R.string.current_passcode_is_pattern));
                AlertDialog dialogPincode = new AlertDialog.Builder(requireContext(), R.style.SheetDialog).create();

                dialogPincode.setCancelable(false);
                dialogPincode.setView(viewDialogPincode);
                dialogPincode.show();

                viewDialogPincode.getLayoutParams().width = (int) (84.444f * w / 100);

                viewDialogPincode.getViewYesNo().getTvNo().setOnClickListener(v -> dialogPincode.cancel());

                viewDialogPincode.getViewYesNo().getTvYes().setOnClickListener(v -> {
                    createFragment(theme, Constant.POS_PIN_CODE);
                    dialogPincode.cancel();
                });
                break;
            case 1:
                ViewDialogTextDiary viewDialogPattern = new ViewDialogTextDiary(requireContext());

                viewDialogPattern.getTvTitle().setText(getString(R.string.change_theme));
                viewDialogPattern.getTvContent().setText(getString(R.string.current_passcode_is_pincode));

                AlertDialog dialogPattern = new AlertDialog.Builder(requireContext(), R.style.SheetDialog).create();
                dialogPattern.setCancelable(false);
                dialogPattern.setView(viewDialogPattern);
                dialogPattern.show();

                viewDialogPattern.getLayoutParams().width = (int) (84.444f * w / 100);

                viewDialogPattern.getViewYesNo().getTvNo().setOnClickListener(v -> dialogPattern.cancel());

                viewDialogPattern.getViewYesNo().getTvYes().setOnClickListener(v -> {
                    createFragment(theme, Constant.POS_PATTERN);
                    dialogPattern.cancel();
                });
                break;
            case 2:
                ViewDialogTextDiary viewDialogNone = new ViewDialogTextDiary(requireContext());

                viewDialogNone.getTvTitle().setText(getString(R.string.change_theme));
                viewDialogNone.getTvContent().setText(getString(R.string.current_passcode_is_none));

                AlertDialog dialogNone = new AlertDialog.Builder(requireContext(), R.style.SheetDialog).create();
                dialogNone.setCancelable(false);
                dialogNone.setView(viewDialogNone);
                dialogNone.show();

                viewDialogNone.getLayoutParams().width = (int) (84.444f * w / 100);

                viewDialogNone.getViewYesNo().getTvNo().setOnClickListener(v -> dialogNone.cancel());

                viewDialogNone.getViewYesNo().getTvYes().setOnClickListener(v -> {
                    PasscodeFragment passcodeFragment = PasscodeFragment.newInstance("", (o, p) -> {
                        if (DataLocalManager.getInt(Constant.TYPE_LOCK) == 0 || DataLocalManager.getInt(Constant.TYPE_LOCK) == 2)
                            if (theme.getPrice().equals("vip")) clickVip();
                            else {
                                startDownloadTemplate(requireContext(), theme, isDone -> {
                                    Toast.makeText(requireContext(), getString(R.string.done), Toast.LENGTH_SHORT).show();
                                    DataLocalManager.setOption(theme.getName(), Constant.THEME_LOCK);
                                    themeAdapter.setSelected(theme);
                                });
                            }
                    }, isTouch -> {
                    }, false);
                    Utils.replaceFragment(getParentFragmentManager(), passcodeFragment, true, true, true);
                    dialogNone.cancel();
                });
                break;
            case 3:
                ViewDialogTextDiary viewDialogPass = new ViewDialogTextDiary(requireContext());

                viewDialogPass.getTvTitle().setText(getString(R.string.change_theme));
                viewDialogPass.getTvContent().setText(getString(R.string.no_passcode_setting_do_you_want_to_create_a_passcode));

                AlertDialog dialogPass = new AlertDialog.Builder(requireContext(), R.style.SheetDialog).create();
                dialogPass.setCancelable(false);
                dialogPass.setView(viewDialogPass);
                dialogPass.show();

                viewDialogPass.getLayoutParams().width = (int) (84.444f * w / 100);

                viewDialogPass.getViewYesNo().getTvNo().setOnClickListener(v -> dialogPass.cancel());

                viewDialogPass.getViewYesNo().getTvYes().setOnClickListener(v -> {
                    PasscodeFragment passcodeFragment = PasscodeFragment.newInstance("", (o, p) -> {
                        if (DataLocalManager.getInt(Constant.TYPE_LOCK) == 0 || DataLocalManager.getInt(Constant.TYPE_LOCK) == 2)
                            if (theme.getPrice().equals("vip")) clickVip();
                            else {
                                startDownloadTemplate(requireContext(), theme, isDone -> {
                                    Toast.makeText(requireContext(), getString(R.string.done), Toast.LENGTH_SHORT).show();
                                    DataLocalManager.setOption(theme.getName(), Constant.THEME_LOCK);
                                    themeAdapter.setSelected(theme);
                                });
                            }
                    }, isTouch -> {
                    }, false);
                    Utils.replaceFragment(getParentFragmentManager(), passcodeFragment, true, true, true);
                    dialogPass.cancel();
                });
                break;
        }
    }

    private void createFragment(ThemeModel theme, int positionView) {
        lockScreenFragment = LockScreenFragment.newInstance((o, pos) -> {
            if (DataLocalManager.getInt(Constant.TYPE_LOCK) == 0 || DataLocalManager.getInt(Constant.TYPE_LOCK) == 2)
                if (theme.getPrice().equals("vip")) clickVip();
                else {
                    startDownloadTemplate(requireContext(), theme, isDone -> {
                        Toast.makeText(requireContext(), getString(R.string.done), Toast.LENGTH_SHORT).show();
                        DataLocalManager.setOption(theme.getName(), Constant.THEME_LOCK);
                        themeAdapter.setSelected(theme);
                    });
                }

//            Utils.clearBackStack(getParentFragmentManager());
        }, positionView, false, isExitCheck -> {
            lockScreenFragment.popBackStack();
        });
        Utils.replaceFragment(getParentFragmentManager(), lockScreenFragment, false, true, true);
    }

    private boolean checkTheme(ThemeModel theme) {
        if (theme.getName().equals("default")) return true;
        File file = new File(Utils.getStore(requireContext()) + "/" + Constant.THEME_PASSCODE_FOLDER + theme.getName().substring(0, 13) + "/" + theme.getName());

        return file.exists();
    }

    private void startDownloadTemplate(Context context, ThemeModel themeModel, ICheckTouch isDone) {
        //to upper case first letter: story->Story
        String path = Utils.getStore(context) + "/" + Constant.THEME_PASSCODE_FOLDER + themeModel.getName().substring(0, 13);
        File directory = new File(path);
        if (!directory.exists()) directory.mkdirs();

        FileDownloadService.DownloadRequest downloadRequest =
                new FileDownloadService.DownloadRequest(themeModel.getUrl(), path + "/" + themeModel.getName() + "1" + ".zip");

        downloadRequest.setRequiresUnzip(true);
        downloadRequest.setDeleteZipAfterExtract(true);
        downloadRequest.setUnzipAtFilePath(path);
        FileDownloadService.OnDownloadStatusListener listener = new FileDownloadService.OnDownloadStatusListener() {
            public void onDownloadStarted() {

            }

            @Override
            public void onDownloadCompleted() {
                themeModel.setOnl(false);

                isDone.checkTouch(true);
                if (viewDialogLoadTheme != null) {
                    viewDialogLoadTheme.getCustomSeekbarDownload().setVisibility(View.GONE);
                    viewDialogLoadTheme.getTv().setText(getString(R.string.apply));
                    viewDialogLoadTheme.getTv().setBackgroundResource(R.drawable.border_green);
                    viewDialogLoadTheme.getTv().setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onDownloadFailed() {
                Toast.makeText(context, getString(R.string.download_failed), Toast.LENGTH_SHORT).show();

                if (viewDialogLoadTheme != null) {
                    viewDialogLoadTheme.getCustomSeekbarDownload().setVisibility(View.GONE);
                    viewDialogLoadTheme.getTv().setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onDownloadProgress(int progress) {
                if (viewDialogLoadTheme != null)
                    viewDialogLoadTheme.getCustomSeekbarDownload().setProgress(progress);
            }
        };
        FileDownloadService.FileDownloader downloader = FileDownloadService.FileDownloader.getInstance(downloadRequest, listener);
        downloader.download(context);
    }

    private void clickVip() {
        Toast.makeText(requireContext(), getString(R.string.you_need_upgrade_to_vip), Toast.LENGTH_SHORT).show();
        VipOneFragment vipOneFragment = VipOneFragment.newInstance();
        Utils.replaceFragment(getParentFragmentManager(), vipOneFragment, true, true, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (themeAdapter != null) themeAdapter.setData(lstTheme);
    }
}
