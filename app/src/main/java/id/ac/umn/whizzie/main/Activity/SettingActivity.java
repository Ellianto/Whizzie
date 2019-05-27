package id.ac.umn.whizzie.main.Activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.google.android.gms.dynamic.SupportFragmentWrapper;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.Settings.SettingAddressDetailFragment;
import id.ac.umn.whizzie.main.Settings.SettingAddressFragment;
import id.ac.umn.whizzie.main.Settings.SettingFragment;

public class SettingActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    private ImageButton backButton;
    boolean genieMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Toolbar Initation
        Toolbar toolbarSetting = findViewById(R.id.toolbarSetting);
        setSupportActionBar(toolbarSetting);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        genieMode = getIntent().getBooleanExtra("genie_mode", false);

        frameLayout = findViewById(R.id.genie_profile_fragment_holder);
        backButton  = findViewById(R.id.setting_back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragManager = getSupportFragmentManager();
                int count = getSupportFragmentManager().getBackStackEntryCount();
                Fragment currFrag = fragManager.getFragments().get(count>0?count-1:count);

                Log.d("TEST", currFrag.toString());

                if(currFrag.getClass().equals(SettingFragment.class)){
                    onBackPressed();
                } else if(currFrag.getClass().equals(SettingAddressFragment.class)){
                    setFragment(new SettingFragment());
                } else if(currFrag.getClass().equals(SettingAddressDetailFragment.class)){
                    setFragment(new SettingAddressFragment());
                }
            }
        });

        setFragment(new SettingFragment());
    }

    public boolean getMode(){return genieMode;}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}
