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
import org.mantap.finalcuk.model.Video;

import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoListViewHolder> {
    private LayoutInflater inflater;
    private List<Video> videoList;

    public VideoListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public VideoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoListViewHolder(inflater.inflate(R.layout.videolist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoListViewHolder holder, int position) {
        holder.bind(videoList.get(position));
    }

    @Override
    public int getItemCount() {
        return videoList != null ? videoList.size() : 0;
    }

    public static class VideoListViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private ImageButton option;
        private TextView title;

        public VideoListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            title = itemView.findViewById(R.id.video_title);
            option = itemView.findViewById(R.id.optionButton);
        }

        void bind(Video video) {
            title.setText(video.getId() + "");
            option.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(itemView.getContext(), option);
                popupMenu.inflate(R.menu.media_option);
                popupMenu.show();
            });
        }
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
        notifyDataSetChanged();
    }
}
