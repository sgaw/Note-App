package com.playground.sgaw.notetaker;

import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;

/**
 * Tests for @link{MainActivity}.
 * Created by shirley.gaw on 1/30/15.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mainActivity;
    private LinearLayout notesContainer;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        mainActivity = getActivity();
        notesContainer = (LinearLayout) mainActivity.findViewById(R.id.notes_container);
    }

    @SmallTest
    public void testPreconditions() {
        assertNotNull(mainActivity);
        assertNotNull(notesContainer);
    }

    @MediumTest
    public void testNotesContainer() {
        final View decorView = mainActivity.getWindow().getDecorView();
        ViewAsserts.assertOnScreen(decorView, notesContainer);
        assertEquals("editText should always be visible.", View.VISIBLE,
                notesContainer.getVisibility());

        assertTrue("Must have at least one text view/note in home screen.",
                notesContainer.getChildCount() > 0);
    }
}
