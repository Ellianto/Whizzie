package id.ac.umn.whizzie.Search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import id.ac.umn.whizzie.R;


public class SearchCardAdapter extends RecyclerView.Adapter<SearchCardAdapter.SearchCardHolder> {
    private Context ctx;
    private List<SearchCard> scList;

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
    public void onBindViewHolder(@NonNull SearchCardAdapter.SearchCardHolder holder, int i) {
        SearchCard temp = scList.get(i);

        holder.tvCardTitle.setText(temp.getCardName());
        holder.tvDispName.setText(temp.getProfileName());
        holder.tvCardCount.setText(temp.getCardCount());
    }

    class SearchCardHolder extends RecyclerView.ViewHolder{
        ImageView profPic, cardPic;
        TextView tvDispName, tvCardTitle, tvCardCount;


        public SearchCardHolder(@NonNull View itemView) {
            super(itemView);

            profPic = itemView.findViewById(R.id.search_card_profile_picture);
            cardPic = itemView.findViewById(R.id.search_card_image);

            tvDispName = itemView.findViewById(R.id.search_card_display_name);
            tvCardTitle = itemView.findViewById(R.id.search_card_title);
            tvCardCount = itemView.findViewById(R.id.search_badge_count);
        }
    }
}
