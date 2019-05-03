package id.ac.umn.whizzie.Wishes;


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

import id.ac.umn.whizzie.Activity.WisherActivity;
import id.ac.umn.whizzie.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class WishesFragment extends Fragment {

    RecyclerView timeline_recview;

    public WishesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        timeline_recview = view.findViewById(R.id.timeline_recycler_view);

        ((WisherActivity) getActivity()).showActionBar();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timeline_recview.setHasFixedSize(true);
        timeline_recview.setLayoutManager(new LinearLayoutManager(this.getContext()));

        List<WishesCard> tiList = new ArrayList<>();

        // TODO : Query Random Wishes here to be rendered

        // TODO : Set on click listener of Search

        WishesCardAdapter tiAdapter = new WishesCardAdapter(this.getContext(), tiList);
        timeline_recview.setAdapter(tiAdapter);
    }
}
