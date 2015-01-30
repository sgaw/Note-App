package com.playground.sgaw.notetaker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Home screen activity for note taking app.  Shows all currently saved notes.
 *
 * Currently, re-reads all stored notes from filesystem on each onCreate call.
 */
public class MainActivity extends ActionBarActivity {
    private static final String LOG_TAG = MainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Read notes from files created by this app, each note corresponding to one file.
        // TODO(sgaw): Differentiate between app startup and resume
        displayNotes();

        // Show something if there are no notes yet.
        LinearLayout notesLayout = (LinearLayout) findViewById(R.id.notes_container);
        if (notesLayout.getChildCount() == 0) {
            TextView stubTextView = new TextView(this);
            stubTextView.setText(R.string.empty_notes_text);
            notesLayout.addView(stubTextView);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // User clicked create new note menu item, navigate to the note editor.
        if (id == R.id.action_create) {
            Intent intent = new Intent(this, EditNoteActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Add all saved notes to home screen.
     */
    private void displayNotes() {
        Log.i(LOG_TAG, "displayNotes");
        String[] files = getFilesDir().list();
        // TODO(sgaw): Make file reading asynchronous callbacks
        // TODO(sgaw): sort by modification time, handle need to scroll with many notes.
        for (String filename : files) {
            Log.i(LOG_TAG, "Filename: " + filename);
            if (EditNoteActivity.isNoteFilename(filename)) {
                // Read note from file, dropping inaccessible notes
                String contents = readNoteContents(filename);
                if (contents != null) {
                    addNote(filename, contents);
                }
            } else {
                // Shouldn't be writing spurious files in this app.
                throw new AssertionError("Unrecognized note filename: " + filename);
            }
        }
    }

    /**
     * Add a note to list on home screen for browsing.
     * @param filename the underlying file for saving note contents.
     * @param contents the note contents.
     */
    private void addNote(String filename, String contents) {
        Log.i(LOG_TAG, "addNote");
        NoteTextView noteTextView = new NoteTextView(this, filename);
        noteTextView.setText(contents);

        Log.i(LOG_TAG, "Created noteTextView " + noteTextView.toString());
        LinearLayout notesLayout = (LinearLayout) findViewById(R.id.notes_container);
        notesLayout.addView(noteTextView);
    }

    /**
     * Read contents of note from the specified file.
     *
     * Returns null if there are I/O issues.
     */
    private String readNoteContents(String filename) {
        Log.i(LOG_TAG, "readNote, filename:" + filename);
        try {
            StringBuffer stringBuffer = new StringBuffer("");

            StringBuilder builder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(
                    new FileReader(getFilesDir() + "/" + filename));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Log.i(LOG_TAG, line);
                builder.append(line);
                builder.append("\n");
            }
            bufferedReader.close();
            return builder.toString().trim(); // remove added trailing whitespace.
        } catch (FileNotFoundException fnfe) {
            Log.e(LOG_TAG, "Unable to open file: " + filename, fnfe);
            return null;
        } catch (IOException ioe) {
            Log.e(LOG_TAG, "Unable to read file: " + filename, ioe);
            return null;
        }
    }

    /**
     * TextView to display notes that stores the underlying note filename.
     *
     * Navigates to the note editor when the user clicks on a note.
     */
    protected class NoteTextView extends TextView {
        private final String filename;
        private String previewText;

        public NoteTextView(Context context, String filename) {
            super(context);
            this.filename = filename;

            // Navigate to note editor when user clicks on a note.
            Log.i(LOG_TAG, "NoteTextView.setClickListener");
            this.setOnClickListener(new OnClickListener() {
                /**
                 * Send an intent to the EditNoteActivity with the contents of this note and
                 * the underlying file that must be edited.
                 * @param v the NoteTextView that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    Log.i(LOG_TAG, "NoteTextView.OnClickListener.onClick");
                    Intent intent = new Intent(v.getContext(), EditNoteActivity.class);
                    NoteTextView textView = (NoteTextView) v;
                    intent.putExtra(EditNoteActivity.EXTRA_MESSAGE, textView.getText().toString());
                    intent.putExtra(EditNoteActivity.EXTRA_FILENAME, textView.filename);
                    Log.i(LOG_TAG, "EXTRA_MESSAGE: "
                            + intent.getStringExtra(EditNoteActivity.EXTRA_MESSAGE));
                    startActivity(intent);
                }
            });

            // TODO(sgaw): Read note asynchronously showing just preview text in case of long notes
        }

        @Override
        public String toString() {
            return "Filename ("+ filename + ")\nMessage(" + getText().toString() + ")";
        }
    }
}
