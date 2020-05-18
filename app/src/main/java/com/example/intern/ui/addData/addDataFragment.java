package com.example.intern.ui.addData;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.intern.LoginActivity;
import com.example.intern.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class addDataFragment extends Fragment {

    private addDataViewModel notificationsViewModel;

    Button logOut;

    FirebaseAuth auth;

    ArrayList<String> items = new ArrayList<>();
    Spinner spinner;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(addDataViewModel.class);
        View root = inflater.inflate(R.layout.fragment_adddata, container, false);

        auth=FirebaseAuth.getInstance();

        logOut=root.findViewById(R.id.logOut);


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.getInstance().signOut();

                FirebaseAuth.getInstance().signOut();
                Intent i1 = new Intent(getActivity(), LoginActivity.class);
                startActivity(i1);
                Toast.makeText(getActivity(), "Logout Successfully!", Toast.LENGTH_SHORT).show(); }
        });

        return root;
    }
}