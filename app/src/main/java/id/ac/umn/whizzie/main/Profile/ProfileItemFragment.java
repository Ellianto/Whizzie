package id.ac.umn.whizzie.main.Profile;


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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.Activity.MainActivity;
import id.ac.umn.whizzie.main.Search.SearchCard;
import id.ac.umn.whizzie.main.Search.SearchCardAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileItemFragment extends Fragment {
    RecyclerView rv;
    List<SearchCard> scList;

    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();
    StorageReference strf = FirebaseStorage.getInstance().getReference();

    Context ctx;
    String currUid, dispName;

    boolean genie_mode;

    public ProfileItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile_item, container, false);

        rv = v.findViewById(R.id.profile_product_recycler_view);

        ctx = this.getContext();
        genie_mode = ((MainActivity) ctx).getMode();

        currUid = getArguments().get("currUid").toString();
        dispName = getArguments().get("dispName").toString();

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        scList = new ArrayList<>();

        if(genie_mode) loadMyProducts();
        else loadMyWishes();
    }


    private void loadMyWishes(){
        dbrf.child("wishes").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    if(ds.child("uidUpWish").getValue().toString().equals(currUid)){
                        final String wishID = ds.getKey();
                        final String wish_title = ds.child("titleWish").getValue().toString();
                        final String wish_image = ds.child("pictureWish").getValue().toString();

                        dbrf.child("wishRelation").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot datass) {

                                scList.add(new SearchCard(dispName,
                                        currUid + "/profile.jpg",
                                        wish_title,
                                        wish_image,
                                        datass.child(wishID).getChildrenCount(),
                                        0,
                                        false,
                                        wishID));

                                setRecView();
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

    private void loadMyProducts(){
        dbrf.child("products").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    if(ds.child("uidUpProduct").getValue().toString().equals(currUid)){
                        final String prodID = ds.getKey();
                        final String prod_title = ds.child("nameProduct").getValue().toString();
                        final String prod_image = ds.child("pictureProduct").getValue().toString();

                        dbrf.child("productRelation").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot datass) {

                                scList.add(new SearchCard(dispName,
                                        currUid + "/profile.jpg",
                                        prod_title,
                                        prod_image,
                                        datass.child(prodID).getChildrenCount(),
                                        0,
                                        true,
                                        prodID));

                                setRecView();
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

    private void setRecView(){
        SearchCardAdapter sca = new SearchCardAdapter(getActivity(), scList);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new GridLayoutManager(this.getContext(), 2, GridLayoutManager.VERTICAL, false));

        rv.setAdapter(sca);
    }
}
