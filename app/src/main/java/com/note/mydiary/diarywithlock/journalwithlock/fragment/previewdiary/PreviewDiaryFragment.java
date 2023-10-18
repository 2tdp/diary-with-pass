package com.note.mydiary.diarywithlock.journalwithlock.fragment.previewdiary;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.adddiary.AddDiary;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewdialog.ViewDialogTextDiary;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewpreview.ViewPreviewDiary;
import com.note.mydiary.diarywithlock.journalwithlock.activity.database.DatabaseRealm;
import com.note.mydiary.diarywithlock.journalwithlock.adapter.ContentPreviewDiaryAdapter;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICheckTouch;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.pickpicture.PreviewPictureFragment;
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.DiaryModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.picture.PicModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.remiads.ads.FullManager;

import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PreviewDiaryFragment extends Fragment {

    public static String ID_DIARY = "idDiary";

    private ViewPreviewDiary viewPreviewDiary;
    private final ICheckTouch isDel, resetCalendar;

    private DiaryModel diary;
    private boolean isCallBack, isPlayAudio;
    private int idDiary = -1, w, oldPosition;

    private MediaPlayer mediaPlayer;

    public PreviewDiaryFragment(ICheckTouch isDel, ICheckTouch resetCalendar) {
        this.isDel = isDel;
        this.resetCalendar = resetCalendar;
    }

    public static PreviewDiaryFragment newInstance(int idDiary, ICheckTouch isDel, ICheckTouch resetCalendar) {
        PreviewDiaryFragment previewDiaryFragment = new PreviewDiaryFragment(isDel, resetCalendar);
        Bundle bundle = new Bundle();
        bundle.putInt(ID_DIARY, idDiary);
        previewDiaryFragment.setArguments(bundle);
        return previewDiaryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        w = getResources().getDisplayMetrics().widthPixels;
        if (getArguments() != null) idDiary = getArguments().getInt(ID_DIARY);

        viewPreviewDiary = new ViewPreviewDiary(requireContext());
        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
            viewPreviewDiary.createTheme(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
            viewPreviewDiary.getViewToolbar().createTheme(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        init();
        return viewPreviewDiary;
    }

    @SuppressLint("SimpleDateFormat")
    private void init() {
        ContentPreviewDiaryAdapter contentPreviewDiaryAdapter = new ContentPreviewDiaryAdapter(requireContext(),
                (ob, p) -> {
                    PreviewPictureFragment previewPictureFragment = PreviewPictureFragment.newInstance(((PicModel) ob).getArrPic(), "");
                    Utils.replaceFragment(getParentFragmentManager(), previewPictureFragment, true, true, true);
                },
                (o, position, isDone) -> {

                    FileDescriptor fileDescriptor = Utils.createFileDescriptorFromByteArray(requireContext(), (byte[]) o);
                    if (fileDescriptor == null) {
                        Toast.makeText(requireContext(), getResources().getString(R.string.cant_play_audio), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (isPlayAudio) {
                        handStop();
                        if (oldPosition != position) handPlay(fileDescriptor, isDone);
                        else isDone.checkTouch(true);
                    } else handPlay(fileDescriptor, isDone);

                    oldPosition = position;
                });

        if (idDiary != -1)
            DatabaseRealm.getOtherDiary(idDiary, (o, pos) -> {
                diary = (DiaryModel) o;
                viewPreviewDiary.getTvDateTime().setText(new SimpleDateFormat(Constant.FULL_DATE_DAY, Locale.ENGLISH).format(new Date(diary.getDateTimeStamp())));
                viewPreviewDiary.getTvTitle().setText(diary.getTitleDiary());
                contentPreviewDiaryAdapter.setData(diary.getLstContent());
            });

        viewPreviewDiary.getRcvDiary().setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        viewPreviewDiary.getRcvDiary().setAdapter(contentPreviewDiaryAdapter);

        evenClick();
    }

    private void evenClick() {
        viewPreviewDiary.getViewToolbar().getIvBack().setOnClickListener(v -> {
            getParentFragmentManager().popBackStack(this.getClass().getSimpleName(), 1);
            resetCalendar.checkTouch(true);
        });
        viewPreviewDiary.getRlEdit().setOnClickListener(v -> {
            if (getActivity() != null){
                FullManager.getInstance().showAds(getActivity(), this::editNote);
            }else {
                editNote();
            }

        });
        viewPreviewDiary.getViewToolbar().getIvVip().setOnClickListener(v -> {

            ViewDialogTextDiary viewDialogTextDiary = new ViewDialogTextDiary(requireContext());
            viewDialogTextDiary.getTvTitle().setText(requireContext().getResources().getString(R.string.del_diary));
            viewDialogTextDiary.getTvContent().setText(requireContext().getResources().getString(R.string.are_you_sure_want_to_delete_this_diary));

            AlertDialog dialog = new AlertDialog.Builder(requireContext(), R.style.SheetDialog).create();
            dialog.setView(viewDialogTextDiary);
            dialog.setCancelable(false);
            dialog.show();
            viewDialogTextDiary.getLayoutParams().width = (int) (84.998f * w / 100);

            viewDialogTextDiary.getViewYesNo().getTvNo().setOnClickListener(vNo -> dialog.cancel());
            viewDialogTextDiary.getViewYesNo().getTvYes().setOnClickListener(vYes -> {
                DatabaseRealm.delOtherDiary(diary.getId(), isDone -> isDel.checkTouch(true), resetCalendar);
                dialog.cancel();
                getParentFragmentManager().popBackStack();
            });
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isCallBack) init();
    }

    public void handStop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        mediaPlayer = null;
        isPlayAudio = false;
    }

    private void handPlay(FileDescriptor fileDescriptor, ICheckTouch isDone) {

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(fileDescriptor);

            mediaPlayer.setOnCompletionListener(mediaPlayer -> {
                Log.d("2tdp", "setOnCompletionListener: ");
                handStop();
                isDone.checkTouch(true);
            });

            mediaPlayer.setOnPreparedListener(mp -> {
                isPlayAudio = true;
                mediaPlayer.start();
            });

            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Log.d("2tdp", "onError: " + what + "-" + extra);
                return false;
            });

            mediaPlayer.prepareAsync();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void editNote(){
        isCallBack = true;
        if (diary != null)
            DataLocalManager.setInt(diary.getId(), Constant.KEY_ID_DIARY);
        Utils.setIntent(requireContext(), AddDiary.class.getName());
    }
}
