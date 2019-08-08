package com.android.medipass;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDoctorInfo extends Fragment {

    TextView regTv, phNoTV, emailTV;
    ConstraintLayout callBtn;

    public FragmentDoctorInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_doctor_info, container, false);

        regTv = view.findViewById(R.id.regTV);
        phNoTV = view.findViewById(R.id.phNoTv);
        emailTV = view.findViewById(R.id.emailTV);
        callBtn = view.findViewById(R.id.callBtn);

        String regNo = getArguments().getString("regNo");
        final String phNo = getArguments().getString("phNo");
        String email = getArguments().getString("email");


        regTv.setText(regNo);
        phNoTV.setText(phNo);
        emailTV.setText(email);

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialContactPhone(phNo);
            }
        });

        return view;



    }

    private void dialContactPhone(final String phNo) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phNo, null)));
    }

}
