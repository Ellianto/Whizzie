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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.List;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.Activity.CartActivity;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemHolder> {

    Context ctx;
    List<CartItemCard> cicList;

    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();
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
        final CartItemCard temp = cicList.get(i);

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

        if(!ctx.getClass().equals(CartActivity.class)) viewHolder.deleteBtn.setVisibility(View.GONE);
        else {
            viewHolder.deleteBtn.setVisibility(View.VISIBLE);
            viewHolder.itemQty.setEnabled(false);

            viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setTitle("Remove Item From Cart");
                    builder.setMessage("Are you sure you want to remove this item?");
                    builder.setCancelable(true);

                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbrf.child("products").child(temp.getItemID()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    dbrf.child("cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                            child(dataSnapshot.child("uidUpProduct").getValue().toString()).
                                            child(dataSnapshot.getKey()).
                                            removeValue();

                                    Toast.makeText(ctx, "Removed item from cart", Toast.LENGTH_SHORT).show();
                                    ((CartActivity)ctx).finish();
                                    ctx.startActivity(((CartActivity)ctx).getIntent());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {}
                            });
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
    }

    @NonNull
    @Override
    public CartItemAdapter.CartItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.card_item_cart, null);
        CartItemAdapter.CartItemHolder holder = new CartItemAdapter.CartItemHolder(view);
        return holder;
    }

    class CartItemHolder extends RecyclerView.ViewHolder{
        ImageView itemImage;
        TextView itemName, itemPrice, itemQty;
        ImageButton deleteBtn;

        public CartItemHolder(@NonNull View itemView) {
            super(itemView);

            itemImage   = itemView.findViewById(R.id.cart_item_card_image);
            itemName    = itemView.findViewById(R.id.cart_item_card_name);
            itemPrice   = itemView.findViewById(R.id.cart_item_card_price);
            itemQty     = itemView.findViewById(R.id.cart_item_card_qty);
            deleteBtn   = itemView.findViewById(R.id.cart_item_card_delete_btn);
        }
    }
}
