package id.ac.umn.whizzie.Home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import id.ac.umn.whizzie.R;

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
        FeaturedGenieCard temp = fgList.get(i);

        view.featured_genie_card_name.setText(temp.getGenie_name());
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
        TextView featured_genie_card_name;
        ImageView featured_genie_card_image;
        Button featured_genie_card_follow;

        public FeaturedGenieCardHolder(@NonNull View itemView) {
            super(itemView);

            featured_genie_card_name = itemView.findViewById(R.id.featured_genie_card_title);
            featured_genie_card_image = itemView.findViewById(R.id.featured_genie_card_image);
            featured_genie_card_follow = itemView.findViewById(R.id.featured_genie_card_button_follow);
        }
    }
}
