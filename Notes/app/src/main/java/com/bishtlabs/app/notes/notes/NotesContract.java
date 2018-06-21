package com.bishtlabs.app.notes.notes;

import com.bishtlabs.app.notes.data.Note;

import java.util.List;

/**
 * Created by nsbisht on 6/21/18.
 */

public interface NotesContract {

    interface View {
        void showProgress();
        void hideProgress();
        void showNotes(List<Note> notes);
        void showToast(String message);
        void showAddNote();
    }

    interface Presenter {
        void getNotes();
        void addNewNote();

    }
}
