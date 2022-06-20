package com.app.hirenx.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hirenx.ConsumerProfile.HomePageClientActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;
import com.app.hirenx.R;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private String login;
    private FirebaseAuth mAuth;
    private String phNumber, verificationId;
    private EditText edtNumberLogin;
    private Button btnOTP, btnSignUp;
    private ProgressDialog pd;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        imgBack = findViewById(R.id.img_move_towards_registration_type);

        pd = new ProgressDialog(LoginActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Sending OTP...");

        login = "login";

        mAuth = FirebaseAuth.getInstance();
        btnOTP = findViewById(R.id.btn_request_otp);
        edtNumberLogin = findViewById(R.id.edt_phone_number_login);
        btnSignUp = findViewById(R.id.btn_move_towards_signup);

        edtNumberLogin.setText("+");


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent moveToRegister = new Intent(getApplicationContext(), RegistrationTypeActivity.class);
                startActivity(moveToRegister);
            }
        });




        btnOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                phNumber = "+"+edtNumberLogin.getText().toString();

                if (TextUtils.isEmpty(edtNumberLogin.getText())){

                    edtNumberLogin.setError("Enter Phone Number");

                }else{

                    pd.show();

                    final FirebaseFirestore db = FirebaseFirestore.getInstance();
                    final DocumentReference docRef = db.collection("users").document(phNumber);
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(final DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                //redirect to home page

                                sendVerificationCode(phNumber);




                            } else {

                                pd.dismiss();

                                Toast.makeText(getApplicationContext(), "Please Register Your Account First!", Toast.LENGTH_SHORT).show();
                                //redirect to sign up page
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }

                //final FirebaseUser user = task.getResult().getUser();

            }
        });
    }

    //otp generator
    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // callback method is called on Phone auth provider.
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            pd.dismiss();

            mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
            verificationId = s;
            Intent intent = new Intent(LoginActivity.this, OTPActivity.class);
            intent.putExtra("phoneNumber", phNumber);
            intent.putExtra("verificationId", verificationId);
            intent.putExtra("authenticationType", "login");
            startActivity(intent);

        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {


        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            pd.dismiss();
            Toast.makeText(getApplicationContext(), "Sorry Code Has Not Been Sent!" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
}