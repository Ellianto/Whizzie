package id.ac.umn.whizzie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.btnLoggingIn);
        Button signUpButton = findViewById(R.id.btnMoveToSignUp);



        final IntentMovement intentMovement = new IntentMovement(LoginActivity.this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentMovement.moveToTargetNormal(HomeActivity.class);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                intentMovement.moveToTargetNormal(SignUpActivity.class);
            }
        });
    }
}
