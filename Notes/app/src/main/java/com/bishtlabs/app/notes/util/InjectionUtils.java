package com.bishtlabs.app.notes.util;

import com.bishtlabs.app.notes.data.InMemoryNotesServiceApiImpl;
import com.bishtlabs.app.notes.data.NoteRepositories;
import com.bishtlabs.app.notes.data.NotesRepository;

/**
 * Created by naren on 6/22/18.
 */

public class InjectionUtils {

    public static NotesRepository injectNotesRepository() {
        return NoteRepositories.getInMemoryNotesRepository(new InMemoryNotesServiceApiImpl());
    }
}
