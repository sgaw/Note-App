package com.playground.sgaw.notetaker;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.LinearLayout;

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
    public void setUp() {
        mainActivity = getActivity();
        notesContainer = (LinearLayout) mainActivity.findViewById(R.id.notes_container);
    }

    @SmallTest
    public void testPreconditions() {
        assertNotNull(mainActivity);
        assertNotNull(notesContainer);
    }
}
