package com.sportstv.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.sportstv.tvonline.ChannelDetailsActivity;
import com.sportstv.tvonline.R;
import com.sportstv.db.DatabaseHelper;
import com.sportstv.item.ItemChannel;
import com.sportstv.util.Constant;
import com.github.ornolfr.ratingview.RatingView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.sportstv.tvonline.SplashActivity.inter;

/**
 * Created by laxmi.
 */
public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ItemRowHolder> {

    private ArrayList<ItemChannel> dataList;
    private Context mContext;
    private int rowLayout;
    private DatabaseHelper databaseHelper;
    InterstitialAd mInterstitialAd;


    public ChannelAdapter(Context context, ArrayList<ItemChannel> dataList, int rowLayout) {
        this.dataList = dataList;
        this.mContext = context;
        this.rowLayout = rowLayout;
        databaseHelper = new DatabaseHelper(mContext);
    }

    @NonNull
    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemRowHolder holder, final int position) {
        final ItemChannel singleItem = dataList.get(position);
        holder.text.setText(singleItem.getChannelName());
        holder.textCategory.setText(singleItem.getChannelCategory());
        Picasso.get().load(singleItem.getImage()).placeholder(R.drawable.icon).into(holder.image);
        holder.ratingView.setRating(Float.parseFloat(singleItem.getChannelAvgRate()));
        holder.ratingView.setVisibility(View.GONE);
        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(mContext, ChannelDetailsActivity.class);
                intent.putExtra(Constant.CHANNEL_ID, singleItem.getId());
                intent.putExtra(Constant.CHANNEL_TITLE, singleItem.getChannelName());
                intent.putExtra(Constant.CATEGORY_NAME, singleItem.getChannelCategory());
                intent.putExtra(Constant.CHANNEL_IMAGE, singleItem.getImage());
                intent.putExtra(Constant.CHANNEL_AVG_RATE, singleItem.getChannelAvgRate());
                intent.putExtra(Constant.CHANNEL_URL, singleItem.getChannelUrl());
                System.out.println("fando"+singleItem.getChannelUrl());





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









            }
        });

        holder.textMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(mContext, holder.textMenu);
                popup.inflate(R.menu.options_menu);
                Menu popupMenu = popup.getMenu();
                if (databaseHelper.getFavouriteById(singleItem.getId())) {
                    popupMenu.findItem(R.id.option_add_favourite).setVisible(false);
                } else {
                    popupMenu.findItem(R.id.option_remove_favourite).setVisible(false);
                }

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.option_add_favourite:
                                ContentValues fav = new ContentValues();
                                fav.put(DatabaseHelper.KEY_ID, singleItem.getId());
                                fav.put(DatabaseHelper.KEY_TITLE, singleItem.getChannelName());
                                fav.put(DatabaseHelper.KEY_IMAGE, singleItem.getImage());
                                fav.put(DatabaseHelper.KEY_CATEGORY, singleItem.getChannelCategory());
                                databaseHelper.addFavourite(DatabaseHelper.TABLE_FAVOURITE_NAME, fav, null);
                                Toast.makeText(mContext, mContext.getString(R.string.favourite_add), Toast.LENGTH_SHORT).show();
                                break;

                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text, textCategory, textMenu;
        LinearLayout lyt_parent;
        RatingView ratingView;

        ItemRowHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.text);
            textCategory = itemView.findViewById(R.id.textCategory);
            textMenu = itemView.findViewById(R.id.textViewOptions);
            lyt_parent = itemView.findViewById(R.id.rootLayout);
            ratingView = itemView.findViewById(R.id.ratingView);
        }
    }
}
