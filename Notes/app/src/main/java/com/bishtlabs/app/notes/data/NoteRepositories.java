package com.bishtlabs.app.notes.data;

/**
 * Created by naren on 6/22/18.
 */

public class NoteRepositories {

    private static NotesRepository mNoteRepository;

    private NoteRepositories() {

    }

    public static synchronized NotesRepository getInMemoryNotesRepository(NotesServiceApi serviceApi) {
        if(mNoteRepository == null) {
            mNoteRepository = new InMemoryNotesRepository(serviceApi);
        }

        return mNoteRepository;
    }

}
