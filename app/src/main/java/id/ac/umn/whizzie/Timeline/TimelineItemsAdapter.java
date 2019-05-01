package id.ac.umn.whizzie.Timeline;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import id.ac.umn.whizzie.R;

public class TimelineItemsAdapter extends RecyclerView.Adapter<TimelineItemsAdapter.TimelineItemsHolder> {

    private Context ctx;
    List<TimelineItems> tiList;

    public TimelineItemsAdapter(Context ctx, List<TimelineItems> tiList) {
        this.ctx = ctx;
        this.tiList = tiList;
    }

    @NonNull
    @Override
    public TimelineItemsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.card_timeline, null);
        TimelineItemsAdapter.TimelineItemsHolder holder = new TimelineItemsAdapter.TimelineItemsHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineItemsHolder viewHolder, int i) {
        TimelineItems temp = tiList.get(i);

        viewHolder.tvProfName.setText(temp.getProfileName());
        viewHolder.tvProdDesc.setText(temp.getProductDesc());
        viewHolder.tvProdPrice.setText(String.valueOf(temp.getProductPrice()));

        viewHolder.cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Adds to cart
            }
        });
    }

    @Override
    public int getItemCount() {
        return tiList.size();
    }

    class TimelineItemsHolder extends RecyclerView.ViewHolder{
        ImageView profPic, productImg, productImg2;
        TextView tvProfName, tvProdDesc, tvProdPrice;
        ImageButton cartButton;

        public TimelineItemsHolder(@NonNull View itemView) {
            super(itemView);

            profPic = itemView.findViewById(R.id.timeline_items_display_picture);
            productImg = itemView.findViewById(R.id.timeline_items_product_image_1);
            productImg2 = itemView.findViewById(R.id.timeline_items_product_image_2);
            tvProfName = itemView.findViewById(R.id.timeline_items_display_name);
            tvProdDesc = itemView.findViewById(R.id.timeline_items_product_desc);
            tvProdPrice = itemView.findViewById(R.id.timeline_items_product_price);
            cartButton = itemView.findViewById(R.id.timeline_items_button_cart);
        }
    }
}
