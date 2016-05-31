package com.samsung.android.email.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.samsung.android.email.R;
import com.samsung.android.email.model.Email;

/**
 * Created by nsbisht on 5/31/16.
 */
public class EmailListAdapter extends RecyclerView.Adapter<EmailListAdapter.ViewHolder> {

    private Email[] dataSource;

    public EmailListAdapter(Email[] emails) {
        dataSource = emails;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView mFromAddressView;
        protected TextView mDateView;
        protected TextView mTimeView;
        protected TextView mSubjectView;
        protected TextView mBodyView;

        public ViewHolder(View itemView) {
            super(itemView);
            mFromAddressView = (TextView) itemView.findViewById(R.id.from_address_label);
            mDateView = (TextView) itemView.findViewById(R.id.date_label);
            mTimeView = (TextView) itemView.findViewById(R.id.time_label);
            mSubjectView = (TextView) itemView.findViewById(R.id.subject_label);
            mBodyView = (TextView) itemView.findViewById(R.id.body_label);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mFromAddressView.setText(dataSource[position].getmFromAddress());
        holder.mDateView.setText(dataSource[position].getmDate().toString());
        holder.mTimeView.setText(dataSource[position].getmDate().toString());
        holder.mSubjectView.setText(dataSource[position].getmSubject());
        holder.mSubjectView.setText(dataSource[position].getmBody());
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
        return viewHolder;
    }
}
