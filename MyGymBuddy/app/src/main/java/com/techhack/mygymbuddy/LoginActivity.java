package com.techhack.mygymbuddy;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;


public class LoginActivity extends LoginBaseActivity {

    private SignInButton mloginBtn;
    private Button mlogin;
    private EditText mUsername;
    private EditText mPassword;

    private ConnectionResult mConnectionResult;

    private boolean mIntentInProgress;
    private boolean mSignInClicked = false;
    private static final int RC_SIGN_IN = 0;

    private final static String TAG = "LoginActivity";


    protected  GoogleApiClient  buildGoogleApiClient() {
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN);
        return builder.build();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUsername = (EditText)findViewById(R.id.etUserName);
        mPassword = (EditText)findViewById(R.id.etPass);
        mlogin = (Button) findViewById(R.id.btnSingIn);
        mloginBtn = (SignInButton) findViewById(R.id.btn_sign_in);

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( mUsername.getText().toString().equals("admin") && mPassword.getText().toString().equals("admin"))
                {
                    launch();
                }
            }
        });

        mloginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_sign_in:
                        // Signin button clicked
                        signInWithGplus();
                        break;
                }
            }
        });

        mGoogleApiClient = buildGoogleApiClient();
    }

    private void launch() {
        Intent intent = new Intent(LoginActivity.this, InstructionActivity.class);
        startActivity(intent);
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    private void signInWithGplus() {
        if (mGoogleApiClient.isConnected()) {
            mSignInClicked = true;
            launch();
        }
        else {
            mGoogleApiClient.connect();
        }
    }

    /**
     * Method to resolve any signin errors
     * */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        if(mSignInClicked){
            launch();
        }
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
