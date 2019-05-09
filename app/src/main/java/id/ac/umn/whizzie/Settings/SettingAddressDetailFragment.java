package id.ac.umn.whizzie.Settings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.ac.umn.whizzie.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingAddressDetailFragment extends Fragment {


    public SettingAddressDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_address_detail, container, false);
    }

}
