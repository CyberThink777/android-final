package org.mantap.finalcuk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.mantap.finalcuk.adapter.MusicListAdapter;
import org.mantap.finalcuk.viewmodel.MusicViewModel;

public class MusicListFragment extends Fragment {
    private MusicViewModel viewModel;

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
        MusicListAdapter adapter = new MusicListAdapter(this.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        viewModel.getMusicList().observe(getViewLifecycleOwner(), adapter::setMusicList);
        return v;
    }
}
