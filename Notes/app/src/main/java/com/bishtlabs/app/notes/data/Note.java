package com.bishtlabs.app.notes.data;

import java.util.UUID;

/**
 * Created by nsbisht on 6/21/18.
 */

public class Note {

    private String mId;
    private String mTitle;
    private String mDescription;

    public Note(String title, String description) {
        this.mId = UUID.randomUUID().toString();
        this.mTitle = title;
        this.mDescription = description;
    }

    public String getId() {
        return mId;
    }


    public String getTitle() {
        return mTitle;
    }


    public String getDescription() {
        return mDescription;
    }

}
