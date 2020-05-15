package org.mantap.finalcuk.adapter;

import android.content.Context;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.mantap.finalcuk.R;
import org.mantap.finalcuk.model.Music;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MusicListViewHolder> {
    private final LayoutInflater inflater;
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
        private final View itemView;
        private final ImageButton option;
        private final TextView title;
        private final TextView subtitle;
        private final ImageView thumbnail;

        public MusicListViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            title = itemView.findViewById(R.id.music_title);
            subtitle = itemView.findViewById(R.id.music_subtitle);
            option = itemView.findViewById(R.id.optionButton);
            thumbnail = itemView.findViewById(R.id.thumbnail);
        }

        void bind(Music music) {
            title.setText(music.getTitle() + "");
            subtitle.setText(String.format("%s | %s", music.getArtist(), music.getDuration()));
            option.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(itemView.getContext(), option);
                popupMenu.inflate(R.menu.media_option);
                popupMenu.show();
            });
            try {
                thumbnail.setImageBitmap(itemView.getContext()
                        .getApplicationContext()
                        .getContentResolver()
                        .loadThumbnail(music.getUri(), new Size(128, 128), null));
            } catch (IOException e) {
                Log.e("MusicThumbnail", Objects.requireNonNull(e.getMessage()));
            }
        }
    }

    public void setMusicList(List<Music> musicList) {
        this.musicList = musicList;
        notifyDataSetChanged();
    }
}
