package com.android.medipass;

import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private ArrayList<Contact> myContacts = new ArrayList<>();
    RecyclerView recyclerView;
    private TabLayout tabLayout;
    TabItem tabBasicInfo, tabMyContacts;
    private ViewPager viewPager;
    private Profile profile;
    private ProgressBar dCountLoaderPB, cCountLoaderPB;
    private TextView dCountTV, cCountTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getIncomingIntent();

        //Grab components
        tabLayout = (TabLayout) this.findViewById(R.id.tablayout_id);
        tabBasicInfo = (TabItem) this.findViewById(R.id.tabDoctorInfo);
        tabMyContacts = (TabItem) this.findViewById(R.id.myContactsTab);
        viewPager = (ViewPager) this.findViewById(R.id.viewpager_id);

        dCountLoaderPB = this.findViewById(R.id.dCountLoaderPB);
        cCountLoaderPB = this.findViewById(R.id.cCountLoaderPB);

        dCountTV = this.findViewById(R.id.dCountTV);
        cCountTV = this.findViewById(R.id.cCountTV);

//        getContacts();


    }

    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");
        if (getIntent().hasExtra("pid")) {
            Log.d(TAG, "getIncomingIntent: found intent extras.");
            String pid = getIntent().getStringExtra("pid");
            getProfileInfo(pid);
            getProfileStats(pid);
        }
    }

    private void getProfileStats(String pid) {

        String getProfileStatsURI = "https://medipass-server.herokuapp.com/api/user/"+pid+"/profileStats";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getProfileStatsURI, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            JSONObject statsObject = response.getJSONObject("stats");

                            int myDoctorsCount = statsObject.getInt("countD");
                            int myConsultationsCount = statsObject.getInt("countC");

                            cCountLoaderPB.setVisibility(View.GONE);
                            dCountLoaderPB.setVisibility(View.GONE);

                            cCountTV.setText(Integer.toString(myConsultationsCount));
                            dCountTV.setText(Integer.toString(myDoctorsCount));


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

                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                    //TODO


                }
            }
        });

        MySingleton.getInstance(ProfileActivity.this).addToRequestQueue(jsonObjectRequest);

    }

    private void getProfileInfo(String pid) {

        String getProfileInfoURI = "https://medipass-server.herokuapp.com/api/user/"+pid+"/getUserInfo";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getProfileInfoURI, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {

                            Profile profile = new Profile(
                                   response.getString("Dob"),
                                   response.getString("Sex"),
                                   response.getString("Phone_Home"),
                                   response.getString("Phone_Mobile"),
                                   response.getString("BloodType"),
                                   response.getInt("Smoker"),
                                   response.getInt("Drinker"),
                                   response.getInt("Height"),
                                   response.getInt("Weight"),
                                    response.getInt("DNR")
                            );

                            ViewPagerAdapterProfile viewPagerAdapter = new ViewPagerAdapterProfile(getSupportFragmentManager(), tabLayout.getTabCount(), profile);

                            viewPager.setAdapter(viewPagerAdapter);

                            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));



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


                }
            }
        });

        MySingleton.getInstance(ProfileActivity.this).addToRequestQueue(jsonObjectRequest);

    }

//    private void getContacts() {
//
//
//        String getContactsURI = "https://medipass-server.herokuapp.com/api/user/232/getAllContacts";
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getContactsURI, null,
//                new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
//                        try {
//                            JSONArray jsonArray = response.getJSONArray("myContacts");
//
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject myContact = jsonArray.getJSONObject(i);
//
//                                Contact contact = new Contact(
//
//                                        myContact.getInt("contactID"),
//                                        myContact.getInt("phNumber"),
//                                        myContact.getString("contactName")
//
//                                );
//
//                                myContacts.add(contact);
//
//                            }
//                            initRecyclerView();
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//
//                    Toast.makeText(getApplicationContext(), "timeout", Toast.LENGTH_LONG).show();
//
//                } else if (error instanceof AuthFailureError) {
//                    //TODO
//
//
//                    Toast.makeText(getApplicationContext(), "AuthFailure", Toast.LENGTH_SHORT).show();
//
//
//                } else if (error instanceof ServerError) {
//                    //TODO
//
//                    Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
//
//                } else if (error instanceof NetworkError) {
//                    //TODO
//                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
//
//
//                } else if (error instanceof ParseError) {
//                    //TODO
//
//                    Toast.makeText(getApplicationContext(), "parsererror", Toast.LENGTH_SHORT).show();
//
//
//                }
//            }
//        });
//
//        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
//
//    }

//    private void initRecyclerView() {
//
//        Toast.makeText(getApplicationContext(), Integer.toString(myContacts.size()), Toast.LENGTH_SHORT).show();
//        Log.d(TAG, "initRecyclerView: init RecyclerView.");
//        recyclerView = findViewById(R.id.recyclerView_contacts);
//        RVAEmergContacts rvaEmergContacts = new RVAEmergContacts(getApplicationContext(), myContacts);
//        recyclerView.setAdapter(rvaEmergContacts);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//    }
}
