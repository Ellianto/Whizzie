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

        String genieId = getIntent().getStringExtra("genieUid");
        String genieName = getIntent().getStringExtra("genieName");

        fl = findViewById(R.id.genie_profile_fragment_holder);

        Fragment fr = new GenieProfileFragment();

        Bundle b = new Bundle();
        b.putString("genieUid",genieId);
        b.putString("genieName", genieName);

        fr.setArguments(b);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(fl.getId(), fr);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}


//hello imkkmmkkm