package id.ac.umn.whizzie.main.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.Cart.CartItemCard;
import id.ac.umn.whizzie.main.Cart.CartStoreAdapter;
import id.ac.umn.whizzie.main.Cart.CartStoreCard;

public class CartActivity extends AppCompatActivity {
    RecyclerView rvItems;
    TextView tvTotal, tvAddress;
    Button btnCheckout, btnAddress;

    //
    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();
    StorageReference strf = FirebaseStorage.getInstance().getReference();

    String currUid;
    String chosenAddress;
    Long totalPrice;

    // Bikin List untuk Card per toko dan Card per item
    List<CartStoreCard> cscList;
    List<CartItemCard> cicList;

    //
    GenericTypeIndicator<Long> gti = new GenericTypeIndicator<Long>() {};

    long newTransactionKey = 0;
    long storeTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        currUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        chosenAddress = "";

        tvTotal     = findViewById(R.id.cart_text_view_total);
        tvAddress   = findViewById(R.id.cart_text_view_address);
        btnCheckout = findViewById(R.id.cart_checkout_button);
        btnAddress  = findViewById(R.id.cart_address_button);
        rvItems     = findViewById(R.id.cart_recview_items);

        cscList     = new ArrayList<>();
        cicList     = new ArrayList<>();

        totalPrice = (long) 0;

        rvItems.setHasFixedSize(true);
        rvItems.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));

        dbrf.child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnap) {
                if(dataSnap.hasChild(currUid)){
                    btnAddress.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(CartActivity.this, ChooseAddressActivity.class);
                            startActivityForResult(i, 1);
                        }
                    });

                    btnCheckout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(chosenAddress.isEmpty() || chosenAddress.equals(null)){
                                Toast.makeText(CartActivity.this, "Please Choose an address before checking out!", Toast.LENGTH_SHORT).show();
                            } else {
                                dbrf.child("transaction").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot ss : dataSnapshot.getChildren()){
                                            long currKey =  Long.parseLong(ss.getKey());
                                            if(currKey >= newTransactionKey) newTransactionKey = currKey + 1;
                                        }

                                        writeNewTransaction();

                                        Toast.makeText(CartActivity.this, "Successfully Checked Out! Check the Transaction page for details", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(CartActivity.this, MainActivity.class));
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                                });
                            }
                        }
                    });

                    dbrf.child("cart").child(currUid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final long allCount = dataSnapshot.getChildrenCount();

                            for(final DataSnapshot dataSS : dataSnapshot.getChildren()){
                                // Bikin Card Item Cart List baru
                                final long subCount = dataSS.getChildrenCount();

                                for(final DataSnapshot ss : dataSS.getChildren()){
                                    dbrf.child("products").child(ss.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnap){
                                            cicList.add(new CartItemCard(
                                                    dataSnap.child("nameProduct").getValue().toString(),
                                                    dataSnap.child("pictureProduct").getValue().toString(),
                                                    dataSnap.child("priceProduct").getValue(gti),
                                                    ss.getValue(gti),
                                                    ss.getKey()
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
                                        if(cicList.size() == subCount){

                                            CartStoreCard temp = new CartStoreCard(
                                                    snap.child("toko").child("name").getValue().toString(),
                                                    snap.getKey() + "/" + snap.child("imgProfilePicture").getValue().toString(),
                                                    "",
                                                    dataSS.getKey(),
                                                    "",
                                                    storeTotal,
                                                    cicList,
                                                    false,
                                                    false,
                                                    false
                                            );

                                            cscList.add(temp);

                                            totalPrice += storeTotal;

                                            setRecyclerView(allCount);
                                            cicList.clear();
                                            storeTotal = 0;
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
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void setRecyclerView(long totalCount){
        if(totalCount == cscList.size()){
            CartStoreAdapter csa = new CartStoreAdapter(CartActivity.this, cscList);
            rvItems.setAdapter(csa);

            tvTotal.setText("Rp. " + totalPrice);
        }
    }

    private void writeNewTransaction(){
        dbrf.child("transaction").child(String.valueOf(newTransactionKey)).child("address").setValue(chosenAddress);
        dbrf.child("transaction").child(String.valueOf(newTransactionKey)).child("statusPembayaran").setValue("");
        dbrf.child("transaction").child(String.valueOf(newTransactionKey)).child("timestamp").setValue(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date()));
        dbrf.child("transaction").child(String.valueOf(newTransactionKey)).child("totalHarga").setValue(totalPrice);
        dbrf.child("transaction").child(String.valueOf(newTransactionKey)).child("uid").setValue(currUid);

        dbrf.child("cart").child(currUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot datass : dataSnapshot.getChildren()){
                    dbrf.child("transaction").child(String.valueOf(newTransactionKey)).child("pembelian").child(datass.getKey()).child("noResi").setValue("");
                    dbrf.child("transaction").child(String.valueOf(newTransactionKey)).child("pembelian").child(datass.getKey()).child("statusDikirim").setValue("");
                    dbrf.child("transaction").child(String.valueOf(newTransactionKey)).child("pembelian").child(datass.getKey()).child("statusDiterima").setValue("");

                    for(DataSnapshot ss : datass.getChildren()){
                        dbrf.child("transaction").child(String.valueOf(newTransactionKey)).child("pembelian").child(datass.getKey()).child("productID").child(ss.getKey()).setValue(Long.parseLong(ss.getValue().toString()));
                    }
                }

                dbrf.child("cart").child(currUid).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            chosenAddress = data.getStringExtra("alamat");
            tvAddress.setText(chosenAddress);
        }
    }
}


