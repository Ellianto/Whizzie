package id.ac.umn.whizzie;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    RecyclerView home_middle_category, home_bottom_grid;
    ImageView home_top_banner;


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
        home_bottom_grid = view.findViewById(R.id.home_bottom_grid);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO: Load Category CardViews

        // Middle Cateogry List Data Load
        home_middle_category.setHasFixedSize(true);

        home_middle_category.setLayoutManager(new GridLayoutManager(this.getContext(), 2, GridLayoutManager.HORIZONTAL, false));

        List<CategoryCard> ccList = new ArrayList<>();

        ccList.add(new CategoryCard("Furniture"));
        ccList.add(new CategoryCard("Collectibles"));
        ccList.add(new CategoryCard("Artwork"));
        ccList.add(new CategoryCard("Machinery"));
        ccList.add(new CategoryCard("Tools"));
        ccList.add(new CategoryCard("Software"));
        ccList.add(new CategoryCard("Fashion"));

        CategoryCardAdapter ccAdapter = new CategoryCardAdapter(this.getContext(), ccList);

        home_middle_category.setAdapter(ccAdapter);

        // Bottom Grid RecView Data Load
        home_bottom_grid.setHasFixedSize(true);

        home_bottom_grid.setLayoutManager(new GridLayoutManager(this.getContext(), 2, GridLayoutManager.VERTICAL, false));

        List<HomeCard> hcList = new ArrayList<>();

        hcList.add(new HomeCard("Testing 1"));
        hcList.add(new HomeCard("Testing 2"));
        hcList.add(new HomeCard("Testing 3"));
        hcList.add(new HomeCard("Testing 4"));
        hcList.add(new HomeCard("Testing 5"));

        HomeCardAdapter hcAdapter = new HomeCardAdapter(this.getContext(), hcList);

        home_bottom_grid.setAdapter(hcAdapter);
    }
}
