package com.example.nsbisht.userinfoviewer.ui.user;

import android.databinding.ObservableField;

import com.example.nsbisht.userinfoviewer.base.BaseViewModel;
import com.example.nsbisht.userinfoviewer.data.DataManager;
import com.example.nsbisht.userinfoviewer.data.remote.UserInfo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nsbisht on 2/2/18.
 */

public class UserAddViewModel extends BaseViewModel<AddUserNavigator> {

    public final ObservableField<String> firstName =  new ObservableField<>("");
    public final ObservableField<String> lastName = new ObservableField<>("");
    public final ObservableField<String> address =  new ObservableField<>("");
    public final ObservableField<Integer> age = new ObservableField<>(0);

    public UserAddViewModel(DataManager mDataManager) {
        super(mDataManager);
    }

    public void saveUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setFirstName(firstName.get());
        userInfo.setLastName(lastName.get());
        userInfo.setAge(age.get());
        userInfo.setAddress(address.get());
        addUserInfo(userInfo);
    }

    public void addUserInfo(UserInfo userInfo) {
        getDataManager()
                .createUserInfo(userInfo);
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe();
    }

    public ObservableField<String> getFirstName() {
        return firstName;
    }

    public ObservableField<String> getLastName() {
        return lastName;
    }

    public ObservableField<String> getAddress() {
        return address;
    }

    public ObservableField<Integer> getAge() {
        return age;
    }
}
