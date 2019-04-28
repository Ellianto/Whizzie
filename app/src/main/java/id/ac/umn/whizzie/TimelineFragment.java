package id.ac.umn.whizzie;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimelineFragment extends Fragment {

    RecyclerView timeline_recview;

    public TimelineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        timeline_recview = view.findViewById(R.id.timeline_recycler_view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timeline_recview.setHasFixedSize(true);

        List<TimelineItems> tiList = new ArrayList<>();

        tiList.add(new TimelineItems("Ellianto", "Cuma coba coba", 100000));
        tiList.add(new TimelineItems("Alexander", "Tes Test testing", 25000));
        tiList.add(new TimelineItems("Ellianto", "Cek cek cek", 10101));

        TimelineItemsAdapter tiAdapter = new TimelineItemsAdapter(this.getContext(), tiList);
        timeline_recview.setAdapter(tiAdapter);
    }
}
