package com.android.medipass;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import java.util.List;

public class PrescriptionsActivity extends AppCompatActivity {

    private static final String TAG = "PrescriptionsActivity";
    private List<Prescription> myPrescriptionsValid = new ArrayList<>();
    private List<Prescription> myPrescriptionsExpired = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescriptions);

        getIncomingIntent();
    }

    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");
        if (getIntent().hasExtra("pid")) {
            Log.d(TAG, "getIncomingIntent: found intent extras.");
            String pid = getIntent().getStringExtra("pid");
            getPrescriptions(pid);
        }
    }

    private void initRecyclerViewAvailablePs() {
        Log.d(TAG, "initRecyclerView: init RecyclerView.");
        RecyclerView recyclerView = findViewById(R.id.recycleViewAvailableP);
        PrescriptionsRVA prescriptionsRVA = new PrescriptionsRVA(this, myPrescriptionsValid);
        recyclerView.setAdapter(prescriptionsRVA);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initRecyclerViewExpiredPs() {
        Log.d(TAG, "initRecyclerView: init RecyclerView.");
        RecyclerView recyclerView = findViewById(R.id.recycleViewExpiredP);
        PrescriptionsRVA prescriptionsRVA = new PrescriptionsRVA(this, myPrescriptionsExpired);
        recyclerView.setAdapter(prescriptionsRVA);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getPrescriptions(String pid) {

        String getPrescriptionsURI = "https://medipass-server.herokuapp.com/api/user/"+pid+"/prescriptions";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getPrescriptionsURI, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("prescriptions");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject p = jsonArray.getJSONObject(i);

                                Prescription myPrescription = new Prescription(
                                        p.getInt("PrescriptionID"),
                                        p.getInt("Status"),
                                        p.getInt("MedicineID"),
                                        p.getInt("Quantity"),
                                        p.getString("ExpiryDate").substring(0,10),
                                        p.getString("Fname"),
                                        p.getString("Sname"),
                                        p.getString("Name"),
                                        p.getString("Type")
                                );

                                if (myPrescription.getStatus() == 0) {
                                    myPrescriptionsExpired.add(myPrescription);
                                }

                                else {
                                    myPrescriptionsValid.add(myPrescription);
                                }

                            }

                            initRecyclerViewAvailablePs();
                            initRecyclerViewExpiredPs();
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

        MySingleton.getInstance(PrescriptionsActivity.this).addToRequestQueue(jsonObjectRequest);

    }
}
