package com.example.intern;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.*;

public class EnterInfoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    EditText dogNameEt,ownerNameEt,ageEt;
    ImageButton setLocation;
    TextView addressTv;
    Button saveBtn;

    String[] breed;
    String[] gender;

    String dogsBreed,sex,locationString,address;

    FirebaseFirestore db;


    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    LocationManager locationManager;
    LocationListener locationListener;

    ProgressDialog pd;

    static ArrayList<LatLng> locations= new ArrayList<LatLng>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_info);

        dogNameEt=findViewById(R.id.dogNameEdit);
        ownerNameEt=findViewById(R.id.ownerNameEdit);
        ageEt=findViewById(R.id.ageEtEdit);

        setLocation=findViewById(R.id.setLocationEdit);
        addressTv=findViewById(R.id.locationAddresssEditTv);

        saveBtn=findViewById(R.id.saveBtn);

         breed = new String[]{"Select Breed : ","captain marvel", "thor", "tokyo", "cisco", "catlin","Nebula","flash","Iron Man","Rio","Berlin","Nairobi"};
         gender=new String[]{"Select Gender : ","Male","Female"};

        Spinner breedSpinner = findViewById(R.id.spinner_breedEdit);
        final Spinner genderSpinner = findViewById(R.id.spinner_genderEdit);

        pd = new ProgressDialog(this);
        pd.dismiss();

        auth=FirebaseAuth.getInstance();

        ArrayAdapter<String> adapterBreed = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, breed);
        ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,gender);

        breedSpinner.setAdapter(adapterBreed);

        breedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Selected User: "+breed[position] ,Toast.LENGTH_SHORT).show();
                dogsBreed=breed[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        genderSpinner.setAdapter(adapterGender);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Selected Gender: " +gender[position] ,Toast.LENGTH_SHORT).show();
                sex=gender[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        db = FirebaseFirestore.getInstance();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dogName=dogNameEt.getText().toString();
                String ownerName=ownerNameEt.getText().toString();
                String age=ageEt.getText().toString();
                uploadToFirebase(dogName,ownerName,age,dogsBreed,sex,locationString);
            }
        });

        locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    OnGPS();
                } else {
                    getLocation();
                }
            }
        });



    }





    private void getLocation() {

        pd.setTitle("Adding Your location");
        pd.show();
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                pd.dismiss();
                updateLocationInfo(location);
                //addressTv.setText(location.getLongitude()+","+location.getLatitude());
                locationString = location.getLongitude()+","+location.getLatitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (ContextCompat.checkSelfPermission(EnterInfoActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(EnterInfoActivity.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);

        }
        else
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastKnowLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastKnowLocation!=null){
                updateLocationInfo(lastKnowLocation);
            }
        }
    }

    private void updateLocationInfo( final Location location) {

        address=" Could not find address :( ";
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());

        locations.add(new LatLng(location.getLongitude(), location.getLatitude()));

        try{
            List<Address> listAddress=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            if (listAddress != null && listAddress.size()>0){
                address="Address:\n";

                if(listAddress.get(0).getThoroughfare() !=null) {
                    address += listAddress.get(0).getThoroughfare() +","+ "\n";
                }

                if (listAddress.get(0).getSubLocality() !=null){
                    address+= listAddress.get(0).getLocality()+" ,"+"\n";
                }

                if (listAddress.get(0).getLocality() !=null){
                    address+= listAddress.get(0).getLocality()+" ,"+"\n";
                }


                if (listAddress.get(0).getSubAdminArea() !=null){
                    address+= listAddress.get(0).getLocality()+" ,"+"\n";
                }

                if (listAddress.get(0).getPostalCode() !=null){
                    address+= listAddress.get(0).getPostalCode()+" ,"+"\n";
                }

                if (listAddress.get(0).getAdminArea() !=null){
                    address+= listAddress.get(0).getAdminArea()+" "+"\n";
                }


            }
        }catch (Exception e){
            e.printStackTrace();
        }

        addressTv.setText(address);
    }

    private void uploadToFirebase(String dogName, String ownerName, String age, String dogsBreed, String sex,String locationString) {
        pd.setTitle("Adding Data to Firestore");
        pd.show();
        FirebaseUser firebaseUser = auth.getCurrentUser();

//        String id = UUID.randomUUID().toString();
        String userId = firebaseUser.getUid();
        String email = firebaseUser.getEmail();

        Map<String,Object> doc = new HashMap<>();

        doc.put("id",userId);
        doc.put("dogsName",dogName);
        doc.put("ownerName",ownerName);
        doc.put("email",email);
        doc.put("age",age);
        doc.put("dodsBreed",dogsBreed);
        doc.put("dp","");
        doc.put("sex",sex);
        doc.put("address",address);
        doc.put("location",locationString);

        reference = FirebaseDatabase.getInstance().getReference("UserInfo");
        reference.child(userId).setValue(doc).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        showMessage("Data Is Successfully Added");
                        pd.dismiss();
                        Intent intent = new Intent(EnterInfoActivity.this,AddImageActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                pd.dismiss();
                showMessage(e.getMessage());
            }
        });

//        db.collection("UserInfo").document(id).set(doc)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        showMessage("Data Is Successfully Added");
//                        pd.dismiss();
//                        Intent intent = new Intent(EnterInfoActivity.this,MainActivity.class);
//                        startActivity(intent);
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                pd.dismiss();
//                showMessage(e.getMessage().toString());
//            }
//        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED) {
            startListening();

        }

    }
    public void startListening(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

    }

    private void showMessage(String s) {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }


    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}


