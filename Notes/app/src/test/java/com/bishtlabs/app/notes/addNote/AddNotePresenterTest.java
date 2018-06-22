package com.bishtlabs.app.notes.addNote;

import com.bishtlabs.app.notes.data.Note;
import com.bishtlabs.app.notes.data.NotesRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

/**
 * Created by naren on 6/21/18.
 */
public class AddNotePresenterTest {

    @Mock
    private NotesRepository mNotesRepository;

    private AddNotePresenter mAddNotePresenter;

    @Before
    public void setupAddNotePresenter() {
        MockitoAnnotations.initMocks(this);

        mAddNotePresenter = new AddNotePresenter(mNotesRepository);
    }

    @Test
    public void addNotePresenter_saveNote_sucess() {
        mAddNotePresenter.saveNote(new Note("Title", "Description"));

        verify(mAddNotePresenter.getView()).noteSaved();
    }

    @Test
    public void addNotePresenter_saveNote_failed_null() {
        mAddNotePresenter.saveNote(null);

        verify(mAddNotePresenter.getView()).showError("Could not save");
    }

    @Test
    public void addNotePresenter_saveNote_failed_empty() {
        mAddNotePresenter.saveNote(new Note("", ""));

        verify(mAddNotePresenter.getView()).showError("Can not save empty note");
    }

}