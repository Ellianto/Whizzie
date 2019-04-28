package id.ac.umn.whizzie;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {



        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home_bottom_menu:
                    mTextMessage.setText(R.string.title_home);
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    return true;
                case R.id.timeline_bottom_menu:
                    mTextMessage.setText(R.string.title_timeline);
                    startActivity(new Intent(getApplicationContext(), TimelineActivity.class));
                    return true;
                case R.id.post_bottom_menu:
                    mTextMessage.setText(R.string.title_notifications);
                    startActivity(new Intent(getApplicationContext(), PostActivity.class));
                    return true;
                case R.id.notification_bottom_menu:
                    mTextMessage.setText(R.string.title_notifications);
                    startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                    return true;
                case R.id.profile_bottom_menu:
                    mTextMessage.setText(R.string.title_notifications);
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

}
