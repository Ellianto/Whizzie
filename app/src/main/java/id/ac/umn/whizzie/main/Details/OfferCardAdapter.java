package id.ac.umn.whizzie.main.Details;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import id.ac.umn.whizzie.main.Activity.MainActivity;

public class OfferCardAdapter extends RecyclerView.Adapter<OfferCardAdapter.OfferCardHolder> {
    Context ctx;
    List<OfferCard> ocList;

    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();
    StorageReference strf = FirebaseStorage.getInstance().getReference();

    public OfferCardAdapter(Context ctx, List<OfferCard> ocList) {
        this.ctx = ctx;
        this.ocList = ocList;
    }

    @NonNull
    @Override
    public OfferCardHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View v = inflater.inflate(R.layout.card_offers, null);
        OfferCardHolder holder = new OfferCardHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final @NonNull OfferCardHolder holder, int i) {
        final OfferCard temp = ocList.get(i);

        holder.offerName.setText(temp.getCardName());
        holder.offerPrice.setText(String.valueOf(temp.getCardPrice()));

        String imgRef = "whizzie_assets/empty/empty.jpg";

        if(!temp.getCardImage().isEmpty())
            imgRef = "products/" + temp.getCardImage();

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
                                holder.offerImage.setImageBitmap(pic);
                            }
                        });
                    } catch (IOException e){
                        e.printStackTrace();
                    }

                    return null;
                }
            }
        });

        holder.offerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbrf.child("wishRelation").child(temp.getWishKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(temp.getProductKey())){
                            Toast.makeText(ctx, "You already Offered this product!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            dbrf.child("wishRelation").child(temp.getWishKey()).child(temp.getProductKey()).setValue(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date()));
                            dbrf.child("productRelation").child(temp.getProductKey()).child(temp.getWishKey()).setValue(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date()));
                            Toast.makeText(ctx, "Successfully Offered This Product!", Toast.LENGTH_SHORT).show();

                            ctx.startActivity(new Intent(ctx, MainActivity.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {return ocList.size();}

    class OfferCardHolder extends RecyclerView.ViewHolder{
        TextView offerPrice, offerName;
        ImageView offerImage;
        CardView offerCard;

        public OfferCardHolder(@NonNull View itemView) {
            super(itemView);

            offerImage = itemView.findViewById(R.id.offer_card_image);
            offerPrice = itemView.findViewById(R.id.offer_card_price);
            offerName  = itemView.findViewById(R.id.offer_card_title);
            offerCard  = itemView.findViewById(R.id.offer_card_view);
        }
    }
}
