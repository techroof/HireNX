package com.app.hirenx.Authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hirenx.R;

public class RegistrationTypeActivity extends AppCompatActivity {

    private ConstraintLayout crdConsumer, crdPartner;
    private Button btnRequestOTP;
    private String type="consumer";
    private TextView tvConsumer,tvConsumer2,tvPartner,tvPartner2,loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_type);

        crdConsumer=findViewById(R.id.sign_up_as_consumer_cl);
        crdPartner=findViewById(R.id.sign_up_as_partner_cl);
        btnRequestOTP=findViewById(R.id.btn_request_otp);
        tvConsumer=findViewById(R.id.label_consumer);
        tvConsumer2=findViewById(R.id.label_consumer2);
        tvPartner=findViewById(R.id.label_partner);
        tvPartner2=findViewById(R.id.label_partner2);
        loginText=findViewById(R.id.label_account_desc);

        TypedValue selectedTextColor = new TypedValue();
        getApplicationContext().getTheme().resolveAttribute(R.attr.homeCardTextColorSelected, selectedTextColor, true);

        if (type.equals("consumer")){


            crdPartner.setBackgroundResource(R.drawable.cardview_bg);
            crdConsumer.setBackgroundResource(R.drawable.cardview_bg_selected);

            tvConsumer.setTextColor(getResources().getColor(R.color.white));
            tvConsumer2.setTextColor(getResources().getColor(R.color.white));

            TypedValue typedValue = new TypedValue();
            getTheme().resolveAttribute(R.attr.homeCardTextColorUnSelected, typedValue, true);
            int color = ContextCompat.getColor(getApplicationContext(), typedValue.resourceId);

            tvPartner.setTextColor(color);
            tvPartner2.setTextColor(color);

        }

        crdConsumer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                type="consumer";
                crdPartner.setBackgroundResource(R.drawable.cardview_bg);
                crdConsumer.setBackgroundResource(R.drawable.cardview_bg_selected);

                tvConsumer.setTextColor(getResources().getColor(R.color.white));
                tvConsumer2.setTextColor(getResources().getColor(R.color.white));

                TypedValue typedValue = new TypedValue();
                getTheme().resolveAttribute(R.attr.homeCardTextColorUnSelected, typedValue, true);
                int color = ContextCompat.getColor(getApplicationContext(), typedValue.resourceId);

                tvPartner.setTextColor(color);
                tvPartner2.setTextColor(color);


            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent login=new Intent(RegistrationTypeActivity.this,LoginActivity.class);
                startActivity(login);

            }
        });

        crdPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                type="partner";
                crdConsumer.setBackgroundResource(R.drawable.cardview_bg);
                crdPartner.setBackgroundResource(R.drawable.cardview_bg_selected);

                tvPartner.setTextColor(getResources().getColor(R.color.white));
                tvPartner2.setTextColor(getResources().getColor(R.color.white));

                TypedValue typedValue = new TypedValue();
                getTheme().resolveAttribute(R.attr.homeCardTextColorUnSelected, typedValue, true);
                int color = ContextCompat.getColor(getApplicationContext(), typedValue.resourceId);

                tvConsumer.setTextColor(color);
                tvConsumer2.setTextColor(color);

            }
        });

        btnRequestOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(type==null){

                    Toast.makeText(getApplicationContext(), "please select your registration type", Toast.LENGTH_SHORT).show();

                }else if(type.equals("consumer")){

                    Intent moveConsumerRegistration=new Intent(getApplicationContext(),RegisterActivity.class);
                    moveConsumerRegistration.putExtra("userType",type);
                    startActivity(moveConsumerRegistration);
                  //  finish();
                }


                else if(type.equals("partner")){

                    Intent moveConsumerRegistration=new Intent(getApplicationContext(),RegisterActivity.class);
                    moveConsumerRegistration.putExtra("userType",type);
                    startActivity(moveConsumerRegistration);
                    //finish();
                }


            }
        });


    }
}