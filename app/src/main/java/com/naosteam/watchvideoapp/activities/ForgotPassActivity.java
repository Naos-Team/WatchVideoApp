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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.databinding.ActivityForgotPassBinding;
import com.naosteam.watchvideoapp.utils.AdsManager;

public class ForgotPassActivity extends AppCompatActivity {

    private ImageView imv_back;
    private Button btn_submit;
    private EditText edt_email;
    private ActivityForgotPassBinding binding;
    private AwesomeValidation awesomeValidation;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPassBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
        awesomeValidation = new AwesomeValidation(ValidationStyle.UNDERLABEL);
        awesomeValidation.addValidation(this, R.id.edt_forgot_email, Patterns.EMAIL_ADDRESS, R.string.invalid_signup_email);
        ViewClick();

    }

    private void ViewClick(){
        binding.imvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdsManager.showAdmobInterAd(ForgotPassActivity.this, new AdsManager.InterAdsListener() {
                    @Override
                    public void onClick() {
                        onBackPressed();
                    }
                });

            }
        });

        binding.btnSubmitEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()){
                    String email = binding.edtForgotEmail.getText().toString().trim();
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
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.toString();
                        }
                    });
                }
            }
        });
    }
}