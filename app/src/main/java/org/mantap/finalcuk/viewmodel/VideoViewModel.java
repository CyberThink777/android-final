package org.mantap.finalcuk.viewmodel;

import android.app.Application;
import android.content.ContentUris;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
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
    private final MutableLiveData<List<Video>> videoList = new MutableLiveData<>();

    public VideoViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        updateList();
    }

    private void updateList() {
        CompletableFuture.runAsync(() -> {
            //noinspection deprecation
            String[] projection = new String[]{
                    MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.DISPLAY_NAME,
                    MediaStore.Video.Media.SIZE,
                    MediaStore.Video.Media.DATE_ADDED,
                    MediaStore.Video.Media.DATA //Deprecated in API 29! only use this to get file absolute path!
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
                    int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
                    int dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED);
                    //noinspection deprecation
                    int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);

                    while (cursor.moveToNext()) {
                        long id = cursor.getLong(idColumn);
                        String name = cursor.getString(nameColumn);
                        int size = cursor.getInt(sizeColumn);
                        long date = cursor.getLong(dateColumn) * 1000;
                        String filePath = cursor.getString(dataColumn);
                        Uri contentUri = ContentUris.withAppendedId(
                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);

                        int duration;
                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                        retriever.setDataSource(application.getApplicationContext(), contentUri);
                        duration = Integer.parseInt(
                                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                        retriever.release();
                        tmp.add(new Video(contentUri, name, duration, size, date, filePath));
                    }
                    videoList.postValue(tmp);
                }
            }
        });
    }

    public LiveData<List<Video>> getVideoList() {
        return videoList;
    }

    public CompletableFuture<Boolean> remove(Video video) {
        return CompletableFuture.supplyAsync(() ->
                application
                        .getApplicationContext()
                        .getContentResolver()
                        .delete(video.getUri(), null, null))
                .thenApplyAsync(integer -> {
                    if (integer > 0) {
                        updateList();
                        return true;
                    } else return false;
                });
    }
}
