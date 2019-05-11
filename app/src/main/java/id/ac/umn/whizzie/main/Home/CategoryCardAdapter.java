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
import android.util.Log;
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
import id.ac.umn.whizzie.main.Activity.MainActivity;
import id.ac.umn.whizzie.main.Activity.SearchActivity;

public class CategoryCardAdapter extends RecyclerView.Adapter<CategoryCardAdapter.CategoryCardHolder> {
    Context ctx;
    List<CategoryCard> lcc;
    StorageReference sr = FirebaseStorage.getInstance().getReference("whizzie_assets/categories/");

    public CategoryCardAdapter() {
    }

    public CategoryCardAdapter(Context ctx, List<CategoryCard> lcc) {
        this.lcc = lcc;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public CategoryCardHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.card_category, null);
        CategoryCardHolder holder = new CategoryCardHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryCardHolder view, int i) {
        CategoryCard temp = lcc.get(i);
        final String catKey = temp.getCategoryName();

        view.category_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, SearchActivity.class);
                if(((MainActivity)ctx).getMode())
                    i.putExtra("type", "wish");
                else i.putExtra("type", "product");

                i.putExtra("category", catKey);
                ctx.startActivity(i);
            }
        });


        // TODO : Gambar masih ajep-ajep, investigate
        sr.child(temp.getImageID()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                    new loadImage().execute(uri.toString());
            }

            // Cara Fetch Image from Firebase Storage
            // Operasi-operasi network harus dilakukan di thread berbeda
            class loadImage extends AsyncTask<String, String, String>{
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    Log.d("DEBUG", "Fetching...");
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
                                Log.d("DEBUG", "testing");
                                view.category_image_view.setImageBitmap(pic);
                            }
                        });
                    } catch (IOException e){
                        e.printStackTrace();
                    }

                    return null;
                }
            }
        });

        view.category_text_view.setText(temp.getCategoryName());
    }

    @Override
    public int getItemCount() {
        return lcc.size();
    }



    class CategoryCardHolder extends RecyclerView.ViewHolder{
        CardView category_card_view;
        ImageView category_image_view;
        TextView category_text_view;

        public CategoryCardHolder(@NonNull View itemView) {
            super(itemView);

            category_card_view = itemView.findViewById(R.id.category_card_view);
            category_image_view = itemView.findViewById(R.id.category_image_view);
            category_text_view = itemView.findViewById(R.id.category_text_view);
        }
    }
}
