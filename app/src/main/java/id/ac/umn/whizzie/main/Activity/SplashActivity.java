package id.ac.umn.whizzie.main.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import id.ac.umn.whizzie.R;

public class SplashActivity extends AppCompatActivity {

    private static final String PREFERENCE_FILENAME = "user_credentials";
    private static final int PREFERENCE_MODE        = Context.MODE_PRIVATE;
    private static final String KEY_UNAME           = "this_username";
    private static final String KEY_PASSWORD        = "this_password";

    SharedPreferences shp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        shp = getSharedPreferences(PREFERENCE_FILENAME, PREFERENCE_MODE);

        String uname = shp.getString(KEY_UNAME, "");
        String passwd = shp.getString(KEY_PASSWORD, "");

        if(!uname.isEmpty() && !uname.equals(null) && !passwd.isEmpty() && !passwd.equals(null)){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(uname, passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in successful
                        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        finish();
                    } else goToLogin();
                }
            });
        } else goToLogin();
    }

    private void goToLogin(){
        SystemClock.sleep(3000);
        Intent loginIntent = new Intent(SplashActivity.this, RegisterActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
