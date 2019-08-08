package com.android.medipass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<MyConsultations> consultations = new ArrayList<>();
    private Context context;



    public RecyclerViewAdapter(Context context, ArrayList<MyConsultations> consultations) {
        this.consultations = consultations;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_consultation, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Log.d(TAG, "onBindViewHolder: called");

        String doc = "Dr. " +consultations.get(position).getMp_fname() +" "+ consultations.get(position).getMp_sname();

        holder.typeTV.setText(consultations.get(position).getC_type());
        holder.dateTV.setText(consultations.get(position).getDate());
        holder.diagnosticTV.setText(consultations.get(position).getDiagnostics());
        holder.authorTV.setText(doc);


        if (position%2 == 0) {
            holder.parentLayout.setBackgroundColor(Color.parseColor("#e9ecef"));
        } else {
            holder.parentLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + consultations.get(position));

                Intent intent = new Intent(context, EMR.class);
                intent.putExtra("cID", Integer.toString(consultations.get(position).getcID()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return consultations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView typeTV, dateTV, diagnosticTV, authorTV;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {

            super(itemView);

            typeTV = itemView.findViewById(R.id.typeTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            diagnosticTV = itemView.findViewById(R.id.diagnosticTV);
            authorTV = itemView.findViewById(R.id.authorTV);
            parentLayout = itemView.findViewById(R.id.parent_layout);


        }
    }
}
