package id.ac.umn.whizzie.main.Cart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
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

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemHolder> {

    Context ctx;
    List<CartItemCard> cicList;

    StorageReference strf = FirebaseStorage.getInstance().getReference();

    public CartItemAdapter(Context ctx, List<CartItemCard> cicList) {
        this.ctx = ctx;
        this.cicList = cicList;
    }

    @Override
    public int getItemCount() {
        return cicList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final CartItemAdapter.CartItemHolder viewHolder, int i) {
        CartItemCard temp = cicList.get(i);

        viewHolder.itemName.setText(temp.getItemName());
        viewHolder.itemPrice.setText(String.valueOf(temp.getItemPrice()));
        viewHolder.itemQty.setText(String.valueOf(temp.getItemQty()));

        String imgRef = "whizzie_assets/empty/empty.jpg";
        if(!temp.getImageURL().isEmpty()) imgRef = "products/" + temp.getImageURL();

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
                                viewHolder.itemImage.setImageBitmap(pic);
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

    @NonNull
    @Override
    public CartItemAdapter.CartItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.card_address, null);
        CartItemAdapter.CartItemHolder holder = new CartItemAdapter.CartItemHolder(view);
        return holder;
    }

    class CartItemHolder extends RecyclerView.ViewHolder{
        ImageView itemImage;
        TextView itemName, itemPrice, itemQty;
        Button deleteBtn;

        public CartItemHolder(@NonNull View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.cart_item_card_image);
            itemName = itemView.findViewById(R.id.cart_item_card_name);
            itemPrice= itemView.findViewById(R.id.cart_item_card_price);
            itemQty = itemView.findViewById(R.id.cart_item_card_qty);
            deleteBtn = itemView.findViewById(R.id.cart_item_card_delete_btn);
        }
    }
}
