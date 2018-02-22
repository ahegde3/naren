package com.example.nsbisht.userinfoviewer.ui.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.nsbisht.userinfoviewer.base.BaseViewHolder;
import com.example.nsbisht.userinfoviewer.data.remote.UserInfo;
import com.example.nsbisht.userinfoviewer.databinding.ItemUserEmptyViewBinding;
import com.example.nsbisht.userinfoviewer.databinding.UserListCardViewBinding;

import java.util.List;

/**
 * Created by nsbisht on 1/31/18.
 */

public class UserInfoAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_NORMAL = 1;

    private List<UserInfo> mUserInfoList;

    public UserInfoAdapter(List<UserInfo> userInfoList) {
        this.mUserInfoList = userInfoList;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch(viewType) {
            case VIEW_TYPE_NORMAL:
                UserListCardViewBinding binding = UserListCardViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new ViewHolder(binding);

            case VIEW_TYPE_EMPTY:
            default:
                ItemUserEmptyViewBinding emptyViewBinding = ItemUserEmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new EmptyViewHolder(emptyViewBinding);
        }

    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }


    @Override
    public int getItemViewType(int position) {
        if (mUserInfoList != null && mUserInfoList.size() > 0) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if(mUserInfoList.size() < 1) {
            return 1;
        }
        return mUserInfoList.size();
    }

    public class ViewHolder extends BaseViewHolder {

        private UserListCardViewBinding mUserListCardBinding;
        private UserInfoItemViewModel mUserInfoViewModel;

        public ViewHolder(UserListCardViewBinding itemViewBinding) {
            super(itemViewBinding.getRoot());
            this.mUserListCardBinding = itemViewBinding;
        }

        @Override
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

    public class EmptyViewHolder extends BaseViewHolder {


        public EmptyViewHolder(ItemUserEmptyViewBinding itemViewBinding) {
            super(itemViewBinding.getRoot());
        }

        @Override
        public void onBind(int position) {
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
