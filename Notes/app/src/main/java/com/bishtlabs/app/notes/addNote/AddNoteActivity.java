package com.bishtlabs.app.notes.addNote;

import com.bishtlabs.app.notes.base.BaseActivity;

/**
 * Created by naren on 6/21/18.
 */

public class AddNoteActivity extends BaseActivity<AddNotePresenter> implements AddNoteContract.View {



    @Override
    public void showError(String msg) {

    }

    @Override
    public void noteSaved() {

    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected AddNotePresenter getPresenter() {
        return new AddNotePresenter(getRepository());
    }
}
