package com.kodingnext.passwordkeeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

public class LoginActivity extends AppCompatActivity {

    //for username and password initialization
    EditText et_username, et_password;

    //for button login initialization
    Button btn_login;

    //local database for session like saving login data or setting data
    SharedPreferences mPreferences;

    //for video initialization
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //fullscreen Layout
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        //===================================== Video View Start =================================//
        //initialize videoview
        videoView = findViewById(R.id.intro_vv);

        //handle if the video view end
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                //if the video view end, then the video view will play again
                videoView.start();
            }
        });

        //set the video location in raw resources
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.loginbackground));

        //start the video
        videoView.start();
        //====================================== Video View End ==================================//



        //=================================== Login Function Start ===============================//
        //initialize textfield username
        et_username = findViewById(R.id.username_et);
        //initialize textfield password
        et_password = findViewById(R.id.password_et);

        //create shared preferences that named userData
        mPreferences = getSharedPreferences("userData", MODE_PRIVATE);

        //create editor for editing the data in shared preferences
        final SharedPreferences.Editor edit = mPreferences.edit();

        //create code for checking if the data login already have or not when starting the application
        if (mPreferences.contains("IsLoggedIn")) {

            //if application installed

            if (mPreferences.getBoolean("IsLoggedIn", false) == true) {
                //if -> IsLoggedIn <- is true or the user logged in

                //changing the page from LoginActivity to MainActivity
                startActivity( new Intent(LoginActivity.this, MainActivity.class));
                finish();

            }
        } else {

            //if first install apps
            edit.putString("username", "admin")
                    .putString("password", "admin")
                    .putBoolean("IsLoggedIn", false)
                    .apply();
        }

        //initialize login button
        btn_login = findViewById(R.id.login_btn);

        //set login action
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //checking username from username session to username text field
                if (mPreferences.getString("username", "").equals(et_username.getText().toString())) {
                    //checking password from password session to password text field
                    if (mPreferences.getString("password", "").equals(et_password.getText().toString())) {

                        //if the username and password is equals, than change IsLoggedIn variable value to(true)
                        edit.putBoolean("IsLoggedIn", true);
                        //apply changes
                        edit.apply();

                        //if the username and password matching is success than start activity from page login activity to main activity
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();

                        //showing pop up dialog to inform the user that login is success
                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                    } else {

                        //if the password is not equals, than IsLoggedIn variable is still(false)

                        //showing pop up dialog to inform the user that login is fail
                        Toast.makeText(LoginActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    //if the username is not equals, than IsLoggedIn variable is still(false)

                    //showing pop up dialog to inform the user that login is fail
                    Toast.makeText(LoginActivity.this, "Invalid Username", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //==================================== Login Function End ================================//

    }


    //======================================== Video View Start ==================================//
    @Override
    protected void onResume() {
        super.onResume();

        //handle if the video view end
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                //if the video view end, then the video view will play again
                videoView.start();
            }
        });

        //set the video location in raw resources
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.loginbackground));

        //start the video
        videoView.start();
    }
    //========================================= Video View End ===================================//
}
