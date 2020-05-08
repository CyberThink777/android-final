package org.mantap.finalcuk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainFragment extends Fragment {

    MenuAdapter adapter;
    ViewPager2 pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new MenuAdapter(this);
        pager = view.findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        new TabLayoutMediator(tabLayout, pager, (tab, position) -> {
            if (position == 0) {
                tab.setText(R.string.music_page_title);
                tab.setIcon(R.drawable.ic_music_note_white_24dp);
            }
            if (position == 1) {
                tab.setText(R.string.video_page_title);
                tab.setIcon(R.drawable.ic_movie_white_24dp);
            }
        }).attach();
    }
}
