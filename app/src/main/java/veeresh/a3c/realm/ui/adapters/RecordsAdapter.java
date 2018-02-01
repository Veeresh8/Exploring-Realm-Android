package veeresh.a3c.realm.ui.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import veeresh.a3c.realm.R;
import veeresh.a3c.realm.database.RealmManager;
import veeresh.a3c.realm.events.MyEvents;
import veeresh.a3c.realm.models.Record;

/**
 * Created by Veeresh on 3/11/17.
 */
public class RecordsAdapter extends RealmRecyclerViewAdapter<Record, RecordsAdapter.ViewHolder> {

    private Context context;

    public RecordsAdapter(OrderedRealmCollection<Record> orderedRealmCollection, Context context) {
        super(orderedRealmCollection, true);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_records, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.batsmanName.setText(getData().get(position).getName());
        Glide.with(context).load(getData().get(position).getImage()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.profilePicture);
        holder.totalRuns.setText("Runs " + getData().get(position).getTotalScore());
        holder.totalMatches.setText("Matches " + getData().get(position).getMatchesPlayed());


        if (getData().get(position).isFavourite())
            holder.favCheck.setChecked(true);
        else
            holder.favCheck.setChecked(false);


        holder.favCheck.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                RealmManager.recordsDao().saveToFavorites(getData().get(position));
            } else {
                RealmManager.recordsDao().removeFromFavorites(getData().get(position));
            }

        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.profile_picture)
        ImageView profilePicture;
        @BindView(R.id.batsman_name)
        TextView batsmanName;
        @BindView(R.id.check_fav)
        CheckBox favCheck;
        @BindView(R.id.runs)
        TextView totalRuns;
        @BindView(R.id.matches)
        TextView totalMatches;
        @BindView(R.id.card_view)
        CardView cardView;


        @OnClick(R.id.card_view)
        public void navigate() {
            EventBus.getDefault().post(new MyEvents.RecordEvent(getData().get(getAdapterPosition()), profilePicture));
        }


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
