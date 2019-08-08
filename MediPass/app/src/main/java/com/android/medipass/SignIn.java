package com.android.medipass;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;


public class SignIn extends AppCompatActivity implements View.OnClickListener{

    Button signUpBtn;
    EditText emailET, passET;
    TextView signUpTV, loginTV;
    String email, pass;
    String loginURI = "https://medipass-server.herokuapp.com/api/login";
    AlertDialog.Builder builder;
    ImageView ecg;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signUpBtn = findViewById(R.id.signInBtn);
        emailET = (EditText) findViewById(R.id.emailET);
        passET = (EditText) findViewById(R.id.passET);
        signUpTV = (TextView) findViewById(R.id.signUpTV);
        ecg = findViewById(R.id.ecg_gif);
        loginTV = findViewById(R.id.loginTV);
        builder = new AlertDialog.Builder(SignIn.this);

        signUpBtn.setOnClickListener(this);
        signUpTV.setOnClickListener(this);

        progressBar = this.findViewById(R.id.progress_bar);

    }

    private void userLogin() {
        email = emailET.getText().toString().trim();
        pass = passET.getText().toString().trim();

        if (email.equals("") || pass.equals("")) {
            builder.setTitle("Something Went Wrong");
            builder.setMessage("Please fill out all fields");
            displayAlert("some fields havent been filled out");
            loginTV.setText("LOGIN");
            emailET.setVisibility(View.VISIBLE);
            passET.setVisibility(View.VISIBLE);
            ecg.setVisibility(View.GONE);
        }

        else {

            HashMap<String, String> params = new HashMap<String, String>();
                    params.put("email", email);
                    params.put("password", pass);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, loginURI, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                progressBar.setVisibility(View.GONE);
                                String pid = response.getString("PatientID");
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("pId", pid);
                                startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            finish();
//                            startActivity(new Intent(SignIn.this, MainActivity.class));
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    signUpBtn.setText("LOG IN");

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                        Toast.makeText(getApplicationContext(), "timeout", Toast.LENGTH_LONG).show();

                    } else if (error instanceof AuthFailureError) {
                        //TODO


                        Toast.makeText(getApplicationContext(), "AuthFailure", Toast.LENGTH_SHORT).show();
                        displayAlert("404");

                    } else if (error instanceof ServerError) {
                        //TODO
                        Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();

                        emailET.setText("");
                        passET.setText("");
                        emailET.requestFocus();
                        emailET.setVisibility(View.VISIBLE);
                        passET.setVisibility(View.VISIBLE);
                        ecg.setVisibility(View.GONE);
                        loginTV.setText("Log In");

                        displayAlert("404");
                    } else if (error instanceof NetworkError) {
                        //TODO
                        Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();

                        displayAlert("404");
                    } else if (error instanceof ParseError) {
                        //TODO

                        displayAlert("404");
                    }
                }
            });
//            }) {
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("email", email);
//                    params.put("password", pass);
//                    return params;
//                }
//            };



            MySingleton.getInstance(SignIn.this).addToRequestQueue(jsonObjectRequest);



        }
//        Toast.makeText(this, email, Toast.LENGTH_SHORT).show();
    }




    public void  displayAlert(final String code) {

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                if (code.equals("input_error")) {
//                    passET.setText("");
//
//                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signInBtn :
                userLogin();
                progressBar.setVisibility(View.VISIBLE);
                break;

            case R.id.signUpTV :
                startActivity(new Intent(SignIn.this, SignUp.class));
                Toast.makeText(this, "signUp.class", Toast.LENGTH_SHORT).show();
//                finish();
//                startActivity(new Intent(this, SignUp.class));
        }
    }
}
