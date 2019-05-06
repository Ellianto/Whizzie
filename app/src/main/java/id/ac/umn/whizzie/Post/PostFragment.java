package id.ac.umn.whizzie.Post;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
    Spinner combo_box;
    List<String> spinnerArray;
    long wishCount = 0;
    String selectedCategory;

    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();

    public PostFragment() {
        // Required empty public constructor
    }

    ValueEventListener getWishCount = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            wishCount = dataSnapshot.getChildrenCount();
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

        ((WisherActivity) getActivity()).showActionBar();

        combo_box = view.findViewById(R.id.post_combo_box_category);
        postBtn = view.findViewById(R.id.post_button_wish);

        wish_name   = view.findViewById(R.id.post_edit_text_item_name);
        wish_quantity = view.findViewById(R.id.post_edit_text_quantity);
        wish_desc = view.findViewById(R.id.post_edit_text_desc);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Populate Spinner with Categories
        spinnerArray =  new ArrayList<>();

        dbrf.child("categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {loadSpinnerItems(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, spinnerArray);

//        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        combo_box.setAdapter(adapter);

        // TODO : Fix Spinner not selected
        combo_box.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
                Log.d("DEBUG", selectedCategory);

//                combo_box.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(wish_desc.getText().toString().isEmpty() || wish_name.getText().toString().isEmpty() || wish_quantity.getText().toString().isEmpty() || selectedCategory.isEmpty()){
                    // Show Error Toast
                    Toast.makeText(getContext(), "Please fill out all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Write to Database

                    dbrf.child("wishes").addListenerForSingleValueEvent(getWishCount);

                    dbrf.child("wishes").child(String.valueOf(wishCount)).child("category").setValue(combo_box.getSelectedItem().toString());
                    dbrf.child("wishes").child(String.valueOf(wishCount)).child("descWish").setValue(wish_desc.getText().toString());

                    // TODO : Implement Image Uploads
                    dbrf.child("wishes").child(String.valueOf(wishCount)).child("pictureWish").setValue("");

                    dbrf.child("wishes").child(String.valueOf(wishCount)).child("timeWish").setValue(new SimpleDateFormat("yyyy/MM/DD hh:mm:ss").format(new Date()));
                    dbrf.child("wishes").child(String.valueOf(wishCount)).child("titleWish").setValue(wish_name.getText().toString());
                    dbrf.child("wishes").child(String.valueOf(wishCount)).child("uidUpWish").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                }

                // To get what is selected
                //        String selected = sItems.getSelectedItem().toString();
                //            if (selected.equals("what ever the option was")) {
                //        }
            }
        });
    }

    private void loadSpinnerItems(DataSnapshot ds){
        for(DataSnapshot temp : ds.getChildren()){
            spinnerArray.add(temp.getKey());
        }
    }
}
