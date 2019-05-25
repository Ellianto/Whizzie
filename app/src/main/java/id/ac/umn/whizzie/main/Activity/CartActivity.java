package id.ac.umn.whizzie.main.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import id.ac.umn.whizzie.main.Cart.CartItemCard;
import id.ac.umn.whizzie.main.Cart.CartStoreCard;

public class CartActivity extends AppCompatActivity {
    RecyclerView rvItems;
    TextView tvTotal;
    Button btnCheckout;

    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();
    StorageReference strf = FirebaseStorage.getInstance().getReference();

    String currUid;
    Long totalPrice;

    List<CartStoreCard> cscList;
    List<CartItemCard> cicList;

    GenericTypeIndicator<Long> gti = new GenericTypeIndicator<Long>() {};

    long newTransactionKey = 0;
    long storeTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        currUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        tvTotal     = findViewById(R.id.cart_text_view_total);
        btnCheckout = findViewById(R.id.cart_checkout_button);
        rvItems     = findViewById(R.id.cart_recview_items);

        cscList     = new ArrayList<>();

        totalPrice = Long.parseLong("0");

        rvItems.setHasFixedSize(true);
        rvItems.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));

        //TODO : Determine the new Transaction Key

        dbrf.child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnap) {
                if(dataSnap.hasChild(currUid)){
                    btnCheckout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(CartActivity.this, "Successfully Checked Out! Wait Further Instructions", Toast.LENGTH_LONG).show();

                            dbrf.child("cart").child(currUid).removeValue();

                            //TODO: Implement write to Transaction Node here

                            startActivity(new Intent(CartActivity.this, MainActivity.class));
                            finish();
                        }
                    });

                    dbrf.child("cart").child(currUid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSS : dataSnapshot.getChildren()){

                                // Bikin Card Item Cart List baru
                                cicList = new ArrayList<>();
                                storeTotal = 0;

                                for(final DataSnapshot ss : dataSS.getChildren()){
                                    dbrf.child("products").child(ss.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnap) {
                                            cicList.add(new CartItemCard(
                                                    dataSnap.child("nameProduct").getValue().toString(),
                                                    dataSnap.child("pictureProduct").getValue().toString(),
                                                    dataSnap.child("priceProduct").getValue(gti),
                                                    ss.getValue(gti)
                                            ));

                                            storeTotal += ss.getValue(gti) * dataSnap.child("priceProduct").getValue(gti);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                                    });
                                }

                                dbrf.child("users").child(dataSS.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snap) {
                                        cscList.add(new CartStoreCard(
                                            snap.child("toko").child("name").getValue().toString(),
                                            snap.getKey() + "/" + snap.child("imgProfilePicture").getValue().toString(),
                                            storeTotal,
                                            cicList
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}


