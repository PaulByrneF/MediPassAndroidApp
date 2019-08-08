package com.android.medipass;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerViewAdapterMyDoctor extends RecyclerView.Adapter<RecyclerViewAdapterMyDoctor.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<MyDoctor> myDoctors = new ArrayList<>();
    private Context context;


    public RecyclerViewAdapterMyDoctor(Context context, ArrayList<MyDoctor> myDoctors) {
        this.myDoctors = myDoctors;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_mydoctor, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Log.d(TAG, "onBindViewHolder: called");

        final String doc = "Dr. " + myDoctors.get(position).getfName() + " " + myDoctors.get(position).getsName();

        holder.typeTV.setText(myDoctors.get(position).getFos());
        holder.dName.setText(doc);


        // If Doctor has my consent
        if (myDoctors.get(position).getcStatus() == 1) {

            holder.consBtn.setBackground(context.getDrawable(R.drawable.hollowbtn));
            holder.consBtn.setText("Disconsent");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.consBtn.setTextColor(context.getColor(R.color.colorPrimaryDark));
            }



        } else {

            holder.consBtn.setBackground(context.getDrawable(R.drawable.filledbtn));
            holder.consBtn.setText("Consent");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.consBtn.setTextColor(context.getColor(R.color.colorAccent));
            }

        }

        final String consentMsg = "\nI agree to give my consent to " + doc + ", by which will be used solely for the good intention of my personal health and well being.\n\n Are you sure you wish to give your consent?\n\n";
        final String disconsentMsg = "\nBy disconsenting to " + doc + ", they will no longer have access to your medical records and furthermore will have no more participation in the your personal health and well being until you your consent again\n\n Are you Sure you want to disconsent?\n\n";


        holder.consBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + myDoctors.get(position));

                if (myDoctors.get(position).getcStatus() == 1) {

                    alertDialog(holder, position, disconsentMsg, "Disconsent");


                } else {
                    alertDialog(holder, position, consentMsg, "Consent");
                }
            }
        });


        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + myDoctors.get(position));

                Toast.makeText(context, Integer.toString(myDoctors.get(position).getMpid()), Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(context, ViewDoctor.class);
                intent.putExtra("mpid", Integer.toString(myDoctors.get(position).getMpid()));
                intent.putExtra("dName", doc);
                intent.putExtra("fos",  myDoctors.get(position).getFos());
                intent.putExtra("phNo", myDoctors.get(position).getPhNo());
                intent.putExtra("regNo", Integer.toString(myDoctors.get(position).getRegNo()));
                intent.putExtra("email", myDoctors.get(position).getEmail());
                intent.putExtra("cStatus", Integer.toString(myDoctors.get(position).getcStatus()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myDoctors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Button consBtn;
        TextView typeTV, dName;
        CardView parentLayout;

        public ViewHolder(View itemView) {

            super(itemView);

            consBtn = itemView.findViewById(R.id.consentBtn);
            typeTV = itemView.findViewById(R.id.typeTV);
            dName = itemView.findViewById(R.id.dName);
            parentLayout = itemView.findViewById(R.id.parent_layout);


        }
    }

    public void alterConsentStatus(int mpid, int CurrentStatus) {
        String alterConsent = "https://medipass-server.herokuapp.com/api/user/232/connection/alterConsent";

        HashMap<String, Integer> params = new HashMap<String, Integer>();
        params.put("mpid", mpid);
        params.put("status", CurrentStatus);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, alterConsent, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject jsonObject = new JSONObject("status");

                            int newStatus = jsonObject.getInt("newStatus");

                            if (newStatus == 1) {
                                Toast.makeText(context.getApplicationContext(), "consent given", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context.getApplicationContext(), "disconsent", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(context, "timeout", Toast.LENGTH_LONG).show();

                } else if (error instanceof AuthFailureError) {
                    //TODO


                    Toast.makeText(context, "AuthFailure", Toast.LENGTH_SHORT).show();


                } else if (error instanceof ServerError) {
                    //TODO

                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    //TODO
                    Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();


                } else if (error instanceof ParseError) {
                    //TODO

                    Toast.makeText(context, "parsererror", Toast.LENGTH_SHORT).show();


                }
            }
        });

        MySingleton.getInstance(this.context).addToRequestQueue(jsonObjectRequest);
    }

    public void alertDialog(final ViewHolder holder, final int position, String message, String btnText) {


        //pass the 'context' here
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Consent Notice");

        alertDialog.setMessage(message);
        alertDialog.setPositiveButton(btnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alterConsentStatus(myDoctors.get(position).getMpid(), myDoctors.get(position).getcStatus());
                changeStatus(holder, position, myDoctors.get(position).getcStatus());

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
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getColor(R.color.colorGrey));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getColor(R.color.colorPrimaryDark));
        }
    }


    public void changeStatus(final ViewHolder holder, final int position, int status) {

        if (status == 1) {

            myDoctors.get(position).setcStatus(0);

            holder.consBtn.setBackground(context.getDrawable(R.drawable.filledbtn));
            holder.consBtn.setText("Consent");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.consBtn.setTextColor(context.getColor(R.color.colorAccent));
            }
        } else {

            myDoctors.get(position).setcStatus(1);

            holder.consBtn.setBackground(context.getDrawable(R.drawable.hollowbtn));
            holder.consBtn.setText("Disconsent");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.consBtn.setTextColor(context.getColor(R.color.colorPrimaryDark));
            }
        }
    }

}
