package com.naosteam.watchvideoapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.naosteam.watchvideoapp.R;

public class LoginActivity extends AppCompatActivity {

    private EditText edt_email, edt_pass;
    private Button btn_login, btn_login_gg;
    private TextView tv_forgot, tv_signup;
    private AwesomeValidation awesomeValidation;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        awesomeValidation = new AwesomeValidation(ValidationStyle.UNDERLABEL);
        FindView();
        ViewClick();
    }

    private void FindView(){
        edt_email = findViewById(R.id.edt_login_user);
        edt_pass = findViewById(R.id.edt_login_pass);
        btn_login = findViewById(R.id.btn_login);
        btn_login_gg = findViewById(R.id.btn_login_gg);
        tv_forgot = findViewById(R.id.tv_forgotpass_open);
        tv_signup = findViewById(R.id.tv_signup_open);


        awesomeValidation.addValidation(this, R.id.edt_login_user, RegexTemplate.NOT_EMPTY, R.string.invalid_login_user);
        awesomeValidation.addValidation(this, R.id.edt_login_pass, RegexTemplate.NOT_EMPTY, R.string.invalid_login_pass);
    }

    private void ViewClick() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(awesomeValidation.validate()){
                    String email = edt_email.getText().toString().trim();
                    String password = edt_pass.getText().toString().trim();
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if(user.isEmailVerified()){
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        }
                                        else{
                                            user.sendEmailVerification();
                                            Toast.makeText(LoginActivity.this, "Check your email to verify your account", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                    else{
                                        Toast.makeText(LoginActivity.this, "Failed to login. Please check your info", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                    );
                }
            }
        });

        btn_login_gg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        tv_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPassActivity.class));
            }
        });
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }


}