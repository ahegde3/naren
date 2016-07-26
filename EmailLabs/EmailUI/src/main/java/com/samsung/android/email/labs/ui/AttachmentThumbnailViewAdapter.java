package com.samsung.android.email.labs.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.samsung.android.email.labs.R;
import com.samsung.android.email.labs.model.AttachmentFile;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.attachment_thumbview) ImageView mThumbnailImageView;
        @BindView(R.id.play_button) ImageView mPlayButton;

        protected IClickItem mIClick;

        public ViewHolder(View itemView, IClickItem clickItem) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mIClick = clickItem;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mIClick.itemClicked(getAdapterPosition());

        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AttachmentFile file = mAttachmentFiles[position];
        Bitmap thumbNail = null;
        if(file != null) {
            if(file.getmFileType() == AttachmentFile.FILE_TYPE_IMAGE) {
                thumbNail = ThumbnailUtils.extractThumbnail(BitmapFactory.
                        decodeFile(file.getmFilePath()), 400, 400);
                holder.mPlayButton.setVisibility(View.GONE);
            } else if(file.getmFileType() == AttachmentFile.FILE_TYPE_VIDEO) {
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
        ViewHolder viewHolder = new ViewHolder(view, new IClickItem() {
            @Override
            public void itemClicked(int position) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri resUri = Uri.parse("file://" + mAttachmentFiles[position].getmFilePath());
                if(mAttachmentFiles[position].getmFileType() == AttachmentFile.FILE_TYPE_VIDEO) {
                    intent.setDataAndType(resUri, "video/*");
                } else {
                    intent.setDataAndType(resUri, "image/*");
                }
                mContext.startActivity(intent);
            }
        });
        return viewHolder;
    }

    interface IClickItem {
        public void itemClicked(int position);
    }
}

