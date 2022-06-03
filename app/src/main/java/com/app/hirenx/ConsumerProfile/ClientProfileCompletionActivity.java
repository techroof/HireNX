package com.app.hirenx.ConsumerProfile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.hirenx.PartnerProfile.ProfilePagePartnerActivity;
import com.app.hirenx.R;

public class ClientProfileCompletionActivity extends AppCompatActivity {

    private Button btnHire;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile_completion);
        btnHire=findViewById(R.id.btn_hire);

        btnHire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent movetoHirePagePartner=new Intent(getApplicationContext(), HomePageClientActivity.class);

                startActivity(movetoHirePagePartner);
            }
        });
    }
}