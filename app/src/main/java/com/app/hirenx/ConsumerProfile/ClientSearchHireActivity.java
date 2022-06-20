package com.app.hirenx.ConsumerProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.hirenx.Interfaces.JsonPlaceHolderAPI;
import com.app.hirenx.Models.City;
import com.app.hirenx.PartnerProfile.PartnerProfileSetupActivity;
import com.app.hirenx.R;
import com.app.hirenx.SearchDialog.SearchDialogs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientSearchHireActivity extends AppCompatActivity {

    private static String BASE_URL = "https://api.countrystatecity.in/v1/";
    private List<City> cityArrayList;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private String uId, city, skill;
    private ProgressDialog pd;
    private ArrayList<String> skillArraylist, cityNameList;
    ArrayList<SearchDialogs> cityList;
    ArrayList<SearchDialogs> skillList;
    private EditText etCityVillage, etSkills;
    private Button btnHireSearch;
    private ImageView imgBackToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_search_hire);

        pd = new ProgressDialog(ClientSearchHireActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Please wait...");
        etCityVillage = findViewById(R.id.et_search_city);
        etSkills = findViewById(R.id.et_search_skills);
        imgBackToHome = findViewById(R.id.img_move_towards_hire);
        btnHireSearch = findViewById(R.id.btn_hire_search);
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        uId = firebaseAuth.getUid();


        etCityVillage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new SimpleSearchDialogCompat(ClientSearchHireActivity.this, "Search...",
                        "What are you looking for...?", null, cityList, new SearchResultListener<SearchDialogs>() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat dialog, SearchDialogs item, int position) {

                        city = item.getTitle();
                        etCityVillage.setText(city);
                        dialog.dismiss();

                    }
                }).show();
               /* new AlertDialog.Builder(ClientSearchHireActivity.this).setTitle("Select Your City")
                        .setSingleChoiceItems(cityNameList.toArray(new String[cityNameList.size()]), 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etCityVillage.setText(cityNameList.get(selectedPosition));
                                city = cityNameList.get(selectedPosition);

                            }


                        })
                        .show();*/

            }

        });

        etSkills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SimpleSearchDialogCompat(ClientSearchHireActivity.this, "Search...",
                        "What are you looking for...?", null, skillList, new SearchResultListener<SearchDialogs>() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat dialog, SearchDialogs item, int position) {

                        skill = item.getTitle();
                        etSkills.setText(skill);
                        dialog.dismiss();
                    }
                }).show();

                imgBackToHome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        onBackPressed();
                    }
                });

               /* new AlertDialog.Builder(ClientSearchHireActivity.this).setTitle("Select Your City")
                        .setSingleChoiceItems(skillArraylist.toArray(new String[skillArraylist.size()]), 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etSkills.setText(skillArraylist.get(selectedPosition));
                                skill = skillArraylist.get(selectedPosition);

                            }


                        })
                        .show();*/

            }
        });

        //getCities(101);
        getAllCities();
        skillsList();

        btnHireSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (city != null && skill != null) {

                    Intent moveToPartnersList = new Intent(getApplicationContext(), ClientHireSearchListActivity.class);
                    moveToPartnersList.putExtra("city", city);
                    moveToPartnersList.putExtra("skill", skill);
                    startActivity(moveToPartnersList);

                } else {

                    Toast.makeText(ClientSearchHireActivity.this, "Please Fill the Above fields!", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private void getAllCities() {

        cityList = new ArrayList<>();
        firestore.collection("cities").
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String partnerCity = document.getString("cityName");
                                cityList.add(new SearchDialogs(partnerCity));

                            }


                        } else {

                            Log.d("d", "Error getting documents: ", task.getException());

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void skillsList() {

        skillList = new ArrayList<>();
        firestore.collection("skills").
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String skillName = document.getString("skill");
                                skillList.add(new SearchDialogs(skillName));


                            }


                        } else {

                            Log.d("d", "Error getting documents: ", task.getException());

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }


    private void getCities(int countryId) {

        cityList = new ArrayList<>();

        pd.show();

        // countryArraylist = new ArrayList<>();
        cityArrayList = new ArrayList<>();
        cityNameList = new ArrayList<>();
        //cityArrayList=new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        JsonPlaceHolderAPI jsonPlaceHolderAPI = retrofit.create(JsonPlaceHolderAPI.class);
        Call<List<City>> callcity = jsonPlaceHolderAPI.getCitiess();

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
                    cityList.add(new SearchDialogs(cityy.getName()));
                    //content += "id:" + cities.getId() + "\n";
                    //content += "name:" + cityy.getName() + "\n";
                    //content += "state:" + states.getIso2() + "\n";

                }
                pd.dismiss();
                etCityVillage.setEnabled(true);
                // Toast.makeText(getApplicationContext(), "Now you can select cities", Toast.LENGTH_SHORT).show();

                //Toast.makeText(getApplicationContext(), "" + content, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }


}