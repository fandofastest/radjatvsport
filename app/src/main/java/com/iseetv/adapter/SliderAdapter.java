package com.iseetv.adapter;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.iseetv.tvonline.ChannelDetailsActivity;
import com.iseetv.tvonline.R;
import com.iseetv.item.ItemChannel;
import com.iseetv.util.Constant;
import com.github.ornolfr.ratingview.RatingView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.iseetv.tvonline.SplashActivity.inter;

public class SliderAdapter extends PagerAdapter {
    InterstitialAd mInterstitialAd;

    private LayoutInflater inflater;
    private Activity context;
    private ArrayList<ItemChannel> mList;

    public SliderAdapter(Activity context, ArrayList<ItemChannel> itemChannels) {
        this.context = context;
        this.mList = itemChannels;
        inflater = context.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View imageLayout = inflater.inflate(R.layout.row_slider_item, container, false);
        assert imageLayout != null;
        ImageView imageView = imageLayout.findViewById(R.id.image);
        TextView channelName = imageLayout.findViewById(R.id.text);
        TextView channelCategory = imageLayout.findViewById(R.id.textCategory);
        RatingView ratingView = imageLayout.findViewById(R.id.ratingView);
        RelativeLayout rootLayout = imageLayout.findViewById(R.id.rootLayout);

        final ItemChannel itemChannel = mList.get(position);
        Picasso.get().load(itemChannel.getImage()).placeholder(R.drawable.icon).into(imageView);
        channelName.setText(itemChannel.getChannelName());
        channelCategory.setText(itemChannel.getChannelCategory());
        ratingView.setRating(Float.parseFloat(itemChannel.getChannelAvgRate()));
        ratingView.setVisibility(View.GONE);

        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, ChannelDetailsActivity.class);
                intent.putExtra(Constant.CHANNEL_ID, itemChannel.getId());
                intent.putExtra(Constant.CHANNEL_TITLE, itemChannel.getChannelName());
                intent.putExtra(Constant.CATEGORY_NAME, itemChannel.getChannelCategory());
                intent.putExtra(Constant.CHANNEL_IMAGE, itemChannel.getImage());
                intent.putExtra(Constant.CHANNEL_AVG_RATE, itemChannel.getChannelAvgRate());
                intent.putExtra(Constant.CHANNEL_URL, itemChannel.getChannelUrl());

                mInterstitialAd = new InterstitialAd(context);
                mInterstitialAd.setAdUnitId(inter);
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        mInterstitialAd.show();
                        // Code to be executed when an ad finishes loading.
                    }

                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        context.startActivity(intent);

                        // Code to be executed when an ad request fails.
                    }

                    @Override
                    public void onAdOpened() {
                        // Code to be executed when the ad is displayed.
                    }

                    @Override
                    public void onAdClicked() {
                        // Code to be executed when the user clicks on an ad.
                    }

                    @Override
                    public void onAdLeftApplication() {
                        // Code to be executed when the user has left the app.
                    }

                    @Override
                    public void onAdClosed() {
                        context.startActivity(intent);
                        // Code to be executed when the interstitial ad is closed.
                    }
                });


            }
        });

        container.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        (container).removeView((View) object);
    }
}
