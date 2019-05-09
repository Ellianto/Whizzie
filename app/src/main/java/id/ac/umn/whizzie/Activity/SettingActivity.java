package id.ac.umn.whizzie.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import id.ac.umn.whizzie.R;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Toolbar Initation
        Toolbar toolbarSetting = findViewById(R.id.toolbarSetting);
        setSupportActionBar(toolbarSetting);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
