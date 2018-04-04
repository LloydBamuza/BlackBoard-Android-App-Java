package fr.arnaudguyon.tabstackerapp;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sendbird.android.SendBird;

import static fr.arnaudguyon.tabstackerapp.SignInActivity.*;

public class SignUpActivity extends Activity implements Authentication
{
    private static final String TAG = "Sign Up";
    Button btnSignUp;
    EditText edtUsrEmail, edtPass;
    static String usrEmail = "";
    String password;
    volatile boolean successfulSignUp;
    public static final String TEST_OPEN_CHANNEL_ID = "TUT_IT"; // for testing only
    SendBird.

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //Initialize Sendbird
        Log.i(TAG,"Initializing Sendbird API");
        initSDK(SignUpActivity.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        //check if permissions were granted,loop until all permissions are granted
        while(!(hasInternetPerm == PackageManager.PERMISSION_GRANTED && hasStorageRPerm == PackageManager.PERMISSION_GRANTED && hasStorageWPerm == PackageManager.PERMISSION_GRANTED))
        {
            Toast.makeText(SignUpActivity.this,"Please grant all permissions to proceed",Toast.LENGTH_LONG).show();
            requestPermissions(SignUpActivity.this,SignUpActivity.this);
        }

        //set references to widgets
        edtUsrEmail = (EditText) findViewById(R.id.edtUsrEmail);
        edtPass = (EditText) findViewById(R.id.edtPass);
        btnSignUp = (Button) findViewById(R.id.btnSingIn);

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        btnSignUp.setOnClickListener(e->
        {
            login(SignUpActivity.this,edtUsrEmail.getText().toString(),edtPass.getText().toString());
        });
    }
}
