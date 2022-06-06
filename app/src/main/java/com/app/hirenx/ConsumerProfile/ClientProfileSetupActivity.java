package com.app.hirenx.ConsumerProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.app.hirenx.Interfaces.JsonPlaceHolderAPI;
import com.app.hirenx.Models.City;
import com.app.hirenx.Models.States;
import com.app.hirenx.PartnerProfile.PartnerProfileSetup2Activity;
import com.app.hirenx.PartnerProfile.PartnerProfileSetupActivity;
import com.app.hirenx.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientProfileSetupActivity extends AppCompatActivity {

    private String BASE_URL = "https://api.countrystatecity.in/v1/";
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private List<String> statesnameList;
    private List<States> statesList;
    private List<String> countryNameList;
    private List<String> cityNameList;
    private List<City> cityArrayList;
    private EditText etFullName,etEmail,etGender,etdob,etMaritalStatus,etDocumentType,etDocumentIDNumber,etAddress,etAddress2,etLandMark,etPincode,etCity,etState;
    private String fullName,email,gender,dob,maritalStatus,documentType,documentIDNumber,address,address2,landMark,pinCode,city,state,stateId,id;
    private Button btnClientSetup1;
    private String[] genderList,maritalStatusList,documentTypeList;
    final Calendar myCalendar = Calendar.getInstance();
    private ProgressDialog pd;
    private String registerType,uId,phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile_setup);
        genderList = getResources().getStringArray(R.array.gender);
        maritalStatusList = getResources().getStringArray(R.array.maritalstatus);
        documentTypeList = getResources().getStringArray(R.array.documentType);

        firestore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        pd = new ProgressDialog(ClientProfileSetupActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Please wait...");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            registerType= extras.getString("registerType");
            phoneNumber=extras.getString("phoneNumber");

        }else{

            phoneNumber=user.getPhoneNumber();

        }

        //firestore initialization


        uId=mAuth.getCurrentUser().getUid();
        //calender date

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateDate();
            }
        };

        btnClientSetup1=findViewById(R.id.btn_client_proceed_step1);

        etFullName=findViewById(R.id.et_client_fullname);
        etEmail=findViewById(R.id.et_client_email);
        etGender=findViewById(R.id.et_client_gender);
        etdob=findViewById(R.id.et_client_dob);
        etMaritalStatus=findViewById(R.id.et_client_marital_status);
        etDocumentType=findViewById(R.id.et_client_document_type);
        etDocumentIDNumber=findViewById(R.id.et_client_document_id_number);
        etAddress=findViewById(R.id.et_client_address);
        etAddress2=findViewById(R.id.et_client_address2);
        etLandMark=findViewById(R.id.et_client_landmark);
        etPincode=findViewById(R.id.et_client_pincode);
        etCity=findViewById(R.id.et_client_city);
        etState=findViewById(R.id.et_client_state);

        etGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ClientProfileSetupActivity.this).setTitle("Select Your Gender")
                        .setSingleChoiceItems(genderList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etGender.setText(genderList[selectedPosition]);
                                gender= genderList[selectedPosition];
                            }
                        })
                        .show();
            }
        });

        etMaritalStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(ClientProfileSetupActivity.this).setTitle("Select Your Marital Status")
                        .setSingleChoiceItems(maritalStatusList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etMaritalStatus.setText(maritalStatusList[selectedPosition]);
                                maritalStatus= maritalStatusList[selectedPosition];
                            }
                        })
                        .show();
            }
        });

        etDocumentType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(ClientProfileSetupActivity.this).setTitle("Select Your Document Type")
                        .setSingleChoiceItems(documentTypeList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etDocumentType.setText(documentTypeList[selectedPosition]);
                                documentType= documentTypeList[selectedPosition];
                            }
                        })
                        .show();

            }
        });

        etdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DatePickerDialog(ClientProfileSetupActivity.this,R.style.DialogTheme, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });

        etState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new AlertDialog.Builder(ClientProfileSetupActivity.this).setTitle("Select Your State")
                        .setSingleChoiceItems(statesnameList.toArray(new String[statesnameList.size()]), 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etState.setText(statesnameList.get(selectedPosition));
                                state = statesnameList.get(selectedPosition);

                                stateId= statesList.get(selectedPosition).getIso2();


                                getCities(101,stateId);

                            }


                        })
                        .show();
            }

        });

        etCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(state)) {

                    etCity.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "please select your country and state first", Toast.LENGTH_SHORT).show();

                }else {

                    new AlertDialog.Builder(ClientProfileSetupActivity.this).setTitle("Select Your City")
                            .setSingleChoiceItems(cityNameList.toArray(new String[cityNameList.size()]), 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    //Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {

                                    dialog.dismiss();

                                    int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                    etCity.setText(cityNameList.get(selectedPosition));
                                    city = cityNameList.get(selectedPosition);
                                }
                            })
                            .show();
                }}

        });


        getStates(101);

        btnClientSetup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pd.show();

                fullName=etFullName.getText().toString();
                email=etEmail.getText().toString();
                documentIDNumber=etDocumentIDNumber.getText().toString();
                address=etAddress.getText().toString();
                address2=etAddress2.getText().toString();
                landMark=etLandMark.getText().toString();
                pinCode=etPincode.getText().toString();
                city=etCity.getText().toString();
                state=etState.getText().toString();
                dob=etdob.getText().toString();

                if (TextUtils.isEmpty(etFullName.getText().toString())) {

                    etFullName.setError("Please enter your full name");

                }

                if (TextUtils.isEmpty(etEmail.getText().toString())) {

                    etEmail.setError("Please enter email address");

                }

                if (TextUtils.isEmpty(etDocumentIDNumber.getText().toString())) {

                    etDocumentIDNumber.setError("Please enter your document id number");

                }

                if (TextUtils.isEmpty(etAddress.getText().toString())) {

                    etAddress.setError("Please enter your address");

                }

                if (TextUtils.isEmpty(etPincode.getText().toString())) {

                    etPincode.setError("Please Enter your pin code");

                }

                if (TextUtils.isEmpty(etCity.getText().toString())) {

                    etCity.setError("Please enter your City");

                }


                if (TextUtils.isEmpty(etState.getText().toString())) {

                    etState.setError("Please enter your state");

                }

                if(gender==null){

                    etGender.setError("Please enter your gender");

                }


                if(dob==null){

                    etdob.setError("Please enter your date of birth");

                }

                if(maritalStatus==null){

                    etMaritalStatus.setError("Please enter your marital status");

                }


                if(documentType==null){

                    etMaritalStatus.setError("Please enter your document type");

                }


                if (!TextUtils.isEmpty(etFullName.getText().toString()) && !TextUtils.isEmpty(etDocumentIDNumber.getText().toString()) && !TextUtils.isEmpty(etEmail.getText().toString())
                        && !TextUtils.isEmpty(etAddress.getText().toString()) && !TextUtils.isEmpty(etPincode.getText().toString())
                        && !TextUtils.isEmpty(etCity.getText().toString())
                        && !TextUtils.isEmpty(etState.getText().toString())
                        && !TextUtils.isEmpty(etGender.getText().toString())
                        && !TextUtils.isEmpty(etdob.getText().toString())
                        && !TextUtils.isEmpty(etMaritalStatus.getText().toString())
                        && !TextUtils.isEmpty(etDocumentType.getText().toString())) {


                    AddUser(fullName,email, documentIDNumber,address, landMark, pinCode, city, state,address2, gender, dob, maritalStatus,documentType,phoneNumber);

                }


            }
        });

    }

    private void updateDate(){

        String myFormat = "dd/MM/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        etdob.setText(dateFormat.format(myCalendar.getTime()));

    }

    private void AddUser(String fullName,String email,String documentIDNumber,String address, String landMark,String pinCode,String city, String state,String address2, String gender,String dateofbirth,String maritalStatus,String documentType,String phoneNumber){

        //FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

        id=user.getUid();

        Map<String, Object> userProfileMap = new HashMap<>();
        userProfileMap.put("fullName", fullName);
        userProfileMap.put("email", email);
        userProfileMap.put("documentIdNumber", documentIDNumber);
        userProfileMap.put("address", address);
        userProfileMap.put("pinCode", pinCode);
        userProfileMap.put("city", city);
        userProfileMap.put("state", state);
        userProfileMap.put("address2", address2);
        userProfileMap.put("landMark", landMark);
        userProfileMap.put("gender", gender);
        userProfileMap.put("dateofbirth", dateofbirth);
        userProfileMap.put("maritalStatus", maritalStatus);
        userProfileMap.put("documentType", documentType);
        userProfileMap.put("phoneNumber",phoneNumber);
        userProfileMap.put("stepStatus","1");
        userProfileMap.put("id",id);
        userProfileMap.put("activationStatus","normal");


        firestore.collection("users")
                .document(phoneNumber)
                .update(userProfileMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            pd.dismiss();

                            Toast.makeText(getApplicationContext(), ""+gender+documentType, Toast.LENGTH_SHORT).show();
                            Intent movetoPartnerSetup2=new Intent(ClientProfileSetupActivity.this, ClientProfileSetup2Activity.class);
                            movetoPartnerSetup2.putExtra("phoneNumber",phoneNumber);
                            startActivity(movetoPartnerSetup2);
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                            Toast.makeText(getApplicationContext(), "Step 1 completed!", Toast.LENGTH_SHORT).show();

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

    //states and cities

    private void getStates(int id) {

        pd.show();

        statesnameList=new ArrayList<>();
        statesList=new ArrayList<>();

        // Toast.makeText(getApplicationContext(), "Please wait while the states are getting", Toast.LENGTH_LONG).show();

        //countryArraylist = new ArrayList<>();

        // statesList=new ArrayList<>();
        //statesnameList = new ArrayList<>();

        etState.setEnabled(false);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        JsonPlaceHolderAPI jsonPlaceHolderAPI = retrofit.create(JsonPlaceHolderAPI.class);
        Call<List<States>> callstates = jsonPlaceHolderAPI.getStates();

        callstates.enqueue(new Callback<List<States>>() {
            @Override
            public void onResponse(Call<List<States>> call, Response<List<States>> response) {

                if (!response.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "" + response.code(), Toast.LENGTH_LONG).show();
                    etState.setEnabled(false);
                    pd.dismiss();

                    return;
                }

                List<States> states = response.body();
                //String content = "";

                for (States states1 : states) {
                    //names.add(countryStateCity.getName());
                    //countryStateCitieslist.add(countryStateCity);
                    statesnameList.add(states1.getName());
                    //countryStateCities.add(countryStateCity);
                    statesList.add(states1);
                }
                // Toast.makeText(getApplicationContext(), "size"+states.size(), Toast.LENGTH_SHORT).show();
                etState.setEnabled(true);
                etCity.setEnabled(false);
                pd.dismiss();
                // Toast.makeText(getApplicationContext(), "now you can select states", Toast.LENGTH_SHORT).show();

                /* for (States states : statess) {



                 *//*content += "id:" + states.getId() + "\n";
                    content += "name:" + states.getName() + "\n";
                    content += "state:" + states.getIso2() + "\n";*//*


                }*/

                //Toast.makeText(getApplicationContext(), "" + content, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<List<States>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    private void getCities(int countryId,String stateName) {

        pd.show();

        // countryArraylist = new ArrayList<>();
        cityArrayList=new ArrayList<>();
        cityNameList =new ArrayList<>();
        //cityArrayList=new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        JsonPlaceHolderAPI jsonPlaceHolderAPI = retrofit.create(JsonPlaceHolderAPI.class);
        Call<List<City>> callcity = jsonPlaceHolderAPI.getCities(countryId,stateName);

        callcity.enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {

                if (!response.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "" + response.code(), Toast.LENGTH_LONG).show();
                    pd.dismiss();
                    return;
                }

                List<City> cities = response.body();
                String content = "";
                for (City cityy : cities) {


                    cityNameList.add(cityy.getName());
                    //content += "id:" + cities.getId() + "\n";
                    //content += "name:" + cityy.getName() + "\n";
                    //content += "state:" + states.getIso2() + "\n";

                }
                pd.dismiss();
                etCity.setEnabled(true);
                // Toast.makeText(getApplicationContext(), "Now you can select cities", Toast.LENGTH_SHORT).show();

                //Toast.makeText(getApplicationContext(), "" + content, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }
}