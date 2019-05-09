package id.ac.umn.whizzie.Settings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.ac.umn.whizzie.R;

//TODO: Layout awal untuk Setting. Belum dikasi OnClickListener untuk button 'Addresses', 'Change Password', dan 'Log Out'

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingWisherFragment extends Fragment {


    public SettingWisherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_wisher, container, false);
    }

}
