package id.ac.umn.whizzie.main.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

    long newTransactionKey = 0;

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

        dbrf.child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnap) {
                if(dataSnap.hasChild(currUid)){
                    btnCheckout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(CartActivity.this, "Successfully Checked Out! Wait Further Instructions", Toast.LENGTH_LONG).show();

                            dbrf.child("cart").child(currUid).removeValue();

                            startActivity(new Intent(CartActivity.this, MainActivity.class));
                            finish();
                        }
                    });

                    dbrf.child("cart").child(currUid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final long ssCount = dataSnapshot.getChildrenCount();

                            for(DataSnapshot dataSS : dataSnapshot.getChildren()){
                                final long itemQty = dataSS.getValue(long.class);

                                dbrf.child("products").child(dataSS.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot ds) {
                                        Log.d("DEBUG", ds.child("nameProduct").getValue().toString());

                                        ccList.add(new CartCard(
                                                ds.child("nameProduct").getValue().toString(),
                                                ds.child("pictureProduct").getValue().toString(),
                                                ds.getKey(),
                                                itemQty,
                                                Long.parseLong(ds.child("priceProduct").getValue().toString())
                                        ));

                                        totalPrice += (itemQty * Long.parseLong(ds.child("priceProduct").getValue().toString()));

                                        if(ccList.size() == ssCount){
                                            tvTotal.setText("Rp. " + String.valueOf(totalPrice));
                                            CartCardAdapter cca = new CartCardAdapter(CartActivity.this, ccList);
                                            rvItems.setAdapter(cca);
                                        }

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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}


