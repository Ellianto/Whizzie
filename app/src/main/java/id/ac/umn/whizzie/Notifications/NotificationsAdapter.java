package id.ac.umn.whizzie.Notifications;

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

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationsHolder> {

    private Context ctx;
    private List<Notifications> notifList;

    public NotificationsAdapter(Context ctx, List<Notifications> notifList) {
        this.ctx = ctx;
        this.notifList = notifList;
    }

    @NonNull
    @Override
    public NotificationsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.notification_card, null);
        NotificationsAdapter.NotificationsHolder holder = new NotificationsAdapter.NotificationsHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsHolder viewHolder, int i) {
        Notifications temp = notifList.get(i);

        viewHolder.tvDesc.setText(temp.getDesc());
    }

    @Override
    public int getItemCount() {
        return notifList.size();
    }

    class NotificationsHolder extends RecyclerView.ViewHolder{
        ImageView profPic, notifImg;
        TextView tvDesc;

        public NotificationsHolder(@NonNull View itemView) {
            super(itemView);

            profPic = itemView.findViewById(R.id.imageviewNotificationsCardProfPic);
            notifImg = itemView.findViewById(R.id.imageviewNotificationsCardNotifPic);
            tvDesc = itemView.findViewById(R.id.tvNotificationsCardDesc);
        }
    }
}
