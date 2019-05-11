package id.ac.umn.whizzie.main.Settings;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.Activity.SettingActivity;

//TODO: Layout awal untuk Setting. Belum dikasi OnClickListener untuk button 'Addresses', 'Change Password', dan 'Log Out'

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingWisherFragment extends Fragment {
    Context ctx;
    Button editAddress, changePass, logOut;

    String currUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();
    StorageReference strf = FirebaseStorage.getInstance().getReference();


    public SettingWisherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_setting, container, false);

        editAddress = v.findViewById(R.id.setting_address);
        changePass = v.findViewById(R.id.setting_change_password);
        logOut = v.findViewById(R.id.setting_log_out);

        ctx = this.getContext();

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SettingActivity) ctx).setFragment(new SettingAddressFragment());
            }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : Implement Password Change here

                Log.d("DEBUG", FirebaseAuth.getInstance().getCurrentUser().getEmail());
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : Implement log out here
                FirebaseAuth.getInstance().signOut();
            }
        });
    }
}
