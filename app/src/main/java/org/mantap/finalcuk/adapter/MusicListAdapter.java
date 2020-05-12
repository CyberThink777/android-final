package org.mantap.finalcuk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.mantap.finalcuk.R;
import org.mantap.finalcuk.model.Music;

import java.util.List;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MusicListViewHolder> {
    private LayoutInflater inflater;
    private List<Music> musicList;

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
        holder.bind(musicList.get(position));
    }

    @Override
    public int getItemCount() {
        return musicList != null ? musicList.size() : 0;
    }

    public static class MusicListViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private ImageButton option;
        private TextView title;

        public MusicListViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            title = itemView.findViewById(R.id.music_title);
            option = itemView.findViewById(R.id.optionButton);
        }

        void bind(Music music) {
            title.setText(music.getId() + "");
            option.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(itemView.getContext(), option);
                popupMenu.inflate(R.menu.media_option);
                popupMenu.show();
            });
        }
    }

    public void setMusicList(List<Music> musicList) {
        this.musicList = musicList;
        notifyDataSetChanged();
    }
}
