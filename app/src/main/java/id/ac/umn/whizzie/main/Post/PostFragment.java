package id.ac.umn.whizzie.main.Post;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.Activity.MainActivity;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */

public class PostFragment extends Fragment {
    ImageButton upImage;

    // Wish Section Components
    CardView wish_cardview;
    EditText wish_name, wish_desc;
    AutoCompleteTextView wish_combo_box;
    Button postWishButton;

    // Product Section Components
    CardView prod_cardview;
    EditText prod_name, prod_mass, prod_price, prod_desc;
    AutoCompleteTextView prod_combo_box;
    Button postProductButton;

    List<String> spinnerArray;
    Uri filePath;
    long keyCount;
    boolean genieMode;

    Context ctx;
    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();
    StorageReference strf = FirebaseStorage.getInstance().getReference();

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        ctx = this.getContext();
        ((MainActivity) ctx).showActionBar();

        genieMode = ((MainActivity) ctx).getMode();

        upImage = view.findViewById(R.id.post_image_button_add);

        // Wish Section View Linking
        wish_cardview   = view.findViewById(R.id.edit_card_view_wish);
        wish_name       = view.findViewById(R.id.edit_wish_edit_text_wish_name);
        wish_desc       = view.findViewById(R.id.edit_wish_edit_text_desc);
        wish_combo_box  = view.findViewById(R.id.edit_wish_combo_box);
        postWishButton  = view.findViewById(R.id.edit_button_wish);

        // Product Section View Linking
        prod_cardview    = view.findViewById(R.id.edit_card_view_product);
        prod_name        = view.findViewById(R.id.edit_product_edit_text_product_name);
        prod_mass        = view.findViewById(R.id.edit_product_edit_text_product_mass);
        prod_price       = view.findViewById(R.id.edit_product_edit_text_product_price);
        prod_desc        = view.findViewById(R.id.edit_product_edit_text_desc);
        prod_combo_box   = view.findViewById(R.id.edit_product_combo_box);
        postProductButton= view.findViewById(R.id.edit_button_product);

        // Largest Key of the item
        keyCount = 0;

        // The type of the item
        String keyPath = "";

        if(genieMode) keyPath = "products";
        else keyPath = "wishes";

        // Makes sure that new items' keys are incremental
        dbrf.child(keyPath).addValueEventListener(incrementKey);

        return view;
    }

    // On Click Listeners
    View.OnClickListener imageHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(i, "Choose Image"), 1);
        }
    };

    View.OnClickListener postWish = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            // Make sure fields are filled
            if(wish_desc.getText().toString().isEmpty() || wish_name.getText().toString().isEmpty() || wish_combo_box.getText().toString().isEmpty()){
                // Show Error Toast
                Toast.makeText(ctx, "Please fill out all the fields", Toast.LENGTH_SHORT).show();
            }
            else {
                // Write to Database
                dbrf.child("wishes").child(String.valueOf(keyCount)).child("category").setValue(wish_combo_box.getText().toString());

                dbrf.child("wishes").child(String.valueOf(keyCount)).child("descWish").setValue(wish_desc.getText().toString());

                String imgFile = "";

                if(filePath != null){
                    imgFile = String.valueOf(keyCount) + ".jpg";
                    String imgPath = "wishes/" + imgFile;
                    strf.child(imgPath).putFile(filePath);
                }

                dbrf.child("wishes").child(String.valueOf(keyCount)).child("pictureWish").setValue(imgFile);

                dbrf.child("wishes").child(String.valueOf(keyCount)).child("timeWish").setValue(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date()));

                dbrf.child("wishes").child(String.valueOf(keyCount)).child("titleWish").setValue(wish_name.getText().toString());

                dbrf.child("wishes").child(String.valueOf(keyCount)).child("uidUpWish").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

                dbrf.child("wishes").child(String.valueOf(keyCount)).child("isDeleted").setValue(false);
                keyCount++;

                ((MainActivity) ctx).btmNavView.setSelectedItemId(R.id.home_bottom_menu);
            }
        }
    };

    View.OnClickListener postProduct = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            // Make sure fields are filled
            if(prod_name.getText().toString().isEmpty() || prod_desc.getText().toString().isEmpty() || prod_price.getText().toString().isEmpty() || prod_mass.getText().toString().isEmpty() || prod_combo_box.getText().toString().isEmpty()){
                // Show Error Toast
                Toast.makeText(ctx, "Please fill out all the fields", Toast.LENGTH_SHORT).show();
            }
            else{
                // Write to database
                dbrf.child("products").child(String.valueOf(keyCount)).child("category").setValue(prod_combo_box.getText().toString());

                dbrf.child("products").child(String.valueOf(keyCount)).child("descProduct").setValue(prod_desc.getText().toString());

                dbrf.child("products").child(String.valueOf(keyCount)).child("massProduct").setValue(prod_mass.getText().toString());

                dbrf.child("products").child(String.valueOf(keyCount)).child("nameProduct").setValue(prod_name.getText().toString());

                dbrf.child("products").child(String.valueOf(keyCount)).child("priceProduct").setValue(prod_price.getText().toString());

                dbrf.child("products").child(String.valueOf(keyCount)).child("isDeleted").setValue(false);

                String imgFile = "";

                if(filePath != null){
                    imgFile = String.valueOf(keyCount) + ".jpg";
                    String imgPath = "products/" + imgFile;
                    strf.child(imgPath).putFile(filePath);
                }

                dbrf.child("products").child(String.valueOf(keyCount)).child("pictureProduct").setValue(imgFile);

                dbrf.child("products").child(String.valueOf(keyCount)).child("timestamp").setValue(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date()));

                dbrf.child("products").child(String.valueOf(keyCount)).child("uidUpProduct").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

                keyCount++;

                ((MainActivity) ctx).btmNavView.setSelectedItemId(R.id.home_bottom_menu);
            }
        }
    };

    View.OnClickListener dropWishComboBox = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            wish_combo_box.showDropDown();
        }
    };

    View.OnClickListener dropProductComboBox = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {prod_combo_box.showDropDown();
        }
    };

    //Value Event Listeners
    ValueEventListener fetchCategories = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for(DataSnapshot temp : dataSnapshot.getChildren()){
                spinnerArray.add(temp.getKey());
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    };

    //Iterasi semua key objek.
    ValueEventListener incrementKey = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for(DataSnapshot ds : dataSnapshot.getChildren()){ // "foreach"
                long currKey = Long.parseLong(ds.getKey());
                if(currKey > keyCount) keyCount = currKey + 1;
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        upImage.setOnClickListener(imageHandler);

        spinnerArray = new ArrayList<>();

        // Populate Category Combo Box
        dbrf.child("categories").addListenerForSingleValueEvent(fetchCategories);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(ctx, android.R.layout.simple_dropdown_item_1line, spinnerArray);

        if(genieMode){  // Genie Mode
            wish_cardview.setVisibility(View.GONE);
            prod_cardview.setVisibility(View.VISIBLE);

            prod_combo_box.setOnClickListener(dropProductComboBox);
            prod_combo_box.setAdapter(adapter);

            postProductButton.setOnClickListener(postProduct);
        }
        else {  // Wisher Mode
            wish_cardview.setVisibility(View.VISIBLE);
            prod_cardview.setVisibility(View.GONE);

            wish_combo_box.setAdapter(adapter);
            wish_combo_box.setOnClickListener(dropWishComboBox);

            postWishButton.setOnClickListener(postWish);
        }
    }

    // Handle chosen Image File and display it
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();

            try{
                Bitmap bmp = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), filePath);
                upImage.setImageBitmap(bmp);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}