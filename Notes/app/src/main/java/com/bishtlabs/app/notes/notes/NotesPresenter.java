package com.bishtlabs.app.notes.notes;

import com.bishtlabs.app.notes.base.BasePresenter;
import com.bishtlabs.app.notes.data.Note;
import com.bishtlabs.app.notes.data.NotesRepository;

import java.util.List;

/**
 * Created by nsbisht on 6/21/18.
 */

public class NotesPresenter extends BasePresenter<NotesActivity> implements NotesContract.Presenter {

    public NotesPresenter(NotesRepository repo) {
        super(repo);
    }

    @Override
    public void getNotes() {
        getView().showProgress();

        getRepository().getNotes(new NotesRepository.GetNotesCallback() {
            @Override
            public void onSuccess(List<Note> notes) {
                getView().showNotes(notes);
                getView().hideProgress();
            }

            @Override
            public void onError(String error) {
                getView().showToast("Error");
            }
        });
    }

    @Override
    public void addNewNote() {

    }
}
