package id.ac.umn.whizzie.Wishes;

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

public class WishesCardAdapter extends RecyclerView.Adapter<WishesCardAdapter.TimelineCardHolder> {

    private Context ctx;
    List<WishesCard> tcList;

    public WishesCardAdapter(Context ctx, List<WishesCard> tcList) {
        this.ctx = ctx;
        this.tcList = tcList;
    }

    @NonNull
    @Override
    public TimelineCardHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.card_wishes, null);
        WishesCardAdapter.TimelineCardHolder holder = new WishesCardAdapter.TimelineCardHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineCardHolder viewHolder, int i) {
        WishesCard temp = tcList.get(i);

        viewHolder.tvDispName.setText(temp.getProfileName());
        viewHolder.tvWishTitle.setText(temp.getWishesName());
    }

    @Override
    public int getItemCount() {
        return tcList.size();
    }

    class TimelineCardHolder extends RecyclerView.ViewHolder{
        ImageView profPic, wishImg;
        TextView tvDispName, tvWishTitle, tvOfferCount;

        public TimelineCardHolder(@NonNull View itemView) {
            super(itemView);

            profPic = itemView.findViewById(R.id.wishes_card_profile_picture);
            wishImg = itemView.findViewById(R.id.wishes_card_image);

            tvDispName = itemView.findViewById(R.id.wishes_card_display_name);
            tvOfferCount = itemView.findViewById(R.id.wishes_offer_count);
            tvWishTitle = itemView.findViewById(R.id.wishes_card_title);
        }
    }
}
