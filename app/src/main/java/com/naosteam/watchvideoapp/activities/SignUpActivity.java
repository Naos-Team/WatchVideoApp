package com.naosteam.watchvideoapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Patterns;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;
import com.naosteam.watchvideoapp.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText edt_email, edt_phone, edt_name, edt_pw1, edt_pw2;
    private Button btn_signup;
    private TextView tv_back;
    private FirebaseAuth mAuth;
    private AwesomeValidation awesomeValidation;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        awesomeValidation = new AwesomeValidation(ValidationStyle.UNDERLABEL);
        FindView();
        ViewClick();
    }

    private void FindView(){
        edt_email = findViewById(R.id.edt_signup_email);
        edt_pw1 = findViewById(R.id.edt_signup_pw1);
        edt_pw2 = findViewById(R.id.edt_signup_pw2);
        edt_name = findViewById(R.id.edt_signup_name);
        edt_phone = findViewById(R.id.edt_signup_phone);
        tv_back = findViewById(R.id.tv_login_open);
        btn_signup = findViewById(R.id.btn_signup);


        awesomeValidation.addValidation(this, R.id.edt_signup_name, RegexTemplate.NOT_EMPTY, R.string.invalid_signup_name);
        awesomeValidation.addValidation(this, R.id.edt_signup_phone, RegexTemplate.TELEPHONE, R.string.invalid_signup_phone);
        awesomeValidation.addValidation(this, R.id.edt_signup_email, Patterns.EMAIL_ADDRESS, R.string.invalid_signup_email);
        awesomeValidation.addValidation(this, R.id.edt_signup_pw1, ".{6,}", R.string.invalid_signup_pw1);
        awesomeValidation.addValidation(this, R.id.edt_signup_pw2, R.id.edt_signup_pw1, R.string.invalid_signup_pw2);
    }

    private void ViewClick(){
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()){
                    String email = edt_email.getText().toString().trim();
                    String password = edt_pw1.getText().toString().trim();
                    mAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                       @SuppressLint("RestrictedApi") User user = new User(password);
                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                                                    Toast.makeText(SignUpActivity.this, "Successfully. Please check your email.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                }
                            });
                }
            }
        });
    }



}