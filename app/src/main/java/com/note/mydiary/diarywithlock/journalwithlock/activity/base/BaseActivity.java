package com.note.mydiary.diarywithlock.journalwithlock.activity.base;

import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

import java.util.Timer;
import java.util.TimerTask;

public abstract class BaseActivity extends AppCompatActivity {

    //animation
    public static final int res = android.R.id.content;
    public static final int enter = R.anim.slide_in_right;
    public static final int exit = R.anim.slide_out_left;
    public static final int popEnter = R.anim.slide_in_left_small;
    public static final int popExit = R.anim.slide_out_right;

    private int finish = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarTransparent(this);
    }

    protected void setIntent(String nameActivity, boolean isFinish) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(this, nameActivity));
        startActivity(intent, ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left).toBundle());
        if (isFinish) finish();
    }

    protected void openNavigation(String nameActivity, boolean isFinish) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(this, nameActivity));
        startActivity(intent, ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_left_small, R.anim.slide_out_right).toBundle());
        if (isFinish) finish();
    }

    protected void showToast(String msg, int gravity) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }

    protected void replaceFragment(final FragmentManager manager, final Fragment fragment, boolean isAdd,
                                   final boolean addBackStack, boolean isAnimation) {
        try {
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            if (isAnimation)
                fragmentTransaction.setCustomAnimations(enter, exit, popEnter, popExit);
            if (addBackStack)
                fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
            if (isAdd) {
                fragmentTransaction.add(res, fragment, fragment.getClass().getSimpleName());
            } else {
                fragmentTransaction.replace(res, fragment, fragment.getClass().getSimpleName());
            }
            fragmentTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void hideFragment(FragmentManager fragmentManager, Fragment fragment, int animEnter, int animExit) {
        fragmentManager.beginTransaction()
                .setCustomAnimations(animEnter, animExit)
                .hide(fragment)
                .commit();
    }

    protected void showFragment(FragmentManager fragmentManager, Fragment fragment, int animEnter, int animExit) {
        fragmentManager.beginTransaction()
                .setCustomAnimations(animEnter, animExit)
                .show(fragment)
                .commit();
    }

    protected void dropPopUp(View viewDrop, View viewContent, int width, int xOff, int yOff) {
        int w = getResources().getDisplayMetrics().widthPixels;
        PopupWindow popUp = new PopupWindow(viewContent, (int) (width * w / 100), RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        popUp.showAsDropDown(viewDrop, xOff, yOff);
    }

    protected void onBackPressed(boolean isFinish, boolean isNavigation) {
        if (isFinish) {
            if (finish != 0) {
                finish = 0;
                finish();
                Utils.setAnimExit(this);
            } else {
                Toast.makeText(this, R.string.finish, Toast.LENGTH_SHORT).show();
                finish++;
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        finish = 0;
                    }
                }, 1000);
            }
        } else super.onBackPressed();
    }

}
