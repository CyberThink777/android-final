package org.mantap.finalcuk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.mantap.finalcuk.R;

import java.util.List;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MusicListViewHolder> {
    private LayoutInflater inflater;
    private List<Object> musicList;

    public MusicListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MusicListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MusicListViewHolder(inflater.inflate(R.layout.musiclist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MusicListViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class MusicListViewHolder extends RecyclerView.ViewHolder {
        private View itemView;

        public MusicListViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        void bind() {

        }
    }

    public void setMusicList(List<Object> musicList) {
        this.musicList = musicList;
        notifyDataSetChanged();
    }
}
