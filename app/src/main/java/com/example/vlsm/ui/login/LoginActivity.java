package com.example.vlsm.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vlsm.MainActivity;
import com.example.vlsm.R;
import com.example.vlsm.data.Result;
import com.example.vlsm.data.model.LoggedInUser;
import com.example.vlsm.ui.login.LoginViewModel;
import com.example.vlsm.ui.login.LoginViewModelFactory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private LoginActivity thisLoginActivity ;
    ProgressBar loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);

        thisLoginActivity = this;

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                if(usernameEditText.getText().toString().isEmpty()){
                    usernameEditText.setError("This field is required.");
                    loadingProgressBar.setVisibility(View.GONE);

                    return;
                }
                if(passwordEditText.getText().toString().isEmpty()){
                    passwordEditText.setError("This field is required.");
                    loadingProgressBar.setVisibility(View.GONE);
                    return;
                }

                login(
                        usernameEditText.getText().toString(),
                        passwordEditText.getText().toString()
                );
            }
        });
    }
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference mCollecRefUsers;
    public void login(final String username, final String password) {

        loadingProgressBar.setVisibility(View.VISIBLE);

        mCollecRefUsers =  db.collection("users");

        mCollecRefUsers.document(username).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            /*HashMap<String,Object> vic = (HashMap<String, Object>) documentSnapshot.get(username);*/
                            loadingProgressBar.setVisibility(View.GONE);
                            Map<String,Object> vic =documentSnapshot.getData();

                            if(username.equals(vic.get("username"))&&password.equals(vic.get("password"))){
                                startActivityVic(username);

                            }else if(username.equals(vic.get("username"))&&!password.equals(vic.get("password"))){
                                androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(thisLoginActivity);
                                builder.setTitle("Wrong password.")
                                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });

                                builder.create().show();
                            }


                            System.out.println("asd");
                        }else{
                            loadingProgressBar.setVisibility(View.GONE);
                            androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(thisLoginActivity);
                            builder.setMessage("Would you like to create it?")
                                    .setTitle("User doesn't exists")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            /*Invocar firebase y crear*/
                                            HashMap<String, Object> map = new HashMap<>();

                                            map.put("username",username);
                                            map.put("password",password);

                                           /* db.collection("vlsm").document("users").*/
                                            mCollecRefUsers.document(username)
                                                    .set(map)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            startActivityVic(username);
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(thisLoginActivity);
                                                            builder.setMessage("Try later")
                                                                    .setTitle("Can't creat it");
                                                            builder.show();
                                                        }
                                                    });
                                        }
                                    })
                                    .setNegativeButton("No",new  DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            /*ignore*/

                                        }
                                    });

                            builder.create().show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingProgressBar.setVisibility(View.GONE);
                        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(thisLoginActivity);
                        builder.setMessage("Can't get the data from the server")
                                .setTitle("Please try later.")
                                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });

                        builder.create().show();
                    }
                });
        /*mCollecRefUsers.document(username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    loadingProgressBar.setVisibility(View.GONE);
                    if(documentSnapshot.exists()){
                        HashMap<String,Object> vic = (HashMap<String, Object>) documentSnapshot.get(username);

                        if(vic*//*.get("username")*//*==null){
                            *//*userExists[0] = false;*//*
                            androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(thisLoginActivity);
                            builder.setMessage("Would you like to create it?")
                                    .setTitle("User doesn't exists")
                                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            *//*Invocar firebase y crear*//*
                                            HashMap<String, Object> map = new HashMap<>();

                                            map.put("username",username);
                                            map.put("password",password);




                                           *//* db.collection("vlsm").document("users").*//*
                                            db.collection("vlsm").document("users").collection(username).
                                                    document(username).set(map)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            startActivityVic(username);
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(thisLoginActivity);
                                                            builder.setMessage("Try later")
                                                                    .setTitle("Can't creat it");
                                                            builder.show();
                                                        }
                                                    });
                                        }
                                    })
                                    .setNegativeButton("No",new  DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            *//*ignore*//*

                                        }
                                    });

                            builder.create().show();
                        }else
                        if(username.equals(vic.get("username"))&&password.equals(vic.get("password"))){
                          *//*  objectResult[0] = true;
                            userExists[0] = true;*//*

                          startActivityVic(username);

                        }else if(username.equals(vic.get("username"))&&!password.equals(vic.get("password"))){
                            androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(thisLoginActivity);
                            builder.setTitle("Wrong password.")
                                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });

                            builder.create().show();
                        }


                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                   *//* objectResult[0] = false;*//*
                    loadingProgressBar.setVisibility(View.GONE);
                    androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(thisLoginActivity);
                    builder.setMessage("Can't get the data from the server")
                            .setTitle("Please try later.")
                            .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });

                    builder.create().show();
                }
            });*/

   /*         if(objectResult[0]==false){
                return new Result.Error(new IOException("Can't get data."));
            }
            if(userExists[0]==false){
                return new Result.Error(new IOException("User doesn't exists."));
            }
            if(wrongPassword[0]==false){
                return new Result.Error(new IOException("Wrong password."));
            }
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            password,
                            username);

            if(objectResult[0]){
                return new Result.Success<>(fakeUser);
            }else{
                return new Result.Error(new IOException("Error something."));
            }
*/

    }
    public void startActivityVic(String username){
        setResult(Activity.RESULT_OK);

        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
        myIntent.putExtra("usename", username); //Optional parameters
        LoginActivity.this.startActivity(myIntent);

        finish();
    }


    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}