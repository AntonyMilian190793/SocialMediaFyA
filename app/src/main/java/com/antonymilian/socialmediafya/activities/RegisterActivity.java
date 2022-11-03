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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {

    CircleImageView mCircleImageViewBack;
    TextInputEditText mTextInputUsername;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;
    TextInputEditText mTextInputConfirmPassword;
    TextInputEditText mTextInputPhone;
    Button mButtonRegister;
    AuthProvider mAuthProvider;
    UsersProvider mUsersProvider;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mCircleImageViewBack = findViewById(R.id.cicleImageBack);
        mTextInputUsername = findViewById(R.id.textInputUserNameR);
        mTextInputEmail = findViewById(R.id.textInputEmailR);
        mTextInputPassword = findViewById(R.id.textInputPasswordR);
        mTextInputConfirmPassword = findViewById(R.id.textInputConfirmPasswordR);
        mTextInputPhone = findViewById(R.id.textInputPhone);
        mButtonRegister = findViewById(R.id.btnRegister);

        mAuthProvider = new AuthProvider();
        mUsersProvider = new UsersProvider();

        mDialog = new SpotsDialog(RegisterActivity.this, "Espere por favor");

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        mCircleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void register() {
        String username = mTextInputUsername.getText().toString();
        String email = mTextInputEmail.getText().toString();
        String password = mTextInputPassword.getText().toString();
        String confirmPassword = mTextInputConfirmPassword.getText().toString();
        String phone = mTextInputPhone.getText().toString();

        if(!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty() && !phone.isEmpty()){
            if(isEmailValid(email)){
                if(password.equals(confirmPassword)){
                    if(password.length() >= 6){
                        createUser(username, email, password, phone);
                    }else{
                        Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Las contraseñas no cinciden!", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this, "Insertaste todos los campos y el email es valido!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Insertatse todos los campos pero el email no es valido!", Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(this, "Para continuar inserta todos los campos!", Toast.LENGTH_SHORT).show();
        }
    }

    private void createUser(final String username, final String email, String password, final String phone){
        mDialog.show();
        mAuthProvider.register(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String id = mAuthProvider.getUid();
                    User user = new User();
                    user.setId(id);
                    user.setEmail(email);
                    user.setUsername(username);
                    user.setPhone(phone);
                    user.setTimestamp(new Date().getTime());

                    mUsersProvider.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mDialog.dismiss();
                            if(task.isSuccessful()){
                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                //Toast.makeText(RegisterActivity.this, "El usuario se almaceno correctamente en la base de datos!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RegisterActivity.this, "No se pudo almacenar el usuario en la base de datos!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //Toast.makeText(RegisterActivity.this, "El usuario se registro correctamente!", Toast.LENGTH_SHORT).show();
                }else{
                    mDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "No se pudo registrar el usuario!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}