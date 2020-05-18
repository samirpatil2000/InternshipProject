package com.example.intern.ui.addData;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.example.intern.MainActivity;
import com.example.intern.R;

import java.util.ArrayList;

public class addDataFragment extends Fragment {

    private addDataViewModel notificationsViewModel;

    Button button;

    ArrayList<String> items = new ArrayList<>();
    Spinner spinner;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(addDataViewModel.class);
        View root = inflater.inflate(R.layout.fragment_adddata, container, false);

        return root;
    }
}