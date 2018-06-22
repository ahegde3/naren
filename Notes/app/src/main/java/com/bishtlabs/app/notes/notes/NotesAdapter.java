package com.bishtlabs.app.notes.notes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bishtlabs.app.notes.R;
import com.bishtlabs.app.notes.base.BaseViewHolder;
import com.bishtlabs.app.notes.data.Note;

import java.util.List;

/**
 * Created by naren on 6/22/18.
 */

public class NotesAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<Note> mNotesList;

    public NotesAdapter(List<Note> notesList) {
        this.mNotesList = notesList;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return mNotesList == null ? 0 : mNotesList.size();
    }

    class ViewHolder extends BaseViewHolder {
        private TextView mNoteTitleView;
        private TextView mNoteDescriptionView;

        public ViewHolder(View view) {
            super(view);
            mNoteTitleView = view.findViewById(R.id.tv_note_title);
            mNoteDescriptionView = view.findViewById(R.id.tv_note_description);

        }

        @Override
        public void onBind(int position) {
            Note note = mNotesList.get(position);
            mNoteTitleView.setText(note.getTitle());
            mNoteDescriptionView.setText(note.getDescription());
        }
    }
}
