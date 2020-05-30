package org.mantap.finalcuk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import org.mantap.finalcuk.adapter.VideoListAdapter;
import org.mantap.finalcuk.listener.CardItemEventListener;
import org.mantap.finalcuk.model.Video;
import org.mantap.finalcuk.viewmodel.VideoViewModel;

public class VideoListFragment extends Fragment implements CardItemEventListener<Video> {
    private VideoViewModel viewModel;
    private CoordinatorLayout coordinatorLayout;

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
        coordinatorLayout = v.findViewById(R.id.coordinatorLayout);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        viewModel.getVideoList().observe(getViewLifecycleOwner(), adapter::setVideoList);

        TextView noItemText = v.findViewById(R.id.no_item);
        viewModel.getVideoList().observe(getViewLifecycleOwner(), videos -> {
            if (videos.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                noItemText.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                noItemText.setVisibility(View.GONE);
            }
        });
        return v;
    }

    @Override
    public boolean onDelete(View view, Video media) {
        viewModel.remove(media).thenAcceptAsync(result -> {
            if (result) {
                Snackbar.make(coordinatorLayout, R.string.delete_success, Snackbar.LENGTH_SHORT)
                        .show();
            }
        });
        return true;
    }

    @Override
    public void onClick(View view, Video media) {
        NavGraphDirections.ActionToMediaPlayerFragment action =
                NavGraphDirections.actionToMediaPlayerFragment(media.getUri());
        Navigation.findNavController(view).navigate(action);
    }
}
