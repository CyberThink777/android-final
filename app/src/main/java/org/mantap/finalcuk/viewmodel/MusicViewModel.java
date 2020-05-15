package org.mantap.finalcuk.viewmodel;

import android.app.Application;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
    }

    @RequiresApi(api = Build.VERSION_CODES.Q) //TODO FIX This!
    private void populate() {
        CompletableFuture.runAsync(() -> {
            String[] projection = new String[]{
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.SIZE,
                    MediaStore.Audio.Media.DATE_ADDED,
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
                    int durationColumn =
                            cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
                    int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
                    int dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED);

                    while (cursor.moveToNext()) {
                        long id = cursor.getLong(idColumn);
                        String title = cursor.getString(titleColumn);
                        String artist = cursor.getString(artistColumn);
                        int duration = cursor.getInt(durationColumn);
                        int size = cursor.getInt(sizeColumn);
                        long date = cursor.getLong(dateColumn) * 1000;

                        Uri contentUri = ContentUris.withAppendedId(
                                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
                        tmp.add(new Music(contentUri, title, artist, duration, size, date));
                    }
                }
                musicList.postValue(tmp);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public LiveData<List<Music>> getMusicList() {
        populate();
        return musicList;
    }
}
