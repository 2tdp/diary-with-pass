package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.viewfingerprint.callback;

import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.viewfingerprint.view.Fingerprint;

/**
 * Created by Omar on 10/07/2017.
 */

public interface FailAuthCounterCallback {
    void onTryLimitReached(Fingerprint fingerprint);
}
