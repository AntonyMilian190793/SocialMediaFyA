package com.antonymilian.socialmediafya.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.antonymilian.socialmediafya.R;
import com.antonymilian.socialmediafya.models.User;
import com.antonymilian.socialmediafya.providers.AuthProvider;
import com.antonymilian.socialmediafya.providers.UsersProvider;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class CompleteProfileActivity extends AppCompatActivity {

    TextInputEditText mTextInputUsername;
    TextInputEditText mTextimputPhone;

    Button mButtonConfim;
    AuthProvider mAuthProvider;
    UsersProvider mUsersProvider;
    AlertDialog mDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);


        mTextInputUsername = findViewById(R.id.textInputUserNameR);
        mTextimputPhone = findViewById(R.id.textInputPhone);
        mButtonConfim = findViewById(R.id.btnConfirm);

        mAuthProvider = new AuthProvider();
        mUsersProvider = new UsersProvider();

        mDialog = new SpotsDialog(CompleteProfileActivity.this, "Espere por favor");

        mButtonConfim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

    }

    private void register() {
        String username = mTextInputUsername.getText().toString();
        String phone = mTextimputPhone.getText().toString();

        if(!username.isEmpty()){
            updateUser(username, phone);
        }else{
            Toast.makeText(this, "Para continuar inserta todos los campos!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUser(final String username, final String phone){
        String id = mAuthProvider.getUid();
        User user = new User();
        user.setUsername(username);
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        user.setId(id);
        user.setPhone(phone);
        user.setTimestamp(new Date().getTime());
        mDialog.show();
        //map.put("password", password);

        mUsersProvider.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful()) {
                    Intent intent = new Intent(CompleteProfileActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(CompleteProfileActivity.this, "No se pudo almacenar el usuario en la base de datos!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}