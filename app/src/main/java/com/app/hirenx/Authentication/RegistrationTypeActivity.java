package com.app.hirenx.Authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hirenx.R;

public class RegistrationTypeActivity extends AppCompatActivity {

    private CardView crdConsumer, crdPartner;
    private Button btnRequestOTP,btnLogin;
    private String type="consumer";
    private TextView tvConsumer,tvConsumer2,tvPartner,tvPartner2;
    private ImageView imgConsumerAppInfo,imgPartnerAppInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_type);

        crdConsumer=findViewById(R.id.crdview_sign_up_as_consumer);
        crdPartner=findViewById(R.id.crdview_sign_up_as_partner);
        btnRequestOTP=findViewById(R.id.btn_request_otp);
        btnLogin=findViewById(R.id.btn_move_towards_login);
        tvConsumer=findViewById(R.id.label_consumer);
        tvConsumer2=findViewById(R.id.label_consumer2);
        tvPartner=findViewById(R.id.label_partner);
        tvPartner2=findViewById(R.id.label_partner2);
        imgConsumerAppInfo=findViewById(R.id.img_app_info_consumer);
        imgPartnerAppInfo=findViewById(R.id.img_app_info_partner);

        imgConsumerAppInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(RegistrationTypeActivity.this)
                        .setTitle("info !")
                        .setMessage("This will lead to register you as a consumer")

                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        imgPartnerAppInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 new AlertDialog.Builder(RegistrationTypeActivity.this)
                        .setTitle("info !")
                        .setMessage("This will lead to register you as a partner")

                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

        });

        if (type.equals("consumer")){

            crdPartner.setCardBackgroundColor(getResources().getColor(R.color.cardviewwhite));
            crdConsumer.setCardBackgroundColor(getResources().getColor(R.color.buttoncolor));
            tvConsumer.setTextColor(getResources().getColor(R.color.cardviewwhite));
            tvConsumer2.setTextColor(getResources().getColor(R.color.cardviewwhite));
            tvPartner.setTextColor(getResources().getColor(R.color.black));
            tvPartner2.setTextColor(getResources().getColor(R.color.black));
            imgConsumerAppInfo.setColorFilter(ContextCompat.getColor(RegistrationTypeActivity.this, R.color.white));
            imgPartnerAppInfo.setColorFilter(ContextCompat.getColor(RegistrationTypeActivity.this, R.color.black));


        }

        crdConsumer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                type="consumer";
                crdPartner.setCardBackgroundColor(getResources().getColor(R.color.cardviewwhite));
                crdConsumer.setCardBackgroundColor(getResources().getColor(R.color.buttoncolor));
                tvConsumer.setTextColor(getResources().getColor(R.color.cardviewwhite));
                tvConsumer2.setTextColor(getResources().getColor(R.color.cardviewwhite));
                tvPartner.setTextColor(getResources().getColor(R.color.black));
                tvPartner2.setTextColor(getResources().getColor(R.color.black));
                imgConsumerAppInfo.setColorFilter(ContextCompat.getColor(RegistrationTypeActivity.this, R.color.white));
                imgPartnerAppInfo.setColorFilter(ContextCompat.getColor(RegistrationTypeActivity.this, R.color.black));



            }
        });

        crdPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                type="partner";
                crdConsumer.setCardBackgroundColor(getResources().getColor(R.color.cardviewwhite));
                crdPartner.setCardBackgroundColor(getResources().getColor(R.color.buttoncolor));
                tvPartner.setTextColor(getResources().getColor(R.color.cardviewwhite));
                tvPartner2.setTextColor(getResources().getColor(R.color.cardviewwhite));
                tvConsumer.setTextColor(getResources().getColor(R.color.black));
                tvConsumer2.setTextColor(getResources().getColor(R.color.black));
                imgPartnerAppInfo.setColorFilter(ContextCompat.getColor(RegistrationTypeActivity.this, R.color.white));
                imgConsumerAppInfo.setColorFilter(ContextCompat.getColor(RegistrationTypeActivity.this, R.color.black));


            }
        });

        btnRequestOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(type==null){

                    Toast.makeText(getApplicationContext(), "please select your registration type", Toast.LENGTH_SHORT).show();

                }else if(type.equals("consumer")){

                    Intent moveConsumerRegistration=new Intent(getApplicationContext(),RegisterActivity.class);
                    moveConsumerRegistration.putExtra("registrerType",type);
                    startActivity(moveConsumerRegistration);
                  //  finish();
                }


                else if(type.equals("partner")){

                    Intent moveConsumerRegistration=new Intent(getApplicationContext(),RegisterActivity.class);
                    moveConsumerRegistration.putExtra("registrerType",type);
                    startActivity(moveConsumerRegistration);
                    //finish();
                }


            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent moveToLogin=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(moveToLogin);

            }
        });

    }
}