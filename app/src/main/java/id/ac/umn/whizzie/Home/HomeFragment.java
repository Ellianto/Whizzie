package id.ac.umn.whizzie.Home;


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
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import id.ac.umn.whizzie.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    RecyclerView home_middle_category, home_bottom_grid;
    ImageView home_top_banner;

    private List<CategoryCard> ccList;
    private List<FeaturedGenieCard> fgList;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        home_top_banner = view.findViewById(R.id.home_top_banner);
        home_middle_category = view.findViewById(R.id.home_middle_category);
        home_bottom_grid = view.findViewById(R.id.home_bottom_featured_genies);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Middle Category List Data Load
        home_middle_category.setHasFixedSize(true);

        home_middle_category.setLayoutManager(new GridLayoutManager(this.getContext(), 1, GridLayoutManager.HORIZONTAL, false));

        ccList = new ArrayList<>();

        //Testing Fetch Data
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference();

        // Query Must be done using Event Listeners
        dbRef.child("categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadCategoryCard(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        ccList.add(new CategoryCard("Furniture"));
//        ccList.add(new CategoryCard("Collectibles"));
//        ccList.add(new CategoryCard("Artwork"));
//        ccList.add(new CategoryCard("Machinery"));
//        ccList.add(new CategoryCard("Tools"));
//        ccList.add(new CategoryCard("Software"));
//        ccList.add(new CategoryCard("Fashion"));

        // TODO: Load Featured Genies Grid
        // Middle Category List Data Load
        home_bottom_grid.setHasFixedSize(true);

        home_bottom_grid.setLayoutManager(new GridLayoutManager(this.getContext(), 1, GridLayoutManager.HORIZONTAL, false));

        fgList = new ArrayList<>();

        fgList.add(new FeaturedGenieCard("Alexander"));
        fgList.add(new FeaturedGenieCard("Ellianto"));
        fgList.add(new FeaturedGenieCard("Karissa"));
        fgList.add(new FeaturedGenieCard("Leonardo"));

        FeaturedGenieCardAdapter fgAdapter = new FeaturedGenieCardAdapter(this.getContext(), fgList);
        home_bottom_grid.setAdapter(fgAdapter);
    }

    private void loadCategoryCard(DataSnapshot dataSS){
        for(DataSnapshot temp : dataSS.getChildren()){
            Log.d("DATA", temp.getValue().toString());

            CategoryCard cc = temp.getValue(CategoryCard.class);
            Log.d("CLASS", cc.getCategoryName());
            Log.d("CLASS", String.valueOf(cc.getImageID()));

            ccList.add(cc);
        }

        CategoryCardAdapter ccAdapter = new CategoryCardAdapter(this.getContext(), ccList);

        home_middle_category.setAdapter(ccAdapter);
    }

}
