package id.ac.umn.whizzie.Search;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import id.ac.umn.whizzie.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private RecyclerView search_result_grid;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        search_result_grid = view.findViewById(R.id.search_result_grid);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bottom Grid RecView Data Load
        search_result_grid.setHasFixedSize(true);

        search_result_grid.setLayoutManager(new GridLayoutManager(this.getContext(), 2, GridLayoutManager.VERTICAL, false));

        List<SearchCard> scList = new ArrayList<>();

        scList.add(new SearchCard("Testing 1"));
        scList.add(new SearchCard("Testing 2"));
        scList.add(new SearchCard("Testing 3"));
        scList.add(new SearchCard("Testing 4"));
        scList.add(new SearchCard("Testing 5"));

        SearchCardAdapter scAdapter = new SearchCardAdapter(this.getContext(), scList);

        search_result_grid.setAdapter(scAdapter);
    }
}
