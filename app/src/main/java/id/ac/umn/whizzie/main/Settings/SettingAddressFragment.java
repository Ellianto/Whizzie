package id.ac.umn.whizzie.main.Settings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.Activity.SettingActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingAddressFragment extends Fragment {
    RecyclerView  rv;
    Button addAddress;
    TextView notFound;

    List<AddressCard> lac;
    Context ctx;

    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public SettingAddressFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting_address, container, false);

        rv = view.findViewById(R.id.setting_address_list);
        addAddress = view.findViewById(R.id.setting_address_add);
        notFound = view.findViewById(R.id.setting_address_empty);

        ctx = this.getContext();
        lac = new ArrayList<>();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fr = new SettingAddressDetailFragment();
                Bundle b = new Bundle();

                b.putBoolean("edit", false);
                fr.setArguments(b);

                ((SettingActivity) getActivity()).setFragment(fr);
            }
        });

        dbrf.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("alamat")){
                    notFound.setVisibility(View.GONE);
                    String storeAddress = dataSnapshot.child("storeAddress").getValue().toString();

                    for(DataSnapshot ds : dataSnapshot.child("alamat").getChildren()){
                        GenericTypeIndicator<Integer> gti = new GenericTypeIndicator<Integer>() {};

                        lac.add(new AddressCard(
                            ds.getKey(),
                                ds.child("cityName").getValue().toString(),
                                ds.child("detailAddress").getValue().toString(),
                                ds.child("phoneNum").getValue().toString(),
                                String.valueOf(ds.child("postalCode").getValue(gti)),
                                ds.child("provinceName").getValue().toString(),
                                ds.child("cityName").getValue().toString(),
                                storeAddress.equals(ds.getKey())
                        ));
                    }

                    rv.setHasFixedSize(true);
                    rv.setLayoutManager(new GridLayoutManager(ctx, 1, GridLayoutManager.VERTICAL, false));

                    AddressCardAdapter aca = new AddressCardAdapter(ctx, lac);
                    rv.setAdapter(aca);
                } else {
                    rv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}