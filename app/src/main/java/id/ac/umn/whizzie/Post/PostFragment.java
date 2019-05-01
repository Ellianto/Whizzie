package id.ac.umn.whizzie.Post;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import id.ac.umn.whizzie.Activity.WisherActivity;
import id.ac.umn.whizzie.Home.CategoryCard;
import id.ac.umn.whizzie.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {
    Button postBtn;
    EditText wish_name, wish_quantity, wish_budget;
    Spinner combo_box;
    List<String> spinnerArray;

    public PostFragment() {
        // Required empty public constructor
    }


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
        wish_budget = view.findViewById(R.id.post_edit_text_budget);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Populate Spinner with Categories
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference();

        spinnerArray =  new ArrayList<>();

        dbRef.child("categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {loadSpinnerItems(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        combo_box.setAdapter(adapter);

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement Add to Wish to Database

                // To get what is selected
                //        String selected = sItems.getSelectedItem().toString();
                //            if (selected.equals("what ever the option was")) {
                //        }
            }
        });


    }

    private void loadSpinnerItems(DataSnapshot ds){
        for(DataSnapshot temp : ds.getChildren()){
            spinnerArray.add(temp.getValue(CategoryCard.class).getCategoryName());
        }
    }
}
