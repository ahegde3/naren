package com.samsung.android.email.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.samsung.android.email.R;
import com.samsung.android.email.model.AttachmentFile;

import java.io.File;
import java.io.IOException;

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
        protected ImageView mPlayButton;

        public ViewHolder(View itemView) {
            super(itemView);
            mThumbnailImageView = (ImageView) itemView.findViewById(R.id.attachment_thumbview);
            mPlayButton = (ImageView) itemView.findViewById(R.id.play_button);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AttachmentFile file = mAttachmentFiles[position];
        Bitmap thumbNail = null;
        if(file != null) {
            if(file.getmFileType() == AttachmentFile.FILE_TYPE_IMAGE) {
                try {
                    thumbNail = ThumbnailUtils.extractThumbnail(BitmapFactory.
                            decodeStream(mContext.getAssets().open(file.getmFilePath())), 400, 400);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                holder.mPlayButton.setVisibility(View.GONE);
            } else if(file.getmFileType() == AttachmentFile.FILE_TYPE_VIDEO) {
//                Uri video = Uri.parse("android.resource://com.samsung.android.email/raw/" + file.getmFilePath());
//                String fileName = video.getEncodedPath();
//                Log.d("FILE**" , fileName);
                thumbNail = ThumbnailUtils.createVideoThumbnail(file.getmFilePath(), MediaStore.Images.Thumbnails.MINI_KIND);

                Canvas canvas = new Canvas();
                canvas.setBitmap(thumbNail);
                canvas.drawBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.play), new Matrix(), null);

                holder.mThumbnailImageView.setScaleType(ImageView.ScaleType.CENTER);
            }
        }
        if(thumbNail != null) {
            holder.mThumbnailImageView.setImageBitmap(thumbNail);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attachment_thumbnail, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //view.get
            }
        });
        return viewHolder;
    }
}

