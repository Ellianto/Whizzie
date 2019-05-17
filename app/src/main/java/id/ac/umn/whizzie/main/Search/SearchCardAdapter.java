package id.ac.umn.whizzie.main.Search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.Activity.DetailActivity;
import id.ac.umn.whizzie.main.Activity.MainActivity;

public class SearchCardAdapter extends RecyclerView.Adapter<SearchCardAdapter.SearchCardHolder> {
    private Context ctx;
    private List<SearchCard> scList;

    DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();
    StorageReference strf = FirebaseStorage.getInstance().getReference();

    public SearchCardAdapter(Context ctx, List<SearchCard> hcList) {
        this.ctx = ctx;
        this.scList = hcList;
    }

    @NonNull
    @Override
    public SearchCardHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.card_search, null);
        SearchCardHolder holder = new SearchCardHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return scList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchCardHolder holder, int i) {
        final SearchCard temp = scList.get(i);

        holder.tvCardTitle.setText(temp.getCardName());
        holder.tvDispName.setText(temp.getProfileName());

        String priceText = "";

        if(temp.getCardPrice() != 0){
            priceText = String.valueOf(temp.getCardPrice());
        }

        holder.tvCardPrice.setText(priceText);
        holder.tvCardCount.setText(String.valueOf(temp.getCardCount()));
        holder.tvKey.setText(temp.getItemKey());

        final long itemKey = Long.parseLong(temp.getItemKey());

        String imagePath;

        View.OnClickListener ocl;

        if(temp.isProduct()){
            // Kalau sebuah product,
            imagePath = "products/";

            ocl = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ctx, DetailActivity.class);
                    Bundle b = new Bundle();

                    b.putBoolean("isProduct", true);
                    b.putLong("itemKey", itemKey);

                    i.putExtras(b);
                    ctx.startActivity(i);
                }
            };

        }
        else {
            // Kalau sebuah wish,
            imagePath = "wishes/";

            ocl = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ctx, DetailActivity.class);

                    i.putExtra("isProduct", false);
                    i.putExtra("itemKey", itemKey);

                    if(ctx.getClass().equals(MainActivity.class))
                        i.putExtra("genieMode", ((MainActivity)  ctx).getMode());
                    else if(ctx.getClass().equals(DetailActivity.class))
                        i.putExtra("genieMode", ((DetailActivity)ctx).getMode());

                    ctx.startActivity(i);
                }
            };
        }

        holder.cardHolder.setOnClickListener(ocl);

        // Set Item Picture
        String imageRefPath;

        if(temp.getCardImage().isEmpty()) imageRefPath = "whizzie_assets/empty/empty.jpg";
        else imageRefPath = imagePath + temp.getCardImage();

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
                                holder.cardPic.setImageBitmap(pic);
                            }
                        });
                    } catch (IOException e){
                        e.printStackTrace();
                    }

                    return null;
                }
            }
        });

        // Set Profile Picture
        String profileRefPath;

        if(temp.getProfilePic().isEmpty()) profileRefPath = "whizzie_assets/empty/empty_profile.jpg";
        else profileRefPath = "users/" + temp.getProfilePic();

        strf.child(profileRefPath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                                holder.profPic.setImageBitmap(pic);
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

    class SearchCardHolder extends RecyclerView.ViewHolder{
        CardView cardHolder;
        ImageView profPic, cardPic;
        TextView tvDispName, tvCardTitle, tvCardCount, tvCardPrice, tvKey;

        public SearchCardHolder(@NonNull View itemView) {
            super(itemView);

            cardHolder = itemView.findViewById(R.id.search_card_view);

            profPic = itemView.findViewById(R.id.search_card_profile_picture);
            cardPic = itemView.findViewById(R.id.search_card_image);

            tvDispName = itemView.findViewById(R.id.search_card_display_name);
            tvCardTitle = itemView.findViewById(R.id.search_card_title);
            tvCardCount = itemView.findViewById(R.id.search_badge_count);
            tvCardPrice = itemView.findViewById(R.id.search_card_price);

            tvKey = itemView.findViewById(R.id.search_card_key);
        }
    }
}
