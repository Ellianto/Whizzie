package id.ac.umn.whizzie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button loginButton = findViewById(R.id.btnMoveToLogin);
        Button signUpButton = findViewById(R.id.btnSigningUp);

        final IntentMovement intentMovement = new IntentMovement(SignUpActivity.this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentMovement.moveToTargetNormal(MainActivity.class);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // Sign Up, lalu kalau sukses pindah ke Main Activity

                // Untuk Sekarang tetap ke diri sendiri dulu
                intentMovement.moveToTargetNormal(SignUpActivity.class);
            }
        });
    }
}
