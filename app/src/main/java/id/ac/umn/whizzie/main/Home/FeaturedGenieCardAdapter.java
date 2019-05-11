package id.ac.umn.whizzie.main.Home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.Activity.GenieProfileActivity;

public class FeaturedGenieCardAdapter extends RecyclerView.Adapter<FeaturedGenieCardAdapter.FeaturedGenieCardHolder> {

    private List<FeaturedGenieCard> fgList;
    private Context ctx;

    public FeaturedGenieCardAdapter() {

    }

    public FeaturedGenieCardAdapter(Context ctx, List<FeaturedGenieCard> fgList) {
        this.fgList = fgList;
        this.ctx = ctx;
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedGenieCardHolder view, int i) {
        final FeaturedGenieCard temp  = fgList.get(i);
        final String genieID = temp.getGenie_uid();

        view.featured_genie_card_name.setText(temp.getGenie_name());

        view.featured_genie_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, GenieProfileActivity.class);

                Log.d("DEBUG", ((TextView)v.findViewById(R.id.featured_genie_key)).getText().toString());

                // TODO : Fix this
                i.putExtra("genieUid", ((TextView)v.findViewById(R.id.featured_genie_key)).getText().toString());

                ctx.startActivity(i);
            }
        });

        view.featured_genie_card_key.setText(genieID);

        // TODO : Implement fetch genie image via Firebase Storage

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
