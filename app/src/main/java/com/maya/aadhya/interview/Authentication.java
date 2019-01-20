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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Authentication extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText emailLogin;
    private EditText passwordLogin;
    private Button signin;
    private ProgressDialog dialog;
    private ProgressDialog dialog2;

    private CardView card1;
    public CardView card2;

    private TextView signup;
    private TextView forgot;
    private TextView login;

    private Button signupsubmit;
    private EditText emailSignup;
    private EditText passwordSignUp;
    private EditText cnfPasswordSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        auth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(Authentication.this);

        emailLogin = (EditText)findViewById(R.id.emailauth);
        passwordLogin = (EditText)findViewById(R.id.passwordauth);

        card1 = (CardView)findViewById(R.id.lay1);
        card2 = (CardView)findViewById(R.id.lay2);

        signup = (TextView) findViewById(R.id.signupchange);
        forgot = (TextView)findViewById(R.id.forgotpassword);
        login = (TextView)findViewById(R.id.loginchange);

        dialog.setMessage("Authenticating User...Please Wait!");
        dialog.setCanceledOnTouchOutside(false);

        dialog2 = new ProgressDialog(Authentication.this);
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setMessage("Creating Account...Please Wait!");

        signin = (Button)findViewById(R.id.signin);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();

                String email = emailLogin.getText().toString();
                String password = passwordLogin.getText().toString();

                if (!email.equals("") && !password.equals(""))
                {
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Authentication.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        final FirebaseUser user = auth.getCurrentUser();

                                        if (user.isEmailVerified()) {
                                            dialog.dismiss();
                                            Intent i = new Intent(Authentication.this, Dashboard.class);
                                            startActivity(i);
                                        } else {

                                            dialog.dismiss();

                                            final AlertDialog.Builder alert = new AlertDialog.Builder(Authentication.this);
                                            alert.setTitle("Verification Error");
                                            alert.setCancelable(false);
                                            alert.setMessage("Email not Verified Yet. Do you want to send the Verificarion Link again");
                                            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    user.sendEmailVerification()
                                                            .addOnCompleteListener(Authentication.this, new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (!task.isSuccessful()) {
                                                                        Toast.makeText(Authentication.this, "Account created successfully but failed to sent " +
                                                                                        "Verification mail"
                                                                                , Toast.LENGTH_SHORT).show();
                                                                    } else {
                                                                        Toast.makeText(Authentication.this, "Account created successfully and verification sent to MailId"
                                                                                , Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                }
                                            });

                                            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });


                                            alert.show();


                                        }


                                    }
                                    else
                                    {
                                        dialog.dismiss();
                                        Toast.makeText(Authentication.this, "Invalid Details", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                card2.setVisibility(View.VISIBLE);
                card1.setVisibility(View.INVISIBLE);

                emailSignup = (EditText)findViewById(R.id.emailsignup);
                passwordSignUp = (EditText)findViewById(R.id.passwordsignup);
                cnfPasswordSignUp = (EditText)findViewById(R.id.passwordscnf);

                signupsubmit = (Button)findViewById(R.id.signup);

                signupsubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog2.show();
                        String mail;
                        String pass;
                        String cnfPass;

                        mail = emailSignup.getText().toString();
                        pass = passwordSignUp.getText().toString();
                        cnfPass = cnfPasswordSignUp.getText().toString();

                        if (!mail.equals("") && !pass.equals("") && pass.equals(cnfPass))
                        {

                            auth.createUserWithEmailAndPassword(mail, pass)
                                    .addOnCompleteListener(Authentication.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            if (task.isSuccessful())
                                            {

                                                dialog2.dismiss();
                                                FirebaseUser user = auth.getCurrentUser();

                                                user.sendEmailVerification()
                                                        .addOnCompleteListener(Authentication.this, new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if (task.isSuccessful())
                                                                {
                                                                    card2.setVisibility(View.INVISIBLE);
                                                                    card1.setVisibility(View.VISIBLE);
                                                                    Toast.makeText(Authentication.this, "SignUp Successfull and " +
                                                                            "Verification Mail send to Mail id", Toast.LENGTH_SHORT).show();

                                                                }
                                                                else
                                                                {
                                                                    Toast.makeText(Authentication.this, "SignUp Successfull and " +
                                                                            "Verification Mail send to Mail id", Toast.LENGTH_SHORT).show();
                                                                }

                                                            }
                                                        });
                                            }
                                            else

                                            {
                                                dialog2.dismiss();
                                                Toast.makeText(Authentication.this, "Failed to " +
                                                        "SignUp with this details", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });

                        }

                        else
                        {
                            dialog2.dismiss();
                            Toast.makeText(Authentication.this, "Details should follow the format", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card1.setVisibility(View.VISIBLE);
                card2.setVisibility(View.INVISIBLE);
            }
        });
    }
}
