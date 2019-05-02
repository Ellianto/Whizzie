package id.ac.umn.whizzie.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import id.ac.umn.whizzie.Home.HomeFragment;
import id.ac.umn.whizzie.Notifications.NotificationFragment;
import id.ac.umn.whizzie.Post.PostFragment;
import id.ac.umn.whizzie.Profile.ProfileFragment;
import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.Wishes.WishesFragment;

public class WisherActivity extends AppCompatActivity {

    BottomNavigationView btmNavView;
    private FrameLayout frameLayout;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.home_bottom_menu:         setFragment(new HomeFragment()); break;
                case R.id.timeline_bottom_menu:     setFragment(new WishesFragment()); break;
                case R.id.post_bottom_menu:         setFragment(new PostFragment()); break;
                case R.id.notification_bottom_menu: setFragment(new NotificationFragment()); break;
                case R.id.profile_bottom_menu:      setFragment(new ProfileFragment()); break;
            }
            
            return true;
        }
    };

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wisher);

        // Toolbar Initation
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // BottomNavigationView Initiation
        btmNavView = findViewById(R.id.wisher_bottom_nav);
        btmNavView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        frameLayout = findViewById(R.id.wisher_frame_layout);

        //Set Initial Fragment
        setFragment(new HomeFragment());
    }

    public void hideActionBar(){
        getSupportActionBar().hide();
    }

    public void showActionBar(){
        getSupportActionBar().show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
