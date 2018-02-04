package com.example.nsbisht.userinfoviewer.ui.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.nsbisht.userinfoviewer.data.local.db.entity.UserInfo;
import com.example.nsbisht.userinfoviewer.databinding.UserListCardViewBinding;

import java.util.List;

/**
 * Created by nsbisht on 1/31/18.
 */

public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.ViewHolder> {

    private List<UserInfo> mUserInfoList;

    public UserInfoAdapter(List<UserInfo> userInfoList) {
        this.mUserInfoList = userInfoList;
    }

    @Override
    public UserInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserListCardViewBinding binding = UserListCardViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(UserInfoAdapter.ViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return mUserInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private UserListCardViewBinding mUserListCardBinding;
        private UserInfoItemViewModel mUserInfoViewModel;

        public ViewHolder(UserListCardViewBinding itemViewBinding) {
            super(itemViewBinding.getRoot());
            this.mUserListCardBinding = itemViewBinding;
        }

        public void onBind(int position) {

            final UserInfo userInfo = mUserInfoList.get(position);

            mUserInfoViewModel = new UserInfoItemViewModel(userInfo);

            mUserListCardBinding.setViewModel(mUserInfoViewModel);

            // Immediate Binding
            // When a variable or observable changes, the binding will be scheduled to change before
            // the next frame. There are times, however, when binding must be executed immediately.
            // To force execution, use the executePendingBindings() method.
            mUserListCardBinding.executePendingBindings();

        }
    }

    public void clearItems() {
        mUserInfoList.clear();
    }

    public void addItems(List<UserInfo> userInfo) {
        mUserInfoList.addAll(userInfo);
        notifyDataSetChanged();
    }
}
