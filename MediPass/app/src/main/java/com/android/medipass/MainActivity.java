package com.android.medipass;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
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
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView data, welMessageTV;
    Animation slideUp;
    ImageView emrIV, prescIV, mydetsIV, docIV, mediRingIV;
    Button emergBtn;
    String pId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pId = getIntent().getExtras().getString("pId");

        data = findViewById(R.id.data);
        emrIV = findViewById(R.id.emr_icon);
        prescIV = findViewById(R.id.presc_icon);
        mydetsIV = findViewById(R.id.mydetails_icon);
        docIV = findViewById(R.id.doctor_icon);
        mediRingIV = findViewById(R.id.mediring_icon);
        emergBtn = this.findViewById(R.id.emergBtn);

        emrIV.setOnClickListener(this);
        prescIV.setOnClickListener(this);
        mydetsIV.setOnClickListener(this);
        docIV.setOnClickListener(this);
        mediRingIV.setOnClickListener(this);
        emergBtn.setOnClickListener(this);

        welMessageTV = findViewById(R.id.welMessageTV);
        slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        welMessageTV.startAnimation(slideUp);

        userLogin(pId);
    }

    private void userLogin(String pId) {

            String getUserInfoURI = "https://medipass-server.herokuapp.com/api/user/"+pId+"/getUserInfo";


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getUserInfoURI, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String pid = response.getString("PatientID");
                                String fname = response.getString("FName");
                                String sname = response.getString("SName");


                                welMessageTV.setText(fname + " "+ sname);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            data.setText(response.toString());


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

            MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest);



        }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.emr_icon :
                Intent intent = new Intent(getApplicationContext(), Consultations.class);
                intent.putExtra("pId", pId);
                startActivity(intent);
                break;

            case R.id.presc_icon :
                Intent presc_intent = new Intent(getApplicationContext(), PrescriptionsActivity.class);
                presc_intent.putExtra("pid", pId);
                startActivity(presc_intent);

                break;

            case R.id.mydetails_icon :
                Intent profile_intent = new Intent(getApplicationContext(), ProfileActivity.class);
                profile_intent.putExtra("pid", pId);
                startActivity(profile_intent);

                break;

            case R.id.doctor_icon :
                Intent intent_dr = new Intent(getApplicationContext(), Doctors.class);
                intent_dr.putExtra("pId", pId);
                startActivity(intent_dr);

                break;

            case R.id.mediring_icon :
                Intent intent_mr = new Intent(getApplicationContext(), MediRing.class);
                intent_mr.putExtra("pId", pId);
                startActivity(intent_mr);

                break;

            case R.id.emergBtn :
                Intent intent_emerg = new Intent(getApplicationContext(), EmergencyActivity.class);
                intent_emerg.putExtra("pId", pId);
                startActivity(intent_emerg);

                break;
        }
    }

}

