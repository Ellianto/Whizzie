package id.ac.umn.whizzie.main.Cart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.Activity.CartActivity;
import id.ac.umn.whizzie.main.Activity.MainActivity;
import id.ac.umn.whizzie.main.Transaction.TransactionFragment;

public class CartStoreAdapter extends RecyclerView.Adapter<CartStoreAdapter.CartStoreHolder> {

    Context ctx;
    List<CartStoreCard> cscList;

    RecyclerView.RecycledViewPool rvp = new RecyclerView.RecycledViewPool();

    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();
    StorageReference strf = FirebaseStorage.getInstance().getReference();

    public CartStoreAdapter(Context ctx, List<CartStoreCard> cscList) {
        this.ctx = ctx;
        this.cscList = cscList;
    }

    @NonNull
    @Override
    public CartStoreHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.card_store_cart, null);
        CartStoreHolder holder = new CartStoreHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final @NonNull CartStoreHolder holder, int i) {
        final CartStoreCard temp = this.cscList.get(i);

        holder.storeTitle.setText(temp.getStoreName());
        holder.storeTotal.setText("Rp ." + String.valueOf(temp.getStoreTotal()));

        holder.storeItems.setHasFixedSize(true);
        holder.storeItems.setLayoutManager(new GridLayoutManager(ctx, 1, GridLayoutManager.VERTICAL, false));
        holder.storeItems.setAdapter(new CartItemAdapter(ctx, temp.getCicList()));

        if(temp.isSent()) holder.nomorResi.setText(temp.getNoResi());

        String imgRef = "whizzie_assets/empty/empty_profile.jpg";
        if(!temp.getStoreImage().isEmpty()) imgRef = "users/" + temp.getStoreImage();

        if(ctx.getClass().equals(CartActivity.class))
            holder.status.setVisibility(View.GONE);
        else{
            holder.status.setVisibility(View.VISIBLE);

            if(!temp.isGenieMode()){ // Wisher
                if(!temp.isSent()){ // Belum Dikirim
                    holder.status.setText("Menunggu Kiriman");
                    holder.status.setClickable(false);
                } else if(!temp.isReceived()){ // Sudah dikirim, tepi belum diterima
                    holder.status.setText("Konfirmasi Penerimaan");
                    holder.status.setClickable(true);

                    holder.status.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dbrf.child("transaction").
                                    child(temp.getTransKey()).
                                    child("pembelian").
                                    child(temp.getStoreKey()).
                                    child("statusDiterima").
                                    setValue(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date()));

                            ((MainActivity)ctx).setFragment(new TransactionFragment());
                        }
                    });
                } else { // Sudah dikirim dan sudah diterima
                    holder.status.setText("Transaksi Selesai!");
                    holder.status.setClickable(false);
                }
            }
            else { // Genie
                dbrf.child("transaction").child(temp.getTransKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("statusPembayaran").getValue().toString().isEmpty()){
                            holder.status.setText("Menunggu Pembayaran");
                            holder.status.setClickable(false);
                        } else {
                            if(!temp.isSent() && !temp.isReceived()){ // Belum Dikirim
                                holder.status.setText("Kirim Barang");
                                holder.status.setClickable(true);

                                holder.status.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                                        builder.setTitle("Input No Resi");

                                        View dialogContent = LayoutInflater.from(ctx).inflate(R.layout.card_input_resi, null);

                                        final EditText noResiField = dialogContent.findViewById(R.id.input_no_resi);

                                        builder.setView(dialogContent);
                                        builder.setCancelable(true);

                                        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String noResi = noResiField.getText().toString();

                                                dbrf.child("transaction").
                                                        child(temp.getTransKey()).
                                                        child("pembelian").
                                                        child(temp.getStoreKey()).
                                                        child("noResi").
                                                        setValue(noResi);

                                                dbrf.child("transaction").
                                                        child(temp.getTransKey()).
                                                        child("pembelian").
                                                        child(temp.getStoreKey()).
                                                        child("statusDikirim").
                                                        setValue(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date()));

                                                Toast.makeText(ctx, "Barang Berhasil Dikirim!", Toast.LENGTH_SHORT).show();
                                                ((MainActivity)ctx).setFragment(new TransactionFragment());
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
                                });

                            }
                            else if(temp.isSent() && !temp.isReceived()){ // Sudah dikirim, tepi belum diterima
                                holder.status.setText("Menunggu Penerimaan");
                                holder.status.setClickable(false);
                            }
                            else if(temp.isSent() && temp.isReceived()){ // Sudah dikirim dan sudah diterima
                                holder.status.setText("Transaksi Selesai!");
                                holder.status.setClickable(false);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }

        strf.child(imgRef).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                new loadImage().execute(uri.toString());
            }

            class loadImage extends AsyncTask<String, String, String> {
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
                                holder.storeImg.setImageBitmap(pic);
                            }
                        });
                    } catch (IOException e){
                        e.printStackTrace();
                    }

                    return null;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cscList.size();
    }

    class CartStoreHolder extends RecyclerView.ViewHolder{
        ImageView storeImg;
        TextView storeTitle, storeTotal, nomorResi;
        RecyclerView storeItems;
        Button status;

        public CartStoreHolder(@NonNull View itemView) {
            super(itemView);
            storeImg    = itemView.findViewById(R.id.cart_store_card_item_image);
            storeTitle  = itemView.findViewById(R.id.cart_store_card_name);
            storeTotal  = itemView.findViewById(R.id.cart_store_card_total);
            storeItems  = itemView.findViewById(R.id.cart_store_card_items);
            status      = itemView.findViewById(R.id.cart_store_card_status_btn);
            nomorResi   = itemView.findViewById(R.id.resi_store_transaction);
        }
    }
}
