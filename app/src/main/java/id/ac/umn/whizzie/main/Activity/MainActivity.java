package id.ac.umn.whizzie.main.Activity;

import android.content.Intent;
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

import id.ac.umn.whizzie.main.Home.HomeFragment;
import id.ac.umn.whizzie.main.Notifications.NotificationFragment;
import id.ac.umn.whizzie.main.Post.PostFragment;
import id.ac.umn.whizzie.main.Profile.ProfileFragment;
import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.Wishes.WishesFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView btmNavView;
    private FrameLayout frameLayout;
    private boolean search_product = true;

    private boolean genie_mode = false;

    public void switchMode(){
        genie_mode = !genie_mode;
    }

    public boolean getMode(){
        return genie_mode;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.home_bottom_menu: {
                    setFragment(new HomeFragment());
                    search_product = true;
                    break;
                }

                case R.id.wishes_bottom_menu:{
                    setFragment(new WishesFragment());
                    search_product = false;
                    break;
                }

                case R.id.post_bottom_menu:{
                    setFragment(new PostFragment());
                    break;
                }

                case R.id.notification_bottom_menu: {
                    setFragment(new NotificationFragment());
                    break;
                }

                case R.id.profile_bottom_menu:{
                    setFragment(new ProfileFragment());
                    break;
                }
            }
            
            return true;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_search_icon: {
                Intent i = new Intent(this, SearchActivity.class);

                if(search_product) i.putExtra("type", "product");
                else i.putExtra("type", "wish");

                startActivity(i);

                break;
            }
            case R.id.main_cart_icon: {
                // TODO : Implement Move to Cart Activity
                break;
            }
            case R.id.main_chat: {
                // TODO : Implement Chat function
                break;
            }
        }

        return true;
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
