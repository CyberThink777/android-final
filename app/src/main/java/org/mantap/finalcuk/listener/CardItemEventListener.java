package org.mantap.finalcuk.listener;

import android.view.View;

public interface CardItemEventListener<T> {
    public boolean onDelete(View view, T media);

    public void onClick(View view, T media);
}
