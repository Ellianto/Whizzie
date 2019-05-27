package id.ac.umn.whizzie.main.Transaction;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import id.ac.umn.whizzie.main.Activity.MainActivity;
import id.ac.umn.whizzie.main.Cart.CartItemCard;
import id.ac.umn.whizzie.main.Cart.CartStoreCard;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends Fragment {
    RecyclerView rvTransactions;

    Context ctx;
    List<TransactionCard>   tcList;
    List<CartStoreCard>     scList;
    List<CartItemCard>      icList;
    GenericTypeIndicator<Long> gti = new GenericTypeIndicator<Long>() {};

    String currUid;

    long storeTotal = 0;
    boolean genieMode = false;

    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();
    StorageReference strf = FirebaseStorage.getInstance().getReference();

    public TransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ctx = this.getContext();
        tcList = new ArrayList<>();
        scList = new ArrayList<>();
        icList = new ArrayList<>();

        storeTotal = 0;
        genieMode = ((MainActivity)ctx).getMode();

        View v = inflater.inflate(R.layout.fragment_transaction, container, false);

        currUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        rvTransactions = v.findViewById(R.id.recycler_transactions);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        dbrf.child("transaction").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot dataSnap : dataSnapshot.getChildren()){
                    if(dataSnap.child("uid").getValue().toString().equals(currUid)){
                        if(dataSnap.child("statusPembayaran").getValue().toString().isEmpty() || dataSnap.child("statusPembayaran").getValue().toString().equals(null)){
                            tcList.add(new TransactionCard(
                                    dataSnap.getKey(),
                                    dataSnap.child("address").getValue().toString(),
                                    dataSnap.child("timestamp").getValue().toString(),
                                    dataSnap.child("totalHarga").getValue(gti),
                                    scList
                            ));

                            setRecView();
                        }
                        else {
                            // Sudah bayar
                            final long storeCount = dataSnap.child("pembelian").getChildrenCount();

                            for(final DataSnapshot ds : dataSnap.child("pembelian").getChildren()){
                                storeTotal = 0;

                                final long productCount = ds.child("productID").getChildrenCount();

                                for(final DataSnapshot snap : ds.child("productID").getChildren()){
                                    Log.d("TEST", "WOOOOOIIIIIIIII");

                                    dbrf.child("products").child(snap.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot datass) {
                                            icList.add(new CartItemCard(
                                                    datass.child("nameProduct").getValue().toString(),
                                                    datass.child("pictureProduct").getValue().toString(),
                                                    datass.child("priceProduct").getValue(gti),
                                                    snap.getValue(gti),
                                                    snap.getKey()
                                            ));

                                            storeTotal += snap.getValue(gti) * datass.child("priceProduct").getValue(gti);

                                            if(icList.size() == productCount){
                                                dbrf.child("users").child(ds.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot ss) {
                                                        scList.add(new CartStoreCard(
                                                                ss.child("toko").child("name").getValue().toString(),
                                                                ss.getKey() + "/" + ss.child("imgProfilePicture").getValue().toString(),
                                                                dataSnap.getKey(),
                                                                ds.getKey(),
                                                                ds.child("noResi").getValue().toString(),
                                                                storeTotal,
                                                                icList,
                                                                !ds.child("statusDikirim").getValue().toString().isEmpty(),
                                                                !ds.child("statusDiterima").getValue().toString().isEmpty(),
                                                                genieMode
                                                        ));

                                                        if(storeCount == scList.size()){
                                                            tcList.add(new TransactionCard(
                                                                    dataSnap.getKey(),
                                                                    dataSnap.child("address").getValue().toString(),
                                                                    dataSnap.child("timestamp").getValue().toString(),
                                                                    dataSnap.child("totalHarga").getValue(gti),
                                                                    scList
                                                            ));

                                                            setRecView();
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
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void setRecView(){
        Log.d("TEST", "TCLIST SIZE : " + tcList.size());
        Log.d("TEST", "SCLIST SIZE : " + scList.size());
        Log.d("TEST", "iCLIST SIZE : " + icList.size());

        rvTransactions.setHasFixedSize(true);
        rvTransactions.setLayoutManager(new GridLayoutManager(ctx, 1, GridLayoutManager.VERTICAL, false));

        TransactionCardAdapter tca = new TransactionCardAdapter(ctx, tcList);
        rvTransactions.setAdapter(tca);

        scList.clear();
        icList.clear();
    }
}
