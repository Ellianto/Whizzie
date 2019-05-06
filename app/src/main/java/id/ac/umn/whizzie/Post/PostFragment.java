package id.ac.umn.whizzie.Post;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.ac.umn.whizzie.Activity.WisherActivity;
import id.ac.umn.whizzie.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {
    Button postBtn;
    EditText wish_name, wish_quantity, wish_desc;
    List<String> spinnerArray;
    long wishCount = 0;
    AutoCompleteTextView combo_box;

    Context ctx;


    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();

    public PostFragment() {
        // Required empty public constructor
    }

    ValueEventListener getWishCount = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            wishCount = dataSnapshot.getChildrenCount();
            Log.d("DEBUG", "Wish Count : " + String.valueOf(wishCount));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        ctx = container.getContext();

        ((WisherActivity) getActivity()).showActionBar();

        combo_box = view.findViewById(R.id.post_combo_box);
        postBtn = view.findViewById(R.id.post_button_wish);

        wish_name   = view.findViewById(R.id.post_edit_text_item_name);
        wish_quantity = view.findViewById(R.id.post_edit_text_quantity);
        wish_desc = view.findViewById(R.id.post_edit_text_desc);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        combo_box.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                combo_box.showDropDown();
            }
        });

        spinnerArray = new ArrayList<>();

        dbrf.child("wishes").addListenerForSingleValueEvent(getWishCount);

        dbrf.child("categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot temp : dataSnapshot.getChildren()){
                    spinnerArray.add(temp.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_dropdown_item_1line, spinnerArray);
        combo_box.setAdapter(adapter);

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(wish_desc.getText().toString().isEmpty() || wish_name.getText().toString().isEmpty() || wish_quantity.getText().toString().isEmpty() || combo_box.getText().toString().isEmpty()){
                    // Show Error Toast
                    Toast.makeText(ctx, "Please fill out all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Write to Database
                    dbrf.child("wishes").child(String.valueOf(wishCount)).child("category").setValue(combo_box.getText().toString());
                    dbrf.child("wishes").child(String.valueOf(wishCount)).child("descWish").setValue(wish_desc.getText().toString());

                    // TODO : Implement Image Uploads
                    dbrf.child("wishes").child(String.valueOf(wishCount)).child("pictureWish").setValue("");
                    dbrf.child("wishes").child(String.valueOf(wishCount)).child("qtyWish").setValue(Integer.parseInt(wish_quantity.getText().toString()));

                    dbrf.child("wishes").child(String.valueOf(wishCount)).child("timeWish").setValue(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date()));
                    dbrf.child("wishes").child(String.valueOf(wishCount)).child("titleWish").setValue(wish_name.getText().toString());
                    dbrf.child("wishes").child(String.valueOf(wishCount)).child("uidUpWish").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    Toast.makeText(ctx, "Item Uploaded Successfully", Toast.LENGTH_SHORT).show();

                    wish_quantity.setText("");
                    wish_name.setText("");
                    wish_desc.setText("");
                    combo_box.setText("");
                }
            }
        });
    }
}
