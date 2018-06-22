package com.bishtlabs.app.notes.data;

import java.util.List;

/**
 * Created by naren on 6/22/18.
 */

public class InMemoryNotesRepository implements NotesRepository {

    private NotesServiceApi mNoteServiceAPI;

    public InMemoryNotesRepository(NotesServiceApi noteServiceAPI) {
        mNoteServiceAPI = noteServiceAPI;
    }

    @Override
    public void getNotes(final GetNotesCallback callback) {
        mNoteServiceAPI.getAllNotes(new NotesServiceApi.NotesServiceCallback<List<Note>>() {
            @Override
            public void onLoaded(List<Note> notes) {
                callback.onSuccess(notes);
                return;
            }
        });
    }

    @Override
    public void saveNote(Note note) {
        mNoteServiceAPI.saveNote(note);
    }
}
