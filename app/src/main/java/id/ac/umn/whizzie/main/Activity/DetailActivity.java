package id.ac.umn.whizzie.main.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.VisibilityAwareImageButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.Details.DetailsFragment;
import id.ac.umn.whizzie.main.Search.SearchCard;
import id.ac.umn.whizzie.main.Search.SearchCardAdapter;

public class DetailActivity extends AppCompatActivity {
    boolean genieMode;
    boolean isProduct;
    String itemKey;

    FrameLayout fl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        fl = findViewById(R.id.detail_activity_fragment_holder);

        genieMode = getIntent().getBooleanExtra("genieMode", false);
        isProduct = getIntent().getBooleanExtra("isProduct", false);
        itemKey = String.valueOf(getIntent().getLongExtra("itemKey", 0));

        Fragment dtl = new DetailsFragment();

        Bundle b = new Bundle();

        b.putString("itemKey", itemKey);

        dtl.setArguments(b);
        setFragment(dtl);
    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(fl.getId(), fragment);
        fragmentTransaction.commit();
    }

    public boolean getMode(){ return genieMode; }

    public boolean getIsProduct(){ return isProduct;}

    public String getItemKey(){ return itemKey;}
}
