package id.ac.umn.whizzie.main.Wishes;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import id.ac.umn.whizzie.main.Activity.MainActivity;
import id.ac.umn.whizzie.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class WishesFragment extends Fragment {

    RecyclerView wishes_recview;

    public WishesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wishes, container, false);
        wishes_recview = view.findViewById(R.id.wishes_recycler_view);

        ((MainActivity) getActivity()).showActionBar();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        wishes_recview.setHasFixedSize(true);
        wishes_recview.setLayoutManager(new LinearLayoutManager(this.getContext()));

        List<WishesCard> tiList = new ArrayList<>();

        // TODO : Set on click listener of Search

        WishesCardAdapter tiAdapter = new WishesCardAdapter(this.getContext(), tiList);
        wishes_recview.setAdapter(tiAdapter);
    }
}
