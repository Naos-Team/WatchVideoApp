package com.naosteam.watchvideoapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.naosteam.watchvideoapp.R;

public class ForgotPassActivity extends AppCompatActivity {

    private ImageView imv_back;
    private Button btn_submit;
    private EditText edt_email;
    private AwesomeValidation awesomeValidation;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        mAuth = FirebaseAuth.getInstance();
        awesomeValidation = new AwesomeValidation(ValidationStyle.UNDERLABEL);
        FindView();
        ViewClick();

    }

    private void FindView(){
        imv_back.findViewById(R.id.imv_back_to_login);
        edt_email.findViewById(R.id.edt_forgot_email);
        btn_submit.findViewById(R.id.btn_submit_email);


        awesomeValidation.addValidation(this, R.id.edt_forgot_email, Patterns.EMAIL_ADDRESS, R.string.invalid_signup_email);
    }

    private void ViewClick(){
        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()){
                    String email = edt_email.getText().toString().trim();
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ForgotPassActivity.this, "Please check your email to reset password", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(ForgotPassActivity.this, "Try again. Something wrong happened!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });
    }
}