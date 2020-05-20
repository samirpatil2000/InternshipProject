package com.example.intern.ui.addData;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.bumptech.glide.Glide;
import com.example.intern.EditProfileActivity;
import com.example.intern.LoginActivity;
import com.example.intern.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.google.firebase.storage.StorageReference;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class ProfileFragment extends Fragment {





    private profileViewModel notificationsViewModel;
    TextView dogNameTv,ownerNameTv,breedTv,genderTv,ageTv,emailTv,addressTv;
    CircleImageView dpImg;

    ProgressBar progressBar;

    ProgressDialog pd;

    Button editProfile;



    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    // for Intent
    String dogName,ownerName,age,sex,email,address,dogBreed,dp;


    FirebaseAuth auth;
    FirebaseUser current_user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = ViewModelProviders.of(this).get(profileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        dogNameTv=root.findViewById(R.id.dogNameProfileTv);
        ownerNameTv=root.findViewById(R.id.ownerProfile);
        breedTv=root.findViewById(R.id.breedPrfoileTv);
        genderTv=root.findViewById(R.id.sexProfileTv);
        ageTv=root.findViewById(R.id.ageProfileTv);
        emailTv=root.findViewById(R.id.emailProfile);
        addressTv=root.findViewById(R.id.addressProfile);
        dpImg=root.findViewById(R.id.imageProfile);
        editProfile=root.findViewById(R.id.editProfileBtn);

        progressBar=root.findViewById(R.id.progressBarImgProfile);
        progressBar.setVisibility(View.VISIBLE);

        pd= new ProgressDialog(getActivity());
        pd.setTitle("loading...");
        pd.show();

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference= firebaseDatabase.getReference("UserInfo");
        storageReference = getInstance().getReference();

        auth=FirebaseAuth.getInstance();
        current_user=auth.getCurrentUser();

        Query query = databaseReference.orderByChild("id").equalTo(current_user.getUid());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){

                     dogName = ""+ds.child("dogsName").getValue();
                     ownerName = ""+ds.child("ownerName").getValue();
                     age = ""+ds.child("age").getValue();
                     sex = ""+ds.child("sex").getValue();
                     email = ""+ds.child("email").getValue();
                     address = ""+ds.child("address").getValue();
                     dogBreed ="" +ds.child("dodsBreed").getValue();
                     dp = ""+ds.child("dp").getValue();





                    pd.dismiss();
                    Glide.with(getActivity()).load(dp).into(dpImg);
                    progressBar.setVisibility(View.INVISIBLE);
                    dogNameTv.setText(dogName);
                    ownerNameTv.setText(ownerName);
                    ageTv.setText(age);
                    genderTv.setText(sex);
                    emailTv.setText(email);
                    addressTv.setText(address);
                    breedTv.setText(dogBreed);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // edit Profile
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("dogName",dogName);
                intent.putExtra("ownerName",ownerName);
                intent.putExtra("age",age);
                intent.putExtra("sex",sex);
                intent.putExtra("address",address);
                intent.putExtra("dogBreed",dogBreed);
                intent.putExtra("dp",dp);
                startActivity(intent);

            }
        });




        // Menu in this fragment
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logOut:
                auth.getInstance().signOut();

                FirebaseAuth.getInstance().signOut();
                Intent i1 = new Intent(getActivity(), LoginActivity.class);
                startActivity(i1);
                Toast.makeText(getActivity(), "Logout Successfully!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}