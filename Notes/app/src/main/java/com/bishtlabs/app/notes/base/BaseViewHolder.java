package com.bishtlabs.app.notes.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by nsbisht on 6/21/18.
 */

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    protected abstract void onBind(int position);
}
