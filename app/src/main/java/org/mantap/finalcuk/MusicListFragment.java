package org.mantap.finalcuk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.mantap.finalcuk.adapter.MusicListAdapter;
import org.mantap.finalcuk.model.Music;

import java.util.Arrays;

public class MusicListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_music, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.music_recycler_view);
        MusicListAdapter adapter = new MusicListAdapter(this.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        //TODO this is only placeholder
        adapter.setMusicList(Arrays.asList(new Music(123123), new Music(1), new Music(110)));
        return v;
    }
}
