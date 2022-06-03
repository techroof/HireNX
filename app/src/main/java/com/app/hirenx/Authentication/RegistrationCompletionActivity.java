package com.app.hirenx.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.hirenx.ConsumerProfile.ClientProfileSetupActivity;
import com.app.hirenx.R;
import com.app.hirenx.PartnerProfile.PartnerProfileSetupActivity;

public class RegistrationCompletionActivity extends AppCompatActivity {

    private Button btnProfileCompletion;
    private String registrarType,phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_completion);

        btnProfileCompletion=findViewById(R.id.btn_profile_completion);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {


            registrarType= extras.getString("registrerType");
            phoneNumber=extras.getString("phoneNumber");
        }

        btnProfileCompletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(registrarType.equals("consumer")){

                    Intent movetoClientProfileSetup =new Intent(getApplicationContext(), ClientProfileSetupActivity.class);
                    movetoClientProfileSetup.putExtra("registerType",registrarType);
                    movetoClientProfileSetup.putExtra("phoneNumber",phoneNumber);
                    startActivity(movetoClientProfileSetup);

                }else if(registrarType.equals("partner")){

                    Intent movetoPartnerProfileSetup =new Intent(getApplicationContext(), PartnerProfileSetupActivity.class);
                    movetoPartnerProfileSetup.putExtra("registerType",registrarType);
                    movetoPartnerProfileSetup.putExtra("phoneNumber",phoneNumber);
                    startActivity(movetoPartnerProfileSetup);
                }
            }
        });


    }
}