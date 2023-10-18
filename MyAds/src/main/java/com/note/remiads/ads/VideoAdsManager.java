package com.note.remiads.ads;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.RewardedVideoListener;
import com.note.remiads.itf.LoadAdsListen;
import com.note.remiads.itf.ShowVideoListen;
import com.note.remiads.utils.RmSave;

public class VideoAdsManager {

    private static VideoAdsManager videoAds;

    public static VideoAdsManager getInstance() {
        if (videoAds == null)
            videoAds = new VideoAdsManager();
        return videoAds;
    }

    private RewardedAd rewardedAd;
    private boolean reward;

    public void loadAds(Context context, LoadAdsListen loadAdsListen) {
        if (RmSave.getPay(context))
            return;
        if (rewardedAd == null) {
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(context, context.getString(com.note.remiads.R.string.ads_video), adRequest, new RewardedAdLoadCallback() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    IronSource.loadRewardedVideo();
                    rewardedAd = null;
                    if (loadAdsListen != null)
                        loadAdsListen.onLoaded();
                }

                @Override
                public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                    VideoAdsManager.this.rewardedAd = rewardedAd;
                    if (loadAdsListen != null)
                        loadAdsListen.onLoaded();
                }
            });
        } else {
            if (loadAdsListen != null)
                loadAdsListen.onLoaded();
        }
    }

    public void showRewardedVideo(Activity activity, ShowVideoListen showVideoListen) {
        reward = false;
        if (rewardedAd != null) {
            rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdShowedFullScreenContent() {
                    loadAds(activity, null);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    rewardedAd = null;
                    showRewardedVideo(activity, showVideoListen);
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    rewardedAd = null;
                    if (showVideoListen != null)
                        showVideoListen.onShowDismiss(reward);
                    loadAds(activity, null);
                }
            });
            rewardedAd.show(activity, rewardItem -> reward = true);
        } else if (IronSource.isRewardedVideoAvailable()) {
            IronSource.setRewardedVideoListener(new RewardedVideoListener() {
                @Override
                public void onRewardedVideoAdOpened() {

                }

                @Override
                public void onRewardedVideoAdClosed() {
                    if (showVideoListen != null)
                        showVideoListen.onShowDismiss(reward);
                    loadAds(activity, null);
                }

                @Override
                public void onRewardedVideoAvailabilityChanged(boolean b) {

                }

                @Override
                public void onRewardedVideoAdStarted() {

                }

                @Override
                public void onRewardedVideoAdEnded() {

                }

                @Override
                public void onRewardedVideoAdRewarded(Placement placement) {
                    reward = true;
                }

                @Override
                public void onRewardedVideoAdShowFailed(IronSourceError ironSourceError) {
                    loadAds(activity, null);
                    if (showVideoListen != null)
                        showVideoListen.onShowDismiss(reward);
                }

                @Override
                public void onRewardedVideoAdClicked(Placement placement) {

                }
            });
            IronSource.showRewardedVideo();
        } else {
            if (showVideoListen != null)
                showVideoListen.onShowDismiss(reward);
        }
    }

    public boolean isAdAvailable() {
        return rewardedAd != null || IronSource.isRewardedVideoAvailable();
    }

    public boolean isAdmobAvailable() {
        return rewardedAd != null;
    }

    public void onResume(Activity activity) {
        IronSource.onResume(activity);
    }

    public void onPause(Activity activity) {
        IronSource.onPause(activity);
    }
}
