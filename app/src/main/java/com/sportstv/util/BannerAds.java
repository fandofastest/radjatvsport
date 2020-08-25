package com.sportstv.util;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.sportstv.tvonline.SplashActivity;

public class BannerAds {
    public static void ShowBannerAds(Context context, LinearLayout mAdViewLayout) {
        AdView mAdView = new AdView(context);
        mAdView.setAdSize(AdSize.SMART_BANNER);
        mAdView.setAdUnitId(SplashActivity.banner);
        AdRequest.Builder builder = new AdRequest.Builder();

        mAdView.loadAd(builder.build());
        mAdViewLayout.addView(mAdView);
    }
}
