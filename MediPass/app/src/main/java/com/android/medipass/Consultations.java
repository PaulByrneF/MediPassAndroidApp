package com.android.medipass;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class Consultations extends AppCompatActivity{

    private static final String TAG = "Consultations";
    private ArrayList<MyConsultations> consultations = new ArrayList<>();
    SwipeRefreshLayout swipe_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultations);

        String pId = getIntent().getExtras().getString("pId");
        Toast.makeText(getApplicationContext(), pId, Toast.LENGTH_LONG).show();

        swipe_container = findViewById(R.id.swipe_container);
        swipe_container.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimaryDark), ContextCompat.getColor(this, R.color.colorPrimary));

        getConsultations(pId);
    }


    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init RecyclerView");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, consultations);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }



    private void getConsultations(String pId) {

        String getConsultationsURI = "https://medipass-server.herokuapp.com/api/user/"+pId+"/consultations";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getConsultationsURI, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("Consultations");

                            for (int i = 0; i<jsonArray.length(); i++) {
                                JSONObject c = jsonArray.getJSONObject(i);


                                MyConsultations myConsultation = new MyConsultations(
                                        c.getInt("ConsultationID"),
                                        c.getString("Diagnostic"),
                                        c.getString("Date").substring(0,10),
                                        c.getString("FName"),
                                        c.getString("SName"),
                                        c.getString("FieldOdSpecialization")
                                );
                                consultations.add(myConsultation);

                            }

                            initRecyclerView();
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

        MySingleton.getInstance(Consultations.this).addToRequestQueue(jsonObjectRequest);



    }

}
