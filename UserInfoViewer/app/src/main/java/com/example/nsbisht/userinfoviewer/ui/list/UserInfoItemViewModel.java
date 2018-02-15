package com.example.nsbisht.userinfoviewer.ui.list;

import android.databinding.ObservableField;

import com.example.nsbisht.userinfoviewer.data.remote.UserInfo;


/**
 * Created by nsbisht on 1/31/18.
 */

public class UserInfoItemViewModel {

    public ObservableField<String> firstName;
    public ObservableField<String> lastName;
    public ObservableField<Integer> age;
    public ObservableField<String> address;

    public UserInfoItemViewModel(UserInfo user) {
        firstName = new ObservableField<>(user.getFirstName());
        lastName = new ObservableField<>(user.getLastName());
        age = new ObservableField<>(user.getAge());
        address = new ObservableField<>(user.getAddress());
    }

    public void onItemClick() {
    }

    public interface UserInfoItemViewModelListener {
        void onItemClick(String blogUrl);
    }
}
