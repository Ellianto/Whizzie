package id.ac.umn.whizzie.main.Wishes;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import id.ac.umn.whizzie.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class WishesFragment extends Fragment {
    Context ctx;

    RecyclerView wishes_recview;

    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();
    ArrayMap<String, String> unamePair;
    List<WishesCard> wcList;

    public WishesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wishes, container, false);
        wishes_recview = view.findViewById(R.id.wishes_recycler_view);

        ctx = this.getContext();

        unamePair = new ArrayMap<>();
        wcList = new ArrayList<>();

        loadUsernameList();
        loadRecentWishes();
        Log.d("DEBUG", "SELESAI FETCH");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        wishes_recview.setHasFixedSize(true);
        wishes_recview.setLayoutManager(new GridLayoutManager(ctx, 1, GridLayoutManager.VERTICAL, false));
        WishesCardAdapter wcAdapter = new WishesCardAdapter(ctx, wcList);
        wishes_recview.setAdapter(wcAdapter);
    }

    private void loadRecentWishes(){
        // TODO : Investigate and Check this
        dbrf.child("wishes").orderByChild("timeWish").limitToFirst(10).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    final String upName = unamePair.get(ds.child("uidUpWish").getValue().toString());
                    final String wishDesc = ds.child("descWish").getValue().toString();
                    final String wishTitle = ds.child("titleWish").getValue().toString();
                    final String imgPath = ds.child("pictureWish").getValue().toString();

                    dbrf.child("wishRelation").child(ds.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSS) {
                            wcList.add(new WishesCard(
                                    upName,
                                    wishDesc,
                                    wishTitle,
                                    imgPath,
                                    dataSS.getChildrenCount()
                            ));

                            Log.d("DEBUG", "NAMBAH 1");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    // Pre-load the UID-Username mapping to render the usernames in the Search Card;
    private void loadUsernameList(){
        dbrf.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot user : dataSnapshot.getChildren()){
                    String user_id = user.getKey();
                    String username = user.child("name").getValue().toString();

                    unamePair.put(user_id, username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
