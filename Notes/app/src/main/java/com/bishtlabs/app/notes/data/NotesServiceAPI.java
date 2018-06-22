package com.bishtlabs.app.notes.data;

import java.util.List;

/**
 * Created by nsbisht on 6/21/18.
 */

public interface NotesServiceAPI {

    interface NotesServiceCallback<T> {
        void onLoaded(T notes);
    }

    void getAllNotes(NotesServiceCallback<List<Note>> notes);

    void saveNote(Note note);
}
