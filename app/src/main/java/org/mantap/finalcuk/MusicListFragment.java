package org.mantap.finalcuk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import org.mantap.finalcuk.adapter.MusicListAdapter;
import org.mantap.finalcuk.listener.CardItemEventListener;
import org.mantap.finalcuk.model.Music;
import org.mantap.finalcuk.viewmodel.MusicViewModel;

public class MusicListFragment extends Fragment implements CardItemEventListener<Music> {
    private MusicViewModel viewModel;
    private CoordinatorLayout coordinatorLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MusicViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_music, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.music_recycler_view);
        MusicListAdapter adapter = new MusicListAdapter(this.getContext(), this);
        coordinatorLayout = v.findViewById(R.id.coordinatorLayout);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        viewModel.getMusicList().observe(getViewLifecycleOwner(), adapter::setMusicList);
        return v;
    }

    @Override
    public boolean onDelete(View view, Music media) {
        viewModel.remove(media).thenAcceptAsync(result -> {
            if (result) {
                Snackbar.make(coordinatorLayout, R.string.delete_success, Snackbar.LENGTH_SHORT)
                        .show();
            }
        });
        return true;
    }

    @Override
    public void onClick(View view, Music media) {

    }
}
