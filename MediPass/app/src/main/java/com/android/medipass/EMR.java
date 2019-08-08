package com.android.medipass;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class EMR extends AppCompatActivity {

    String cID;
    private ArrayList<MyMedicines> medicines = new ArrayList<>();
    TextView symptomsTV, diagnosticsTV, treatmentTV, bpressureTV, notesTV, mp_nameTV, dateTV;
    private static final String TAG = "EMR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emr);

        getIncomingIntent();

        Log.d(TAG, "onCreate: started.");
        symptomsTV = findViewById(R.id.emr_symptomsTV);
        diagnosticsTV = findViewById(R.id.emr_diagnosticTV);
        treatmentTV = findViewById(R.id.emr_treatmentTV);
        bpressureTV = findViewById(R.id.emr_bpressureTV);
        notesTV = findViewById(R.id.emr_notesTV);
        mp_nameTV = findViewById(R.id.emr_mp_nameTV);
        dateTV = findViewById(R.id.emr_dateTV);

    }

    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");
        if (getIntent().hasExtra("cID")) {
            Log.d(TAG, "getIncomingIntent: found intent extras.");

            cID = getIntent().getStringExtra("cID");
            getConsultations(cID);
        }
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init RecyclerView.");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapterEMRP adapterEMRP = new RecyclerViewAdapterEMRP(this, medicines);
        recyclerView.setAdapter(adapterEMRP);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void getConsultations(String cID) {

        String getEMR_URI = "https://medipass-server.herokuapp.com/api/emr/"+cID;


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getEMR_URI, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject emr = response.getJSONObject("emr");

                            String cid = emr.getString("cid");
                            String pid = emr.getString("pid");
                            String mpid = emr.getString("mpid");
                            String symptoms = emr.getString("symptoms");
                            String diagnostics = emr.getString("diagnostics");
                            String treatments = emr.getString("treatments");
                            String bpressure = emr.getString("bpressure");
                            String notes = emr.getString("notes");
                            String date = emr.getString("date").substring(0,10);
                            String mpFname = emr.getString("mpFname");
                            String mpSname = emr.getString("mpSname");
                            String prescid = emr.getString("prescid");
                            String expdate = emr.getString("expdate");
                            String status = emr.getString("status");

                            setUpLayout(mpFname, mpSname, date, diagnostics, symptoms, treatments, bpressure, notes);

                            JSONArray meds = emr.getJSONArray("medicines");

                            for (int i = 0; i<meds.length(); i++) {

                                JSONObject mObj = meds.getJSONObject(i);

                                int mid = mObj.getInt("mid");
                                int quantity = mObj.getInt("quantity");
                                String comments = mObj.getString("comments");
                                String mName = mObj.getString("mName");
                                String purpose = mObj.getString("purpose");
                                String type = mObj.getString("type");
                                String altNames = mObj.getString("altNames");
                                String mDesc = mObj.getString("mdesc");

                                MyMedicines myMeds = new MyMedicines(
                                        mid, quantity, comments, mName, purpose, type, altNames, mDesc);

                                medicines.add(myMeds);

                            }

                            initRecyclerView();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//                        try {
//
//                            //GETS the parent array
//                            JSONArray emrArray = response.getJSONArray("emr");
//
//                            for (int i = 0; i<emrArray.length(); i++) {
//
//                                //GET THE CONSULTATION OBJECT
//                                JSONObject emr = emrArray.getJSONObject(i);
//
//                                String symptoms = emr.getString("Symptoms");
//                                String diagnostics = emr.getString("Diagnostic");
//                                String treatment = emr.getString("Treatment");
//                                String bpressure = emr.getString("BPressure");
//                                String notes = emr.getString("Notes");
//                                String date = emr.getString("Date").substring(0,10);
//
//
//                                //GET THE PRESCRIPTION ARRAY
//                                JSONArray presArray = emr.getJSONArray("p");
//
//                                for (int z = 0; z<presArray.length(); z++) {
//                                    JSONObject p = presArray.getJSONObject(z);
//
//                                    JSONArray medArray = p.getJSONArray("pm");
//
//                                    int mID, quantity;
//                                    String comments, mName, purpose, type, altName, forms, desc;
//
//                                    for (int x = 0; x<medArray.length(); x++) {
//                                        JSONObject medp = medArray.getJSONObject(x);
//                                        JSONObject m = medp.getJSONObject("m");
//
//
//                                            mID = medp.getInt("MedicineID");
//                                            quantity = medp.getInt("Quantity");
//                                            comments = medp.getString("Comments");
//                                            mName = m.getString("Name");
//                                            purpose = m.getString("Purpose");
//                                            type = m.getString("Type");
//                                            altName = m.getString("AlsoCalled");
//                                            forms = m.getString("Forms");
//                                            desc = m.getString("Description");
//
//
//                                            MyMedicines myMedicines = new MyMedicines( mID, quantity, comments, mName, purpose, type, altName, forms, desc
//                                            );
//
//                                            medicines.add(myMedicines);
//
//
//
//                                    }
//
//
//
//
//                                }
//
//                                symptomsTV.setText(symptoms);
//                                diagnosticsTV.setText(diagnostics);
//                                treatmentTV.setText(treatment);
//                                bpressureTV.setText(bpressure);
//                                notesTV.setText(notes);
////                                mp_nameTV.setText(mp_name);
//                                dateTV.setText(date);
//
//                                initRecyclerView();
//
//                            }
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

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

        MySingleton.getInstance(EMR.this).addToRequestQueue(jsonObjectRequest);



    }

    private void setUpLayout(String mpFname, String mpSname, String date, String diagnostics, String symptoms, String treatments, String bpressure, String notes) {

        symptomsTV.setText(symptoms);
        diagnosticsTV.setText(diagnostics);
        treatmentTV.setText(treatments);
        bpressureTV.setText(bpressure);
        notesTV.setText(notes);
        mp_nameTV.setText("Dr. "+mpFname +" "+ mpSname);
        dateTV.setText(date);

    }
}
