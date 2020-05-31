package org.mantap.finalcuk;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MediaPlayerFragment extends Fragment {
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private Uri[] mediaUris;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaUris = (Uri[]) requireArguments().getParcelableArray("mediaUris");
        currentWindow = requireArguments().getInt("curPos");
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
        bottomNav.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
        bottomNav.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_media_player, container, false);
        playerView = v.findViewById(R.id.media_player_view);
        return v;
    }

    private void initialize() {
        player = new SimpleExoPlayer.Builder(requireContext()).build();
        playerView.setPlayer(player);
        MediaSource mediaSource = buildMediaSource(mediaUris);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare(mediaSource, false, false);
    }

    private MediaSource buildMediaSource(Uri[] uris) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(requireContext(), "musicplayer");
        ProgressiveMediaSource.Factory mediaSourceFactory = new ProgressiveMediaSource.Factory(dataSourceFactory);
        ConcatenatingMediaSource mediaSource = new ConcatenatingMediaSource();
        for (Uri uri : uris) {
            mediaSource.addMediaSource(mediaSourceFactory.createMediaSource(uri));
        }
        return mediaSource;
    }

    private void release() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
    }

    @Override
    public void onStop() {
        super.onStop();
        release();
    }
}
