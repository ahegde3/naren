package com.bishtlabs.app.notes.addNote;

import com.bishtlabs.app.notes.data.Note;

/**
 * Created by naren on 6/21/18.
 */

public interface AddNoteContract {
    interface View {
        void showError(String msg);
        void noteSaved();
    }

    interface Presenter {
        void saveNote(Note note);
    }
}
