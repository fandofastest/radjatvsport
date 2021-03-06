package com.sportstv.tvonline;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.Nullable;

import com.google.android.ump.ConsentDebugSettings;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;
import com.google.android.ump.UserMessagingPlatform;

import io.vov.vitamio.utils.Log;

public class GDPRcheckV2 {
    private ConsentInformation consentInformation;
    private ConsentForm consentForm;
    private String appid;
    private static GDPRcheckV2 instance;
    private  Context context;
    private  Activity activity;
    public GDPRcheckV2(Context context) {
        this.context=context;
    }


    public GDPRcheckV2 withContextAndActivty(Activity activty) {
        instance = new GDPRcheckV2(context);
        this.activity=activty;

        return instance;
    }

    public GDPRcheckV2 withPublisherIds(String appid) {
        this.appid = appid;
        if (instance == null)
            throw new NullPointerException("Please call withContext first");
        return instance;
    }


    public   void check (){
        ConsentRequestParameters params = new ConsentRequestParameters
                .Builder()
                .setAdMobAppId(appid)
                .build();
        consentInformation = UserMessagingPlatform.getConsentInformation(context);
        consentInformation.requestConsentInfoUpdate(
                activity,
                params,
                new ConsentInformation.OnConsentInfoUpdateSuccessListener() {
                    @Override
                    public void onConsentInfoUpdateSuccess() {
                        if (consentInformation.isConsentFormAvailable()) {
                            loadForm(context,activity);
                        }
                        // The consent information state was updated.
                        // You are now ready to check if a form is available.
                    }
                },
                new ConsentInformation.OnConsentInfoUpdateFailureListener() {
                    @Override
                    public void onConsentInfoUpdateFailure(FormError formError) {
                        Log.e("Errrrr",formError.getErrorCode()+formError.getMessage());
                        // Handle the error.
                    }
                });


    }

    private void loadForm(Context context,Activity activity){
        UserMessagingPlatform.loadConsentForm(
                context,
                new UserMessagingPlatform.OnConsentFormLoadSuccessListener() {
                    @Override
                    public void onConsentFormLoadSuccess(ConsentForm consentForm) {
                       GDPRcheckV2.this.consentForm = consentForm;

                        if(consentInformation.getConsentStatus() == ConsentInformation.ConsentStatus.REQUIRED) {
                            consentForm.show(
                                    activity,
                                    new ConsentForm.OnConsentFormDismissedListener() {
                                        @Override
                                        public void onConsentFormDismissed(@Nullable FormError formError) {
                                            // Handle dismissal by reloading form.
                                            loadForm(context, activity);
                                        }
                                    });

                        }

                    }
                },
                new UserMessagingPlatform.OnConsentFormLoadFailureListener() {
                    @Override
                    public void onConsentFormLoadFailure(FormError formError) {
                        /// Handle Error.
                    }
                }
        );
    }
}
