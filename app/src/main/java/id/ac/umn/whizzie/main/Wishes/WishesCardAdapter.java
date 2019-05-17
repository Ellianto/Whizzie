package id.ac.umn.whizzie.main.Wishes;

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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.Activity.DetailActivity;
import id.ac.umn.whizzie.main.Activity.MainActivity;

public class WishesCardAdapter extends RecyclerView.Adapter<WishesCardAdapter.WishesCardHolder> {

    private Context ctx;
    List<WishesCard> wcList;

    StorageReference strf  = FirebaseStorage.getInstance().getReference();

    public WishesCardAdapter(Context ctx, List<WishesCard> wcList) {
        this.ctx = ctx;
        this.wcList = wcList;
    }

    @NonNull
    @Override
    public WishesCardHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.card_wishes, null);
        WishesCardAdapter.WishesCardHolder holder = new WishesCardAdapter.WishesCardHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final WishesCardHolder viewHolder, int i) {
        final WishesCard temp = wcList.get(i);

        viewHolder.tvDispName.setText(temp.getProfileName());
        viewHolder.tvWishTitle.setText(temp.getWishesName());
        viewHolder.tvOfferCount.setText(String.valueOf(temp.getWishesCount()));
        viewHolder.tvDesc.setText(temp.getWishesDesc());
        viewHolder.tvWishKey.setText(temp.getWishKey());
        viewHolder.tvUserKey.setText(temp.getUserKey());

        viewHolder.cardViewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, DetailActivity.class);

                i.putExtra("isProduct", false);
                i.putExtra("itemKey", temp.getWishKey());
                i.putExtra("genieMode", ((MainActivity)  ctx).getMode());

                ctx.startActivity(i);
            }
        });

        // Fetch Wish Image
        strf.child("wishes/" + temp.getWishPic()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                new loadWishImage().execute(uri.toString());
            }

            // Cara Fetch Image from Firebase Storage
            // Operasi-operasi network harus dilakukan di thread berbeda
            class loadWishImage extends AsyncTask<String, String, String> {
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
                                viewHolder.wishImg.setImageBitmap(pic);
                            }
                        });
                    } catch (IOException e){
                        e.printStackTrace();
                    }

                    return null;
                }
            }
        });

        // Fetch User Profile Picture
        strf.child("users/" + temp.getUserKey() + "/profile.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                new loadProfilePicture().execute(uri.toString());
            }

            // Cara Fetch Image from Firebase Storage
            // Operasi-operasi network harus dilakukan di thread berbeda
            class loadProfilePicture extends AsyncTask<String, String, String> {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected String doInBackground(String... strings) {
                    try{
                        URL url = new URL(strings[0]);
                        final Bitmap profile_picture = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                        Handler h = new Handler(Looper.getMainLooper());

                        // Operasi yang mengubah View harus di Main Thread
                        // Karena akan dijalankan di dalam fragment, pakai handler
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                viewHolder.profPic.setImageBitmap(profile_picture);
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
        return wcList.size();
    }

    class WishesCardHolder extends RecyclerView.ViewHolder{
        CardView cardViewHolder;
        ImageView profPic, wishImg;
        TextView tvDispName, tvWishTitle, tvOfferCount, tvDesc, tvWishKey, tvUserKey;

        public WishesCardHolder(@NonNull View itemView){
            super(itemView);

            cardViewHolder = itemView.findViewById(R.id.card_wishes);

            profPic = itemView.findViewById(R.id.wishes_card_profile_picture);
            wishImg = itemView.findViewById(R.id.wishes_card_image);

            tvDispName = itemView.findViewById(R.id.wishes_card_display_name);
            tvOfferCount = itemView.findViewById(R.id.wishes_offer_count);
            tvWishTitle = itemView.findViewById(R.id.wishes_card_title);
            tvDesc = itemView.findViewById(R.id.wishes_card_desc);

            tvWishKey = itemView.findViewById(R.id.wishes_card_wishKey);
            tvUserKey = itemView.findViewById(R.id.wishes_card_userKey);
        }
    }
}
