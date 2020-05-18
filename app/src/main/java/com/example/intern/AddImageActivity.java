package com.example.intern;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class AddImageActivity extends AppCompatActivity {
    CircleImageView circleImageView;
    Button nextBtn,addImage;
    TextView catchErrorTv;

    private static final int PICK_IMAGE1=1;
    private static final int PICK_IMAGE2=1;
    private static final int REQUEST_IMAGE_CAPTURE = 101;

    FirebaseAuth auth;
    FirebaseUser current_user;

    ProgressDialog pd;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    StorageReference storageReference;


    Uri imageUri;
    int PReqCode = 1 ;

    String storagePath = "Users_Dp/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);

        circleImageView=findViewById(R.id.imageAddViewCircular);
        nextBtn=findViewById(R.id.addImageBtn);
        addImage=findViewById(R.id.addImage);


        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("UserInfo");
        storageReference = getInstance().getReference();

        auth= FirebaseAuth.getInstance();
        current_user=auth.getCurrentUser();

        pd= new ProgressDialog(this);
        pd.dismiss();

        catchErrorTv=findViewById(R.id.catchErrorTv);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkaAndRequestForPermissionForGallery();
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddImageActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });

    }

//    private void addImage(Uri imageUri) {
//        pd.setTitle("Adding Your location");
//        pd.show();
//        String filePathAndName= storagePath+"_"+ current_user.getUid();
//        StorageReference storageReference1= storageReference.child(filePathAndName);
//        storageReference.putFile(imageUri)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                        while(!uriTask.isSuccessful());
//                        Uri downloadUri = uriTask.getResult();
//
//                        if(uriTask.isSuccessful()) {
//                            HashMap<String, Object> results = new HashMap<>();
//                            results.put("dp", downloadUri.toString());
//
//                            databaseReference.child(current_user.getUid()).updateChildren(results)
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            pd.dismiss();
//                                            Toast.makeText(AddImageActivity.this,"Image Updated ...",Toast.LENGTH_SHORT).show();
//                                            ;
//                                        }
//                                    }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    pd.dismiss();
//                                    Toast.makeText(AddImageActivity.this,"Failed "+e.getMessage(),Toast.LENGTH_SHORT).show();
//                                    catchErrorTv.setText(e.getMessage());
//                                }
//                            });
//                        }else {
//                            pd.dismiss();
//                            Toast.makeText(AddImageActivity.this,"Failed",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                pd.dismiss();
//                Toast.makeText(AddImageActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
//                catchErrorTv.setText(e.getMessage());
//            }
//        });
//    }
private void addImage(Uri imageUri) {
        pd.setTitle("Adding Your Image");
        pd.show();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReferenceProfilePic = firebaseStorage.getReference();
    StorageReference imageRef = storageReferenceProfilePic.child("dp");

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

                            databaseReference.child(current_user.getUid()).updateChildren(results)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            pd.dismiss();
                                            Toast.makeText(AddImageActivity.this,"Image Updated ...",Toast.LENGTH_SHORT).show();
                                            ;
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(AddImageActivity.this,"Failed "+e.getMessage(),Toast.LENGTH_SHORT).show();
                                    catchErrorTv.setText(e.getMessage());
                                }
                            });
                        }else {
                            pd.dismiss();
                            Toast.makeText(AddImageActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                        }

                    //if the upload is successful
                    //hiding the progress dialog
                    //and displaying a success toast
                //    dismissDialog();
                    String profilePicUrl = taskSnapshot.getStorage().getDownloadUrl().toString();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    pd.dismiss();
                    showMessage(exception.getMessage());

                    //if the upload is not successful
                    //hiding the progress dialog
                  //  dismissDialog();
                    //and displaying error message
                    Toast.makeText(AddImageActivity.this, exception.getCause().getLocalizedMessage(), Toast.LENGTH_LONG).show();
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
        if(ContextCompat.checkSelfPermission(AddImageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(AddImageActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(AddImageActivity.this,"Please accept for required permission",Toast.LENGTH_LONG).show();
            }
            else{
                ActivityCompat.requestPermissions(AddImageActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);
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
            circleImageView.setImageURI(imageUri);
            addImage(imageUri);
        }
    }

    private void showMessage(String s) {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

}
