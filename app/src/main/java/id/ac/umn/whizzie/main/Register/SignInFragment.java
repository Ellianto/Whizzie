package id.ac.umn.whizzie.main.Register;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.Activity.MainActivity;

import static android.support.constraint.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {
    Context ctx;

    public SignInFragment() {
        // Required empty public constructor
    }

    private TextView dontHaveAnAccount, forgotPass;
    private FrameLayout parentFrameLayout;
    private ImageButton closeButton;
    private Button signInBtn;
    private EditText edtEmail, edtPass;

    private static final String PREFERENCE_FILENAME = "user_credentials";
    private static final int PREFERENCE_MODE        = Context.MODE_PRIVATE;
    private static final String KEY_UNAME           = "this_username";
    private static final String KEY_PASSWORD        = "this_password";

    FirebaseAuth fbA = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        dontHaveAnAccount = view.findViewById(R.id.sign_in_redirect_sign_up);
        closeButton = view.findViewById(R.id.sign_in_close_btn);
        parentFrameLayout =  getActivity().findViewById(R.id.register_frame_layout);
        signInBtn = view.findViewById(R.id.sign_in_btn);
        edtPass = view.findViewById(R.id.sign_in_password);
        edtEmail = view.findViewById(R.id.sign_in_email);
        forgotPass = view.findViewById(R.id.sign_in_forgot_password);

        ctx = this.getContext();

        forgotPass.setClickable(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final SharedPreferences shp = ctx.getSharedPreferences(PREFERENCE_FILENAME, PREFERENCE_MODE);

        String uname = shp.getString(KEY_UNAME, "");
        String passwd = shp.getString(KEY_PASSWORD, "");
        if(!uname.isEmpty() && !uname.equals(null) && !passwd.isEmpty() && !passwd.equals(null)){
            fbA.signInWithEmailAndPassword(uname, passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in successful
                        Intent mainIntent = new Intent(getContext(), MainActivity.class);
                        startActivity(mainIntent);
                        getActivity().finish();
                    }
                }
            });
        }

        super.onViewCreated(view, savedInstanceState);


        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtEmail.getText().toString().isEmpty()){
                    Toast.makeText(ctx, "Please fill in the Email Address Field First", Toast.LENGTH_LONG).show();
                } else{
                    final String emailAddr = edtEmail.getText().toString();

                    FirebaseAuth.getInstance().sendPasswordResetEmail(emailAddr).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ctx, "An email has been sent to " + emailAddr, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        dontHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignUpFragment());
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtEmail.getText().toString().isEmpty() || edtPass.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Please fill the email and password field", Toast.LENGTH_SHORT).show();
                }
                else {
                    fbA.signInWithEmailAndPassword(edtEmail.getText().toString(), edtPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in successful
                                SharedPreferences.Editor shpEditor = shp.edit();
                                shpEditor.putString(KEY_UNAME, edtEmail.getText().toString());
                                shpEditor.putString(KEY_PASSWORD, edtPass.getText().toString());

                                shpEditor.apply();

                                Intent mainIntent = new Intent(getContext(), MainActivity.class);
                                startActivity(mainIntent);
                                getActivity().finish();
                            } else {
                                // Sign in failed
                                Toast.makeText(getContext(), "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}
