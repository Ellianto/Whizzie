package id.ac.umn.whizzie.main.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.Search.SearchCard;
import id.ac.umn.whizzie.main.Search.SearchCardAdapter;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView rcSearch;
    private ImageButton searchBtn;
    private EditText edtKeyword;

    private List<SearchCard> scList;
    private ArrayMap<String, String> unamePair;
    private String categoryFilter;

    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        rcSearch = findViewById(R.id.search_recycler_view);
        searchBtn = findViewById(R.id.search_button);
        edtKeyword = findViewById(R.id.search_keyword);

        categoryFilter = getIntent().getStringExtra("category");

        unamePair = new ArrayMap<>();

        loadUsernameList();

        scList = new ArrayList<>();
        View.OnClickListener lookForItems;

        // Load Initial and set On Click Listener on Search Button
        if(getIntent().getStringExtra("type").equals("product")){
            // Kalau Search Product
            loadProductCard();

            lookForItems = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO : Setelah implement Overload/Nullable, add here
                }
            };

        } else {
            // Kalau Search Wishes
            loadWishesCard();

            lookForItems = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO : Setelah implement Overload/Nullable, add here

                }
            };
        }

        searchBtn.setOnClickListener(lookForItems);

        SearchCardAdapter scAdapter = new SearchCardAdapter(this, scList);
        rcSearch.setHasFixedSize(true);
        rcSearch.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        rcSearch.setAdapter(scAdapter);
    }

    // Pre-load the UID-Username mapping to render the usernames in the Search Card;
    private void loadUsernameList(){
        dbRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot user : dataSnapshot.getChildren()){
                    String user_id = user.getKey();
                    String username = user.child("name").getValue().toString();

                    unamePair.put(user_id, username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    // TODO : Try to implement Nullable or Overloading
    private void loadProductCard(){
        // Fetch Products
        dbRef.child("products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                for(DataSnapshot product : ds.getChildren()){
                    // If a category filter exists and doesn't match, continue the loop
                    if(!categoryFilter.equals("all") && !categoryFilter.equals(product.child("category").getValue().toString()))
                        continue;

                    final String itemKey = product.getKey();
                    final String uid = product.child("uidUpProduct").getValue().toString();

                    final String prodName = product.child("nameProduct").getValue().toString();

                    final long prodPrice = product.child("priceProduct").getValue(long.class);

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
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void loadWishesCard(){
        // Fetch Wishes
        dbRef.child("wishes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                for(DataSnapshot wishes : ds.getChildren()){
                    // If a category filter exists and doesn't match, continue the loop
                    if(!categoryFilter.equals("all") && !categoryFilter.equals(wishes.child("category").getValue().toString()))
                        continue;

                    final String itemKey = wishes.getKey();
                    final String uid = wishes.child("uidUpWish").getValue().toString();
                    final String wishName = wishes.child("titleWish").getValue().toString();

                    // Fetch Offer Counts for this wishes
                    dbRef.child("wishRelation").child(wishes.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Add to List
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
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
