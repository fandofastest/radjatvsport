package com.sportstv.adapter;

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
import com.sportstv.tvonline.ChannelDetailsActivity;
import com.sportstv.tvonline.R;
import com.sportstv.item.ItemChannel;
import com.sportstv.tvonline.SplashActivity;
import com.sportstv.util.Ads;
import com.sportstv.util.Constant;
import com.github.ornolfr.ratingview.RatingView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.sportstv.tvonline.SplashActivity.faninter;
import static com.sportstv.tvonline.SplashActivity.inter;

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

                Ads ads = new Ads();

                if (SplashActivity.ads.equals("admob")){
                    ads.showinter(context,SplashActivity.inter);
                }
                else {
                    ads.showinterfb(context,faninter);
                }
                ads.setCustomObjectListener(new Ads.MyCustomObjectListener() {
                    @Override
                    public void onAdsfinish() {
                        context.startActivity(intent);

                    }

                    @Override
                    public void onRewardOk() {

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
