package id.ac.umn.whizzie.main.Settings;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.Activity.SettingActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingAddressDetailFragment extends Fragment {
    EditText addrName, addrRec, addrCity, addrProv, addrDetail, addrPostal, addrPhone;
    Button btnSave, btnDelete;
    Switch storeSwitch;

    Context ctx;
    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();

    boolean isStore = false;
    boolean isEdit = false;

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String currAddrKey;

    public SettingAddressDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting_address_detail, container, false);

        isEdit = getArguments().getBoolean("edit");

        ctx = this.getContext();

        addrName = view.findViewById(R.id.setting_address_name);
        addrRec = view.findViewById(R.id.setting_receiver_name);
        addrCity = view.findViewById(R.id.setting_city_name);
        addrProv = view.findViewById(R.id.setting_province_name);
        addrDetail = view.findViewById(R.id.setting_detail_address);
        addrPostal = view.findViewById(R.id.setting_postal_code);
        addrPhone = view.findViewById(R.id.setting_phone_number);

        btnSave = view.findViewById(R.id.address_detail_save);
        btnDelete = view.findViewById(R.id.address_detail_delete);

        storeSwitch = view.findViewById(R.id.store_bool_switch);

        storeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isStore = isChecked;
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(isEdit){
            currAddrKey = getArguments().getString("addrKey");

            dbrf.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot ds) {
                    if(ds.child("storeAddress").getValue().toString().equals(currAddrKey)){
                        storeSwitch.setChecked(true);
                        storeSwitch.setEnabled(false);
                    }

                    Address holder = ds.child("alamat").child(currAddrKey).getValue(Address.class);

                    addrName.setText(currAddrKey);

                    addrCity.setText(holder.cityName);
                    addrDetail.setText(holder.detailAddress);
                    addrPhone.setText(holder.phoneNum);
                    addrProv.setText(holder.provinceName);
                    addrRec.setText(holder.receiverName);
                    addrPostal.setText(String.valueOf(holder.postalCode));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });

            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setTitle("Confirm Delete Item");
                    builder.setMessage("Are you sure you want to delete the item?");
                    builder.setCancelable(true);

                    builder.setPositiveButton("Delete Address", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbrf.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.child("storeAddress").getChildren().toString().equals(currAddrKey));
                                    dbrf.child("users").child(uid).child("storeAddress").setValue("");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {}
                            });

                            dbrf.child("users").child(uid).child("alamat").child(currAddrKey).removeValue();

                            ((SettingActivity) ctx).setFragment(new SettingAddressFragment());
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {dialog.cancel();}
                    });

                    builder.show();


                }
            });
        } else {
            btnDelete.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String address_name = addrName.getText().toString();
                String address_receive = addrRec.getText().toString();
                String address_city = addrCity.getText().toString();
                String address_province = addrProv.getText().toString();
                String address_detail = addrDetail.getText().toString();

                int address_postal = 0;
                if(!addrPostal.getText().toString().isEmpty())
                    address_postal = Integer.parseInt(addrPostal.getText().toString());

                String address_phone = addrPhone.getText().toString();

                if(address_city.isEmpty() || address_detail.isEmpty() || address_name.isEmpty() || addrPostal.getText().toString().isEmpty() || address_phone.isEmpty() | address_province.isEmpty()
                 || address_receive.isEmpty()){
                    Toast.makeText(ctx, "Please fill out all the fields!", Toast.LENGTH_LONG).show();
                }
                else {
                    final Address addr = new Address(address_city, address_detail, address_phone, address_postal, address_province, address_receive);

                    if(isEdit){
                        // Update
                        // Remove current
                        dbrf.child("users").child(uid).child("alamat").child(currAddrKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Populate with current Data
                                dbrf.child("users").child("alamat").child(currAddrKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        dbrf.child("users").child(uid).child("alamat").child(address_name).setValue(addr);
                                        if(isStore) dbrf.child("users").child(uid).child("storeAddress").setValue(address_name);

                                        ((SettingActivity) ctx).setFragment(new SettingAddressFragment());
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                                });
                            }
                        });
                    } else {
                        // Create New or Update if same key
                        dbrf.child("users").child(uid).child("alamat").child(address_name).setValue(addr);
                        if(isStore) dbrf.child("users").child(uid).child("storeAddress").setValue(address_name);

                        ((SettingActivity) ctx).setFragment(new SettingAddressFragment());
                    }
                }
            }
        });
    }
}
