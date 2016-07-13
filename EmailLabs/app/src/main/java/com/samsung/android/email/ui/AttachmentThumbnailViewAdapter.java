package com.samsung.android.email.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.samsung.android.email.R;
import com.samsung.android.email.model.AttachmentFile;

/**
 * Created by nsbisht on 7/11/16.
 */
public class AttachmentThumbnailViewAdapter extends RecyclerView.Adapter<AttachmentThumbnailViewAdapter.ViewHolder> {

    private Context mContext;
    private AttachmentFile[] mAttachmentFiles;

    public AttachmentThumbnailViewAdapter(Context context, AttachmentFile[] files) {
        mContext = context;
        mAttachmentFiles = files;
    }

    @Override
    public int getItemCount() {
        return mAttachmentFiles.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView mThumbnailImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mThumbnailImageView = (ImageView) itemView.findViewById(R.id.attachment_thumbview);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mThumbnailImageView.setImageBitmap(mBitmaps[position]);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attachment_thumbnail, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.get
            }
        });
        return viewHolder;
    }
}

