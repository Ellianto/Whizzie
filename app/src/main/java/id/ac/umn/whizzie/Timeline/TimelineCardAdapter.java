package id.ac.umn.whizzie.Timeline;

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

public class TimelineCardAdapter extends RecyclerView.Adapter<TimelineCardAdapter.TimelineItemsHolder> {

    private Context ctx;
    List<TimelineCard> tiList;

    public TimelineCardAdapter(Context ctx, List<TimelineCard> tiList) {
        this.ctx = ctx;
        this.tiList = tiList;
    }

    @NonNull
    @Override
    public TimelineItemsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.card_timeline, null);
        TimelineCardAdapter.TimelineItemsHolder holder = new TimelineCardAdapter.TimelineItemsHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineItemsHolder viewHolder, int i) {
        TimelineCard temp = tiList.get(i);
        // TODO : Re-implement this and fix the naming
    }

    @Override
    public int getItemCount() {
        return tiList.size();
    }

    class TimelineItemsHolder extends RecyclerView.ViewHolder{
        ImageView profPic, wishImg;
        TextView tvDispName, tvWishTitle, tvOfferCount;

        public TimelineItemsHolder(@NonNull View itemView) {
            super(itemView);

            profPic = itemView.findViewById(R.id.timeline_card_profile_picture);
            wishImg = itemView.findViewById(R.id.timeline_card_image);

            tvDispName = itemView.findViewById(R.id.timeline_card_display_name);
            tvOfferCount = itemView.findViewById(R.id.timeline_offer_count);
            tvWishTitle = itemView.findViewById(R.id.timeline_card_title);
        }
    }
}
