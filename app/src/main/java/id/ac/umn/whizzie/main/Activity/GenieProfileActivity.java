package id.ac.umn.whizzie.main.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.GenieProfile.GenieProfileFragment;

public class GenieProfileActivity extends AppCompatActivity {

    FrameLayout fl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genie_profile);

        // TODO : Toolbar Initiation

        String genieId = getIntent().getStringExtra("genieUid");

        fl = findViewById(R.id.genie_profile_fragment_holder);

        Fragment fr = new GenieProfileFragment();

        Bundle b = new Bundle();
        b.putString("genieID",genieId);

        fr.setArguments(b);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(fl.getId(), fr);
        fragmentTransaction.commit();
    }

}
