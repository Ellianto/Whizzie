package id.ac.umn.whizzie.main.Profile;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.Activity.MainActivity;
import id.ac.umn.whizzie.main.Activity.SettingActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    TabLayout tl;
    FrameLayout fl;
    TextView tvName;
    ImageButton profile_setting_button;
    Button modeSwitch;

    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();

    String currUid, dispName;

    TabLayout.OnTabSelectedListener wisherTabs = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            switch (tab.getPosition()){
                case 0: setFragment(new ProfileWishFragment()); break;
                case 1: setFragment(new ProfileTransactionFragment()); break;
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    TabLayout.OnTabSelectedListener genieTabs = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            switch (tab.getPosition()){
                case 0: setFragment(new ProfileProductFragment()); break;
                case 1: setFragment(new ProfileTransactionFragment()); break;
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ((MainActivity) getActivity()).hideActionBar();

        tvName = view.findViewById(R.id.profile_display_name);

        tl = view.findViewById(R.id.profile_tab_layout);
        fl = view.findViewById(R.id.profile_fragment_holder);

        modeSwitch = view.findViewById(R.id.profile_creator_button);

        // Button for setting
        profile_setting_button = view.findViewById(R.id.profile_setting_button);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        dbrf.child("users").child(currUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setDisplayName(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        tl.addOnTabSelectedListener(wisherTabs);

        setFragment(new ProfileWishFragment());

        // Button Setting Intent
        profile_setting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        modeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).switchMode();
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        Bundle b = new Bundle();

        b.putString("dispName", dispName);
        b.putString("currUid", currUid);

        fragment.setArguments(b);
        fragmentTransaction.replace(fl.getId(), fragment);

        fragmentTransaction.commit();
    }

    private void setDisplayName(DataSnapshot ds){
        dispName = ds.child("name").getValue().toString();
        tvName.setText(dispName);
    }
}
