package com.example.intern;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.*;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class EditProfileActivity extends AppCompatActivity {

    String[] breed;
    String[] gender;
    CircleImageView imageView;
    EditText dogNameEt,ownerNameEt,ageEt;
    ImageButton setLocation;
    String locationString;
    String locationStringUpdated;
    Button editBtn;
    TextView addressTv,sexTv,breedTv;



    String sexFromSpinner,breedFromSpinner;

    private static final int PICK_IMAGE1=1;
    private static final int PICK_IMAGE2=1;
    private static final int REQUEST_IMAGE_CAPTURE = 101;


    ProgressDialog pd;
    FirebaseAuth auth;
    FirebaseUser current_user;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    StorageReference storageReference;


    Uri imageUri;
    int PReqCode = 1 ;

    String storagePath = "Users_Dp/";

    LocationManager locationManager;
    LocationListener locationListener;

    String dogName,ownerName,age,sex,email,address,dogBreed,dp;
    String addressUpdated;

    static ArrayList<LatLng> locations= new ArrayList<LatLng>();





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


        auth= FirebaseAuth.getInstance();
        current_user=auth.getCurrentUser();


        pd = new ProgressDialog(this);
        pd.dismiss();

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

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("UserInfo");
        storageReference = getInstance().getReference();




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

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    OnGPS();
                } else {
                    getLocation();
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkaAndRequestForPermissionForGallery();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dogNameUpdated = dogNameEt.getText().toString();
                String ownerUpdated =  ownerNameEt.getText().toString();
                String ageUpdated = ageEt.getText().toString();
                String breedUpdated = breedTv.getText().toString();
                String genderUpdated = sexTv.getText().toString();



                updateInfo(dogNameUpdated,ownerUpdated,ageUpdated,breedUpdated,genderUpdated,addressUpdated,locationStringUpdated,imageUri);


            }
        });
    }

    private void updateInfo(final String dogNameUpdated, final String ownerUpdated, final String ageUpdated,
                            final String breedUpdated, final String genderUpdated,
                            final String addressUpdated, final String locationStringUpdated, Uri imageUri) {




        pd.setTitle("Adding Your Image");
        pd.show();
        String path=storagePath+"ProfileImages"+"_"+current_user.getUid();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReferenceProfilePic = firebaseStorage.getReference();
        StorageReference imageRef = storageReference.child(path);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();

                        if(uriTask.isSuccessful()) {
                            HashMap<String, Object> results = new HashMap<>();
                            results.put("dp", downloadUri.toString());
//                            results.put("dogsName",dogNameUpdated);
//                            results.put("ownerName",ownerUpdated);
//                            results.put("age",ageUpdated);
//                            results.put("dodsBreed",breedUpdated);
//                            results.put("sex",genderUpdated);
//                            results.put("address",addressUpdated);
//                            results.put("location",locationStringUpdated);

                            databaseReference.child(current_user.getUid()).updateChildren(results)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            pd.dismiss();
                                            Toast.makeText(EditProfileActivity.this,"Successfully Updated",Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(EditProfileActivity.this,"Failed "+e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            pd.dismiss();
                            Toast.makeText(EditProfileActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                        }
                        String profilePicUrl = taskSnapshot.getStorage().getDownloadUrl().toString();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pd.dismiss();
                        showMessage(exception.getMessage());
                        Toast.makeText(EditProfileActivity.this, exception.getCause().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //calculating progress percentage
//                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                        //displaying percentage in progress dialog
//                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                });
    }

    private void checkaAndRequestForPermissionForGallery(){
        if(ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(EditProfileActivity.this,"Please accept for required permission",Toast.LENGTH_LONG).show();
            }
            else{
                ActivityCompat.requestPermissions(EditProfileActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);
            }
        }else{
            // If every thing goes allRight the App has permission to access user gallery
            openGallery();
        }
    }
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== PICK_IMAGE2 && resultCode == RESULT_OK){
            imageUri = data.getData();
  //          imageView.setImageURI(imageUri);
            Glide.with(this).load(imageUri).into(imageView);
//            addImage(imageUri);
        }
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
                locationStringUpdated = location.getLongitude()+","+location.getLatitude();
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
        if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(EditProfileActivity.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED) {

            startListening();

        }

    }

    private void updateLocationInfo( final Location location) {

        addressUpdated=" Could not find address :( ";
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());

        locations.add(new LatLng(location.getLongitude(), location.getLatitude()));

        try{
            List<Address> listAddress=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            if (listAddress != null && listAddress.size()>0){
                addressUpdated="Address:\n";

                if(listAddress.get(0).getThoroughfare() !=null) {
                    addressUpdated += listAddress.get(0).getThoroughfare() +","+ "\n";
                }

                if (listAddress.get(0).getSubLocality() !=null){
                    addressUpdated+= listAddress.get(0).getLocality()+" ,"+"\n";
                }

                if (listAddress.get(0).getLocality() !=null){
                    addressUpdated+= listAddress.get(0).getLocality()+" ,"+"\n";
                }

                if (listAddress.get(0).getSubAdminArea() !=null){
                    addressUpdated+= listAddress.get(0).getLocality()+" ,"+"\n";
                }

                if (listAddress.get(0).getPostalCode() !=null){
                    addressUpdated+= listAddress.get(0).getPostalCode()+" ,"+"\n";
                }

                if (listAddress.get(0).getAdminArea() !=null){
                    addressUpdated+= listAddress.get(0).getAdminArea()+" "+"\n";
                }


            }
        }catch (Exception e){
            e.printStackTrace();
        }

        addressTv.setText(addressUpdated);
    }

    public void startListening(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

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

    private void showMessage(String s) {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

}
