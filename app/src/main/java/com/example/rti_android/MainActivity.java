package com.example.rti_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button b = (Button)findViewById(R.id.buttonConnect);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.err.print("buttonConnect Clicked");
                EditText username = (EditText)findViewById(R.id.editTextUsername);
                EditText password = (EditText)findViewById(R.id.editTextPassword);
                Switch newAccount = (Switch) findViewById(R.id.switchIsNewAccount);

                String usernameStr = username.getText().toString();
                String passwordStr = password.getText().toString();
                boolean newAccountBool = newAccount.isChecked();

                if(usernameStr.isEmpty()){
                    Dialog.showAlertDialog(MainActivity.this,"Erreur", "Le nom d'utilisateur ne peut pas être vide !", null);
                    Log.e("MainActivity_app",  "username cannot be empty !");

                }
                else if(passwordStr.isEmpty()){
                    Dialog.showAlertDialog(MainActivity.this,"Erreur", "Le mot de passe ne peut pas être vide !", null);
                    Log.e("MainActivity_app",  "Password cannot be empty !");

                }
                else{
                    Log.e("MainActivity_app",  "Creating message");
                    String message = "LOGIN#" + usernameStr + "#" + passwordStr + "#" + (newAccountBool ? "1" : "0") + "#";

                    try {
                        new AsyncSocketAccessor.AsyncSendMessage(message).execute();
                        new AsyncSocketAccessor.AsyncReceiveMessage(MainActivity.this).execute();
                    } catch (Exception e) {
                        Log.e("MainActivity_app",  "ERROR : " + e.getMessage());
                    }

                }
            }
        });
    }

}