package org.mantap.finalcuk.viewmodel;

import android.app.Application;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import org.mantap.finalcuk.model.Video;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class VideoViewModel extends AndroidViewModel {
    private final Application application;
    private MutableLiveData<List<Video>> videoList = new MutableLiveData<>();

    public VideoViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public CompletableFuture<Void> populate() {
        return CompletableFuture.runAsync(() -> {
            String[] projection = new String[]{
                    MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.DISPLAY_NAME,
                    MediaStore.Video.Media.DURATION,
                    MediaStore.Video.Media.SIZE,
                    MediaStore.Video.Media.DATE_ADDED,
            };
            String sortOrder = MediaStore.Video.Media.DISPLAY_NAME + " ASC";
            List<Video> tmp = new ArrayList<>();
            try (Cursor cursor = application.getApplicationContext().getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    projection, null, null, sortOrder
            )) {
                if (cursor != null) {
                    int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
                    int nameColumn =
                            cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                    int durationColumn =
                            cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
                    int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
                    int dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED);

                    while (cursor.moveToNext()) {
                        long id = cursor.getLong(idColumn);
                        String name = cursor.getString(nameColumn);
                        int duration = cursor.getInt(durationColumn);
                        int size = cursor.getInt(sizeColumn);
                        String date = cursor.getString(dateColumn);

                        Uri contentUri = ContentUris.withAppendedId(
                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
                        tmp.add(new Video(contentUri, name, duration, size, date));
                    }
                }
                videoList.postValue(tmp);
            }
        });
    }

    public LiveData<List<Video>> getVideoList() {
        populate();
        return videoList;
    }
}
