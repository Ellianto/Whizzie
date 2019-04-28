package id.ac.umn.whizzie;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    BottomNavigationView btmNavView;
    RecyclerView rvNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // Toolbar Initation
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // BottomNavigationView Initiation
        btmNavView = findViewById(R.id.navigation);

        final IntentMovement im = new IntentMovement(NotificationActivity.this);

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

        rvNotifications = findViewById(R.id.rvNotifications);

        rvNotifications.setHasFixedSize(true);

        List<Notifications> notifList = new ArrayList<>();

        notifList.add(new Notifications("Ini pemberitahuan pertama!!!"));
        notifList.add(new Notifications("Artinya, ini yang kedua"));
        notifList.add(new Notifications("Berarti ini yang ketiga dong"));

        NotificationsAdapter notifAdapter = new NotificationsAdapter(this, notifList);

        rvNotifications.setAdapter(notifAdapter);
    }
}
