package id.ac.umn.whizzie;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    BottomNavigationView btmNavView;
    RecyclerView rvHomeBottomGrid;

    IntentMovement im = new IntentMovement(this);

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {



        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.home_bottom_menu:         im.moveToTargetNormal(HomeActivity.class); break;
                case R.id.timeline_bottom_menu:     im.moveToTargetNormal(TimelineActivity.class); break;
                case R.id.post_bottom_menu:         im.moveToTargetNormal(PostActivity.class); break;
                case R.id.notification_bottom_menu: im.moveToTargetNormal(NotificationActivity.class); break;
                case R.id.profile_bottom_menu:      im.moveToTargetNormal(ProfileActivity.class); break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Toolbar Initation
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // BottomNavigationView Initiation
        btmNavView = findViewById(R.id.wisher_bottom_nav);
        btmNavView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Recycler View Initiation
        rvHomeBottomGrid = findViewById(R.id.home_bottom_grid);

        rvHomeBottomGrid.setHasFixedSize(true);

        rvHomeBottomGrid.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));

        List<HomeCard> hcList = new ArrayList<>();

        hcList.add(new HomeCard("Testing 1"));
        hcList.add(new HomeCard("Testing 2"));
        hcList.add(new HomeCard("Testing 3"));
        hcList.add(new HomeCard("Testing 4"));
        hcList.add(new HomeCard("Testing 5"));

        HomeCardAdapter hcAdapter = new HomeCardAdapter(this, hcList);

        rvHomeBottomGrid.setAdapter(hcAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
