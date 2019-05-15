package id.ac.umn.whizzie.main.Settings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.Activity.SettingActivity;

public class AddressCardAdapter extends RecyclerView.Adapter<AddressCardAdapter.AddressCardHolder> {
    Context ctx;
    List<AddressCard> lac;

    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();

    public AddressCardAdapter(Context ctx, List<AddressCard> lac) {
        this.ctx = ctx;
        this.lac = lac;
    }

    @NonNull
    @Override
    public AddressCardHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.card_address, null);
        AddressCardHolder holder = new AddressCardHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddressCardHolder v, int i) {
        AddressCard temp = lac.get(i);


        final String addrKey = temp.getNameAddress();

        v.receiver_name.setText(temp.getReceiverName());
        v.postal_code.setText(temp.getPostalCode());
        v.province_name.setText(temp.getProvinceName());
        v.city_name.setText(temp.getCityName());
        v.detail_address.setText(temp.getDetailAddress());
        v.phone_number.setText(temp.getPhoneNumber());

        if(temp.isStore()) v.store_status.setVisibility(View.VISIBLE);
        else v.store_status.setVisibility(View.GONE);

        v.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG", addrKey);

                Bundle b = new Bundle();
                b.putBoolean("edit", true);
                b.putString("addrKey", addrKey);

                Fragment fr = new SettingAddressDetailFragment();
                fr.setArguments(b);

                ((SettingActivity)ctx).setFragment(fr);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lac.size();
    }

    class AddressCardHolder extends RecyclerView.ViewHolder{
        TextView receiver_name, detail_address, city_name, province_name, postal_code, phone_number, store_status;
        CardView cardView;

        public AddressCardHolder(@NonNull View itemView) {
            super(itemView);
            receiver_name = itemView.findViewById(R.id.receiver_name);
            detail_address = itemView.findViewById(R.id.detail_address);
            city_name = itemView.findViewById(R.id.city_name);
            province_name = itemView.findViewById(R.id.province_name);
            postal_code = itemView.findViewById(R.id.postal_code);
            store_status = itemView.findViewById(R.id.store_status);
            phone_number = itemView.findViewById(R.id.phone_number);

            cardView = itemView.findViewById(R.id.address_card_view);
        }

    }
}
