package id.ac.umn.whizzie.Notifications;


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
public class NotificationFragment extends Fragment {

    RecyclerView notification_recview;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        notification_recview = view.findViewById(R.id.notification_recycler_view);

        ((WisherActivity) getActivity()).showActionBar();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notification_recview.setHasFixedSize(true);
        notification_recview.setLayoutManager(new LinearLayoutManager(this.getContext()));

        List<Notifications> notifList = new ArrayList<>();

        notifList.add(new Notifications("Ini pemberitahuan pertama!!!"));
        notifList.add(new Notifications("Artinya, ini yang kedua"));
        notifList.add(new Notifications("Berarti ini yang ketiga dong"));

        NotificationsAdapter notifAdapter = new NotificationsAdapter(this.getContext(), notifList);

        notification_recview.setAdapter(notifAdapter);
    }
}
