package id.ac.umn.whizzie.main.Details;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import id.ac.umn.whizzie.main.Activity.DetailActivity;
import id.ac.umn.whizzie.main.Search.SearchCard;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfferFragment extends Fragment {
    RecyclerView rvOffer;

    Context ctx;
    List<OfferCard> ocList;

    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();
    StorageReference strf  = FirebaseStorage.getInstance().getReference();
    GenericTypeIndicator<Long> gti = new GenericTypeIndicator<Long>() {};

    String currUid, itemKey;

    public OfferFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_offer, container, false);

        ctx = this.getContext();
        ocList = new ArrayList<>();

        itemKey = ((DetailActivity)ctx).getItemKey();
        currUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        rvOffer = v.findViewById(R.id.offer_recview);
        rvOffer.setHasFixedSize(true);
        rvOffer.setLayoutManager(new GridLayoutManager(ctx, 2, GridLayoutManager.VERTICAL, false));

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbrf.child("users").child(currUid).child("toko").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("products")){
                    for(DataSnapshot ss : dataSnapshot.child("products").getChildren()){
                        dbrf.child("products").child(ss.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnap) {
                                ocList.add(new OfferCard(
                                    dataSnap.child("nameProduct").getValue().toString(),
                                    dataSnap.child("pictureProduct").getValue().toString(),
                                    dataSnap.child("priceProduct").getValue(gti),
                                    dataSnap.getKey(),
                                    itemKey
                                ));

                                OfferCardAdapter oca = new OfferCardAdapter(ctx, ocList);
                                rvOffer.setAdapter(oca);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {}
                        });
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
