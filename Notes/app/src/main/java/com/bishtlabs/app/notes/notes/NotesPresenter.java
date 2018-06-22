package com.bishtlabs.app.notes.notes;

import com.bishtlabs.app.notes.base.BasePresenter;
import com.bishtlabs.app.notes.data.NotesRepository;

/**
 * Created by nsbisht on 6/21/18.
 */

public class NotesPresenter extends BasePresenter<NotesActivity> implements NotesContract.Presenter {

    public NotesPresenter(NotesRepository repo) {
        super(repo);
    }

    @Override
    public void getNotes() {

    }

    @Override
    public void addNewNote() {

    }
}
