package id.ac.umn.whizzie.Wisher.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.Wisher.GenieProfile.GenieProfileFragment;

public class GenieProfileActivity extends AppCompatActivity {

    FrameLayout fl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genie_profile);

        // TODO : Toolbar Initiation

        fl = findViewById(R.id.genie_profile_fragment_holder);
        setFragment(new GenieProfileFragment());
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(fl.getId(), fragment);
        fragmentTransaction.commit();
    }
}
