package com.bishtlabs.app.notes.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bishtlabs.app.notes.data.NotesRepository;

/**
 * Created by nsbisht on 6/21/18.
 */

public abstract class BaseActivity <P extends BasePresenter> extends AppCompatActivity {

    private P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    protected abstract int getLayoutId();

    protected abstract P getPresenter();

    private void init() {
        setContentView(getLayoutId());
        mPresenter = getPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unbindView();
    }

    protected NotesRepository getRepository() {
        return null;
    }
}
