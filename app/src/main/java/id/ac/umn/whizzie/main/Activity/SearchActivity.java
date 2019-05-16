package id.ac.umn.whizzie.main.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
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

        rcSearch.setHasFixedSize(true);
        rcSearch.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));

        View.OnClickListener lookForItems;

        //TODO: Empty the Recycler View before refilling with searched items
        if(getIntent().getStringExtra("type").equals("product")){
            // Kalau Search Product
            loadProductCard("");

            lookForItems = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String keyword = edtKeyword.getText().toString();

                    Log.d("DEBUG", keyword);

                    loadProductCard(keyword);
                }
            };

        } else {
            // Kalau Search Wishes
            loadWishesCard("");

            lookForItems = new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String keyword = edtKeyword.getText().toString();

                    Log.d("DEBUG", keyword);

                    loadWishesCard(keyword);
                }
            };
        }

        searchBtn.setOnClickListener(lookForItems);
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

    private void loadProductCard(final String keyword){
        scList = new ArrayList<>();

        // Fetch Products
        dbRef.child("products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                for(DataSnapshot product : ds.getChildren()){
                    Log.d("DEBUG", product.getKey());

                    // If a category filter exists and doesn't match, continue the loop
                    if(!categoryFilter.equals("all") && !categoryFilter.equals(product.child("category").getValue().toString()))
                        continue;

                    final String prodName = product.child("nameProduct").getValue().toString();

                    if(!keyword.isEmpty() && !prodName.toLowerCase().contains(keyword.toLowerCase()))
                        continue;

                    final String itemKey = product.getKey();
                    final String uid = product.child("uidUpProduct").getValue().toString();
                    final String prodImg = product.child("pictureProduct").getValue().toString();


                    final long prodPrice = Long.parseLong(product.child("priceProduct").getValue().toString());

                    dbRef.child("productRelation").child(itemKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            scList.add(new SearchCard(
                                    unamePair.get(uid),
                                    uid + "/profile.jpg",
                                    prodName,
                                    prodImg,
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

        setRecView();
    }

    private void loadWishesCard(final String keyword){
        scList = new ArrayList<>();

        // Fetch Wishes
        dbRef.child("wishes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                for(DataSnapshot wishes : ds.getChildren()){
                    // If a category filter exists and doesn't match, continue the loop
                    if(!categoryFilter.equals("all") && !categoryFilter.equals(wishes.child("category").getValue().toString()))
                        continue;

                    final String wishName = wishes.child("titleWish").getValue().toString();

                    if(!keyword.isEmpty() && !wishName.toLowerCase().contains(keyword.toLowerCase()))
                        continue;

                    final String itemKey = wishes.getKey();
                    final String uid = wishes.child("uidUpWish").getValue().toString();
                    final String wishImage = wishes.child("pictureWish").getValue().toString();

                    // Fetch Offer Counts for this wishes
                    dbRef.child("wishRelation").child(wishes.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Add to List
                            scList.add(new SearchCard(
                                    unamePair.get(uid),
                                    uid + "/profile.jpg",
                                    wishName,
                                    wishImage,
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

        setRecView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setRecView(){
        SearchCardAdapter sca = new SearchCardAdapter(this, scList);

        rcSearch.setAdapter(sca);
    }
}
