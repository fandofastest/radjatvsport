package com.iseetv.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.iseetv.adapter.CategoryAdapter;
import com.iseetv.adapter.ChannelAdapter;
import com.iseetv.adapter.SliderAdapter;
import com.iseetv.tvonline.MainActivity;
import com.iseetv.tvonline.R;
import com.iseetv.item.ItemCategory;
import com.iseetv.item.ItemChannel;
import com.iseetv.util.Constant;
import com.iseetv.util.ItemOffsetDecoration;
import com.iseetv.util.NetworkUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import me.relex.circleindicator.CircleIndicator;

import static com.iseetv.tvonline.SplashActivity.defaultimage;
import static com.iseetv.tvonline.SplashActivity.statususer;

/**
 * Created by laxmi.
 */

public class HomeFragment extends Fragment {

    ArrayList<ItemChannel> mSliderList, mLatestList,mlistindo,mlistsport;
    ArrayList<ItemCategory> mCategoryList;
    SliderAdapter sliderAdapter;
    ScrollView mScrollView;
    ProgressBar mProgressBar;
    ViewPager mViewPager;
    CircleIndicator circleIndicator;
    RecyclerView rvLatest, rvCategory,rvindo,rvsport;
    Button btnMoreLatest, btnMoreCategory,btnmoreindo,btnmoresport;
    ChannelAdapter channelAdapter,indoAdapter,sportdapter;
    CategoryAdapter categoryAdapter;
    LinearLayout lyt_not_found;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mSliderList = new ArrayList<>();
        mLatestList = new ArrayList<>();
        mCategoryList = new ArrayList<>();
        mlistindo= new ArrayList<>();
        mlistsport= new ArrayList<>();
        lyt_not_found = rootView.findViewById(R.id.lyt_not_found);
        mScrollView = rootView.findViewById(R.id.scrollView);
        mProgressBar = rootView.findViewById(R.id.progressBar);
        mViewPager = rootView.findViewById(R.id.viewPager);
        circleIndicator = rootView.findViewById(R.id.indicator_unselected_background);
        rvLatest = rootView.findViewById(R.id.rv_latest);
        rvindo=rootView.findViewById(R.id.rv_indonesia);
        rvsport=rootView.findViewById(R.id.rv_korea);

//        rvCategory = rootView.findViewById(R.id.rv_category);
        btnMoreLatest = rootView.findViewById(R.id.btn_latest);
        btnmoreindo=rootView.findViewById(R.id.btn_homeindo);
        btnmoresport=rootView.findViewById(R.id.btn_homekorea);
//        btnMoreCategory = rootView.findViewById(R.id.btn_category);

        rvLatest.setHasFixedSize(true);
        rvLatest.setNestedScrollingEnabled(false);
        rvLatest.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        rvindo.setHasFixedSize(true);
        rvindo.setNestedScrollingEnabled(false);
        rvindo.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvsport.setHasFixedSize(true);
        rvsport.setNestedScrollingEnabled(false);
        rvsport.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(requireActivity(), R.dimen.item_offset);
        rvLatest.addItemDecoration(itemDecoration);
        rvindo.addItemDecoration(itemDecoration);
        rvsport.addItemDecoration(itemDecoration);

