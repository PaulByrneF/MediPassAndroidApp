package com.android.medipass;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBasicInfo extends Fragment {

    TextView dobTV, sexTV, phHomeTV, phMobTV, smokerTV, drinkerTV, heightTV, weightTV, bTypeTV, dnrTV;

    public FragmentBasicInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_basic_info, container, false);

        dobTV = view.findViewById(R.id.dobTV);
        sexTV = view.findViewById(R.id.sexTV);
        phHomeTV = view.findViewById(R.id.phHomeTV);
        phMobTV = view.findViewById(R.id.phMobTV);
        smokerTV = view.findViewById(R.id.smokerTV);
        drinkerTV = view.findViewById(R.id.drinkerTV);
        heightTV = view.findViewById(R.id.heightTV);
        weightTV = view.findViewById(R.id.weightTV);
        bTypeTV = view.findViewById(R.id.bTypeTV);
//        dnrTV = view.findViewById(R.id.dnrTV);

        String dob = getArguments().getString("dob");
        String sex = getArguments().getString("sex");
        String phHome = getArguments().getString("phHome");
        String phMobile = getArguments().getString("phMobile");
        String smoker = getArguments().getString("smoker");
        String drinker = getArguments().getString("drinker");
        String height = getArguments().getString("height");
        String weight = getArguments().getString("weight");
        String bType = getArguments().getString("bType");
        String dnr = getArguments().getString("dnr");


        dobTV.setText(dob);
        sexTV.setText(sex);
        phHomeTV.setText(phHome);
        phMobTV.setText(phMobile);
        smokerTV.setText(smoker);
        drinkerTV.setText(drinker);
        heightTV.setText(height);
        weightTV.setText(weight);
        bTypeTV.setText(bType);
//        dnrTV.setText(dnr);


        return view;
    }

}
