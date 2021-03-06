package org.mantap.finalcuk.viewmodel;

import android.app.Application;
import android.content.ContentUris;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import org.mantap.finalcuk.model.Music;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MusicViewModel extends AndroidViewModel {
    private final Application application;
    private final MutableLiveData<List<Music>> musicList = new MutableLiveData<>();

    public MusicViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        updateList();
    }

    private void updateList() {
        CompletableFuture.runAsync(() -> {
            //noinspection deprecation
            String[] projection = new String[]{
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.SIZE,
                    MediaStore.Audio.Media.DATE_ADDED,
                    MediaStore.Audio.Media.DATA,//Deprecated in API 29! only use this to get file absolute path!
                    MediaStore.Audio.Media.ALBUM_ID
            };
            String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
            List<Music> tmp = new ArrayList<>();
            try (Cursor cursor = application.getApplicationContext().getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection, null, null, sortOrder
            )) {
                if (cursor != null) {
                    int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
                    int titleColumn =
                            cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
                    int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
                    int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
                    int dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED);
                    //noinspection deprecation
                    int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                    int albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);

                    while (cursor.moveToNext()) {
                        long id = cursor.getLong(idColumn);
                        String title = cursor.getString(titleColumn);
                        String artist = cursor.getString(artistColumn);
                        int size = cursor.getInt(sizeColumn);
                        long date = cursor.getLong(dateColumn) * 1000;
                        String filePath = cursor.getString(dataColumn);
                        Uri contentUri = ContentUris.withAppendedId(
                                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
                        Uri albumArtUri;
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                            albumArtUri = getAlbumArt(cursor.getInt(albumIdColumn));
                        } else albumArtUri = null;

                        int duration;
                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                        retriever.setDataSource(application.getApplicationContext(), contentUri);
                        duration = Integer.parseInt(
                                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                        retriever.release();
                        tmp.add(new Music(contentUri, title, artist, duration, size, date, filePath, albumArtUri));
                    }
                    musicList.postValue(tmp);
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    private Uri getAlbumArt(int id) {
        try (Cursor cursor = application.getApplicationContext().getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + " = ?",
                new String[]{String.valueOf(id)},
                null
        )) {
            if (cursor != null && cursor.moveToFirst()) {
                String albumArt = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                return Uri.parse(albumArt);
            } else return null;
        }
    }

    public LiveData<List<Music>> getMusicList() {
        return musicList;
    }

    public CompletableFuture<Boolean> remove(Music music) {
        return CompletableFuture.supplyAsync(() ->
                application
                        .getApplicationContext()
                        .getContentResolver()
                        .delete(music.getUri(), null, null))
                .thenApplyAsync(integer -> {
                    if (integer > 0) {
                        updateList();
                        return true;
                    } else return false;
                });
    }
}
