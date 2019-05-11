package id.ac.umn.whizzie.main.GenieProfile;


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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.Search.SearchCard;
import id.ac.umn.whizzie.main.Search.SearchCardAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class GenieProfileFragment extends Fragment {
    Context ctx;
    List<SearchCard> prodList;

    RecyclerView rv;
    TextView dispName;

    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();
    StorageReference strf = FirebaseStorage.getInstance().getReference();

    String genieUid, genieName;

    public GenieProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_genie_profile, container, false);

        ctx = this.getContext();

        rv = view.findViewById(R.id.genie_profile_products_list);
        dispName = view.findViewById(R.id.genie_profile_display_name);

        genieUid = getArguments().getString("genieUid");

        dbrf.child("users").child(genieUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                genieName = dataSnapshot.child("name").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new GridLayoutManager(ctx, 2, GridLayoutManager.VERTICAL, false));

        dbrf.child("products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadGenieProducts(dataSnapshot);
                SearchCardAdapter sca = new SearchCardAdapter(ctx, prodList);

                rv.setAdapter(sca);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadGenieProducts(DataSnapshot dataSnapshot){
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            final String itemKey = dataSnapshot.getKey();
            final String prodName = dataSnapshot.child("nameProduct").getValue().toString();

            GenericTypeIndicator<Long> gti = new GenericTypeIndicator<Long>() {};
            final long prodPrice = dataSnapshot.child("priceProduct").getValue(gti);

            dbrf.child("productRelation").child(itemKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot ds) {
                    prodList.add(new SearchCard(
                            genieName,
                            prodName,
                            ds.getChildrenCount(),
                            prodPrice,
                            true,
                            itemKey
                    ));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError ds) {

                }
            });
        }
    }
}