        btnMoreLatest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) requireActivity()).hideShowBottomView(false);
                ((MainActivity) requireActivity()).navigationItemSelected(1);
                String categoryName = getString(R.string.International);
                FragmentManager fm = getFragmentManager();
                LatestFragment f1 = new LatestFragment();
                Bundle args = new Bundle();
                args.putString("info", "int");
                f1.setArguments(args);
                assert fm != null;
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.Container, f1, categoryName);
                ft.addToBackStack(null);

                ft.commit();
                ((MainActivity) requireActivity()).setToolbarTitle(categoryName);
            }
        });

        btnmoreindo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) requireActivity()).hideShowBottomView(false);
                ((MainActivity) requireActivity()).navigationItemSelected(1);
                String categoryName = getString(R.string.indonesia);
                FragmentManager fm = getFragmentManager();
                LatestFragment f1 = new LatestFragment();
                Bundle args = new Bundle();
                args.putString("info", "id");
                f1.setArguments(args);
                assert fm != null;
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.Container, f1, categoryName);
                ft.addToBackStack(null);
                ft.commit();
                ((MainActivity) requireActivity()).setToolbarTitle(categoryName);
            }
        });
        btnmoresport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).hideShowBottomView(false);
                ((MainActivity) requireActivity()).navigationItemSelected(1);
                String categoryName = getString(R.string.Korea);
                FragmentManager fm = getFragmentManager();
                LatestFragment f1 = new LatestFragment();
                Bundle args = new Bundle();
                args.putString("info", "Sport TV");
                f1.setArguments(args);
                assert fm != null;
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.Container, f1, categoryName);
                ft.addToBackStack(null);

                ft.commit();
                ((MainActivity) requireActivity()).setToolbarTitle(categoryName);
            }
        });



        if (NetworkUtils.isConnected(getActivity())) {
            getchannelint();
            getchanneliid();
            getchannelisport();
        } else {
            Toast.makeText(getActivity(), getString(R.string.conne_msg1), Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    private void getchanneliid() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constant.BASEURLCHANNEL+"id.m3u", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                mProgressBar.setVisibility(View.VISIBLE);
                mScrollView.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mProgressBar.setVisibility(View.GONE);
                mScrollView.setVisibility(View.VISIBLE);

                String result = new String(responseBody);
                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONObject objJson;
                                 JSONArray jsonLatest = mainJson.getJSONArray(Constant.ARRAY_NAME);
                    for (int i = 0; i < jsonLatest.length(); i++) {
                        objJson = jsonLatest.getJSONObject(i);
                        ItemChannel objItem = new ItemChannel();
                        objItem.setId(String.valueOf(i));
                        objItem.setChannelName(objJson.getString("title"));
                        objItem.setChannelCategory("Indonesia");
                        objItem.setImage(objJson.getString("thumb_square"));
                        if (objJson.getString("thumb_square").equals("")){
                            objItem.setImage(defaultimage);
                        }

                        if (statususer.equals("aman")){
                            objItem.setChannelUrl(objJson.getString("url"));
                        }else{
                            objItem.setChannelUrl(null);
                            objItem.setImage(defaultimage);
                        }


                        objItem.setChannelAvgRate("5");
                        mlistindo.add(objItem);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                displayData();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mProgressBar.setVisibility(View.GONE);
                mScrollView.setVisibility(View.GONE);
                lyt_not_found.setVisibility(View.VISIBLE);
            }
        });
    }



    private void getchannelisport() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constant.URLSPORT, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                mProgressBar.setVisibility(View.VISIBLE);
                mScrollView.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mProgressBar.setVisibility(View.GONE);
                mScrollView.setVisibility(View.VISIBLE);

                String result = new String(responseBody);
                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONObject objJson;
                    JSONArray jsonLatest = mainJson.getJSONArray(Constant.ARRAY_NAME);
                    for (int i = 0; i < jsonLatest.length(); i++) {
                        objJson = jsonLatest.getJSONObject(i);
                        ItemChannel objItem = new ItemChannel();
                        objItem.setId(String.valueOf(i));
                        objItem.setChannelName(objJson.getString("title"));
                        objItem.setChannelCategory("Korean");
                        objItem.setImage(objJson.getString("thumb_square"));
                        if (objJson.getString("thumb_square").equals("")){
                            objItem.setImage(defaultimage);
                        }

                        if (statususer.equals("aman")){
                            objItem.setChannelUrl(objJson.getString("url"));
                        }else{
                            objItem.setChannelUrl(null);
                            objItem.setImage(defaultimage);
                        }


                        objItem.setChannelAvgRate("5");
                        mlistsport.add(objItem);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                displayData();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mProgressBar.setVisibility(View.GONE);
                mScrollView.setVisibility(View.GONE);
                lyt_not_found.setVisibility(View.VISIBLE);
            }
        });
    }


    private void getchannelint() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constant.BASEURLCHANNEL+"int.m3u", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                mProgressBar.setVisibility(View.VISIBLE);
                mScrollView.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mProgressBar.setVisibility(View.GONE);
                mScrollView.setVisibility(View.VISIBLE);

                String result = new String(responseBody);
                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonSlider = mainJson.getJSONArray(Constant.ARRAY_NAME);

                    JSONObject objJson;
                    for (int i = 0; i < jsonSlider.length(); i++) {
                        objJson = jsonSlider.getJSONObject(i);
                        ItemChannel objItem = new ItemChannel();
                        objItem.setId(String.valueOf(i));
                        objItem.setChannelName(objJson.getString("title"));
                        objItem.setChannelCategory("International");
                        objItem.setImage(objJson.getString("thumb_square"));
                        if (objJson.getString("thumb_square").equals("")){
                            objItem.setImage(defaultimage);
                        }

                        if (statususer.equals("aman")){
                            objItem.setChannelUrl(objJson.getString("url"));
                        }else{
                            objItem.setChannelUrl(null);
                            objItem.setImage(defaultimage);
                        }

                        objItem.setChannelAvgRate("5");
                        mSliderList.add(objItem);
                    }

                    JSONArray jsonLatest = mainJson.getJSONArray(Constant.ARRAY_NAME);
                    for (int i = 0; i < jsonLatest.length(); i++) {
                        objJson = jsonLatest.getJSONObject(i);
                        ItemChannel objItem = new ItemChannel();
                        objItem.setId(String.valueOf(i));
                        objItem.setChannelName(objJson.getString("title"));
                        objItem.setChannelCategory("International");
                        objItem.setImage(objJson.getString("thumb_square"));
                        if (objJson.getString("thumb_square").equals("")){
                            objItem.setImage(defaultimage);
                        }

                        if (statususer.equals("aman")){
                            objItem.setChannelUrl(objJson.getString("url"));
                        }else{
                            objItem.setChannelUrl(null);
                            objItem.setImage(defaultimage);
                        }
                        objItem.setChannelAvgRate("5");
                        mLatestList.add(objItem);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                displayData();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mProgressBar.setVisibility(View.GONE);
                mScrollView.setVisibility(View.GONE);
                lyt_not_found.setVisibility(View.VISIBLE);
            }
        });
    }


    private void displayData() {
        sliderAdapter = new SliderAdapter(requireActivity(), mSliderList);
        mViewPager.setAdapter(sliderAdapter);
        circleIndicator.setViewPager(mViewPager);

        channelAdapter = new ChannelAdapter(getActivity(), mLatestList, R.layout.row_home_channel_item);
        rvLatest.setAdapter(channelAdapter);
        indoAdapter = new ChannelAdapter(getActivity(), mlistindo, R.layout.row_home_channel_item);
        rvindo.setAdapter(indoAdapter);
        sportdapter = new ChannelAdapter(getActivity(), mlistsport, R.layout.row_home_channel_item);
        rvsport.setAdapter(sportdapter);
//
//        categoryAdapter = new CategoryAdapter(getActivity(), mCategoryList, R.layout.row_home_category_item);
//        rvCategory.setAdapter(categoryAdapter);
    }

}
