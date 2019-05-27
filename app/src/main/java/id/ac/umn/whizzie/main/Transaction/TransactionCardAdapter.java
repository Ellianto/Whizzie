package id.ac.umn.whizzie.main.Transaction;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.Activity.MainActivity;
import id.ac.umn.whizzie.main.Cart.CartStoreAdapter;

public class TransactionCardAdapter extends RecyclerView.Adapter<TransactionCardAdapter.TransactionCardHolder> {
    Context ctx;
    List<TransactionCard> tcList;

    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();

    public TransactionCardAdapter(Context ctx, List<TransactionCard> tcList) {
        this.ctx = ctx;
        this.tcList = tcList;
    }

    @NonNull
    @Override
    public TransactionCardHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.card_transaction, null);
        TransactionCardHolder holder = new TransactionCardHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionCardHolder holder, int i) {
        final TransactionCard temp = tcList.get(i);

        holder.transID.setText("ID : " + temp.getTransactionID());
        holder.transTotal.setText("Rp. " + String.valueOf(temp.getTotalHarga()));
        holder.transTimestamp.setText(temp.getTimestamp());

        if(temp.getScList().isEmpty()){
            holder.transStatus.setVisibility(View.VISIBLE);

            holder.paymentBtn.setVisibility(View.VISIBLE);
            holder.paymentBtn.setClickable(true);
            holder.paymentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbrf.child("transaction").child(temp.getTransactionID()).child("statusPembayaran").setValue(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date()));

                    Toast.makeText(ctx, "Pembayaran berhasil!", Toast.LENGTH_SHORT).show();

                    ((MainActivity)ctx).setFragment(new TransactionFragment());
                }
            });
        }
        else {
            holder.transStatus.setVisibility(View.GONE);
            holder.paymentBtn.setVisibility(View.GONE);
            holder.paymentBtn.setClickable(false);

            holder.rvItems.setHasFixedSize(true);
            holder.rvItems.setLayoutManager(new GridLayoutManager(ctx, 1, GridLayoutManager.VERTICAL, false));
            holder.rvItems.setAdapter(new CartStoreAdapter(ctx, temp.getScList()));
        }
    }

    @Override
    public int getItemCount() {
        return tcList.size();
    }

    class TransactionCardHolder extends RecyclerView.ViewHolder{
        TextView transStatus, transTotal, transID, transTimestamp;
        Button paymentBtn;
        RecyclerView rvItems;

        public TransactionCardHolder(@NonNull View itemView) {
            super(itemView);

            transID         = itemView.findViewById(R.id.transaction_id);
            transStatus     = itemView.findViewById(R.id.transaction_status);
            transTimestamp  = itemView.findViewById(R.id.transaction_timestamp);
            transTotal      = itemView.findViewById(R.id.transaction_total);

            paymentBtn      = itemView.findViewById(R.id.payment_transaction_btn);

            rvItems         = itemView.findViewById(R.id.transaction_items);
        }
    }
}
