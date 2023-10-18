package com.note.mydiary.diarywithlock.journalwithlock.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.base.BaseActivity;

public class ClearDataActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clear_data_layout);

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_clear_data, null);
        TextView tvAllow = view.findViewById(R.id.tvAllow);

        AlertDialog dialog = new AlertDialog.Builder(this, R.style.SheetDialog).create();
        dialog.setView(view);
        dialog.setCancelable(false);
        dialog.show();

        tvAllow.setOnClickListener(v -> {
            setIntent(SplashActivity.class.getName(), true);
            dialog.cancel();
        });
    }
}
