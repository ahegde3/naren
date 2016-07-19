package com.samsung.android.email.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.samsung.android.email.R;
import com.samsung.android.email.model.AttachmentFile;
import com.samsung.android.email.model.Email;
import com.samsung.android.email.ui.letterInIcon.util.ColorGenerator;
import com.samsung.android.email.ui.letterInIcon.util.TextShapeDrawable;
import com.samsung.android.email.utils.Ulitily;

/**
 * Created by nsbisht on 5/31/16.
 */
public class EmailListAdapter extends RecyclerView.Adapter<EmailListAdapter.ViewHolder> {

    private Email[] dataSource;
    private TextShapeDrawable.IBuilder mDrawableBuilder;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private Context mContext;

    public EmailListAdapter(Context context, Email[] emails) {
        mContext = context;
        dataSource = emails;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView mFromAddressIcon;
        protected TextView mFromAddressView;
        protected TextView mDateView;
        protected TextView mSubjectView;
        protected TextView mBodyView;
        protected RecyclerView mAttachmentThumbnailRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            mFromAddressIcon = (ImageView) itemView.findViewById(R.id.from_address_image_view);
            mFromAddressView = (TextView) itemView.findViewById(R.id.from_address_label);
            mDateView = (TextView) itemView.findViewById(R.id.date_label);
            mSubjectView = (TextView) itemView.findViewById(R.id.subject_label);
            mBodyView = (TextView) itemView.findViewById(R.id.body_label);
            mAttachmentThumbnailRecyclerView = (RecyclerView) itemView.findViewById(R.id.attachment_thumbnail_recycler_view);

        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextShapeDrawable drawable = mDrawableBuilder.
                build(String.valueOf(dataSource[position].getmFromAddress().charAt(0)),
                        mColorGenerator.getColor(dataSource[position].getmFromAddress()));
        holder.mFromAddressIcon.setImageDrawable(drawable);
        holder.mFromAddressView.setText(dataSource[position].getmFromAddress());
        holder.mDateView.setText(Ulitily.getTime(dataSource[position].getmDate()));
        holder.mSubjectView.setText(dataSource[position].getmSubject());
        holder.mBodyView.setText(dataSource[position].getmBody());
        if(position == 4) {
            AttachmentFile[] attachmentFiles = new AttachmentFile[]{
                    new AttachmentFile("/sdcard/pic2.jpg", AttachmentFile.FILE_TYPE_IMAGE),
                    new AttachmentFile("/sdcard/Cars.mp4", AttachmentFile.FILE_TYPE_VIDEO),
                    new AttachmentFile("/sdcard/pic4.jpg", AttachmentFile.FILE_TYPE_IMAGE),
                    new AttachmentFile("/sdcard/download.jpg", AttachmentFile.FILE_TYPE_IMAGE),
                    new AttachmentFile("/sdcard/Vid.mp4", AttachmentFile.FILE_TYPE_VIDEO)};
            dataSource[position].setmAttachmentFiles(attachmentFiles);
        }
        if(dataSource[position].getmAttachmentFiles() != null &&
                dataSource[position].getmAttachmentFiles().length > 0) {
            AttachmentThumbnailViewAdapter attachmentListAdapter =
                    new AttachmentThumbnailViewAdapter(mContext, dataSource[position].getmAttachmentFiles());
            holder.mAttachmentThumbnailRecyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
            holder.mAttachmentThumbnailRecyclerView.setLayoutManager(layoutManager);
            holder.mAttachmentThumbnailRecyclerView.setAdapter(attachmentListAdapter);
        } else {
            holder.mAttachmentThumbnailRecyclerView.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return dataSource.length;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        mDrawableBuilder = TextShapeDrawable.builder().round();
        return viewHolder;
    }
}
