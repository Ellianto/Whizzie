package id.ac.umn.whizzie.main.Profile;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.net.URL;
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
    TextView tvName, tvTitle, tvDesc;
    ImageButton profile_setting_button;
    Button modeSwitch;
    RecyclerView rvItems;
    ImageView backdropImage, profilePictureImage;

    Context ctx;
    List<SearchCard> scList;

    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();
    StorageReference strf = FirebaseStorage.getInstance().getReference();

    String currUid, dispName, profPicPath, backdropPath;
    boolean genieMode = false;

    public ProfileFragment() {
        // Required empty public constructor
    }

    // Value Event Listeners
    ValueEventListener setupProfile = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot ds) {

            if(genieMode) dispName = ds.child("toko").child("name").getValue().toString();
            else dispName = ds.child("name").getValue().toString();

            profPicPath = ds.child("imgProfilePicture").getValue().toString();
            backdropPath = ds.child("imgBackground").getValue().toString();

            if(genieMode){
                tvDesc.setVisibility(View.VISIBLE);
                tvDesc.setText(ds.child("toko").child("description").getValue().toString());
            } else {
                tvDesc.setVisibility(View.GONE);
                tvDesc.setText("");
            }

            tvName.setText(dispName);

            String backdropRefPath = "";

            if(backdropPath.isEmpty()) backdropRefPath = "whizzie_assets/empty/empty.jpg";
            else backdropRefPath = "users/"  + currUid + "/backdrop.jpg";

            // Set Backdrop Picture
            strf.child(backdropRefPath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    new loadImage().execute(uri.toString());
                }

                // Cara Fetch Image from Firebase Storage
                // Operasi-operasi network harus dilakukan di thread berbeda
                class loadImage extends AsyncTask<String, String, String> {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected String doInBackground(String... strings) {
                        try{
                            URL url = new URL(strings[0]);
                            final Bitmap pic = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                            Handler h = new Handler(Looper.getMainLooper());

                            // Operasi yang mengubah View harus di Main Thread
                            // Karena akan dijalankan di dalam fragment, pakai handler
                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    backdropImage.setImageBitmap(pic);
                                }
                            });
                        } catch (IOException e){
                            e.printStackTrace();
                        }

                        return null;
                    }
                }
            });

            String profileRefPath = "";

            if(profPicPath.isEmpty()) profileRefPath = "whizzie_assets/empty/empty_profile.jpg";
            else profileRefPath = "users/" + currUid + "/profile.jpg";

            // Set Profile Picture
            strf.child(profileRefPath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    new loadImage().execute(uri.toString());
                }

                // Cara Fetch Image from Firebase Storage
                // Operasi-operasi network harus dilakukan di thread berbeda
                class loadImage extends AsyncTask<String, String, String> {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected String doInBackground(String... strings) {
                        try{
                            URL url = new URL(strings[0]);
                            final Bitmap pic = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                            Handler h = new Handler(Looper.getMainLooper());

                            // Operasi yang mengubah View harus di Main Thread
                            // Karena akan dijalankan di dalam fragment, pakai handler
                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    profilePictureImage.setImageBitmap(pic);
                                }
                            });
                        } catch (IOException e){
                            e.printStackTrace();
                        }

                        return null;
                    }
                }
            });
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    };

    // On Click Listeners

    View.OnClickListener goToSettings = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            intent.putExtra("genie_mode", ((MainActivity) ctx).getMode());
            startActivity(intent);
            getActivity().finish();
        }
    };

    View.OnClickListener switchMode = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((MainActivity) getActivity()).switchMode();
            ((MainActivity) getActivity()).setFragment(new ProfileFragment());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ((MainActivity) getActivity()).hideActionBar();

        ctx = this.getContext();

        tvName                  = view.findViewById(R.id.profile_display_name);
        tvTitle                 = view.findViewById(R.id.profile_recview_title);
        tvDesc                  = view.findViewById(R.id.profile_genie_desc);
        modeSwitch              = view.findViewById(R.id.profile_creator_button);
        profile_setting_button  = view.findViewById(R.id.profile_setting_button);

        backdropImage           = view.findViewById(R.id.profile_background_image);
        profilePictureImage     = view.findViewById(R.id.profile_display_image);

        rvItems = view.findViewById(R.id.profile_recview_items);

        genieMode = ((MainActivity) ctx).getMode();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Fetch user details
        currUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbrf.child("users").child(currUid).addValueEventListener(setupProfile);

        rvItems.setHasFixedSize(true);
        rvItems.setLayoutManager(new GridLayoutManager(ctx, 2, GridLayoutManager.VERTICAL, false));

        scList = new ArrayList<>();

        if(genieMode)
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

                                setRecView();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError ds) {}
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        else
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

        // Button Setting Intent
        profile_setting_button.setOnClickListener(goToSettings);

        if(genieMode) modeSwitch.setText("Wisher Mode");
        else {
            modeSwitch.setText("Genie Mode");
            modeSwitch.setBackgroundColor(getResources().getColor(R.color.colorOrange));
        }

        modeSwitch.setOnClickListener(switchMode);
    }

    private void setRecView(){
        SearchCardAdapter sca = new SearchCardAdapter(ctx, scList);
        rvItems.setAdapter(sca);
    }
}
