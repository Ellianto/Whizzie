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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.Activity.CartActivity;

public class CartCardAdapter extends RecyclerView.Adapter<CartCardAdapter.CartCardHolder> {

    Context ctx;
    List<CartCard> ccList;

    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();
    StorageReference strf = FirebaseStorage.getInstance().getReference();

    public CartCardAdapter(Context ctx, List<CartCard> ccList) {
        this.ctx = ctx;
        this.ccList = ccList;
    }

    @NonNull
    @Override
    public CartCardHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.card_store_cart, null);
        CartCardHolder holder = new CartCardHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CartCardHolder viewHolder, int i) {
        final CartCard temp = ccList.get(i);

        viewHolder.tvPrice.setText(String.valueOf(temp.getItemPrice()));
        viewHolder.edtQty.setText(String.valueOf(temp.getItemQty()));
        viewHolder.tvName.setText(temp.getItemName());

        // Set Item Picture
        String imageRefPath;

        if(temp.getImagePath().isEmpty()) imageRefPath = "whizzie_assets/empty/empty.jpg";
        else imageRefPath = "products/" + temp.getImagePath();

        strf.child(imageRefPath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                new loadImage().execute(uri.toString());
            }

            // Cara Fetch Image from Firebase Storage
            // Operasi-operasi network harus dilakukan di thread berbeda
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
                                viewHolder.imgProduct.setImageBitmap(pic);
                            }
                        });
                    } catch (IOException e){
                        e.printStackTrace();
                    }

                    return null;
                }
            }
        });

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("Reset Passworc");
                builder.setMessage("Are you sure you want to remove this from your cart?");
                builder.setCancelable(true);

                builder.setPositiveButton("Remove Item from Cart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    final DialogInterface editPassword = dialog;

                    dbrf.child("cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(temp.getItemKey()).removeValue();

                        ((CartActivity) ctx).finish();
                        ctx.startActivity(((CartActivity) ctx).getIntent());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {dialog.cancel();}
                });

                builder.show();
            }
        });

        viewHolder.edtQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String qty = viewHolder.edtQty.getText().toString();

                if(qty.isEmpty())
                    dbrf.child("cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(temp.getItemKey()).setValue(0);
                else
                    dbrf.child("cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(temp.getItemKey()).setValue(Long.valueOf(qty));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public int getItemCount() {
        return ccList.size();
    }

    class CartCardHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvPrice;
        EditText edtQty;
        ImageView imgProduct;
        ImageButton deleteButton;

        public CartCardHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.cart_store_card_item_name);
            tvPrice = itemView.findViewById(R.id.cart_store_card_item_price);
            edtQty = itemView.findViewById(R.id.cart_card_item_qty);
            imgProduct = itemView.findViewById(R.id.cart_card_item_image);
            deleteButton = itemView.findViewById(R.id.cart_card_delete_button);
        }
    }
}
