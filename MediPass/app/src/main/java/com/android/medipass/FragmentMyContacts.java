package com.android.medipass;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMyContacts extends Fragment {
private Button addContactBtn;

    private static final String TAG = "FragmentMyContacts";
    private ArrayList<Contact> myContacts = new ArrayList<>();
    RecyclerView recyclerView;



    public FragmentMyContacts() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getContacts();
        View view = inflater.inflate(R.layout.fragment_my_contacts, container, false);

        addContactBtn = view.findViewById(R.id.addContactBtn);

        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog();
            }
        });

        return view;
    }

    private void getContacts() {


        String getContactsURI = "https://medipass-server.herokuapp.com/api/user/232/getAllContacts";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getContactsURI, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray jsonArray = response.getJSONArray("myContacts");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject myContact = jsonArray.getJSONObject(i);

                                Contact contact = new Contact(

                                        myContact.getInt("contactID"),
                                        myContact.getString("phNumber"),
                                        myContact.getString("contactName")

                                );

                                myContacts.add(contact);

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

                    Toast.makeText(getContext(), "timeout", Toast.LENGTH_LONG).show();

                } else if (error instanceof AuthFailureError) {
                    //TODO


                    Toast.makeText(getContext(), "AuthFailure", Toast.LENGTH_SHORT).show();


                } else if (error instanceof ServerError) {
                    //TODO

                    Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    //TODO
                    Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();


                } else if (error instanceof ParseError) {
                    //TODO

                    Toast.makeText(getContext(), "parsererror", Toast.LENGTH_SHORT).show();


                }
            }
        });

        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init RecyclerView.");
        recyclerView = getView().findViewById(R.id.recyclerView_contacts);
        RVAEmergContacts rvaEmergContacts = new RVAEmergContacts(getContext(), myContacts);
        recyclerView.setAdapter(rvaEmergContacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void alertDialog() {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.dialog_addcontact, null);
        final EditText cNameET = (EditText) mView.findViewById(R.id.contactNameET);
        final EditText cNumberTV = (EditText) mView.findViewById(R.id.contactNumberET);
        final Button addBtn = (Button) mView.findViewById(R.id.addContactBtn_Dialog);
        Button cancelBtn = (Button) mView.findViewById(R.id.cancelBtn_Dialog);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cNameET.getText().toString().isEmpty() && !cNumberTV.getText().toString().isEmpty()) {
                    addContact(cNumberTV.getText().toString(), cNameET.getText().toString());
                } else {
                    Toast.makeText(getContext(), "Please fill out fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void addContact(String contactNumber, String contactName) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("phoneNo", contactNumber);
        params.put("cName", contactName);

        String getContactsURI = "https://medipass-server.herokuapp.com/api/user/232/addContact";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getContactsURI, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(getContext(), "timeout", Toast.LENGTH_LONG).show();

                } else if (error instanceof AuthFailureError) {
                    //TODO


                    Toast.makeText(getContext(), "AuthFailure", Toast.LENGTH_SHORT).show();


                } else if (error instanceof ServerError) {
                    //TODO

                    Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    //TODO
                    Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();


                } else if (error instanceof ParseError) {
                    //TODO

                    Toast.makeText(getContext(), "parsererror", Toast.LENGTH_SHORT).show();


                }
            }
        });

        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

    }

}
