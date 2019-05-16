package id.ac.umn.whizzie.main.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.Cart.CartCard;
import id.ac.umn.whizzie.main.Cart.CartCardAdapter;

public class CartActivity extends AppCompatActivity {
    RecyclerView rvItems;
    TextView tvTotal;
    Button btnCheckout;

    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();
    StorageReference strf = FirebaseStorage.getInstance().getReference();

    List<CartCard> ccList;
    String currUid;
    Long totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ccList = new ArrayList<>();


        currUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        tvTotal     = findViewById(R.id.cart_text_view_total);
        btnCheckout = findViewById(R.id.cart_checkout_button);
        rvItems     = findViewById(R.id.cart_recview_items);

        totalPrice = Long.parseLong("0");

        rvItems.setHasFixedSize(true);
        rvItems.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));

        dbrf.child("cart").child(currUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSS : dataSnapshot.getChildren()){
                    GenericTypeIndicator<Long> gti = new GenericTypeIndicator<Long>() {};
                    final long itemQty = dataSS.child(dataSS.getKey()).getValue(gti);

                    dbrf.child("products").child(dataSS.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot ds) {
                            ccList.add(new CartCard(
                                    ds.child("nameProduct").getValue().toString(),
                                    ds.child("pictureProduct").getValue().toString(),
                                    ds.getKey(),
                                    itemQty,
                                    Long.parseLong(ds.child("priceProduct").getValue().toString())
                            ));

                            totalPrice += (itemQty * Long.parseLong(ds.child("priceProduct").getValue().toString()));
                            setRecView();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        tvTotal.setText("Rp. " + String.valueOf(totalPrice));
    }

    private void setRecView(){
        CartCardAdapter cca = new CartCardAdapter(CartActivity.this, ccList);
        rvItems.setAdapter(cca);
    }
}


