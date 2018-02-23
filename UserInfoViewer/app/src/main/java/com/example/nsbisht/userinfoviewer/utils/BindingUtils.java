package com.example.nsbisht.userinfoviewer.utils;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.nsbisht.userinfoviewer.data.remote.UserInfo;
import com.example.nsbisht.userinfoviewer.ui.list.UserInfoAdapter;

import java.util.ArrayList;

/**
 * Created by nsbisht on 2/2/18.
 */

public class BindingUtils {

    @BindingAdapter({"adapter"})
    public static void addUserInfoItems(RecyclerView recyclerView,
                                          ArrayList<UserInfo> userInfos) {
        UserInfoAdapter adapter = (UserInfoAdapter) recyclerView.getAdapter();
        if(adapter != null) {
            adapter.clearItems();
            adapter.addItems(userInfos);
        }
    }

    @BindingAdapter({"integertext"})
    public static void setAgeField(TextView view, Integer value) {
        view.setText(Integer.toString(value));
    }
}
