package veeresh.a3c.realm.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import veeresh.a3c.realm.R;
import veeresh.a3c.realm.database.RealmManager;
import veeresh.a3c.realm.models.Record;

/**
 * Created by Veeresh on 3/11/17.
 */

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {

    private ArrayList<Record> recordArrayList;
    private Context context;

    public FavouritesAdapter(ArrayList<Record> recordArrayList, Context context) {
        this.recordArrayList = recordArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fav, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.batsmanName.setText(recordArrayList.get(position).getName());
        Glide.with(context).load(recordArrayList.get(position).getImage()).into(holder.profilePicture);
        holder.favCheck.setChecked(recordArrayList.get(position).isFavourite());


        holder.favCheck.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (!checkBox.isChecked()) {
                RealmManager.recordsDao().removeFromFavorites(recordArrayList.get(position));
                recordArrayList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());

            }

        });
    }

    @Override
    public int getItemCount() {
        return recordArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.profile_picture)
        ImageView profilePicture;
        @BindView(R.id.batsman_name)
        TextView batsmanName;
        @BindView(R.id.check_fav)
        CheckBox favCheck;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
