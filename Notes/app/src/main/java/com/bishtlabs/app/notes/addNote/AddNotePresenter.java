package com.bishtlabs.app.notes.addNote;

import com.bishtlabs.app.notes.base.BasePresenter;
import com.bishtlabs.app.notes.data.Note;
import com.bishtlabs.app.notes.data.NotesRepository;

/**
 * Created by naren on 6/21/18.
 */

public class AddNotePresenter extends BasePresenter<AddNoteActivity> implements AddNoteContract.Presenter {

    public AddNotePresenter(NotesRepository repo) {
        super(repo);
    }

    @Override
    public void saveNote(Note note) {

    }
}
