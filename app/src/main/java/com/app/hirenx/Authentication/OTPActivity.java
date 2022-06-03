package com.app.hirenx.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hirenx.ConsumerProfile.HomePageClientActivity;
import com.app.hirenx.HomeActivity;
import com.app.hirenx.PartnerProfile.ProfilePagePartnerActivity;
import com.app.hirenx.R;
import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {

    private ImageView imgMoveRegistration;

    private PinView pinView;

    private String getcod, verificationId,userType, enterCode, resendCodeVerificationId,uId;

    private Button btnVerify;

    FirebaseAuth firebaseAuth;

    FirebaseFirestore firestore;

    private String phoneNumber, authentication, registrarType, resentPhoneNumber;

    private TextView tvPhoneNumber;

    private TextView tvResendCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);

        imgMoveRegistration = findViewById(R.id.img_move_towards_registration);

        pinView = findViewById(R.id.pin_view);

        btnVerify = findViewById(R.id.btn_verify);

        tvPhoneNumber = findViewById(R.id.label_phone_number);

        tvResendCode = findViewById(R.id.tv_code_desc);

        firebaseAuth = FirebaseAuth.getInstance();

        firestore = FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();

        getcod = getIntent().getStringExtra("backendotp");

        if (extras != null) {


            verificationId = extras.getString("code");

            phoneNumber = extras.getString("phoneNumber");

            verificationId = extras.getString("verificationId");

            authentication = extras.getString("authentication");

            registrarType = extras.getString("registrerType");

            tvPhoneNumber.setText(phoneNumber);

            //Toast.makeText(getApplicationContext(), "" + registrarType + authentication, Toast.LENGTH_SHORT).show();

            // and get whatever type user account id is
        }

        imgMoveRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent moveRegistration = new Intent(getApplicationContext(), RegisterActivity.class);

                startActivity(moveRegistration);*/

                onBackPressed();

            }
        });

        tvResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resentPhoneNumber = tvPhoneNumber.getText().toString();

                sendVerificationCode(resentPhoneNumber);

                Toast.makeText(getApplicationContext(), "resending code please wait...", Toast.LENGTH_SHORT).show();
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enterCode = pinView.getText().toString();

                verifyCode(enterCode);
            }
        });
    }


    private void verifyCode(String code) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.
                            /*Intent i = new Intent(OtpApproval.this, HomeScreen.class);
                            startActivity(i);*/

                            if(authentication!=null){

                                if (authentication.equals("login")) {

                                /*Intent movetoHomeActivity =new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(movetoHomeActivity);*/

                                    //signinwith credentials

                                    final FirebaseUser user = task.getResult().getUser();
                                    String uid = user.getUid();
                                    final FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    final DocumentReference docRef = db.collection("users").document(phoneNumber);
                                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(final DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                //redirect to home page

                                                userType=documentSnapshot.getString("userType");

                                                if(userType.equals("consumer")){


                                                    Intent movetoHomeActivity = new Intent(getApplicationContext(), HomePageClientActivity.class);

                                                    startActivity(movetoHomeActivity);

                                                    Toast.makeText(OTPActivity.this, "Successfully logged in", Toast.LENGTH_LONG).show();

                                                }else{


                                                    Intent movetoProfilePagePartner = new Intent(getApplicationContext(), ProfilePagePartnerActivity.class);

                                                    startActivity(movetoProfilePagePartner);

                                                    Toast.makeText(OTPActivity.this, "Successfully logged in", Toast.LENGTH_LONG).show();

                                                }

                                            } else {

                                                Intent movetoHomeActivity = new Intent(getApplicationContext(), RegisterActivity.class);
                                                startActivity(movetoHomeActivity);
                                                Toast.makeText(OTPActivity.this, "Please Register Yourself First", Toast.LENGTH_LONG).show();

                                                //redirect to sign up page
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }

                                //------------------------------------------\\

                            } else if (registrarType!=null&&registrarType.equals("consumer")) {

                                Toast.makeText(OTPActivity.this, "Your account has been created", Toast.LENGTH_LONG).show();

                                Intent movetoCompletionActivity = new Intent(getApplicationContext(), RegistrationCompletionActivity.class);
                                movetoCompletionActivity.putExtra("registrerType", registrarType);
                                movetoCompletionActivity.putExtra("phoneNumber",phoneNumber);
                                startActivity(movetoCompletionActivity);

                            } else if (registrarType!=null&&registrarType.equals("partner")) {

                                AddStatus(registrarType);


                                Intent movetoCompletionActivity = new Intent(getApplicationContext(), RegistrationCompletionActivity.class);
                                movetoCompletionActivity.putExtra("registrerType", registrarType);
                                movetoCompletionActivity.putExtra("phoneNumber",phoneNumber);
                                startActivity(movetoCompletionActivity);

                            } else {

                                AddStatus(registrarType);


                                Intent movetoCompletionActivity = new Intent(getApplicationContext(), RegistrationCompletionActivity.class);
                                movetoCompletionActivity.putExtra("registrerType", registrarType);
                                movetoCompletionActivity.putExtra("phoneNumber",phoneNumber);
                                startActivity(movetoCompletionActivity);

                            }


                            //finish();
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(OTPActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    //otp generator
    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
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
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {


        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void AddStatus(String userType) {

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

        uId=user.getPhoneNumber();
        Map<String, Object> userTypeMap = new HashMap<>();
        userTypeMap.put("userType", userType);

        firestore.collection("users")
                .document(uId)
                .set(userTypeMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(OTPActivity.this, "Your account has been created", Toast.LENGTH_LONG).show();

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "Failed To Create Account", Toast.LENGTH_SHORT).show();


            }
        });

    }
}

