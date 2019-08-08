package com.android.medipass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Doctors extends AppCompatActivity {

    private static final String TAG = "Doctors";
    private ArrayList<Doctor> doctors = new ArrayList<>();
    private ArrayList<MyDoctor> myDoctors = new ArrayList<>();

    String pid;
    EditText searchID;
    RecyclerView recyclerView_AD, recyclerView_MD;
    private TabLayout tabLayout;
    TabItem tabMyDoctors, tabAllDoctors;
    private ViewPager viewPager;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);

        Log.d(TAG, "onCreate: ");
        getIncomingIntent();
        findMyDoctors();

        //Grab components
        tabLayout = (TabLayout) this.findViewById(R.id.tablayout_id);
        tabAllDoctors = (TabItem) this.findViewById(R.id.tabAllDoctors);
        tabMyDoctors = (TabItem) this.findViewById(R.id.tabMyDoctors);
        viewPager = (ViewPager) this.findViewById(R.id.viewpager_id);
        progressBar = (ProgressBar) this.findViewById(R.id.progress_bar);
        searchID = (EditText) this.findViewById(R.id.searchID);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        searchID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                doctors.clear();

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                progressBar.setVisibility(View.VISIBLE);
                findDoctors(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");
        if (getIntent().hasExtra("pId")) {
            Log.d(TAG, "getIncomingIntent: found intent extras.");
            pid = getIntent().getStringExtra("pId");

        }
    }


    private void findDoctors(CharSequence charSequence) {

        String getDoctorsSQL = "https://medipass-server.herokuapp.com/api/findDoctor/" + charSequence + "";


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getDoctorsSQL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        doctors.clear();
                        try {
                            JSONArray jsonArray = response.getJSONArray("doctors");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject c = jsonArray.getJSONObject(i);

                                Doctor doctor = new Doctor(
                                        c.getInt("MedPractionerID"),
                                        c.getInt("AddressID"),
                                        c.getString("FName"),
                                        c.getString("SName"),
                                        c.getString("Email"),
                                        c.getString("FieldOdSpecialization")
                                );

                                doctors.add(doctor);

                            }
                            progressBar.setVisibility(View.GONE);
                            initRecyclerView();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                doctors.clear();


                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(getApplicationContext(), "timeout", Toast.LENGTH_LONG).show();

                } else if (error instanceof AuthFailureError) {
                    //TODO


                    Toast.makeText(getApplicationContext(), "AuthFailure", Toast.LENGTH_SHORT).show();


                } else if (error instanceof ServerError) {
                    //TODO

                    Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    //TODO
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();


                } else if (error instanceof ParseError) {
                    //TODO


                }
            }
        });

        MySingleton.getInstance(Doctors.this).addToRequestQueue(jsonObjectRequest);


    }

    private void findMyDoctors() {

        String findMyDoctorsURI = "https://medipass-server.herokuapp.com/api/user/232/connections";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, findMyDoctorsURI, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("myDoctors");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject md = jsonArray.getJSONObject(i);

                                MyDoctor myDoctor = new MyDoctor(

                                        md.getInt("MedPractionerID"),
                                        md.getInt("ConsentStatus"),
                                        md.getInt("RegNumber"),
                                        md.getString("CreatedAt"),
                                        md.getString("ExpireAt"),
                                        md.getString("FName"),
                                        md.getString("SName"),
                                        md.getString("Email"),
                                        md.getString("PhNumber"),
                                        md.getString("FieldOdSpecialization"),
                                        md.getInt("AddressID")

                                );

                                myDoctors.add(myDoctor);


                            }

                            progressBar.setVisibility(View.GONE);
                            initRecyclerViewMyDoctors();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(getApplicationContext(), "timeout", Toast.LENGTH_LONG).show();

                } else if (error instanceof AuthFailureError) {
                    //TODO


                    Toast.makeText(getApplicationContext(), "AuthFailure", Toast.LENGTH_SHORT).show();


                } else if (error instanceof ServerError) {
                    //TODO

                    Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    //TODO
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();


                } else if (error instanceof ParseError) {
                    //TODO

                    Toast.makeText(getApplicationContext(), "parsererror", Toast.LENGTH_SHORT).show();


                }
            }
        });

        MySingleton.getInstance(Doctors.this).addToRequestQueue(jsonObjectRequest);

    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init RecyclerView.");
        recyclerView_AD = findViewById(R.id.recyclerView_doctors);
        RecyclerViewAdapterFindD recyclerViewAdapterFindD = new RecyclerViewAdapterFindD(this, doctors);
        recyclerView_AD.setAdapter(recyclerViewAdapterFindD);
        recyclerView_AD.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initRecyclerViewMyDoctors() {
        Log.d(TAG, "initRecyclerView: init RecyclerView.");
        recyclerView_MD = findViewById(R.id.recyclerView_myDoctors);
        RecyclerViewAdapterMyDoctor recyclerViewAdapterMyDoctor = new RecyclerViewAdapterMyDoctor(this, myDoctors);
        recyclerView_MD.setAdapter(recyclerViewAdapterMyDoctor);
        recyclerView_MD.setLayoutManager(new LinearLayoutManager(this));
    }

}
