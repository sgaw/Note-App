package com.playground.sgaw.notetaker;

import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;
import android.widget.EditText;

/**
 * Activity unit tests for @link{EditNoteActivity}.
 * Created by shirley.gaw on 1/30/15.
 */
public class EditNoteActivityTest extends ActivityInstrumentationTestCase2<EditNoteActivity> {
    private EditNoteActivity editNoteActivity;
    private EditText editText;

    public EditNoteActivityTest() {
        super(EditNoteActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        editNoteActivity = getActivity();
        editText = (EditText) editNoteActivity.findViewById(R.id.editNoteText);
    }

    @SmallTest
    public void testPreconditions() {
        assertNotNull(editNoteActivity);
        assertNotNull(editText);
    }

    @MediumTest
    public void testEditTextLayout() {
        final View decorView = editNoteActivity.getWindow().getDecorView();
        ViewAsserts.assertOnScreen(decorView, editText);
        assertEquals("editText should always be visible.", View.VISIBLE, editText.getVisibility());
    }

    @SmallTest
    public void testEditText_hintText() {
        assertEquals(editNoteActivity.getString(R.string.body_text_hint),
                editText.getHint().toString());
    }
}
