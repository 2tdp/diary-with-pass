package com.note.mydiary.diarywithlock.journalwithlock.fragment.record;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.ViewRecord;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewdialog.ViewDialogRecord;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICallBackItem;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.TimeCounter;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class RecordFragment extends Fragment {

    ViewRecord viewRecord;

    private ICallBackItem callBack;

    private int w;
    private boolean isRecording, isPause;
    private String fileName, nameAudio;
    private MediaRecorder mediaRecorder;
    private TimeCounter timeCounter;

    public RecordFragment(ICallBackItem callBack) {
        this.callBack = callBack;
    }

    public static RecordFragment newInstance(ICallBackItem callBack) {
        return new RecordFragment(callBack);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        w = getResources().getDisplayMetrics().widthPixels;
        viewRecord = new ViewRecord(requireContext());
        viewRecord.setFocusable(true);
        viewRecord.setClickable(true);

        mediaRecorder = new MediaRecorder();
        timeCounter = new TimeCounter(86400000, 1000);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        evenClick();
        return viewRecord;
    }

    private void evenClick() {
        viewRecord.getViewRecord().setOnClickListener(v -> {
            if (checkPermission()) startRecord();
        });

        viewRecord.getViewPause().setOnClickListener(v -> clickPause());
        viewRecord.getViewStop().setOnClickListener(v -> clickStop());
    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 1445);
            return false;
        } else return true;
    }

    @SuppressLint("SimpleDateFormat")
    private void startRecord() {
        fileName = requireActivity().getExternalFilesDir("/").getAbsolutePath();
        nameAudio = "app_diary_record_" + new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(Calendar.getInstance(Constant.LOCALE_DEFAULT).getTime().getTime());
        fileName += "/" + nameAudio + ".3gp";

        viewRecord.getViewRecord().setVisibility(View.GONE);
        viewRecord.getLinearMain().setVisibility(View.VISIBLE);
        viewRecord.getTvTime().setVisibility(View.VISIBLE);

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(fileName);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), getResources().getString(R.string.err_record), Toast.LENGTH_SHORT).show();
        }
        mediaRecorder.start();
        timeCounter.start();
        timeCounter.setOnclickView(time -> viewRecord.getTvTime().setText(calculatorTime(time)));
        isRecording = true;
    }

    private void clickStop() {
        timeCounter.onFinish();
        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();
        mediaRecorder = null;

        ViewDialogRecord dialogRecord = new ViewDialogRecord(requireContext());
        AlertDialog dialog = new AlertDialog.Builder(requireContext(), R.style.SheetDialog).create();
        dialog.setView(dialogRecord);
        dialog.setCancelable(false);
        dialog.show();
        dialogRecord.getLayoutParams().width = (int) (84.444f * w / 100);

        File file = new File(fileName);

        dialogRecord.getEt().setText(nameAudio);
        dialogRecord.getViewYesNo().getTvNo().setOnClickListener(v -> {
            if (file.exists()) file.delete();
            dialog.cancel();
            Utils.clearBackStack(getParentFragmentManager());
        });
        dialogRecord.getViewYesNo().getTvYes().setOnClickListener(v -> {
            if (!dialogRecord.getEt().getText().toString().equals(nameAudio))
                nameAudio = dialogRecord.getEt().getText().toString();

            if (file.exists()) {
                String fileDir = requireActivity().getExternalFilesDir("/").getAbsolutePath() + "/" + nameAudio + ".3gp";
                File newName = new File(fileDir);
                file.renameTo(newName);

                callBack.callBackItem(fileDir, -1);
                dialog.cancel();
                Utils.clearBackStack(getParentFragmentManager());
            }
        });
    }

    private void clickPause() {
        if (viewRecord.getViewPause().getTv().getText().equals(getResources().getString(R.string.pause))) {
            viewRecord.getViewPause().getImg().setImageResource(R.drawable.ic_play);
            viewRecord.getViewPause().getTv().setText(getResources().getString(R.string.action_continue));
            if (mediaRecorder != null && isRecording) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) mediaRecorder.pause();
                    timeCounter.pause();

                    Log.d("2tdp", "clickPause: ");

                    isPause = true;
                    isRecording = false;
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        } else if (viewRecord.getViewPause().getTv().getText().equals(getResources().getString(R.string.action_continue))) {
            viewRecord.getViewPause().getImg().setImageResource(R.drawable.ic_pause);
            viewRecord.getViewPause().getTv().setText(getResources().getString(R.string.pause));

            if (mediaRecorder != null && !isRecording && isPause) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) mediaRecorder.resume();
                    timeCounter.resume();
                    isPause = false;
                    isRecording = true;

                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private String calculatorTime(long time) {
        String str;

        if (time < 60) {
            if (time < 10) str = "00:0" + time;
            else str = "00:" + time;
        } else {
            int minute = (int) (time / 60);
            int second = (int) (time - 60 * minute);
            if (minute < 10) {
                if (second < 10) str = "0" + minute + ":0" + second;
                else str = "0" + minute + ":" + second;
            } else {
                if (second < 10) str = minute + ":0" + second;
                else str = minute + ":" + second;
            }
        }

        return str;
    }
}