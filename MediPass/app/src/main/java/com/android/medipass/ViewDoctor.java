package com.android.medipass;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class ViewDoctor extends AppCompatActivity {

    String dName, fos, phNo, email, mpid, regNo, cStatus;
    TextView nameTV, fOSTV;
    String pid = "232";
    Button addDocBtn;


    private static final String TAG = "ViewDoctor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_doctor);

        getIncomingIntent();
        Toast.makeText(getApplicationContext(), regNo, Toast.LENGTH_SHORT).show();

        nameTV = this.findViewById(R.id.nameTV);
        fOSTV = this.findViewById(R.id.fOSTV);
        addDocBtn = this.findViewById(R.id.addDocBtn);
        TabLayout tabLayout = this.findViewById(R.id.tablayout_id);
        TabItem tabDoctorInfo = this.findViewById(R.id.tabDoctorInfo);
        TabItem tabPracticeInfo = this.findViewById(R.id.tabPracticeInfo);
        ViewPager viewPager = this.findViewById(R.id.viewpager_id);

        nameTV.setText(dName);
        fOSTV.setText(fos);
        changeConsentBtn();


        final String consentMsg = "\nI agree to give my consent to " + dName + ", by which will be used solely for the good intention of my personal health and well being.\n\n Are you sure you wish to give your consent?\n\n";
        final String disconsentMsg = "\nBy disconsenting to " + dName + ", they will no longer have access to your medical records and furthermore will have no more participation in the your personal health and well being until you your consent again\n\n Are you Sure you want to disconsent?\n\n";


        addDocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cStatus.equals("1")) {
                    alertDialog(disconsentMsg, "Disconsent");
                } else {
                    alertDialog(consentMsg, "Give Consent");
                }
            }
        });

        ViewPagerViewDoctorAdapter pageAdapter = new ViewPagerViewDoctorAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), phNo, email, regNo);

        viewPager.setAdapter(pageAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


    }

    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");
        if ((getIntent().hasExtra("dName"))
                && (getIntent().hasExtra("fos"))
                && (getIntent().hasExtra("phNo"))
                && (getIntent().hasExtra("email"))
                && (getIntent().hasExtra("regNo"))
                && (getIntent().hasExtra("mpid"))
        ){
            Log.d(TAG, "getIncomingIntent: found intent extras.");
            dName = getIntent().getStringExtra("dName");
            fos = getIntent().getStringExtra("fos");
            phNo = getIntent().getStringExtra("phNo");
            email = getIntent().getStringExtra("email");
            regNo = getIntent().getStringExtra("regNo");
            mpid = getIntent().getStringExtra("mpid");
            cStatus = getIntent().getStringExtra("cStatus");

            Toast.makeText(getApplicationContext(), regNo, Toast.LENGTH_SHORT).show();
        }
    }

    public void alertDialog(String message, String btnText) {


        //pass the 'context' here
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewDoctor.this);
        alertDialog.setTitle("Consent Notice");

        alertDialog.setMessage(message);
        alertDialog.setPositiveButton(btnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ViewDoctor.this, "Consent Giving ... fix later", Toast.LENGTH_SHORT).show();
                if (cStatus.equals("1")) {

                    cStatus = "0";
                } else {
                    cStatus = "1";
                }
                changeConsentBtn();
            }
        });
        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        AlertDialog dialog = alertDialog.create();
        dialog.show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ViewDoctor.this.getColor(R.color.colorGrey));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ViewDoctor.this.getColor(R.color.colorPrimaryDark));
        }
    }


    private void addDoctor(String pid, String mpid) {

        String addDoctorURI = "https://medipass-server.herokuapp.com/api/user/"+pid+"/addConnection/"+mpid;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, addDoctorURI, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(getApplicationContext(), "Doctor Added", Toast.LENGTH_SHORT).show();


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

        MySingleton.getInstance(ViewDoctor.this).addToRequestQueue(jsonObjectRequest);

    }

    private void changeConsentBtn() {

        if (cStatus.equals("1")) {
            addDocBtn.setBackground(getDrawable(R.drawable.hollowbtn));
            addDocBtn.setText("Disconsent");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                addDocBtn.setTextColor(getColor(R.color.colorPrimaryDark));
            }
        } else {
            addDocBtn.setBackground(getDrawable(R.drawable.filledbtn));
            addDocBtn.setText("Consent");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                addDocBtn.setTextColor(getColor(R.color.colorAccent));
            }
        }

    }
}
