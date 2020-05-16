package org.mantap.finalcuk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import org.mantap.finalcuk.adapter.VideoListAdapter;
import org.mantap.finalcuk.listener.CardItemEventListener;
import org.mantap.finalcuk.model.Video;
import org.mantap.finalcuk.viewmodel.VideoViewModel;

public class VideoListFragment extends Fragment implements CardItemEventListener<Video> {
    private VideoViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(VideoViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_video, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.video_recycler_view);
        VideoListAdapter adapter = new VideoListAdapter(this.getContext(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        viewModel.getVideoList().observe(getViewLifecycleOwner(), adapter::setVideoList);
        return v;
    }

    @Override
    public boolean onDelete(View view, Video media) {
        viewModel.remove(media).thenAcceptAsync(result -> {
            if (result) {
                Snackbar.make(view, R.string.delete_success, Snackbar.LENGTH_SHORT)
                        .show();
            }
        });
        return true;
    }

    @Override
    public void onClick(View view, Video media) {

    }
}
