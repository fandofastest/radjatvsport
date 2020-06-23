package com.sportstv.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.sportstv.tvonline.R;
import com.sportstv.tvonline.VideoPlayActivity;
import com.sportstv.item.ItemVideo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.sportstv.tvonline.SplashActivity.inter;

/**
 * Created by laxmi.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ItemRowHolder> {

    private ArrayList<ItemVideo> dataList;
    private Context mContext;
    InterstitialAd mInterstitialAd;


    public VideoAdapter(Context context, ArrayList<ItemVideo> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category_item, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRowHolder holder, final int position) {
        final ItemVideo singleItem = dataList.get(position);
        holder.text.setText(singleItem.getVideoTitle());
        Picasso.get().load(singleItem.getVideoThumbnailB()).placeholder(R.drawable.icon).into(holder.image);
        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(mContext, VideoPlayActivity.class);
                    intent.putExtra("videoUrl", singleItem.getVideoUrl());
                    mInterstitialAd = new InterstitialAd(mContext);
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
                            mContext.startActivity(intent);

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
                            mContext.startActivity(intent);
                            // Code to be executed when the interstitial ad is closed.
                        }
                    });



                    mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;
        LinearLayout lyt_parent;

        ItemRowHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.text);
            lyt_parent = itemView.findViewById(R.id.rootLayout);

        }
    }
}
