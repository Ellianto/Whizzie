package id.ac.umn.whizzie.main.Register;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import id.ac.umn.whizzie.main.Activity.MainActivity;
import id.ac.umn.whizzie.R;


/**
 * A simple {@link Fragment} subclass.
 */

public class SignUpFragment extends Fragment {

    public SignUpFragment() {
        // Required empty public constructor
    }

    private TextView alreadyHaveAnAccount;
    private FrameLayout parentFrameLayout;

    private EditText email;
    private EditText fullName;
    private EditText password;
    private EditText confirmPassword;

    private ImageButton closeBtn;
    private Button signUpBtn;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+(\\.[a-z]+)+";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        alreadyHaveAnAccount = view.findViewById(R.id.sign_up_redirect_sign_in);
        parentFrameLayout =  getActivity().findViewById(R.id.register_frame_layout);

        email = view.findViewById(R.id.sign_up_email);
        fullName = view.findViewById(R.id.sign_up_full_name);
        password = view.findViewById(R.id.sign_up_password);
        confirmPassword = view.findViewById(R.id.sign_up_confirm_password);

        closeBtn = view.findViewById(R.id.sign_up_close_btn);
        signUpBtn = view.findViewById(R.id.sign_up_btn);

        progressBar = view.findViewById(R.id.sign_up_progressBar);

        firebaseAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailAndPassword();
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs(){
        if (TextUtils.isEmpty(email.getText()) || TextUtils.isEmpty(fullName.getText()) || TextUtils.isEmpty(confirmPassword.getText()) || TextUtils.isEmpty(password.getText()) || password.length() < 8){
            signUpBtn.setEnabled(false);
            signUpBtn.setTextColor(Color.argb(50,255,255,255));
        }else {
            signUpBtn.setEnabled(true);
            signUpBtn.setTextColor(Color.rgb(255,255,255));
        }
    }

    private void checkEmailAndPassword(){
        final String textEmail = email.getText().toString();
        final String textPass = password.getText().toString();

        if (textEmail.matches(emailPattern)){
            if (textPass.equals(confirmPassword.getText().toString())){

                progressBar.setVisibility(View.VISIBLE);
                signUpBtn.setEnabled(false);
                signUpBtn.setTextColor(Color.argb(50,255,255,255));

                firebaseAuth.createUserWithEmailAndPassword(textEmail, textPass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    firebaseAuth.signInWithEmailAndPassword(textEmail, textPass);

                                    String uid = firebaseAuth.getCurrentUser().getUid();

                                    DatabaseReference fdb = FirebaseDatabase.getInstance().getReference();

                                    fdb.child("users").child(uid).child("email").setValue(textEmail);
                                    fdb.child("users").child(uid).child("name").setValue(fullName.getText().toString());
                                    fdb.child("users").child(uid).child("storeAddress").setValue("");
                                    fdb.child("users").child(uid).child("imgBackground").setValue("");
                                    fdb.child("users").child(uid).child("imgProfilePicture").setValue("");

                                    Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(mainIntent);
                                    getActivity().finish();
                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    signUpBtn.setEnabled(true);
                                    signUpBtn.setTextColor(Color.rgb(255,255,255));
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(),error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                confirmPassword.setError("Password doesn't match!");
            }
        }else{
            email.setError("Invalid Email!");
        }
    }
}
