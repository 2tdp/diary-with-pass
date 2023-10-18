package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.viewfingerprint.callback;

/**
 * Created by Omar on 02/07/2017.
 */

public interface FingerprintCallback {
    void onAuthenticationSucceeded();
    void onAuthenticationFailed();
    void onAuthenticationError(int errorCode, String error);
}
