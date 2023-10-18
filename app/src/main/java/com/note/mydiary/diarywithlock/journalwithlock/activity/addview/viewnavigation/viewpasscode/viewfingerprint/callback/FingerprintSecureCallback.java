package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.viewfingerprint.callback;

import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.viewfingerprint.utils.FingerprintToken;

/**
 * Created by Omar on 02/07/2017.
 */

public interface FingerprintSecureCallback {
    void onAuthenticationSucceeded();
    void onAuthenticationFailed();
    void onNewFingerprintEnrolled(FingerprintToken token);
    void onAuthenticationError(int errorCode, String error);
}
