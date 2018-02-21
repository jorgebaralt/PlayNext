package com.phereapp.phere.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.phereapp.phere.R;
import com.phereapp.phere.createPhere.CreateNewPhereActivity;
import com.phereapp.phere.login.SelectActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateJoinPheresFragment extends Fragment {


    public CreateJoinPheresFragment() {
        // Required empty public constructor
    }

    private Button mCreateHostPhere;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_create_join_pheres, container, false);


        //CODE HERE
        mCreateHostPhere = (Button) rootView.findViewById(R.id.btn_create_createJoinPhere);

        mCreateHostPhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createNewPhereIntent = new Intent(getActivity(), CreateNewPhereActivity.class);
                startActivity(createNewPhereIntent);
                //TODO : CREATE INTENT AND TRANSITION FROM FRAGMENT TO NEW ACTIVY
            }
        });



        return rootView;
    }

}