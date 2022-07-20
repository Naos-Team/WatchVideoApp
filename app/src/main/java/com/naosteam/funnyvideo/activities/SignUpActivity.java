package com.naosteam.funnyvideo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Range;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;
import com.naosteam.funnyvideo.R;
import com.naosteam.funnyvideo.asynctasks.ExecuteQueryAsync;
import com.naosteam.funnyvideo.listeners.ExecuteQueryAsyncListener;
import com.naosteam.funnyvideo.models.Users_M;
import com.naosteam.funnyvideo.utils.AdsManager;
import com.naosteam.funnyvideo.utils.Methods;

import okhttp3.RequestBody;

public class SignUpActivity extends AppCompatActivity {

    private EditText edt_email, edt_phone, edt_name, edt_pw1, edt_pw2,edt_age;
    private Button btn_signup;
    private TextView tv_back;
    private FirebaseAuth mAuth;
    private AwesomeValidation awesomeValidation;
    private int age;
    GoogleSignInOptions options;
    private final static int RC_SIGN_IN = 123;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
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
        edt_age = findViewById(R.id.edt_signup_age);


        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(SignUpActivity.this, R.id.edt_signup_name, RegexTemplate.NOT_EMPTY, R.string.invalid_signup_name);
        awesomeValidation.addValidation(SignUpActivity.this, R.id.edt_signup_phone, RegexTemplate.TELEPHONE, R.string.invalid_signup_phone);
        awesomeValidation.addValidation(SignUpActivity.this, R.id.edt_signup_email, Patterns.EMAIL_ADDRESS, R.string.invalid_signup_email);
        awesomeValidation.addValidation(SignUpActivity.this, R.id.edt_signup_pw1, ".{6,}", R.string.invalid_signup_pw1);
        awesomeValidation.addValidation(SignUpActivity.this, R.id.edt_signup_pw2, R.id.edt_signup_pw1, R.string.invalid_signup_pw2);
        awesomeValidation.addValidation(SignUpActivity.this, R.id.edt_signup_age, Range.closed(0,100), R.string.invalid_age);

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
                    SignUp();
                }
            }
        });
    }


    private void SignUp(){
        String email = edt_email.getText().toString().trim();
        String password = edt_pw1.getText().toString().trim();
        String name = edt_name.getText().toString().trim();
        String phone = edt_phone.getText().toString().trim();
        String t_age = edt_age.getText().toString().trim();
        age = Integer.parseInt(t_age);

        if(Methods.getInstance().isNetworkConnected(SignUpActivity.this)) {
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {
                                            }
                                        })
                    .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                e.printStackTrace();
                                                e.toString();
                                            }

                                        })
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    String url = "";
                                                    if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null){
                                                        url = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
                                                    }
                                                    else{
                                                        url = "empty";
                                                    }
                                                    @SuppressLint("RestrictedApi") Users_M user = new Users_M(FirebaseAuth.getInstance().getCurrentUser().getUid(), name, email, phone, age, url);
                                                    FirebaseDatabase.getInstance().getReference("Users")
                                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                                                                ExecuteQueryAsyncListener listener = new ExecuteQueryAsyncListener() {
                                                                    @Override
                                                                    public void onStart() {

                                                                    }

                                                                    @Override
                                                                    public void onEnd(boolean status) {
                                                                        if(status){
                                                                            Toast.makeText(SignUpActivity.this, "Successfully. Please check your email.", Toast.LENGTH_SHORT).show();
                                                                        }else{
                                                                            Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }

                                                                };

                                                                Bundle bundle = new Bundle();
                                                                bundle.putString("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());

                                                                RequestBody requestBody = Methods.getInstance().getLoginRequestBody("METHOD_SIGNUP",bundle);
                                                                ExecuteQueryAsync async = new ExecuteQueryAsync(requestBody, listener);
                                                                async.execute();
                                                                AdsManager.showAdmobInterAd(SignUpActivity.this, new AdsManager.InterAdsListener() {
                                                                    @Override
                                                                    public void onClick() {
                                                                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                                                    }
                                                                });

                                                            } else {
                                                                Toast.makeText(SignUpActivity.this, "Something wrong happened. Please try again", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                                    Toast.makeText(SignUpActivity.this, "This account has already been in use", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                    }
        else{
            Toast.makeText(SignUpActivity.this, "No Internet Connected", Toast.LENGTH_SHORT).show();
        }



    }



}