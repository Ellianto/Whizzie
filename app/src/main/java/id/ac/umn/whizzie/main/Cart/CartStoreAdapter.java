package id.ac.umn.whizzie.main.Cart;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.Activity.CartActivity;
import id.ac.umn.whizzie.main.Activity.MainActivity;

public class CartStoreAdapter extends RecyclerView.Adapter<CartStoreAdapter.CartStoreHolder> {

    Context ctx;
    List<CartStoreCard> cscList;

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
        CartStoreCard temp = cscList.get(i);

        holder.storeTitle.setText(temp.getStoreName());
        holder.storeTotal.setText(String.valueOf(temp.getStoreTotal()));

        holder.storeItems.setAdapter(new CartItemAdapter(ctx, temp.getCicList()));

        String imgRef = "whizzie_assets/empty/empty_profile.jpg";

        if(!temp.getStoreImage().isEmpty()) imgRef = "users/" + temp.getStoreImage();

        if(ctx.getClass().equals(CartActivity.class))
            holder.status.setVisibility(View.GONE);
        else{
            holder.status.setVisibility(View.VISIBLE);

            holder.status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Implement this
                }
            });
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
        TextView storeTitle, storeTotal;
        RecyclerView storeItems;
        Button status;

        public CartStoreHolder(@NonNull View itemView) {
            super(itemView);
            storeImg = itemView.findViewById(R.id.cart_store_card_item_image);
            storeTitle = itemView.findViewById(R.id.cart_store_card_store_name);
            storeTotal = itemView.findViewById(R.id.cart_store_card_total);
            storeItems = itemView.findViewById(R.id.cart_store_card_items);
            status = itemView.findViewById(R.id.cart_store_card_status_btn);

            storeItems.setHasFixedSize(true);
            storeItems.setLayoutManager(new GridLayoutManager(ctx, 1, GridLayoutManager.VERTICAL, false));
        }
    }
}
