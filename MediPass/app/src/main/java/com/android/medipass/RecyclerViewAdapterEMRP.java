package com.android.medipass;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class RecyclerViewAdapterEMRP extends RecyclerView.Adapter<RecyclerViewAdapterEMRP.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapterEMRP";

    private ArrayList<MyMedicines> medicines = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapterEMRP(Context context, ArrayList<MyMedicines> medicines) {
        this.medicines = medicines;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_prescription, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Log.d(TAG, "onBindViewHolder: called");

        holder.medNameTV.setText(medicines.get(position).getmName());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on :"+medicines.get(position));

                Toast.makeText(context, Integer.toString(medicines.get(position).getmID()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {

        TextView medNameTV;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            medNameTV = itemView.findViewById(R.id.drugNameTV);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
