package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.viewfingerprint.callback;

import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.viewfingerprint.utils.FingerprintToken;

/**
 * Created by Omar on 08/01/2018.
 */

public interface FingerprintDialogSecureCallback {
    void onAuthenticationSucceeded();
    void onAuthenticationCancel();
    void onNewFingerprintEnrolled(FingerprintToken token);
}
