package id.ac.umn.whizzie.main.Settings;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.Activity.SettingActivity;
import id.ac.umn.whizzie.main.Activity.SplashActivity;


/**
 * A simple {@link Fragment} subclass.
 */

    // TODO: Implement Multimode here

    // TODO: Fetch images here
public class SettingFragment extends Fragment {
    Context ctx;
    Button editAddress, changePass, logOut, changeName;
    TextView dispName;

    boolean genieMode;

    String currUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();
    StorageReference strf = FirebaseStorage.getInstance().getReference();

    // Value event listeners
    ValueEventListener setupProfile = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(genieMode) dispName.setText(dataSnapshot.child("toko").child("name").getValue().toString());
            else dispName.setText(dataSnapshot.child("name").getValue().toString());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    };

    // On Click Listeners

    View.OnClickListener sendEmailChangePass = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

            // Send password reset email
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ctx, "An email has been sent to " + email, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    View.OnClickListener changeNameDialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setTitle("Change Display Name");

            View dialogContent = LayoutInflater.from(ctx).inflate(R.layout.card_change_display_name, (ViewGroup) getView(), false);

            final EditText newDispName = dialogContent.findViewById(R.id.change_display_name_edit_text);

            builder.setView(dialogContent);
            builder.setCancelable(true);

            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String new_disp_name = newDispName.getText().toString();

                    dbrf.child("users").child(currUid).child("name").setValue(new_disp_name);
                    Toast.makeText(ctx, "Display Name Changed Successfully!", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }
    };

    View.OnClickListener logOutEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ctx, SplashActivity.class));
            ((SettingActivity) ctx).finish();
        }
    };

    View.OnClickListener moveToEditAddress = new View.OnClickListener() {
        @Override
        public void onClick(View v) {((SettingActivity) ctx).setFragment(new SettingAddressFragment());}
    };

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_setting, container, false);

        ctx = this.getContext();

        dispName    = v.findViewById(R.id.setting_display_name);
        editAddress = v.findViewById(R.id.setting_address);
        changePass  = v.findViewById(R.id.setting_change_password);
        logOut      = v.findViewById(R.id.setting_log_out);
        changeName  = v.findViewById(R.id.setting_change_display_name);

        genieMode = Boolean.getBoolean(getArguments().get("genieMode").toString());

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbrf.child("users").child(currUid).addValueEventListener(setupProfile);

        changeName.setOnClickListener(changeNameDialog);

        editAddress.setOnClickListener(moveToEditAddress);

        changePass.setOnClickListener(sendEmailChangePass);

        logOut.setOnClickListener(logOutEvent);
    }
}
