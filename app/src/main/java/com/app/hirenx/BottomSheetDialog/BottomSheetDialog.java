package com.app.hirenx.BottomSheetDialog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.app.hirenx.ConsumerProfile.ClientHireSearchListActivity;
import com.app.hirenx.PartnerProfile.PartnerProfileSetup2Activity;
import com.app.hirenx.PartnerProfile.PartnerProfileSetupActivity;
import com.app.hirenx.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    private String todaysDate, expireDates, id, getExpireDates;
    private String phoneNumber, email;
    private String price;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private Button btnPayPartner;
    private ProgressDialog pd;
    private Boolean isBefore;

    @Override
    public int getTheme() {
        return R.style.NoBackgroundDialogTheme;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.bottom_sheet,
                container, false);
        //this.getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialogsheet);
        v.setBackgroundResource(R.drawable.dialogsheet);

        btnPayPartner = v.findViewById(R.id.btn_hire_client_search_list);
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        GetPrice();
        GetPaymentsPlans();
        Calendar c = Calendar.getInstance();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime expireDate = LocalDateTime.now().plusHours(24);

        todaysDate = dtf.format(now).toString();
        expireDates = dtf.format(expireDate).toString();

        pd = new ProgressDialog(getContext());
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Please wait...");

        //AddExpiryDate(expireDates);

        btnPayPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pd.show();

                if (getExpireDates != null) {

                    //Toast.makeText(getContext(), ""+LocalDateTime.parse(now.format(dtf)), Toast.LENGTH_SHORT).show();
                    isBefore = now.isBefore(LocalDateTime.parse(getExpireDates, dtf));

                    if (isBefore == true) {

                        Toast.makeText(getContext(), "Its before", Toast.LENGTH_SHORT).show();

                    } else if (isBefore == false) {

                        Toast.makeText(getContext(), "Its after", Toast.LENGTH_SHORT).show();

                        Payment();

                    }


                } else {

                    Payment();
                }

            }
        });

        return v;
    }

    private void GetPrice() {

        firestore.collection("price").document("0GsjKOGv5kkTfGieZf46").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                price = task.getResult().getString("price");
                GetPlanExpireDate();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    void Payment() {

        String samount = String.valueOf(price);
        int amount = Math.round(Float.parseFloat(samount) * 100);

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_DXOgPYJ7mPhPmn");
        checkout.setImage(R.drawable.hnxlogo);

        JSONObject object = new JSONObject();
        try {

            object.put("name", "HireNx");
            object.put("description", "Service Order Payment");
            object.put("theme.color", "");
            object.put("amount", amount);
            object.put("prefill.contact", phoneNumber);
            object.put("prefill.email", email);

            pd.dismiss();

            checkout.open(getActivity(), object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void AddExpiryDate(String expireDates) {

        pd.show();
        FirebaseUser user = mAuth.getCurrentUser();

        id = user.getPhoneNumber();

        Map<String, Object> userProfileMap = new HashMap<>();
        userProfileMap.put("planExpireDate", expireDates);

        firestore.collection("users")
                .document(id)
                .update(userProfileMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            pd.dismiss();

                            Toast.makeText(getActivity().getApplicationContext(), "Successfull", Toast.LENGTH_SHORT).show();
                            //Payment();

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void GetPlanExpireDate() {

        FirebaseUser user = mAuth.getCurrentUser();

        id = user.getPhoneNumber();

        firestore.collection("users").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isComplete()) {

                    getExpireDates = task.getResult().getString("planExpireDate");

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void GetPaymentsPlans() {

        FirebaseUser user = mAuth.getCurrentUser();

        id = user.getPhoneNumber();

        firestore.collection("users").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isComplete()) {

                    phoneNumber = task.getResult().getString("phoneNumber");
                    email = task.getResult().getString("email");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
