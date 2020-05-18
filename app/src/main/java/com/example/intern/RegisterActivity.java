package com.example.intern;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText username_register,email_register,name_register,password_register;
    TextView registerButton;
    FloatingActionButton fab;
    ProgressDialog pd;


    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email_register=findViewById(R.id.email_register);
        password_register=findViewById(R.id.password_register);
        fab=findViewById(R.id.floating_Action_Register);

        auth=FirebaseAuth.getInstance();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.hide();
                pd= new ProgressDialog(RegisterActivity.this);
                pd.setMessage("Please wait..");
                pd.show();


                String email = email_register.getText().toString();
                String password = password_register.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    showMessage(" Enter Correct Email ");
                    email_register.setError("Invalid Email");
                    email_register.setFocusable(true);
                    fab.show();
                    pd.hide();
                }
                else if (password.length() < 6  ) {
                    // every thing is ok then Create User Account
                    showMessage("Password length should be greater than 6 char and make it unique");
                    password_register.setError(" Password length at least 6 characters ");
                    password_register.setFocusable(true);
                    fab.show();
                    pd.hide();
                }
                else{
                    createUser(email,password);
                }
            }
        });
    }

    private void createUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
//                            FirebaseUser firebaseUser = auth.getCurrentUser();
//                            String email= firebaseUser.getEmail();
//                            String userId = firebaseUser.getUid();
//
//                            HashMap<String,Object > hashMap = new HashMap<>();
//
//                            hashMap.put("id",userId);
//                            hashMap.put("email",email);
//
//                            reference = FirebaseDatabase.getInstance().getReference().child("User");
//                            reference.child(userId).setValue(hashMap);

                            pd.dismiss();
                            Intent intent= new Intent(RegisterActivity.this,EnterInfoActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                            showMessage(" Register Successfully ");

                        }else{
                            fab.show();
                            pd.dismiss();
                            showMessage("Authentication Failed "+task.getException());
                        }

                    }
                });

    }

    private void showMessage(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
    public void OpenLoginPage(View view) {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // user is already connected so we need to redirect
            startActivity(new Intent(RegisterActivity.this,MainActivity.class));

        }


    }

}
