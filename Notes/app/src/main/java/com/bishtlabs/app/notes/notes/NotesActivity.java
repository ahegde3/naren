package com.bishtlabs.app.notes.notes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bishtlabs.app.notes.R;
import com.bishtlabs.app.notes.base.BaseActivity;
import com.bishtlabs.app.notes.data.Note;

import java.util.List;

public class NotesActivity extends BaseActivity<NotesPresenter> implements NotesContract.View {

    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private NotesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mRecycleView = findViewById(R.id.rv_notes_list);

        mLayoutManager = new LinearLayoutManager(this);

        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(mLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

        mRecycleView.addItemDecoration(itemDecoration);
        mPresenter.getNotes();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_notes_list;
    }

    @Override
    protected NotesPresenter getPresenter() {
        return new NotesPresenter(getRepository());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notes_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showNotes(List<Note> notes) {
        mAdapter = new NotesAdapter(notes);
        mRecycleView.setAdapter(mAdapter);
    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public void showAddNote() {

    }
}
