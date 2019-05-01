package id.ac.umn.whizzie.Search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import id.ac.umn.whizzie.R;


public class SearchCardAdapter extends RecyclerView.Adapter<SearchCardAdapter.HomeCardHolder> {
    private Context ctx;
    private List<SearchCard> scList;

    public SearchCardAdapter(Context ctx, List<SearchCard> hcList) {
        this.ctx = ctx;
        this.scList = hcList;
    }

    @NonNull
    @Override
    public HomeCardHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.card_search, null);
        HomeCardHolder holder = new HomeCardHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return scList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull SearchCardAdapter.HomeCardHolder homeCardHolder, int i) {
        SearchCard temp = scList.get(i);

        homeCardHolder.tvHomeCardTitle.setText(temp.getCreatorName());

        homeCardHolder.btnHomeCardBigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : Implement Home Card Profile Button
//                IntentMovement im = new IntentMovement(ctx);

//                im.moveToTargetNormal(ProfileActivity.class);
            }
        });

        homeCardHolder.imagebuttonHomeCardSmallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : Implement Home Card Follow Button

            }
        });
    }

    class HomeCardHolder extends RecyclerView.ViewHolder{
        TextView tvHomeCardTitle;
        ImageView imageviewHomeCardImage;
        Button btnHomeCardBigButton;
        ImageButton imagebuttonHomeCardSmallButton;

        public HomeCardHolder(@NonNull View itemView) {
            super(itemView);

            tvHomeCardTitle = itemView.findViewById(R.id.featured_genie_card_title);
            imageviewHomeCardImage = itemView.findViewById(R.id.featured_genie_card_image);
            btnHomeCardBigButton = itemView.findViewById(R.id.featured_genie_card_button_follow);
        }
    }
}
