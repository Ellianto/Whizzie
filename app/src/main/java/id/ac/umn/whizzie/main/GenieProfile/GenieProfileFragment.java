package id.ac.umn.whizzie.main.GenieProfile;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
    TextView dispName, descToko;
    ImageView profilePicture, backgroundPicture;

    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();
    StorageReference strf = FirebaseStorage.getInstance().getReference();

    String genieUid, genieName, profPicPath, bgPath;

    public GenieProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_genie_profile, container, false);

        ctx = this.getContext();
        prodList = new ArrayList<>();

        rv = view.findViewById(R.id.genie_profile_products_list);
        dispName = view.findViewById(R.id.genie_profile_display_name);
        descToko = view.findViewById(R.id.genie_profile_description);

        profilePicture = view.findViewById(R.id.genie_profile_profile_picture);
        backgroundPicture = view.findViewById(R.id.genie_profile_background_image);

        genieUid = getArguments().getString("genieUid");

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbrf.child("users").child(genieUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                genieName = dataSnapshot.child("toko").child("name").getValue().toString();

                profPicPath = dataSnapshot.child("imgProfilePicture").getValue().toString();
                bgPath = dataSnapshot.child("imgBackground").getValue().toString();

                dispName.setText(genieName);
                descToko.setText(dataSnapshot.child("toko").child("description").getValue().toString());

                loadGenieProducts();

                if(profPicPath.isEmpty())
                    profPicPath = "whizzie_assets/empty/empty_profile.jpg";
                else profPicPath = "users/" + genieUid + "/profile.jpg";

                if(bgPath.isEmpty())
                    bgPath = "whiizzie_assets/empty/empty.jpg";
                else bgPath = "users/" + genieUid + "/backdrop.jpg";

                // Fetch Profile Picture Image
                strf.child(profPicPath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                                        profilePicture.setImageBitmap(pic);
                                    }
                                });
                            } catch (IOException e){
                                e.printStackTrace();
                            }

                            return null;
                        }
                    }
                });

                //Fetch Background Picture Image
                strf.child(bgPath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                                        backgroundPicture.setImageBitmap(pic);
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
        });

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new GridLayoutManager(ctx, 2, GridLayoutManager.VERTICAL, false));
        SearchCardAdapter sca = new SearchCardAdapter(ctx, prodList);
        rv.setAdapter(sca);
    }

    private void loadGenieProducts(){
        dbrf.child("products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    final String itemKey = ds.getKey();
                    final String prodName = ds.child("nameProduct").getValue().toString();
                    final String prodImg = ds.child("pictureProduct").getValue().toString();
                    final long prodPrice = Long.parseLong(ds.child("priceProduct").getValue().toString());

                    dbrf.child("productRelation").child(itemKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSS) {
                            prodList.add(new SearchCard(
                                    genieName,
                                    profPicPath,
                                    prodName,
                                    prodImg,
                                    dataSS.getChildrenCount(),
                                    prodPrice,
                                    true,
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
}
