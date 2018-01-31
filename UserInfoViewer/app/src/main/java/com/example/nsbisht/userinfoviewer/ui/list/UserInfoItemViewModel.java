package com.example.nsbisht.userinfoviewer.ui.list;

import android.databinding.ObservableField;

/**
 * Created by nsbisht on 1/31/18.
 */

public class UserInfoItemViewModel {

    public ObservableField<String> firstName;
    public ObservableField<String> lastName;
    public ObservableField<Integer> age;
    public ObservableField<String> address;

    public void onItemClick() {
    }
}
