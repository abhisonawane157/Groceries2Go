package com.example.groceries2go;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private FirebaseAuth Currauth;
    private OnCompleteListener<AuthResult> _Currauth_create_user_listener;
    private OnCompleteListener<AuthResult> _Currauth_sign_in_listener;
    private OnCompleteListener<Void> _Currauth_reset_password_listener;

    EditText edEmail, edPassword, edId;
    TextView tvForgotPass, tvNoAccount;
    Button btnLogin;
    ImageView back;
    RadioGroup rgType;
    RadioButton rdUser, rdSeller;
    private SharedPreferences file;
    private AlertDialog.Builder dialog;
    DatabaseReference userDatabase, userChild;
    private Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        com.google.firebase.FirebaseApp.initializeApp(this);
        initialize();
        initializeLogic();
    }


    private void initialize() {

        back = findViewById(R.id.back);
        edEmail = findViewById(R.id.edEmail);
        edId = findViewById(R.id.edId);
        edPassword = findViewById(R.id.edPassword);
        tvForgotPass = findViewById(R.id.tvForgotPass);
        tvNoAccount = findViewById(R.id.tvNoAccount);
        btnLogin = findViewById(R.id.btnLogin);
        file = getSharedPreferences("file", Activity.MODE_PRIVATE);
        tvForgotPass.setOnClickListener(this);
        tvNoAccount.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        dialog = new AlertDialog.Builder(this);
        Currauth = FirebaseAuth.getInstance();

        rgType = findViewById(R.id.rgType);
        rdSeller = findViewById(R.id.rdSeller);
        rdUser = findViewById(R.id.rdUser);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Exit");
                dialog.setMessage("Do you want to exit?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {
                        finish();
                    }
                });
                dialog.setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {

                    }
                });
                dialog.create().show();
            }
        });
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {

            case R.id.tvNoAccount:
//                startActivity(new Intent(this, RegisterUserActivity.class));
                dialog.setTitle("SignUp");
                dialog.setMessage("SignUp as ?");
                dialog.setPositiveButton("User", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {
                        intent.setClass(getApplicationContext(),RegisterUserActivity.class);
                        startActivity(intent);
                    }
                });
                dialog.setNegativeButton("Seller", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {
                        intent.setClass(getApplicationContext(),RegisterSellerActivity.class);
                        startActivity(intent);
                    }
                });
                dialog.create().show();
                break;

            case R.id.tvForgotPass:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;
            case R.id.btnLogin:
                login();

                //  case R.id.tvNoAccount: startActivity(new Intent(LoginActivity.this,RegisterUserActivity.class) ); break;

        }

    }

    private void login() {

        if (rdUser.isChecked()) {
            String id = edId.getText().toString();
            if (edEmail.getText().toString().equals("") && edPassword.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Please enter valid credentials", Toast.LENGTH_LONG).show();
            }
            else {
                Currauth.signInWithEmailAndPassword(edEmail.getText().toString(), edPassword.getText().toString()).addOnCompleteListener(LoginActivity.this, _Currauth_sign_in_listener);
            }
        } else if (rdSeller.isChecked()) {
            String id = edId.getText().toString();
            if (edEmail.getText().toString().equals("") && edPassword.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Please enter valid credentials", Toast.LENGTH_LONG).show();
            }
            else {
                Currauth.signInWithEmailAndPassword(edEmail.getText().toString(), edPassword.getText().toString()).addOnCompleteListener(LoginActivity.this, _Currauth_sign_in_listener);
            }
        } else {
            Toast.makeText(this, "Kindly select the Type of user", Toast.LENGTH_LONG).show();
        }

    _Currauth_create_user_listener = new OnCompleteListener<AuthResult>() {

        @Override
        public void onComplete(Task<AuthResult> _param1) {
            final boolean _success = _param1.isSuccessful();
            final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

        }
    };

    _Currauth_sign_in_listener = new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(Task<AuthResult> _param1) {
            final boolean _success = _param1.isSuccessful();
            final String errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
            if (_success) {
                if(rdUser.isChecked())
                {
                    file.edit().putString("emailid", edEmail.getText().toString()).apply();
                    file.edit().putString("user_uid", FirebaseAuth.getInstance().getCurrentUser().getUid()).apply();
                    file.edit().putString("type", "User").apply();
                    intent.setClass(getApplicationContext(), HomePageActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Logged in as, "+edEmail.getText().toString(), Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    file.edit().putString("sellerEmail", edEmail.getText().toString()).apply();
                    file.edit().putString("user_uid", FirebaseAuth.getInstance().getCurrentUser().getUid()).apply();
                    file.edit().putString("type", "Seller").apply();
                    intent.setClass(getApplicationContext(), MainSellerActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Logged in as, "+edEmail.getText().toString(), Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            else {

                Toast.makeText(getApplicationContext(), errorMessage,Toast.LENGTH_LONG).show();
            }
        }
    };

    _Currauth_reset_password_listener = new OnCompleteListener<Void>() {
        @Override
        public void onComplete(Task<Void> _param1) {
            final boolean _success = _param1.isSuccessful();

        }
    };
}
    private void initializeLogic() {
        if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
            if(file.getString("type","").equals("User"))
            {
                intent.setClass(getApplicationContext(), HomePageActivity.class);
                Toast.makeText(getApplicationContext(), "Welcome back, "+file.getString("userName",""), Toast.LENGTH_LONG).show();
                startActivity(intent);
                finish();
            }else{
                intent.setClass(getApplicationContext(), MainSellerActivity.class);
                Toast.makeText(getApplicationContext(), "Welcome back, "+file.getString("db_name",""), Toast.LENGTH_LONG).show();
                startActivity(intent);
                finish();
            }
        }
        else {

        }
    }

}


