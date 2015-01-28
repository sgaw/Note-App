package com.playground.sgaw.notetaker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import junit.framework.Assert;

import java.io.FileOutputStream;
import java.io.IOException;


public class EditNoteActivity extends ActionBarActivity {
    public static final String EXTRA_MESSAGE = "com.playground.sgaw.notetaker.MESSAGE";
    public static final String EXTRA_FILENAME = "com.playground.sgaw.notetaker.FILENAME";

    private static final String LOG_TAG = EditNoteActivity.class.getCanonicalName();
    public static final String NOTE_FILE_PREFIX = "Note-";
    public static final String NOTE_FILE_SUFFIX = ".txt";

    /**
     * Checks if filename matches the format for saved notes.  Checks for null input.
     *
     * @param filename the name of a file to pattern match against note stored files.
     */
    public static boolean isNoteFilename(final String filename) {
        Log.i(LOG_TAG, "isNoteFilename");
        if (filename == null) {
            return false;
        }
        return filename.matches(NOTE_FILE_PREFIX + "\\d+" + NOTE_FILE_SUFFIX);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Check if we are editing an existing note.
        String contents = getIntent().getStringExtra(EXTRA_MESSAGE);
        if (contents != null) {
            Log.i(LOG_TAG, "Existing note being edited: " + contents);
            EditText editText = (EditText) findViewById(R.id.editNoteText);
            editText.setText(contents);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // TODO(sgaw): Auto-save if Up button clicked
        switch(item.getItemId()) {
            case R.id.action_confirm:
                onSave();
                return true;
            case R.id.action_delete:
                onDelete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Implements save action.  Extracts note contents and saves state.  Returns to home screen
     * view.
     */
    private void onSave() {
        Log.i(LOG_TAG, "openSave()");
        EditText editText = (EditText) findViewById(R.id.editNoteText);
        String message = editText.getText().toString();

        Intent intent = new Intent(this, MainActivity.class);
        if (message != null && !message.isEmpty()) {
            /* TODO(sgaw): If large number of notes are expected, replace file store with
             database entries */
            try {

                String filename = getIntent().getStringExtra(EXTRA_FILENAME);
                // Re-write note contents to existing files if possible.
                if (!isNoteFilename(filename)) {
                    saveNoteContents(message);
                } else {
                    saveNoteContents(message, filename);
                }

                startActivity(intent);
            } catch (IOException ioe) {
                // Warn user that no save completed
                Toast.makeText(this, R.string.save_fail_text, Toast.LENGTH_SHORT)
                        .show();
                Log.e(LOG_TAG, "onSave", ioe);
                return;
            }
        }
    }

    /**
     * Write contents of note to a new file.
     *
     * @param contents the note contents to save to a file.
     * @throws IOException
     */
    private void saveNoteContents(String contents) throws IOException {
        // Use timestamp to name files
        String filename = NOTE_FILE_PREFIX + System.currentTimeMillis() + NOTE_FILE_SUFFIX;
        saveNoteContents(contents, filename);
    }

    /**
     * Write contents of note to the specified file (internal app storage).
     * @param contents the note contents to save to a file.
     * @param filename the name of a file to overwrite or create.
     * @throws IOException
     */
    private void saveNoteContents(String contents, String filename) throws IOException {
        Log.i(LOG_TAG, "Saving to filename: " + filename);
        FileOutputStream fileOutputStream = openFileOutput(filename, Context.MODE_PRIVATE);
        fileOutputStream.write(contents.getBytes());
        fileOutputStream.close();
    }

    /**
     * Implements delete action.  Removes underlying file representing note contents.  Return
     * to home screen view.
     *
     * If the note was only a draft, the contents are discarded.
     */
    private void onDelete() {
        Log.i(LOG_TAG, "onDelete()");
        Intent callingIntent = getIntent();
        final String filename = callingIntent.getStringExtra(EXTRA_FILENAME);
        // TODO(sgaw): Add confirmation of delete?
        if (filename != null && !filename.isEmpty()) {
            Log.i(LOG_TAG, "Deleting note: " + filename);
            this.deleteFile(filename);
            Toast.makeText(this, R.string.delete_text, Toast.LENGTH_SHORT).show();
        }

        Log.i(LOG_TAG, "onDelete navigating home");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
