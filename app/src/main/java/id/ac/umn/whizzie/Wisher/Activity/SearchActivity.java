package id.ac.umn.whizzie.Wisher.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.Wisher.Search.SearchCard;
import id.ac.umn.whizzie.Wisher.Search.SearchCardAdapter;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView rcSearch;
    private ImageButton searchBtn;
    private EditText edtKeyword;

    private List<SearchCard> scList;
    private ArrayMap<String, String> unamePair;

    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        rcSearch = findViewById(R.id.search_recycler_view);
        searchBtn = findViewById(R.id.search_button);
        edtKeyword = findViewById(R.id.search_keyword);

        unamePair = new ArrayMap<>();

        dbRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadUsernameList(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        scList = new ArrayList<>();

        // Load Initial and set On Click Listener on Search Button
        if(getIntent().getStringExtra("type").equals("product")){
            // Kalau Search Product
            dbRef.child("products").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    loadProductCard(dataSnapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } else {
            // Kalau Search Wishes
            dbRef.child("wishes").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    loadWishesCard(dataSnapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        SearchCardAdapter scAdapter = new SearchCardAdapter(this, scList);
        rcSearch.setHasFixedSize(true);
        rcSearch.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        rcSearch.setAdapter(scAdapter);
    }

    // Pre-load the UID-Username mapping to render the usernames in the Search Card;
    private void loadUsernameList(DataSnapshot ds){
        for(DataSnapshot uid : ds.getChildren()){
            String user_id = uid.getKey();
            String username = "";

            for(DataSnapshot temp : uid.getChildren()){
                if(temp.getKey().equals("name")){
                    username = temp.getValue().toString();
                    break;
                }
            }

            unamePair.put(user_id, username);
        }
    }

    private void loadProductCard(DataSnapshot ds){
        for(DataSnapshot product : ds.getChildren()){
            final String itemKey = product.getKey();
            Log.d("DEBUG",itemKey);
            final String uid = product.child("uidUpProduct").getValue().toString();
            Log.d("DEBUG",uid);

            final String prodName = product.child("nameProduct").getValue().toString();
            Log.d("DEBUG",prodName);

            GenericTypeIndicator<Long> gti = new GenericTypeIndicator<Long>() {};
            final long prodPrice = product.child("priceProduct").getValue(gti);

            dbRef.child("productRelation").child(itemKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    scList.add(new SearchCard(
                            unamePair.get(uid),
                            prodName,
                            dataSnapshot.getChildrenCount(),
                            prodPrice,
                            true,
                            itemKey
                    ));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

//            Product temp = product.getValue(Product.class);

//            scList.add(new SearchCard(unamePair.get(temp.getUidUpProduct()), temp.getNameProduct(), wishCount, temp.getPriceProduct()));
        }
    }

    private void loadWishesCard(DataSnapshot ds){
        for(DataSnapshot wishes : ds.getChildren()){
            final String itemKey = wishes.getKey();
            final String uid = wishes.child("uidUpWish").getValue().toString();
            final String wishName = wishes.child("titleWish").getValue().toString();

            dbRef.child("wishRelation").child(wishes.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    scList.add(new SearchCard(
                       unamePair.get(uid),
                       wishName,
                       dataSnapshot.getChildrenCount(),
                       0,
                false,
                        itemKey
                    ));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
