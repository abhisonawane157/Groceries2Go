package com.example.groceries2go;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ForgotPasswordActivity extends AppCompatActivity {

    ImageButton btnBack;
    EditText edEmail;
    Button btnRecover;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
    }

    private void initialize(){

        btnBack=findViewById(R.id.btnBack);
        edEmail=findViewById(R.id.edEmail);
        btnRecover=findViewById(R.id.btnRecover);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}