package com.bishtlabs.app.notes.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naren on 6/22/18.
 */

public class InMemoryNotesServiceApiImpl implements NotesServiceApi {

    private List<Note> mSavedNotes;

    @Override
    public void getAllNotes(NotesServiceCallback<List<Note>> notes) {
        if(mSavedNotes == null) {
            mSavedNotes = new ArrayList<>();
            mSavedNotes.add(new Note("Tarzan", "Wild Thing!!"));
        }
        notes.onLoaded(mSavedNotes);
    }

    @Override
    public void saveNote(Note note) {
        if(mSavedNotes == null) {
            mSavedNotes = new ArrayList<>();
        }

        mSavedNotes.add(note);
    }
}
