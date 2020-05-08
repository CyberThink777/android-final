package org.mantap.finalcuk;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MenuAdapter extends FragmentStateAdapter {

    public MenuAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1)
            return new VideoListFragment();
        else return new MusicListFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
