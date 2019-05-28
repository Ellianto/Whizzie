package id.ac.umn.whizzie.main.Details;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import id.ac.umn.whizzie.main.Activity.DetailActivity;
import id.ac.umn.whizzie.main.GenieProfile.GenieProfileFragment;
import id.ac.umn.whizzie.main.Search.SearchCard;
import id.ac.umn.whizzie.main.Search.SearchCardAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {
    ImageView dtlImage, uploaderImage;
    Button dtlOffer, dtlATC, dtlEdit, dtlDelete;
    TextView dtlTitle, dtlDesc, dtlRVTitle, uploaderName;
    CardView uploaderCard;
    RecyclerView rvItems;

    Context ctx;
    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();
    StorageReference strf = FirebaseStorage.getInstance().getReference();

    List<SearchCard> scList;
    boolean genieMode;
    boolean isProduct;
    String itemKey;
    String currUid;
    String itemUploader;

    // Value Event Listeners
    ValueEventListener fetchProductDetails          = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot ss) {
            dtlTitle.setText(ss.child("nameProduct").getValue().toString());
            dtlDesc.setText(ss.child("descProduct").getValue().toString());
            itemUploader = ss.child("uidUpProduct").getValue().toString();

            String imageName = ss.child("pictureProduct").getValue().toString();

            String imagePath;

            if(imageName.isEmpty()) imagePath = "whizzie_assets/empty/empty.jpg";
            else imagePath = "products/" + imageName;

            // Set Product Image
            strf.child(imagePath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) { new loadImage().execute(uri.toString()); }

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
                                public void run() { dtlImage.setImageBitmap(pic); }
                            });
                        } catch (IOException e){ e.printStackTrace(); }

                        return null;
                    }
                }
            });

            // Show special buttons if it's own product
            if(itemUploader.equals(currUid)){
                dtlEdit.setVisibility(View.VISIBLE);
                dtlDelete.setVisibility(View.VISIBLE);

                dtlOffer.setVisibility(View.GONE);
                dtlATC.setVisibility(View.GONE);

                dtlEdit.setOnClickListener(editItem);
                dtlDelete.setOnClickListener(deleteItem);
            }
            else {
                dtlEdit.setVisibility(View.GONE);
                dtlDelete.setVisibility(View.GONE);
            }

            dbrf.child("users").child(itemUploader).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    uploaderName.setText(dataSnapshot.child("toko").child("name").getValue().toString());

                    String profPicPath;

                    if(dataSnapshot.child("imgProfilePicture").getValue().toString().isEmpty()) profPicPath = "whizzie_assets/empty/empty.jpg";
                    else profPicPath = "products/" + dataSnapshot.child("imgProfilePicture").getValue().toString();

                    // Set Product Image
                    strf.child(profPicPath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) { new loadImage().execute(uri.toString()); }

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
                                        public void run() { uploaderImage.setImageBitmap(pic); }
                                    });
                                } catch (IOException e){ e.printStackTrace(); }

                                return null;
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    };

    ValueEventListener fetchWishDetails             = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot ss) {
            dtlTitle.setText(ss.child("titleWish").getValue().toString());
            dtlDesc.setText(ss.child("descWish").getValue().toString());
            itemUploader = ss.child("uidUpWish").getValue().toString();

            String imageName = ss.child("pictureWish").getValue().toString();

            String imagePath;

            if(imageName.isEmpty()) imagePath = "whizzie_assets/empty/empty.jpg";
            else imagePath = "wishes/" + imageName;

            // Set Product Image
            strf.child(imagePath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                                    dtlImage.setImageBitmap(pic);
                                }
                            });
                        } catch (IOException e){
                            e.printStackTrace();
                        }

                        return null;
                    }
                }
            });

            // Show special buttons if it's own product
            if(itemUploader.equals(currUid)){
                dtlEdit.setVisibility(View.VISIBLE);
                dtlDelete.setVisibility(View.VISIBLE);

                dtlOffer.setVisibility(View.GONE);
                dtlATC.setVisibility(View.GONE);

                dtlEdit.setOnClickListener(editItem);
                dtlDelete.setOnClickListener(deleteItem);
            }
            else {
                dtlEdit.setVisibility(View.GONE);
                dtlDelete.setVisibility(View.GONE);
            }

            dbrf.child("users").child(itemUploader).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    uploaderName.setText(dataSnapshot.child("name").getValue().toString());

                    String profPicPath;

                    if(dataSnapshot.child("imgProfilePicture").getValue().toString().isEmpty()) profPicPath = "whizzie_assets/empty/empty.jpg";
                    else profPicPath = "products/" + dataSnapshot.child("imgProfilePicture").getValue().toString();

                    // Set Product Image
                    strf.child(profPicPath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) { new loadImage().execute(uri.toString()); }

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
                                        public void run() { uploaderImage.setImageBitmap(pic); }
                                    });
                                } catch (IOException e){ e.printStackTrace(); }

                                return null;
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    };

    ValueEventListener fetchRelatedProducts         = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            final long ssCount = dataSnapshot.getChildrenCount();

            for(DataSnapshot ss : dataSnapshot.getChildren()){
                dbrf.child("products").child(ss.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSS) {
                        final String prodName = dataSS.child("nameProduct").getValue().toString();
                        final String prodImg = dataSS.child("pictureProduct").getValue().toString();
                        final String itemKey = dataSS.getKey();
                        final String uid = dataSS.child("uidUpProduct").getValue().toString();
                        final long prodPrice = Long.parseLong(dataSS.child("priceProduct").getValue().toString());

                        dbrf.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot Snapshot) {
                                final String profPicPath = Snapshot.child("imgProfilePicture").getValue().toString();
                                final String profileName = Snapshot.child("name").getValue().toString();

                                dbrf.child("productRelation").child(itemKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot datas) {
                                        scList.add(new SearchCard(
                                            profileName,
                                            profPicPath,
                                            prodName,
                                            prodImg,
                                            datas.getChildrenCount(),
                                            prodPrice,
                                            true,
                                            itemKey
                                        ));

                                        if(scList.size() == ssCount){
                                            SearchCardAdapter sca = new SearchCardAdapter(ctx, scList);
                                            rvItems.setAdapter(sca);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    };

    ValueEventListener fetchRelatedWishes           = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            final long ssCount = dataSnapshot.getChildrenCount();

            for(DataSnapshot ss : dataSnapshot.getChildren()){
                dbrf.child("wishes").child(ss.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSS) {
                        final String wishName = dataSS.child("titleWish").getValue().toString();
                        final String wishImg = dataSS.child("pictureWish").getValue().toString();
                        final String itemKey = dataSS.getKey();
                        final String uid = dataSS.child("uidUpWish").getValue().toString();

                        dbrf.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot Snapshot) {
                                final String profPicPath = Snapshot.child("imgProfilePicture").getValue().toString();
                                final String profileName = Snapshot.child("name").getValue().toString();

                                dbrf.child("wishRelation").child(itemKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot datas) {
                                        scList.add(new SearchCard(
                                                profileName,
                                                profPicPath,
                                                wishName,
                                                wishImg,
                                                datas.getChildrenCount(),
                                                0,
                                                false,
                                                itemKey
                                        ));

                                        if(scList.size() == ssCount){
                                            SearchCardAdapter sca = new SearchCardAdapter(ctx, scList);
                                            rvItems.setAdapter(sca);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    };

    ValueEventListener deleteProductWishRelations   = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for(DataSnapshot dataSS : dataSnapshot.getChildren())
                if(dataSS.hasChild(itemKey))
                    dbrf.child("wishRelation").child(dataSS.getKey()).child(itemKey).removeValue();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    };

    ValueEventListener deleteWishProductRelation    = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for(DataSnapshot dataSS : dataSnapshot.getChildren())
                if(dataSS.hasChild(itemKey))
                    dbrf.child("productRelation").child(dataSS.getKey()).child(itemKey).removeValue();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    };

    // On Click Listeners
    View.OnClickListener editItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {((DetailActivity) ctx).setFragment(new EditFragment());}
    };
    View.OnClickListener offerProduct = new View.OnClickListener() {
        @Override
        public void onClick(View v) {((DetailActivity) ctx).setFragment(new OfferFragment());}
    };

    View.OnClickListener deleteItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setTitle("Confirm Delete Item");
            builder.setMessage("Are you sure you want to delete the item?");
            builder.setCancelable(true);

            builder.setPositiveButton("Delete Item", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String refPath;

                    if(isProduct){
                        refPath = "products";
                        dbrf.child("productRelation").child(itemKey).removeValue();

                        dbrf.child("wishRelation").addListenerForSingleValueEvent(deleteProductWishRelations);
                    }
                    else{
                        refPath = "wishes";
                        dbrf.child("wishRelation").child(itemKey).removeValue();

                        dbrf.child("productRelation").addListenerForSingleValueEvent(deleteWishProductRelation);
                    }

                    dbrf.child(refPath).child(itemKey).child("isDeleted").setValue(true);
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {dialog.cancel();}
            });

            builder.show();
        }
    };

    View.OnClickListener addToCart = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dbrf.child("products").child(itemKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(final @NonNull DataSnapshot dataSnapshot) {
                    dbrf.child("cart").child(currUid).child(dataSnapshot.child("uidUpProduct").getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot ss) {
                            if(ss.hasChild(itemKey)){
                                Toast.makeText(ctx, "This item is already in your cart!", Toast.LENGTH_SHORT).show();
                            } else {
                                dbrf.child("cart").child(currUid).child(dataSnapshot.child("uidUpProduct").getValue().toString()).child(itemKey).setValue(1);
                                Toast.makeText(ctx, "Successfully Added to cart!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }
    };

    View.OnClickListener goToGenieProfile = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Fragment fr = new GenieProfileFragment();

            Bundle b = new Bundle();
            b.putString("genieUid", itemUploader);

            fr.setArguments(b);

            ((DetailActivity)ctx).setFragment(fr);
        }
    };

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        ctx = this.getContext();

        isProduct   = ((DetailActivity) ctx).getIsProduct();
        genieMode   = ((DetailActivity) ctx).getMode();
        itemKey     = ((DetailActivity) ctx).getItemKey();
        currUid     = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Layout Linking
        dtlImage        = view.findViewById(R.id.detail_item_image);

        dtlTitle        = view.findViewById(R.id.detail_item_title);
        dtlDesc         = view.findViewById(R.id.detail_item_desc);
        dtlRVTitle      = view.findViewById(R.id.detail_recview_title);

        dtlOffer        = view.findViewById(R.id.detail_offer_button);
        dtlATC          = view.findViewById(R.id.detail_atc_button);
        dtlEdit         = view.findViewById(R.id.detail_edit_button);
        dtlDelete       = view.findViewById(R.id.detail_delete_button);

        uploaderCard    = view.findViewById(R.id.uploader_card);
        uploaderImage   = view.findViewById(R.id.detail_uploader_image);
        uploaderName    = view.findViewById(R.id.detail_uploader_name);

        rvItems         = view.findViewById(R.id.detail_recview_items);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        scList = new ArrayList<>();

        rvItems.setHasFixedSize(true);
        rvItems.setLayoutManager(new GridLayoutManager(ctx, 2, GridLayoutManager.VERTICAL, false));

        String itemPath;

        // Render Offered Stuffs
        if(isProduct){
            itemPath = "products";
            uploaderCard.setClickable(true);
            uploaderCard.setOnClickListener(goToGenieProfile);
            dtlRVTitle.setText("Offered Wishes");

            dtlOffer.setVisibility(View.GONE);
            dtlATC.setVisibility(View.VISIBLE);

            dtlATC.setOnClickListener(addToCart);

            dbrf.child(itemPath).child(itemKey).addListenerForSingleValueEvent(fetchProductDetails);
            dbrf.child("productRelation").child(itemKey).addListenerForSingleValueEvent(fetchRelatedWishes);
        }
        else{
            itemPath = "wishes";
            uploaderCard.setClickable(false);
            dtlRVTitle.setText("Offered Products");

            dtlOffer.setVisibility(View.VISIBLE);
            dtlATC.setVisibility(View.GONE);

            dtlOffer.setOnClickListener(offerProduct);

            dbrf.child(itemPath).child(itemKey).addListenerForSingleValueEvent(fetchWishDetails);
            dbrf.child("wishRelation").child(itemKey).addListenerForSingleValueEvent(fetchRelatedProducts);
        }
    }
}
