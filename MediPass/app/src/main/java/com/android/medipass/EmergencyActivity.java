package com.android.medipass;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;


public class EmergencyActivity extends AppCompatActivity implements View.OnClickListener{

    NfcAdapter nfcAdapter;
    Button scanBtn;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC available!", Toast.LENGTH_LONG).show();
        } else {
            finish();
        }

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        scanBtn = (Button) findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(this);

    }






    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);

            if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
                Toast.makeText(this, "NFCintent!", Toast.LENGTH_SHORT).show();

                Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);


                if (parcelables != null && parcelables.length > 0) {

                    readTextFromTag((NdefMessage) parcelables[0]);

                } else {

                    Toast.makeText(this, "No Ndef messages found!", Toast.LENGTH_SHORT).show();
                }
            }
        }



    private void readTextFromTag(NdefMessage ndefMessage) {

        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if (ndefRecords != null && ndefRecords.length > 0) {

            NdefRecord ndefRecord = ndefRecords[0];

            String tagContent = getTextFromNdefRecord(ndefRecord);

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGPS();
            }else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                getLocation();
            }


            createEmergency(tagContent);

        } else {

            Toast.makeText(this, "No Ndef Records Found!", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onResume() {

        enableForegroundDispatchSystem();
        super.onResume();
    }

    @Override
    protected void onPause() {

        disableForegroundDispatchSystem();
        super.onPause();
    }

    private void enableForegroundDispatchSystem() {

        Intent intent = new Intent(this, EmergencyActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilter = new IntentFilter[]{};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilter, null);
    }

    private void disableForegroundDispatchSystem() {
        nfcAdapter.disableForegroundDispatch(this); //happens

    }

    public String getTextFromNdefRecord(NdefRecord ndefRecord) {

        String tagContent = null;

        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1,
                    payload.length - languageSize - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("getTextFromNdefMessage", e.getMessage(), e);
        }
        return tagContent;
    }

    private void createEmergency(String medikey) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("medikey", medikey);
        params.put("latitude", latitude);
        params.put("longitude", longitude);

        String getContactsURI = "https://medipass-server.herokuapp.com/api/createEmergency";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getContactsURI, new JSONObject(params),

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(EmergencyActivity.this, response.toString(), Toast.LENGTH_SHORT).show();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(EmergencyActivity.this, "timeout", Toast.LENGTH_LONG).show();

                } else if (error instanceof AuthFailureError) {
                    //TODO


                    Toast.makeText(EmergencyActivity.this, "AuthFailure", Toast.LENGTH_SHORT).show();


                } else if (error instanceof ServerError) {
                    //TODO

                    Toast.makeText(EmergencyActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    //TODO
                    Toast.makeText(EmergencyActivity.this, "Network Error", Toast.LENGTH_SHORT).show();


                } else if (error instanceof ParseError) {
                    //TODO

                    Toast.makeText(EmergencyActivity.this, "parsererror", Toast.LENGTH_SHORT).show();


                }
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(EmergencyActivity.this).addToRequestQueue(jsonObjectRequest);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scanBtn:


                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGPS();
                }else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getLocation();
                }

                alertDialog();

                break;
        }
    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(EmergencyActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EmergencyActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                latitude = String.valueOf(lat).substring(0, 7);
                longitude = String.valueOf(lng).substring(0, 7);;

                Toast.makeText(this, "incident created", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Unable to trace location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void buildAlertMessageNoGPS() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {
                        dialogInterface.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void alertDialog() {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(EmergencyActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_scanner, null);
//        final EditText cNameET = (EditText) mView.findViewById(R.id.contactNameET);
//        final EditText cNumberTV = (EditText) mView.findViewById(R.id.contactNumberET);
        final Button reqbtn = (Button) mView.findViewById(R.id.reqbtn);
        Button cancelBtn = (Button) mView.findViewById(R.id.cancelBtn);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();

//        reqbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                if (!cNameET.getText().toString().isEmpty() && !cNumberTV.getText().toString().isEmpty()) {
//////                    addContact(cNumberTV.getText().toString(), cNameET.getText().toString());
////                } else {
//////                    Toast.makeText(getContext(), "Please fill out fields", Toast.LENGTH_SHORT).show();
////                }
//            }
//        });
//
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        dialog.show();
    }
}
