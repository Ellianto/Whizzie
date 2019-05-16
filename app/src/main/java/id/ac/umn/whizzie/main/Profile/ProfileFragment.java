package id.ac.umn.whizzie.main.Profile;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
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
import id.ac.umn.whizzie.main.Activity.SettingActivity;
import id.ac.umn.whizzie.main.Search.SearchCard;
import id.ac.umn.whizzie.main.Search.SearchCardAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    TextView tvName, tvTitle;
    ImageButton profile_setting_button;
    Button modeSwitch;
    RecyclerView rvItems;

    Context ctx;
    List<SearchCard> scList;

    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();
    StorageReference strf = FirebaseStorage.getInstance().getReference();

    String currUid, dispName, profPicPath;
    boolean genieMode = false;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ((MainActivity) getActivity()).hideActionBar();

        ctx = this.getContext();

        tvName = view.findViewById(R.id.profile_display_name);
        tvTitle = view.findViewById(R.id.profile_recview_title);
        modeSwitch = view.findViewById(R.id.profile_creator_button);
        profile_setting_button = view.findViewById(R.id.profile_setting_button);

        rvItems = view.findViewById(R.id.profile_recview_items);

        genieMode = ((MainActivity) ctx).getMode();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Fetch user details
        currUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbrf.child("users").child(currUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { setupProfile(dataSnapshot);}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        rvItems.setHasFixedSize(true);
        rvItems.setLayoutManager(new GridLayoutManager(ctx, 2, GridLayoutManager.VERTICAL, false));

        scList = new ArrayList<>();

        if(genieMode) loadProducts();
        else loadWishes();

        setRecView();

        // Button Setting Intent
        profile_setting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                intent.putExtra("genie_mode", ((MainActivity) ctx).getMode());
                startActivity(intent);
                getActivity().finish();
            }
        });

        modeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).switchMode();
                ((MainActivity) getActivity()).setFragment(new ProfileFragment());
            }
        });
    }

    private void setupProfile(DataSnapshot ds){
        dispName = ds.child("name").getValue().toString();
        profPicPath = ds.child("imgProfilePicture").getValue().toString();

        tvName.setText(dispName);
    }

    private void loadProducts(){
        dbrf.child("products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    final String uid = ds.child("uidUpProduct").getValue().toString();

                    if(!uid.equals(currUid)) continue;

                    final String itemKey = ds.getKey();
                    final String prodName = ds.child("nameProduct").getValue().toString();
                    final String prodImg = ds.child("pictureProduct").getValue().toString();
                    final long prodPrice = Long.parseLong(ds.child("priceProduct").getValue().toString());

                    dbrf.child("productRelation").child(itemKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSS) {
                            scList.add(new SearchCard(
                                    dispName,
                                    profPicPath,
                                    prodName,
                                    prodImg,
                                    dataSS.getChildrenCount(),
                                    prodPrice,
                                    genieMode,
                                    itemKey
                            ));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError ds) {}
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void loadWishes(){
        dbrf.child("wishes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                for(DataSnapshot wishes : ds.getChildren()){
                    final String uid = wishes.child("uidUpWish").getValue().toString();

                    if(!uid.equals(currUid)) continue;

                    final String itemKey = wishes.getKey();
                    final String wishName = wishes.child("titleWish").getValue().toString();
                    final String wishImage = wishes.child("pictureWish").getValue().toString();

                    // Fetch Offer Counts for this wishes
                    dbrf.child("wishRelation").child(wishes.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Add to List
                            scList.add(new SearchCard(
                                    dispName,
                                    profPicPath,
                                    wishName,
                                    wishImage,
                                    dataSnapshot.getChildrenCount(),
                                    0,
                                    genieMode,
                                    itemKey
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
    private void setRecView(){
        SearchCardAdapter sca = new SearchCardAdapter(ctx, scList);
        rvItems.setAdapter(sca);
    }

}
