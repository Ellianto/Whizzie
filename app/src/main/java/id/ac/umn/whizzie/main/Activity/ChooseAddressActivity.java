package id.ac.umn.whizzie.main.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.Settings.AddressCard;
import id.ac.umn.whizzie.main.Settings.AddressCardAdapter;

public class ChooseAddressActivity extends AppCompatActivity {

    RecyclerView rvAddress;
    TextView kosong;
    private String currUid;

    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_address);

        rvAddress = findViewById(R.id.choose_addr_recview);
        kosong    = findViewById(R.id.tv_empty);

        currUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        dbrf.child("users").child(currUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("alamat")){
                    kosong.setVisibility(View.GONE);
                    rvAddress.setVisibility(View.VISIBLE);

                    rvAddress.setHasFixedSize(true);
                    rvAddress.setLayoutManager(new GridLayoutManager(ChooseAddressActivity.this, 1, GridLayoutManager.VERTICAL, false));

                    List<AddressCard> acList = new ArrayList<>();

                    String storeAddress = dataSnapshot.child("storeAddress").getValue().toString();
                    GenericTypeIndicator<Long> gti = new GenericTypeIndicator<Long>() {};

                    for(DataSnapshot ds : dataSnapshot.child("alamat").getChildren()){
                        acList.add(new AddressCard(
                                ds.getKey(),
                                ds.child("cityName").getValue().toString(),
                                ds.child("detailAddress").getValue().toString(),
                                ds.child("phoneNum").getValue().toString(),
                                String.valueOf(ds.child("postalCode").getValue(gti)),
                                ds.child("provinceName").getValue().toString(),
                                ds.child("cityName").getValue().toString(),
                                storeAddress.equals(ds.getKey())
                        ));
                    }

                    AddressCardAdapter aca = new AddressCardAdapter(ChooseAddressActivity.this, acList);
                    rvAddress.setAdapter(aca);
                } else {
                    kosong.setVisibility(View.VISIBLE);
                    rvAddress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
