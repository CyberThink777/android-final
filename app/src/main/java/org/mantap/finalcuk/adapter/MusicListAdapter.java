package org.mantap.finalcuk.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import org.mantap.finalcuk.R;
import org.mantap.finalcuk.listener.CardItemEventListener;
import org.mantap.finalcuk.model.Music;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MusicListViewHolder> {
    private final LayoutInflater inflater;
    private List<Music> musicList;
    private final CardItemEventListener<Music> listener;

    public MusicListAdapter(Context context, CardItemEventListener<Music> listener) {
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MusicListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MusicListViewHolder(inflater.inflate(R.layout.musiclist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MusicListViewHolder holder, int position) {
        holder.bind(musicList.get(position), listener);
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

        void bind(Music music, CardItemEventListener<Music> listener) {
            title.setText(music.getTitle() + "");
            subtitle.setText(String.format("%s | %s", music.getArtist(), music.getDuration()));
            option.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(itemView.getContext(), option);
                popupMenu.inflate(R.menu.media_option);
                popupMenu.show();
                //Handle Details
                popupMenu.getMenu().getItem(1).setOnMenuItemClickListener(item -> onClickDetails(music));
                //Handle Delete
                popupMenu.getMenu().getItem(0).setOnMenuItemClickListener(item -> listener.onDelete(itemView, music));
            });
            itemView.setOnClickListener(v -> listener.onClick(v, music));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                try {
                    thumbnail.setImageBitmap(itemView.getContext()
                            .getApplicationContext()
                            .getContentResolver()
                            .loadThumbnail(music.getUri(), new Size(128, 128), null));
                } catch (IOException e) {
                    Log.e("MusicThumbnail", Objects.requireNonNull(e.getMessage()));
                }
            } else {
                Uri albumArt = music.getAlbumArt();
                if (albumArt != null)
                    thumbnail.setImageURI(music.getAlbumArt());
            }
        }

        private boolean onClickDetails(Music music) {
            new MaterialAlertDialogBuilder(itemView.getContext())
                    .setTitle(R.string.details)
                    .setMessage(music.toString()).show();
            return true;
        }
    }

    public void setMusicList(List<Music> musicList) {
        this.musicList = musicList;
        notifyDataSetChanged();
    }

}
