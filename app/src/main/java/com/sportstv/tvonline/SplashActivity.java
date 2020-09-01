package com.sportstv.tvonline;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.sportstv.util.Ads;
import com.sportstv.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.vov.vitamio.utils.Log;


public class SplashActivity extends AppCompatActivity {

    MyApplication App;
    private boolean mIsBackButtonPressed;
    private static final int SPLASH_DURATION = 2000;
    public static String statususer,banner,inter,admobappid,movieapk,statusapp,apkupdate,faninter,fanbanner,ads;
    public static String defaultimage="https://fando.xyz/tvku.jpg";
     InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        App = MyApplication.getInstance();

        getStatusapp(Constant.SERVER_URL+"getstatus.php");

    }

    @Override
    public void onBackPressed() {
        // set the flag to true so the next activity won't start up
        mIsBackButtonPressed = true;
        super.onBackPressed();

    }


    private void getStatusapp(String url){

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
//                    JSONObject jsonObject=response.getJSONObject("status");
                       statususer = response.getString("status");
                       banner = response.getString("banner");
                       inter=response.getString("inter");
                       movieapk=response.getString("movieapk");
                       admobappid=response.getString("admobappid");
                       apkupdate=response.getString("apkupdate");
                       statusapp=response.getString("statusapp");
                        faninter=response.getString("faninter");
                        fanbanner=response.getString("fanbanner");
                        ads=response.getString("ads");

                    Button button= findViewById(R.id.buttonstart);
                    ProgressBar progressBar =findViewById(R.id.progressbar);
                    progressBar.setVisibility(View.GONE);
                    button.setVisibility(View.VISIBLE);

                       if (statusapp.equals("0")){
                                update();
                                button.setText("UPDATE");
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        update();
                                    }
                                });

                       }
                       else{
                        button.setOnClickListener(view -> kehome());

                       }

















                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("exr",error);
            }
        });

        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);


    }

    private void update() {
        new SweetAlertDialog(SplashActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Update App")
                .setContentText("App Need To Update")
                .setConfirmText("Update")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog
                                .setTitleText("Update From Playstore")
                                .setContentText("Please Wait, Open Playstore")
                                .setConfirmText("Go")
                                .changeAlertType(SweetAlertDialog.PROGRESS_TYPE);

                        final Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(
                                    "https://play.google.com/store/apps/details?id="+SplashActivity.apkupdate));
                            intent.setPackage("com.android.vending");
                            startActivity(intent);
//                                Do something after 100ms
                        }, 3000);



                    }
                })

                .show();
    }




    public  void kehome(){
        Intent intent = new Intent(SplashActivity.this,MainActivity.class);

        Ads ads = new Ads();

        if (SplashActivity.ads.equals("admob")){
            ads.showinter(SplashActivity.this,SplashActivity.inter);
        }
        else {
            ads.showinterfb(SplashActivity.this,faninter);
        }
        ads.setCustomObjectListener(new Ads.MyCustomObjectListener() {
            @Override
            public void onAdsfinish() {
                startActivity(intent);
                finish();
            }

            @Override
            public void onRewardOk() {

            }
        });


    }



}
