package id.ac.umn.whizzie.main.Transaction;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class TransactionCardAdapter extends RecyclerView.Adapter {
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    class TransactionCardHolder extends RecyclerView.ViewHolder{
        public TransactionCardHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
