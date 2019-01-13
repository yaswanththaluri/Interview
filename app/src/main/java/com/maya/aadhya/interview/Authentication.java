package com.maya.aadhya.interview;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Authentication extends AppCompatActivity {

    private EditText phone;
    private Button submit;
    private String number;
    private FirebaseAuth auth;
    private String verificationID;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String code;
    private ProgressDialog p;
    private ProgressDialog p2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        auth = FirebaseAuth.getInstance();

        phone = (EditText)findViewById(R.id.mobile);
        submit = (Button)findViewById(R.id.getotp);

        final CardView v = (CardView)findViewById(R.id.lay1);
        final CardView v2 = (CardView)findViewById(R.id.lay2);

        p = new ProgressDialog(this);
        p.setCanceledOnTouchOutside(false);
        p.setMessage("Verifying Mobile...Please wait!");

         p2= new ProgressDialog(this);
        p2.setCanceledOnTouchOutside(false);
        p2.setMessage("Sending OTP...Please wait!");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                number = phone.getText().toString();

                if (!number.equals("") && number.length()==10)
                {
                    p2.show();
                    authenticate(number);
                }
                else
                {
                    Toast.makeText(Authentication.this, "Phone number not valid", Toast.LENGTH_SHORT).show();
                }

            }
        });


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                Toast.makeText(Authentication.this, "Phone Number Verified", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Authentication.this, ProfileEdit.class);
                startActivity(i);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toast.makeText(Authentication.this, e.toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                verificationID = s;
                mResendToken = forceResendingToken;

                p2.dismiss();

                v.setVisibility(View.INVISIBLE);
                v2.setVisibility(View.VISIBLE);

                Button b1 = (Button)findViewById(R.id.submitotp);
                final EditText t = (EditText)findViewById(R.id.verifyotp);

                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        code = t.getText().toString();
                        if (!code.equals(""))
                        {
                            p.show();
                            verify();
                        }
                    }
                });




                Toast.makeText(Authentication.this, "Verification code sent", Toast.LENGTH_SHORT).show();
            }
        };
    }


    public void authenticate(String phoneNumber)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks
        );

    }

    public void verify()
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    p.dismiss();
                    Toast.makeText(Authentication.this, "Phone Number Verified success", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Authentication.this, ProfileEdit.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(Authentication.this, "Verification Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
