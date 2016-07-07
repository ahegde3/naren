package com.samsung.android.email.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.samsung.android.email.R;

/**
 * Created by nsbisht on 7/6/16.
 */
public class AttachmentListAdapter extends ArrayAdapter<Bitmap> {

    private Context mContext;
    private Bitmap[] mBitmaps;

    public AttachmentListAdapter(Context context, Bitmap[] bitmaps) {
        super(context, -1 ,bitmaps);
        mContext = context;
        mBitmaps = bitmaps;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.attachment_thumbnail, parent, false);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.attachment_thumbview);
        imageView.setImageBitmap(mBitmaps[position]);
        return rowView;
    }
}
