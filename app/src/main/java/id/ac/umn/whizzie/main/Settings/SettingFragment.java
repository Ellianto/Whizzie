package id.ac.umn.whizzie.main.Settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.IOException;
import java.net.URL;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.Activity.SettingActivity;
import id.ac.umn.whizzie.main.Activity.SplashActivity;


/**
 * A simple {@link Fragment} subclass.
 */

public class SettingFragment extends Fragment {
    Context ctx;
    Button editAddress, changePass, logOut, changeName;
    ImageView profPic, bgPic;
    TextView dispName;

    private static final String PREFERENCE_FILENAME = "user_credentials";
    private static final int PREFERENCE_MODE        = Context.MODE_PRIVATE;
    private static final String KEY_UNAME           = "this_username";
    private static final String KEY_PASSWORD        = "this_password";

    boolean genieMode;

    String currUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();
    StorageReference strf = FirebaseStorage.getInstance().getReference();

    OnSuccessListener loadProfilePicture = new OnSuccessListener<Uri>() {
        @Override
        public void onSuccess(Uri uri) {
            new loadImage(profPic).execute(uri.toString());
        }
    };

    OnSuccessListener loadBackgroundImage = new OnSuccessListener<Uri>() {
        @Override
        public void onSuccess(Uri uri) {
            new loadImage(bgPic).execute(uri.toString());
        }
    };

    // Value event listeners
    ValueEventListener setupProfile = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(genieMode) dispName.setText(dataSnapshot.child("toko").child("name").getValue().toString());
            else dispName.setText(dataSnapshot.child("name").getValue().toString());

            String profPicRef = "whizzie_assets/empty/empty_profile.jpg";

            if(!dataSnapshot.child("imgProfilePicture").getValue().toString().isEmpty())
                profPicRef = "users/" + currUid + "/" + dataSnapshot.child("imgProfilePicture").getValue().toString();

            strf.child(profPicRef).getDownloadUrl().addOnSuccessListener(loadProfilePicture);

            String bgPicRef = "whizzie_assets/empty/empty.jpg";

            if(!dataSnapshot.child("imgBackground").getValue().toString().isEmpty())
                bgPicRef = "users/" + currUid + "/" + dataSnapshot.child("imgBackground").getValue().toString();

            strf.child(bgPicRef).getDownloadUrl().addOnSuccessListener(loadBackgroundImage);
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

                    if(!genieMode) dbrf.child("users").child(currUid).child("name").setValue(new_disp_name);
                    else dbrf.child("users").child(currUid).child("toko").child("name").setValue(new_disp_name);
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

            SharedPreferences shp = ctx.getSharedPreferences(PREFERENCE_FILENAME, PREFERENCE_MODE);
            SharedPreferences.Editor shpEditor = shp.edit();
            shpEditor.remove(KEY_PASSWORD);
            shpEditor.remove(KEY_UNAME);

            shpEditor.apply();

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

        profPic     = v.findViewById(R.id.setting_profile_picture);
        bgPic       = v.findViewById(R.id.setting_background_image);

        genieMode = ((SettingActivity)ctx).getMode();

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

    class loadImage extends AsyncTask<String, String, String> {
        ImageView temp;

        public loadImage(ImageView temp) {
            this.temp = temp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                URL url = new URL(strings[0]);
                final Bitmap pic = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                Handler h = new Handler(Looper.getMainLooper());

                // Operasi yang mengubah View harus di Main Thread
                // Karena akan dijalankan di dalam fragment, pakai handler
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        temp.setImageBitmap(pic);
                    }
                });
            } catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }
    }
}
