package id.ac.umn.whizzie.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.Settings.SettingWisherFragment;

public class SettingActivity extends AppCompatActivity {

    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Toolbar Initation
        Toolbar toolbarSetting = findViewById(R.id.toolbarSetting);
        setSupportActionBar(toolbarSetting);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        frameLayout = findViewById(R.id.wisher_frame_layout_setting);
        setFragment(new SettingWisherFragment());
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}
