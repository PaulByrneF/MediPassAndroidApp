package com.android.medipass;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionsRVA extends RecyclerView.Adapter<PrescriptionsRVA.ViewHolder> {

    private static final String TAG = "PrescriptionsRVA";
    private List<Prescription> myPrescriptions = new ArrayList<>();
    private Context context;

    public PrescriptionsRVA(Context context, List<Prescription> myPrescriptions) {
        this.myPrescriptions = myPrescriptions;
        this.context = context;
    }

    @NonNull
    @Override
    public PrescriptionsRVA.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_prescription, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PrescriptionsRVA.ViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder: called");

        if (myPrescriptions.get(position).getStatus() == 0) {
            holder.expiryDateTV.setText("EXPIRED");
            holder.expiryDateTV.setTextColor(Color.RED);
            holder.expiryDateTitleTV.setText("");
        }
        else {
            holder.expiryDateTV.setText(myPrescriptions.get(position).getExpDate());
        }

        holder.drugNameTV.setText(myPrescriptions.get(position).getmName());
        holder.drugTypeTV.setText(myPrescriptions.get(position).getType());
        holder.drugQuantTV.setText(Integer.toString(myPrescriptions.get(position).getQuantity()));

    }

    @Override
    public int getItemCount() {
        return myPrescriptions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView drugNameTV, expiryDateTV, drugTypeTV, drugQuantTV,expiryDateTitleTV ;
        ConstraintLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            drugNameTV = itemView.findViewById(R.id.drugNameTV);
            expiryDateTV = itemView.findViewById(R.id.expiryDateTV);
            expiryDateTitleTV = itemView.findViewById(R.id.expiryDateTitleTV);
            drugTypeTV = itemView.findViewById(R.id.drugTypeTV);
            drugQuantTV = itemView.findViewById(R.id.drugQuantTV);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
