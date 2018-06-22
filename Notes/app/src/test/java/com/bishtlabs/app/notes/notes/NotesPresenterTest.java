package com.bishtlabs.app.notes.notes;

import com.bishtlabs.app.notes.data.Note;
import com.bishtlabs.app.notes.data.NotesRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

/**
 * Created by nsbisht on 6/21/18.
 */
public class NotesPresenterTest {

    private static List<Note> NOTES =  new ArrayList<Note>();


    private static String ERROR_MSG = "Fail to load notes";

    @Mock
    private NotesRepository mNotesRepository;

    private NotesPresenter mNotesPresenter;

    @Captor
    ArgumentCaptor<NotesRepository.GetNotesCallback> mGetNotesCallbackCaptor;

    @Before
    public void setupNotesPresenter() {
        MockitoAnnotations.initMocks(this);

        mNotesPresenter = new NotesPresenter(mNotesRepository);
        NOTES.add(new Note("Title1", "Description1"));
        NOTES.add(new Note("Title2", "Description2"));
    }
    @Test
    public void notesPresenter_getNotes_sucess() {
        mNotesPresenter.getNotes();

        verify(mNotesPresenter.getView()).showProgress();

        verify(mNotesRepository).getNotes(mGetNotesCallbackCaptor.capture());
        mGetNotesCallbackCaptor.getValue().onSuccess(NOTES);

        verify(mNotesPresenter.getView()).hideProgress();

        verify(mNotesPresenter.getView()).showNotes(NOTES);

    }

    @Test
    public void notesPresenter_getNotes_error() {
        mNotesPresenter.getNotes();

        verify(mNotesPresenter.getView()).showProgress();

        verify(mNotesRepository).getNotes(mGetNotesCallbackCaptor.capture());
        mGetNotesCallbackCaptor.getValue().onError(ERROR_MSG);

        verify(mNotesPresenter.getView()).hideProgress();

        verify(mNotesPresenter.getView()).showToast(ERROR_MSG);

    }

    @Test
    public void notesPresenter_addNewNote() {
        mNotesPresenter.addNewNote();

        verify(mNotesPresenter.getView()).showAddNote();
    }

}