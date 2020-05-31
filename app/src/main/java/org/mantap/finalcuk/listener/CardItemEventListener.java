package org.mantap.finalcuk.listener;

import android.view.View;

public interface CardItemEventListener<T> {
    boolean onDelete(View view, T media);

    void onClick(View view, int position);
}
