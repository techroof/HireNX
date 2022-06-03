package com.app.hirenx.PartnerProfile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.hirenx.R;

public class CompletionProfilePartnerActivity extends AppCompatActivity {

    private Button btnProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completion_profile_partner);
        btnProfile=findViewById(R.id.btn_profile);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent movetoProfilePagePartner=new Intent(getApplicationContext(),ProfilePagePartnerActivity.class);

                startActivity(movetoProfilePagePartner);
            }
        });
    }
}