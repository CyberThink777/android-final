package org.mantap.finalcuk.adapter;

import android.content.Context;
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
import org.mantap.finalcuk.model.Video;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoListViewHolder> {
    private final LayoutInflater inflater;
    private List<Video> videoList;
    private final CardItemEventListener<Video> listener;

    public VideoListAdapter(Context context, CardItemEventListener<Video> listener) {
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public VideoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoListViewHolder(inflater.inflate(R.layout.videolist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoListViewHolder holder, int position) {
        holder.bind(videoList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return videoList != null ? videoList.size() : 0;
    }

    public static class VideoListViewHolder extends RecyclerView.ViewHolder {
        private final View itemView;
        private final ImageButton option;
        private final TextView title;
        private final TextView subtitle;
        private final ImageView thumbnail;

        public VideoListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            title = itemView.findViewById(R.id.video_title);
            subtitle = itemView.findViewById(R.id.video_subtitle);
            option = itemView.findViewById(R.id.optionButton);
            thumbnail = itemView.findViewById(R.id.thumbnail);
        }

        void bind(Video video, CardItemEventListener<Video> listener) {
            title.setText(video.getTitle() + "");
            subtitle.setText(String.format("%s | %s", video.getSize(), video.getDuration()));
            option.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(itemView.getContext(), option);
                popupMenu.inflate(R.menu.media_option);
                popupMenu.show();

                //Handle Details
                popupMenu.getMenu().getItem(1).setOnMenuItemClickListener(item -> onClickDetails(video));
                //Handle Delete
                popupMenu.getMenu().getItem(0).setOnMenuItemClickListener(item -> listener.onDelete(itemView, video));
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                try {
                    thumbnail.setImageBitmap(itemView.getContext()
                            .getApplicationContext()
                            .getContentResolver()
                            .loadThumbnail(video.getUri(), new Size(128, 128), null));
                } catch (IOException e) {
                    Log.e("VideoThumbnail", Objects.requireNonNull(e.getMessage()));
                }
            }
        }

        private boolean onClickDetails(Video video) {
            new MaterialAlertDialogBuilder(itemView.getContext())
                    .setTitle(R.string.details)
                    .setMessage(video.toString()).show();
            return true;
        }
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
        notifyDataSetChanged();
    }
}
