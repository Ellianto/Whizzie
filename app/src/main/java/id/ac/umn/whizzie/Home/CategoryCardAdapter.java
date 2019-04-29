package id.ac.umn.whizzie.Home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import id.ac.umn.whizzie.R;

public class CategoryCardAdapter extends RecyclerView.Adapter<CategoryCardAdapter.CategoryCardHolder> {
    private Context ctx;
    private List<CategoryCard> categoryCards;

    public CategoryCardAdapter(Context ctx, List<CategoryCard> categoryCards) {
        this.ctx = ctx;
        this.categoryCards = categoryCards;
    }

    @NonNull
    @Override
    public CategoryCardHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.category_card, null);
        CategoryCardHolder holder = new CategoryCardHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return categoryCards.size();
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryCardHolder categoryCardHolder, int i) {
        CategoryCard temp = categoryCards.get(i);

        categoryCardHolder.categoryName.setText(temp.getCategoryName());

        // TODO : Implement Image View Path Binding

        categoryCardHolder.categoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO : Implement Category Card View On Click
            }
        });
    }

    class CategoryCardHolder extends RecyclerView.ViewHolder{
        ImageView cateogryImage;
        TextView categoryName;
        CardView categoryCard;

        public CategoryCardHolder(@NonNull View itemView) {
            super(itemView);

            cateogryImage = itemView.findViewById(R.id.category_image_view);
            categoryName = itemView.findViewById(R.id.category_text_view);
            categoryCard = itemView.findViewById(R.id.category_card_view);
        }
    }
}
