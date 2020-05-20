package com.example.intern;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    String[] breed;
    String[] gender;
    CircleImageView imageView;
    EditText dogNameEt,ownerNameEt,ageEt;
    ImageButton setLocation;
    String locationString;
    Button editBtn;
    TextView addressTv,sexTv,breedTv;

    Context context;

    String sexFromSpinner,breedFromSpinner;

    LocationManager locationManager;
    LocationListener locationListener;

    String dogName,ownerName,age,sex,email,address,dogBreed,dp;




    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        dogNameEt=findViewById(R.id.dogNameEdit);
        ownerNameEt=findViewById(R.id.ownerNameEdit);
        ageEt=findViewById(R.id.ageEtEdit);
        editBtn=findViewById(R.id.editBtn);
        addressTv =findViewById(R.id.locationAddresssEditTv);
        setLocation=findViewById(R.id.setLocationEdit);
        sexTv=findViewById(R.id.sexTvProfileEdit);
        breedTv=findViewById(R.id.breedTvProfileEdit);


        imageView=findViewById(R.id.imageCircularEdit);

        breed = new String[]{"Select Breed : ","captain marvel", "thor", "tokyo", "cisco", "catlin","Nebula","flash","Iron Man","Rio","Berlin","Nairobi"};
        gender=new String[]{"Select Gender : ","Male","Female"};

        Spinner breedSpinner = findViewById(R.id.spinner_breedEdit);
        final Spinner genderSpinner = findViewById(R.id.spinner_genderEdit);

        ArrayAdapter<String> adapterBreed = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, breed);
        ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,gender);

        breedSpinner.setAdapter(adapterBreed);
        genderSpinner.setAdapter(adapterGender);

        Intent intent = getIntent();

        dogName = intent.getStringExtra("dogName");
        ownerName = intent.getStringExtra("ownerName");
        age = intent.getStringExtra("age");
        sex = intent.getStringExtra("sex");
        address = intent.getStringExtra("address");
        dogBreed = intent.getStringExtra("dogBreed");
        dp = intent.getStringExtra("dp");

        // setting all values

        dogNameEt.setText(dogName);
        ownerNameEt.setText(ownerName);
        ageEt.setText(age);
        addressTv.setText(address);
        Glide.with(this).load(dp).into(imageView);
        sexTv.setText("Sex : " +" "+sex);
        breedTv.setText("Breed : "+" "+dogBreed);



        breedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                breedFromSpinner=breed[position];
                breedTv.setText("Breed : "+" "+breedFromSpinner);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sexFromSpinner=gender[position];
                sexTv.setText("Sex : " +" "+sexFromSpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);


        setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dogNameUpdated = dogNameEt.getText().toString();
                String ownerUpdated =  ownerNameEt.getText().toString();
                String ageUpdates = ageEt.getText().toString();
                String breedUpdated = breedTv.getText().toString();
                String genderUpdated = sexTv.getText().toString();

            }
        });














    }

    public void startListening(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

    }
}
