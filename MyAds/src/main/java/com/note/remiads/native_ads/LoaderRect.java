package com.note.remiads.native_ads;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.note.remiads.R;
import com.note.remiads.utils.RmSave;

public class LoaderRect {

    private NativeResult nativeResult;
    private NativeAd nativeAd;

    public void loadAds(Context c, int id, NativeResult result) {
        if (RmSave.getPay(c)) {
            result.onLoaded(null);
            return;
        }
        this.nativeResult = result;
        AdLoader.Builder builder = new AdLoader.Builder(c, c.getString(id));
        builder.forNativeAd(nativeAd -> {
            if (LoaderRect.this.nativeAd != null)
                LoaderRect.this.nativeAd.destroy();
            LoaderRect.this.nativeAd = nativeAd;
            if (nativeResult != null)
                nativeResult.onLoaded(LoaderRect.this.nativeAd);
        });
        VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(true).build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder().setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_LEFT).setVideoOptions(videoOptions).build();
        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                onDestroy();
                if (nativeResult != null)
                    nativeResult.onLoaded(null);
            }

        }).build();
        adLoader.loadAd(new AdManagerAdRequest.Builder().build());
    }

    public void loadAdsSmall(Context c, int id, NativeResult result) {
        if (RmSave.getPay(c) || !RmSave.isLoadAds(c)) {
            result.onLoaded(null);
            return;
        }
        this.nativeResult = result;
        AdLoader.Builder builder = new AdLoader.Builder(c, c.getString(id));
        builder.forNativeAd(nativeAd -> {
            if (LoaderRect.this.nativeAd != null)
                LoaderRect.this.nativeAd.destroy();
            LoaderRect.this.nativeAd = nativeAd;
            if (nativeResult != null)
                nativeResult.onLoaded(LoaderRect.this.nativeAd);
        });
        NativeAdOptions adOptions = new NativeAdOptions.Builder().setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_LEFT).build();
        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                onDestroy();
                if (nativeResult != null)
                    nativeResult.onLoaded(null);
            }

        }).build();
        adLoader.loadAd(new AdManagerAdRequest.Builder().build());
    }

    public void onDestroy() {
        if (nativeAd != null) {
            nativeAd.destroy();
            nativeAd = null;
        }
    }
}
