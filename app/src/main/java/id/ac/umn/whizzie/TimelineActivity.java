package id.ac.umn.whizzie;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends AppCompatActivity {
    BottomNavigationView btmNavView;
    RecyclerView rvTimelineItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Toolbar Initation
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // BottomNavigationView Initiation
        btmNavView = findViewById(R.id.btmNavTimeline);

        final IntentMovement im = new IntentMovement(TimelineActivity.this);

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

        rvTimelineItems = findViewById(R.id.timeline_recycler_view);

        rvTimelineItems.setHasFixedSize(true);

        List<TimelineItems> tiList = new ArrayList<>();

        tiList.add(new TimelineItems("Ellianto", "Cuma coba coba", 100000));
        tiList.add(new TimelineItems("Alexander", "Tes Test testing", 25000));
        tiList.add(new TimelineItems("Ellianto", "Cek cek cek", 10101));

        TimelineItemsAdapter tiAdapter = new TimelineItemsAdapter(this, tiList);
        rvTimelineItems.setAdapter(tiAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
