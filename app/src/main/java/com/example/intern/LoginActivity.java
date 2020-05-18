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

public class LoginActivity extends AppCompatActivity {

    EditText login_email,login_password;
    FloatingActionButton fab;
    TextView forgetPassword;
    ProgressDialog pd;


    FirebaseAuth auth;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        forgetPassword=findViewById(R.id.forget_password);
        login_email=findViewById(R.id.email_Login);
        login_password=findViewById(R.id.password_login);

        auth=FirebaseAuth.getInstance();
        pd = new ProgressDialog(LoginActivity.this);

        fab=findViewById(R.id.floating_Action_Login);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fab.hide();
                pd.setMessage("Please wait ..");
                pd.show();

                String email = login_email.getText().toString();
                String password = login_password.getText().toString();

                if (email.isEmpty() || password.isEmpty()){
                    fab.show();
                    pd.dismiss();
                    showMessage("All Fields are required ");
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                    fab.show();
                    pd.dismiss();
                    showMessage(" Invalid Email");
                    login_email.setError("Invalid Email");
                    login_email.setFocusable(true);
                }
                else{
                    auth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        pd.dismiss();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        fab.show();
                                        pd.dismiss();
                                        showMessage("Authentication Failed" + task.getException().toString());
                                    }
                                }
                            });
                }

            }
        });
    }
    private void showMessage(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
    public void OpenRegisterPage(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // user is already connected so we need to redirect
            startActivity(new Intent(LoginActivity.this,MainActivity.class));

        }

    }
}
