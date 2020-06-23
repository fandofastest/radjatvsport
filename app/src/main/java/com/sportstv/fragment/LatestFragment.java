package com.sportstv.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sportstv.adapter.ChannelAdapter;
import com.sportstv.tvonline.R;
import com.sportstv.item.ItemChannel;
import com.sportstv.util.Constant;
import com.sportstv.util.ItemOffsetDecoration;
import com.sportstv.util.NetworkUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.sportstv.tvonline.SplashActivity.defaultimage;
import static com.sportstv.tvonline.SplashActivity.statususer;

/**
 * Created by laxmi.
 */
public class LatestFragment extends Fragment {

    ArrayList<ItemChannel> mListItem;
    public RecyclerView recyclerView;
    ChannelAdapter adapter;
    String info;
    private ProgressBar progressBar;
    private LinearLayout lyt_not_found;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.row_recyclerview, container, false);
        mListItem = new ArrayList<>();
        lyt_not_found = rootView.findViewById(R.id.lyt_not_found);
        progressBar = rootView.findViewById(R.id.progressBar);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(requireActivity(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);


        if (getArguments()==null){

             info = "int";

        }

        else{
            info = getArguments().getString("info");
        }


        if (NetworkUtils.isConnected(getActivity())) {
            switch (info) {
                case "int":
                    getLatestItem();
                    break;
                case "id":
                    getindoitem();
                    break;
                case "kr":
                    getkritem();
                    break;
            }


        } else {
            Toast.makeText(getActivity(), getString(R.string.conne_msg1), Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    private void getindoitem() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constant.BASEURLCHANNEL+"id.m3u", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                showProgress(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                showProgress(false);
                String result = new String(responseBody);
                try {
                    JSONObject mainJson = new JSONObject(result);

                    JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
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
                        mListItem.add(objItem);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                displayData();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                showProgress(false);
                lyt_not_found.setVisibility(View.VISIBLE);
            }

        });
    }

    private void getkritem() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constant.BASEURLCHANNEL+"kr.m3u", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                showProgress(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                showProgress(false);
                String result = new String(responseBody);
                try {
                    JSONObject mainJson = new JSONObject(result);

                    JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        ItemChannel objItem = new ItemChannel();
                        objItem.setId(String.valueOf(i));
                        objItem.setChannelName(objJson.getString("title"));
                        objItem.setChannelCategory("Sports TV");
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
                        mListItem.add(objItem);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                displayData();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                showProgress(false);
                lyt_not_found.setVisibility(View.VISIBLE);
            }

        });
    }
    private void getLatestItem() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constant.LATEST_URL, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                showProgress(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                showProgress(false);
                String result = new String(responseBody);
                try {
                    JSONObject mainJson = new JSONObject(result);

                    JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
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
                        mListItem.add(objItem);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                displayData();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                showProgress(false);
                lyt_not_found.setVisibility(View.VISIBLE);
            }

        });
    }

    private void displayData() {
        adapter = new ChannelAdapter(getActivity(), mListItem, R.layout.row_channel_item);
        recyclerView.setAdapter(adapter);

        if (adapter.getItemCount() == 0) {
            lyt_not_found.setVisibility(View.VISIBLE);
        } else {
            lyt_not_found.setVisibility(View.GONE);
        }
    }


    private void showProgress(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            lyt_not_found.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
