package id.ac.umn.whizzie;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    BottomNavigationView btmNavView;
    RecyclerView rvHomeBottomGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Toolbar Initation
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // BottomNavigationView Initiation
        btmNavView = findViewById(R.id.navigation);
        btmNavView = getMenuInflater().inflate();

        final IntentMovement im = new IntentMovement(HomeActivity.this);

        btmNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.home_bottom_menu:  im.moveToTargetNormal(HomeActivity.class); break;
                    case R.id.timeline_bottom_menu: im.moveToTargetNormal(TimelineActivity.class); break;
                    case R.id.post_bottom_menu: im.moveToTargetNormal(PostActivity.class); break;
                    case R.id.notification_bottom_menu: im.moveToTargetNormal(NotificationActivity.class); break;
                    case R.id.profile_bottom_menu: im.moveToTargetNormal(ProfileActivity.class); break;
                }

                return true;
            }
        });

        // Recycler View Initiation
        rvHomeBottomGrid = findViewById(R.id.rvHomeBottomGrid);

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


}
