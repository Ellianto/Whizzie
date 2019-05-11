package id.ac.umn.whizzie.main.Home;

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
import id.ac.umn.whizzie.main.Activity.GenieProfileActivity;

public class FeaturedGenieCardAdapter extends RecyclerView.Adapter<FeaturedGenieCardAdapter.FeaturedGenieCardHolder> {

    private List<FeaturedGenieCard> fgList;
    private Context ctx;

    StorageReference strf = FirebaseStorage.getInstance().getReference();

    public FeaturedGenieCardAdapter() {

    }

    public FeaturedGenieCardAdapter(Context ctx, List<FeaturedGenieCard> fgList) {
        this.fgList = fgList;
        this.ctx = ctx;
    }

    @Override
    public void onBindViewHolder(@NonNull final FeaturedGenieCardHolder view, int i) {
        final FeaturedGenieCard temp  = fgList.get(i);
        final String genieID = temp.getGenie_uid();
        final String genieName = temp.getGenie_name();

        view.featured_genie_card_name.setText(temp.getGenie_name());

        view.featured_genie_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, GenieProfileActivity.class);

                i.putExtra("genieUid", genieID);
                i.putExtra("genieName",genieName);

                ctx.startActivity(i);
            }
        });

        view.featured_genie_card_key.setText(genieID);

        strf.child("users/" + temp.getImage_url()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                                view.featured_genie_card_image.setImageBitmap(pic);
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
    public FeaturedGenieCardHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.card_featured_genies, null);
        FeaturedGenieCardHolder holder = new FeaturedGenieCardHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return fgList.size();
    }

    class FeaturedGenieCardHolder extends RecyclerView.ViewHolder{
        CardView featured_genie_card_view;
        TextView featured_genie_card_name, featured_genie_card_key;
        ImageView featured_genie_card_image;

        public FeaturedGenieCardHolder(@NonNull View itemView) {
            super(itemView);

            featured_genie_card_view = itemView.findViewById(R.id.featured_genie_card_view);
            featured_genie_card_name = itemView.findViewById(R.id.featured_genie_card_title);
            featured_genie_card_image = itemView.findViewById(R.id.featured_genie_card_image);
            featured_genie_card_key = itemView.findViewById(R.id.featured_genie_key);
        }
    }
}
