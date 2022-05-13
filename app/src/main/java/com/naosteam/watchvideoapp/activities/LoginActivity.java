package com.naosteam.watchvideoapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.utils.Constant;

public class LoginActivity extends AppCompatActivity {

    private EditText edt_email, edt_pass;
    private Button btn_login, btn_login_gg, btn_skip;
    private TextView tv_forgot, tv_signup;
    private AwesomeValidation awesomeValidation;
    private FirebaseAuth mAuth;
    private GoogleSignInOptions options;
    private final static int RC_SIGN_IN = 123;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private boolean check_alert, is_first;
    private String gg_email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        GoogleSignInAccount gg_account;
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("DataLogin", Context.MODE_PRIVATE);
        check_alert = sharedPreferences.getBoolean("check_not_show_alert", false);
        is_first = sharedPreferences.getBoolean("is_first", true);
        gg_email = sharedPreferences.getString("gg_email", "");
        options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();
        Constant.ggclient = GoogleSignIn.getClient(this, options);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(LoginActivity.this, R.id.edt_login_user, RegexTemplate.NOT_EMPTY, R.string.invalid_login_user);
        awesomeValidation.addValidation(LoginActivity.this, R.id.edt_login_pass, RegexTemplate.NOT_EMPTY, R.string.invalid_login_pass);
        FindView();
        ViewClick();

//        if (mAuth.getCurrentUser()!=null){
//            if (gg_email!=null){
//                CheckUser(mAuth.getCurrentUser());
//            }
//            else{
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            }
//        }
    }

    private void FindView(){
        edt_email = findViewById(R.id.edt_login_user);
        edt_pass = findViewById(R.id.edt_login_pass);
        btn_login = findViewById(R.id.btn_login);
        btn_login_gg = findViewById(R.id.btn_login_gg);
        tv_forgot = findViewById(R.id.tv_forgotpass_open);
        tv_signup = findViewById(R.id.tv_signup_open);
        btn_skip = findViewById(R.id.btn_skip);
        progressBar = findViewById(R.id.progress_startlg);
        progressBar.setVisibility(View.GONE);
    }

    private void ViewClick() {

        btn_skip.setOnClickListener(v->{
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(awesomeValidation.validate()){
                    String email = edt_email.getText().toString().trim();
                    String password = edt_pass.getText().toString().trim();
                    mAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                }
                            }
                    ).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user.isEmailVerified()){
                                progressBar.setVisibility(View.VISIBLE);
//                                AuthCredential credential = EmailAuthProvider.getCredential(email,password);
//                                mAuth.getCurrentUser().linkWithCredential(credential);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("is_first", false);
                                editor.apply();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                            else{
                                user.sendEmailVerification();
                                Toast.makeText(LoginActivity.this, "Check your email to verify your account", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Failed to login. Please check your info", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btn_login_gg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Button btn_gg, btn_su;
                CheckBox check_dialog;
                TextView tv_dismiss;
                ViewGroup viewGroup = findViewById(android.R.id.content);
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                View view1 = LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialog_layout, viewGroup,false);
                builder.setCancelable(false);
                builder.setView(view1);

                btn_gg = view1.findViewById(R.id.btn_submit);
                btn_su = view1.findViewById(R.id.btn_next_su);
                check_dialog = view1.findViewById(R.id.check_dialog);
                tv_dismiss = view1.findViewById(R.id.tv_dismiss);

                final AlertDialog alertDialog = builder.create();

                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                btn_gg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        if(check_dialog.isChecked()){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("check_not_show_alert", true);
                            editor.apply();


                        }


                        SignInWithGoogle();
                    }
                });

                btn_su.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        if(check_dialog.isChecked()){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("check_not_show_alert", true);
                            editor.apply();


                        }
                        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                    }
                });

                tv_dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });



                if (sharedPreferences.getBoolean("check_not_show_alert", false)){
                    SignInWithGoogle();
                }
                else{
                    alertDialog.show();
                }
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


    }    private void SignInWithGoogle() {
        Intent intent = Constant.ggclient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogleAccount(account);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogleAccount(GoogleSignInAccount account)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
//                            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
//                            mAuth.getCurrentUser().linkWithCredential(credential);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("is_first", false);
                            editor.putString("gg_email", account.getEmail());
                            editor.apply();
                            progressBar.setVisibility(View.VISIBLE);
                            CheckUser(mAuth.getCurrentUser());

                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void CheckUser(FirebaseUser user){
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(LoginActivity.this, UpdateProfileActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}