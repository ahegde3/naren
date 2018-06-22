package com.bishtlabs.app.notes.base;

import com.bishtlabs.app.notes.data.NotesRepository;

/**
 * Created by nsbisht on 6/21/18.
 */

public abstract class BasePresenter <V> {
    private V view;
    private NotesRepository repository;

    public BasePresenter(NotesRepository repo) {
        this.repository = repo;
    }

    public void bindView(V v) {
        this.view = v;
    }

    protected void unbindView() {
        view = null;
    }

    public V getView() {
        return view;
    }

    protected NotesRepository getRepository() {
        return repository;
    }

}
