package com.android.medipass;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class MediRing extends AppCompatActivity implements View.OnClickListener{

    NfcAdapter nfcAdapter;
    ToggleButton tglReadWrite;
    EditText txtTagContent;
    Button reqbtn, syncbtn, act_deactBtn;
    String pid, mediKey;
    int status, mediringid;
    TextView medikeyTV, statusTV;
    CardView card_view_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medi_ring);

        pid = getIntent().getExtras().getString("pId");
        getMediRing(pid);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        tglReadWrite = (ToggleButton) findViewById(R.id.tglReadWrite);
        txtTagContent = (EditText) findViewById(R.id.txtTagContent);
        reqbtn = (Button) findViewById(R.id.reqBtn);
        syncbtn = (Button) findViewById(R.id.syncbtn);
        act_deactBtn = (Button) findViewById(R.id.act_deactBtn);
        medikeyTV = (TextView) findViewById(R.id.medikeyTV);
        statusTV = (TextView) findViewById(R.id.statusTV);
        card_view_status = (CardView) findViewById(R.id.card_view_status);

        act_deactBtn.setOnClickListener(this);
        reqbtn.setOnClickListener(this);

        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC available!", Toast.LENGTH_LONG).show();
        } else {
          finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);

        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Toast.makeText(this, "NFCintent!", Toast.LENGTH_SHORT).show();

            if (tglReadWrite.isChecked()) {

                Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

                if (parcelables != null && parcelables.length > 0) {

                    readTextFromTag((NdefMessage)parcelables[0]);

                } else {

                    Toast.makeText(this, "No Ndef messages found!", Toast.LENGTH_SHORT).show();

                }
            } else {


                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                NdefMessage ndefMessage = createNdefMessage(mediKey);

                writeNdefMessage(tag, ndefMessage);
            }
        }
    }

    private void readTextFromTag(NdefMessage ndefMessage) {

        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if (ndefRecords != null && ndefRecords.length > 0) {

            NdefRecord ndefRecord = ndefRecords[0];

            String tagContent = getTextFromNdefRecord(ndefRecord);

            txtTagContent.setText(tagContent);

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

        Intent intent = new Intent(this, MediRing.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilter = new IntentFilter[]{};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilter, null);
    }

    private void disableForegroundDispatchSystem() {
        nfcAdapter.disableForegroundDispatch(this); //happens

    }

    private void formatTag(Tag tag, NdefMessage ndefMessage) {

        try {

            NdefFormatable ndefFormatable = NdefFormatable.get(tag);

            if (ndefFormatable == null) {
                Toast.makeText(this, "Tag written!", Toast.LENGTH_SHORT).show();
            }

            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();

            Toast.makeText(this, "Tag object cannot be null", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e("formatTag", e.getMessage());
        }
    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage) {

        try {

            if(tag == null) {
                Toast.makeText(this, "Tag object cannot be null", Toast.LENGTH_SHORT).show();
                return;
            }

            Ndef ndef = Ndef.get(tag);

            if (ndef == null) {

                //format tag with the ndef format writes the message.
                formatTag(tag, ndefMessage);
            }
            else {
                ndef.connect();

                if (!ndef.isWritable()) {
                    Toast.makeText(this, "Tag is not writeable!", Toast.LENGTH_SHORT).show();

                    ndef.close();
                    return;
                }

                ndef.writeNdefMessage(ndefMessage);
                ndef.close();

                Toast.makeText(this, "Tag written!", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e("writeNdefMessage", e.getMessage());
        }
    }

    private NdefRecord createTextrecord (String content) {

        try {
            byte[] language;
            language = Locale.getDefault().getLanguage().getBytes("UTF-8");

            final byte[] text = content.getBytes("UTF-8");
            final int languageSize = language.length;
            final int textLength = text.length;
            final ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + languageSize + textLength);

            payload.write((byte) (languageSize & 0x1F));
            payload.write(language, 0, languageSize);
            payload.write(text, 0, textLength);

            return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());
        } catch (UnsupportedEncodingException e) {
            Log.e("createTextRecord", e.getMessage());
        }
        return null;
    }

    private NdefMessage createNdefMessage(String content) {

        NdefRecord ndefRecord = createTextrecord(content);

        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[] { ndefRecord });

        return ndefMessage;
    }

    public void tglReadWriteOnclick(View view) {

        txtTagContent.setText("");
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reqBtn :

                Toast.makeText(this, "Request Function started", Toast.LENGTH_SHORT).show();
                requestMediRing(pid);
                break;

            case R.id.act_deactBtn :

                Toast.makeText(this, "deactivate ring", Toast.LENGTH_SHORT).show();
                alterMediRingStatus(mediringid);
                break;


        }
    }

    private void requestMediRing(String pId) {

        String createMediRing = "https://medipass-server.herokuapp.com/api/user/"+pId+"/syncMediRing/";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, createMediRing,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        mediKey = response;
                        reqbtn.setVisibility(View.INVISIBLE);

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

        MySingleton.getInstance(MediRing.this).addToRequestQueue(stringRequest);



    }

    private void getMediRing(String pId) {

        String createMediRing = "https://medipass-server.herokuapp.com/api/user/"+pId+"/getMediRing/";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, createMediRing, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(getApplicationContext(), "response"+response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = response.getJSONObject("myMediRing");
                            mediringid = jsonObject.getInt("mediringid");
                            mediKey = jsonObject.getString("medikey");
                            status = jsonObject.getInt("status");

                        } catch (JSONException e) {


                            e.printStackTrace();
                        }

                        if (mediKey.length() > 0) {
                            medikeyTV.setText("**** **** "+ mediKey.substring(mediKey.length() - 4));

                        } else {
                            // whatever is appropriate in this case
                            throw new IllegalArgumentException("word has less than 3 characters!");
                        }


                        if (status == 1) {
                            statusTV.setText("ACTIVE");
                            card_view_status.setCardBackgroundColor(Color.GREEN);
                            act_deactBtn.setText("DEACTIVATE");
                            act_deactBtn.setBackgroundColor(Color.RED);

                        } else {
                            statusTV.setText("Not Active");
                            card_view_status.setCardBackgroundColor(Color.RED);
                            act_deactBtn.setText("Activate");
                            act_deactBtn.setBackgroundColor(Color.GREEN);
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

        MySingleton.getInstance(MediRing.this).addToRequestQueue(jsonObjectRequest);



    }

    private void alterMediRingStatus(int mediringid) {

        String createMediRing = "https://medipass-server.herokuapp.com/api/mediring/"+mediringid+"/alterStatus/"+status;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, createMediRing, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject m = response.getJSONObject("myMediRing");
                            status = m.getInt("status");

                            getMediRing(pid);

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

        MySingleton.getInstance(MediRing.this).addToRequestQueue(jsonObjectRequest);

    }
}
