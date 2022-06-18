package com.app.hirenx.OtpReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MessageReceiver extends BroadcastReceiver {

    public SmsBroadcastReceiverListener smsBroadcastReceiverListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() == SmsRetriever.SMS_RETRIEVED_ACTION){

            Bundle extras = intent.getExtras();

            Status smsRetreiverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch (smsRetreiverStatus.getStatusCode()){

                case CommonStatusCodes
                        .SUCCESS:
                    Intent messageIntent = extras.getParcelable(SmsRetriever.SMS_RETRIEVED_ACTION);
                    smsBroadcastReceiverListener.onSuccess(messageIntent);
                    break;
                case CommonStatusCodes.TIMEOUT:
                    smsBroadcastReceiverListener.onFailure();
                    break;

            }
        }
    }

    public interface SmsBroadcastReceiverListener{

        void onSuccess(Intent intent);

        void onFailure();


    }
}
