package com.android.medipass;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerViewAdapterFindD extends RecyclerView.Adapter<RecyclerViewAdapterFindD.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Doctor> doctors = new ArrayList<>();
    private Context context;



    public RecyclerViewAdapterFindD(Context context, ArrayList<Doctor> doctors) {
        this.doctors = doctors;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_doctor, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Log.d(TAG, "onBindViewHolder: called");

        final String doc = "Dr. " +doctors.get(position).getfName() +" "+ doctors.get(position).getsName();

        holder.typeTV.setText(doctors.get(position).getFos());
        holder.dName.setText(doc);

        
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + doctors.get(position));

                Toast.makeText(context, Integer.toString(doctors.get(position).getMedPractitionerID()), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, ViewDoctor.class);
                intent.putExtra("mpid", Integer.toString(doctors.get(position).getMedPractitionerID()));
                intent.putExtra("dName", doc);
                intent.putExtra("fos",  doctors.get(position).getFos());
                intent.putExtra("phNo", "fix this");
                intent.putExtra("regNo", "fix this");
                intent.putExtra("email", doctors.get(position).getEmail());
                intent.putExtra("cStatus", "0");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView typeTV, dName;
        CardView parentLayout;

        public ViewHolder(View itemView) {

            super(itemView);

            typeTV = itemView.findViewById(R.id.typeTV);
            dName = itemView.findViewById(R.id.dName);
            parentLayout = itemView.findViewById(R.id.parent_layout);


        }
    }
}
