package org.mantap.finalcuk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.mantap.finalcuk.R;

import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoListViewHolder> {
    private LayoutInflater inflater;
    private List<Object> videoList;

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
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class VideoListViewHolder extends RecyclerView.ViewHolder {
        private final View itemView;

        public VideoListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        void bind() {

        }
    }

    public void setVideoList(List<Object> videoList) {
        this.videoList = videoList;
        notifyDataSetChanged();
    }
}
