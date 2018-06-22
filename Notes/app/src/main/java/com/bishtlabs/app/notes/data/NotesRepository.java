package com.bishtlabs.app.notes.data;

import java.util.List;

/**
 * Created by nsbisht on 6/21/18.
 */

public interface NotesRepository {

    interface GetNotesCallback {
        void onSuccess(List<Note> notes);
        void onError(String error);
    }

    void getNotes(GetNotesCallback callback);

    void saveNote(Note note);
}
