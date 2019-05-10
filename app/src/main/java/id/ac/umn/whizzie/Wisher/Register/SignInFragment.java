package id.ac.umn.whizzie.Wisher.Register;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import id.ac.umn.whizzie.Wisher.Activity.WisherActivity;
import id.ac.umn.whizzie.R;

import static android.support.constraint.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {


    public SignInFragment() {
        // Required empty public constructor
    }

    private TextView dontHaveAnAccount;
    private FrameLayout parentFrameLayout;
    private ImageButton closeButton;
    private Button signInBtn;
    private EditText edtEmail, edtPass;


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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dontHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignUpFragment());
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//          startActivity(new Intent(getContext(), WisherActivity.class));
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth fbA = FirebaseAuth.getInstance();

                if(edtEmail.getText().toString().isEmpty() || edtPass.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Please fill the email and password field", Toast.LENGTH_SHORT).show();
                }
                else {
                    fbA.signInWithEmailAndPassword(edtEmail.getText().toString(), edtPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in successful
                                Intent mainIntent = new Intent(getActivity(), WisherActivity.class);
                                startActivity(mainIntent);
                                getActivity().finish();
                            } else {
                                // Sign in failed
                                // TODO : Show Error Message
                                Log.e(TAG, "Authentication failed!");
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

    // TODO: Implement Auto Login aftar Sign Up
    // TODO: Also implement auto insert to Realtime Database
}
