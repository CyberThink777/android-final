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
import org.mantap.finalcuk.adapter.VideoListAdapter;
import org.mantap.finalcuk.viewmodel.VideoViewModel;

public class VideoListFragment extends Fragment {
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
        VideoListAdapter adapter = new VideoListAdapter(this.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        viewModel.getVideoList().observe(getViewLifecycleOwner(), adapter::setVideoList);
        return v;
    }
}
