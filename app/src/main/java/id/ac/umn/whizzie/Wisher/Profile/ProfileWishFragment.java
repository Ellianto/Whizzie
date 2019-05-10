package id.ac.umn.whizzie.Wisher.Profile;


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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.Wisher.Search.SearchCard;
import id.ac.umn.whizzie.Wisher.Search.SearchCardAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileWishFragment extends Fragment {
    RecyclerView rv;
    List<SearchCard> lsc;
    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();

    String currUid, dispName;

    public ProfileWishFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_wish, container, false);

        rv = view.findViewById(R.id.profile_wish_recycler_view);
        dispName = this.getArguments().getString("dispName");
        currUid = this.getArguments().getString("currUid");

        Log.d("DEBUG", "test1");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lsc = new ArrayList<>();

        dbrf.child("wishes").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    if(ds.child("uidUpWish").getValue().toString().equals(currUid)){
                        final String wishID = ds.getKey();
                        final String wish_title = ds.child("titleWish").getValue().toString();

                        dbrf.child("wishRelation").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot datass) {

                                lsc.add(new SearchCard(dispName,
                                        wish_title,
                                        datass.child(wishID).getChildrenCount(),
                                        0,
                                        false,
                                        wishID));

                                setRecView();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setRecView(){
        SearchCardAdapter sca = new SearchCardAdapter(getActivity(), lsc);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new GridLayoutManager(this.getContext(), 2, GridLayoutManager.VERTICAL, false));

        rv.setAdapter(sca);
    }
}
