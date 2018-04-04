package fr.arnaudguyon.tabstackerapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sendbird.android.OpenChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.sendbird.android.UserListQuery;

import java.util.List;

public class SignInActivity extends Activity implements Authentication
{
    private static final String TAG = "Sign In";
    static int hasStorageWPerm, hasStorageRPerm,hasInternetPerm;
    Button btnSignIn;
    EditText edtUsrEmail, edtPass;
    static String usrEmail = "";
    String password;
    volatile boolean successfulLogin;
    public static OpenChannel openChannel = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_screen);

        //Initialize Sendbird
        Log.i(TAG,"Initializing Sendbird API");
        initSDK(SignInActivity.this);

        //check if permissions were granted,loop until all permissions are granted
        while(!(hasInternetPerm == PackageManager.PERMISSION_GRANTED && hasStorageRPerm == PackageManager.PERMISSION_GRANTED && hasStorageWPerm == PackageManager.PERMISSION_GRANTED))
        {
            Toast.makeText(SignInActivity.this,"Please grant all permissions to proceed",Toast.LENGTH_LONG).show();
            requestPermissions(SignInActivity.this,SignInActivity.this);
        }

        //set references to widgets
        edtUsrEmail = (EditText) findViewById(R.id.edtUsrEmail);
        edtPass = (EditText) findViewById(R.id.edtPass);
        btnSignIn = (Button) findViewById(R.id.btnSingIn);


    }

    @Override
    protected void onStart()
    {
        super.onStart();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {




                //get username and password
                usrEmail = edtUsrEmail.getText().toString();
                password = edtPass.getText().toString();

                //login through SendBird API
                if(isRegisteredUser(SignInActivity.this, usrEmail))
                {
                    SignInActivity.this.login(SignInActivity.this,usrEmail, password);

                }
                else
                    Toast.makeText(SignInActivity.this, "Not a registered user",Toast.LENGTH_LONG).show();

            }
        });



    }






}
