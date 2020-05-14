package org.mantap.finalcuk.model;

import android.net.Uri;

import java.util.Locale;

public class Video {
    private final Uri uri;
    private final String title;
    private final int duration;
    private final int size;
    private final String dateAdded;

    public Video(Uri uri, String title, int duration, int size, String dateAdded) {
        this.uri = uri;
        this.title = title;
        this.duration = duration;
        this.size = size;
        this.dateAdded = dateAdded;
    }

    public Uri getUri() {
        return uri;
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        int duration = this.duration / 1000;
        int minute = duration / 60;
        int seconds = duration % 60;
        if (seconds < 10)
            return minute + ":0" + seconds;
        return minute + ":" + seconds;
    }

    public String getSize() {
        double size = ((double) this.size) / 1000000;
        return String.format(Locale.ENGLISH, "%.2f MB", size);
    }

    public String getDateAdded() {
        return dateAdded;
    }
}
