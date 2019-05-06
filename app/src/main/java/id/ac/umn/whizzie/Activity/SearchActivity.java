package id.ac.umn.whizzie.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import id.ac.umn.whizzie.Model.Product;
import id.ac.umn.whizzie.Model.Wishes;
import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.Search.SearchCard;
import id.ac.umn.whizzie.Search.SearchCardAdapter;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView rcSearch;
    private ImageButton searchBtn;
    private EditText edtKeyword;

    private List<SearchCard> scList;
//    private List<Pair<String, String>> unamePair;
    private ArrayMap<String, String> unamePair;

    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        rcSearch = findViewById(R.id.search_recycler_view);
        searchBtn = findViewById(R.id.search_button);
        edtKeyword = findViewById(R.id.search_keyword);

//        unamePair = new ArrayList<>();
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

//            unamePair.add(new Pair<String, String>(user_id, username));
        }
    }

    private void loadProductCard(DataSnapshot ds){
        for(DataSnapshot product : ds.getChildren()){
            Product temp = product.getValue(Product.class);

//            List Implementation
//            for(Pair<String,String> pairs : unamePair){
//                if(pairs.first.equals(temp.getUidUpProduct())){
//                    uname = pairs.second;
//                    break;
//                }
//            }

            scList.add(new SearchCard(unamePair.get(temp.getUidUpProduct()), temp.getNameProduct(), temp.getDescProduct(), temp.getWishesCount()));
        }
    }

    private void loadWishesCard(DataSnapshot ds){
        for(DataSnapshot wishes : ds.getChildren()){
            Wishes temp = wishes.getValue(Wishes.class);

//            List Implementation
//            for(Pair<String, String> pairs : unamePair){
//                if(pairs.first.equals(temp.getUidUpWish())){
//                    uname = pairs.second;
//                    break;
//                }
//            }

            scList.add(new SearchCard(unamePair.get(temp.getUidUpWish()), temp.getTitleWish(), temp.getDescWish(), temp.getOfferCount()));
        }
    }
}
