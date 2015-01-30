package com.playground.sgaw.notetaker;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Activity unit tests for @link{EditNoteActivity}.
 * Created by shirley.gaw on 1/30/15.
 */
public class EditNoteActivityTest extends ActivityInstrumentationTestCase2<EditNoteActivity> {
    private static final String TEST_FILENAME = "Note-0000.txt";
    private static final String TEST_CONTENT = "Hello, world!";
    private static final long TIMEOUT_MS = 5000;

    private EditNoteActivity editNoteActivity;
    private EditText editText;

    public EditNoteActivityTest() {
        super(EditNoteActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        setActivityInitialTouchMode(true);
        editNoteActivity = getActivity();
        editText = (EditText) editNoteActivity.findViewById(R.id.editNoteText);
    }

    @Override
    public void tearDown() throws Exception {
        // Clean-up our test files so they don't stay on the app.
        if (fileExists(TEST_FILENAME)) {
            editNoteActivity.deleteFile(TEST_FILENAME);
        }
        super.tearDown();
    }

    @SmallTest
    public void testPreconditions() {
        assertNotNull(editNoteActivity);
        assertNotNull(editText);

        // Assure the test file doesn't already exist.
        assertFalse("Found test file already exists before explicit write called.",
                fileExists(TEST_FILENAME));

        assertTrue("Should be in create state, no note content.",
                editText.getText().toString().isEmpty());
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

    @SmallTest
    public void testIsNoteFilename() {
        assertTrue(EditNoteActivity.isNoteFilename("Note-123456.txt"));
        assertFalse(EditNoteActivity.isNoteFilename("Foo-123456.txt"));
        assertFalse(EditNoteActivity.isNoteFilename("Note-abc.txt"));
        assertFalse(EditNoteActivity.isNoteFilename("Note-123456"));
    }

    @MediumTest
    public void testDeleteMenuItem() throws  IOException{
        writeTestFile();

        // Check the file was written
        assertTrue("Should have written test file.", fileExists(TEST_FILENAME));

        // Need to setup up ActvityMonitor or everything hangs
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(MainActivity.class.getName(), null, true);

        // Need to signal what file should be deleted.
        StubMenuItem deleteStubMenuItem = new StubMenuItem(R.id.action_delete);
        editNoteActivity.onOptionsItemSelected(deleteStubMenuItem);

        MainActivity mainActivity =
                (MainActivity) receiverActivityMonitor.waitForActivityWithTimeout(TIMEOUT_MS);
        assertNotNull("Activity is null.", mainActivity);
        assertEquals("Monitor for mainActivity was not called.", 1,
                receiverActivityMonitor.getHits());
        getInstrumentation().removeMonitor(receiverActivityMonitor);

        assertFalse("Test file was not deleted.", fileExists(TEST_FILENAME));
    }

    private boolean fileExists(String targetFilename) {
        for (String filename : editNoteActivity.fileList()) {
            if (filename.equals(targetFilename)) {
                return true;
            }
        }
        return false;
    }

    private void writeTestFile() throws IOException {
        editNoteActivity.saveNoteContents(TEST_CONTENT, TEST_FILENAME);
    }

    private class StubMenuItem implements MenuItem {
        private int itemIdOverride;

        public StubMenuItem(int itemIdOverride) {
            super();
            this.itemIdOverride = itemIdOverride;
        }

        @Override
        public int getGroupId() {
            return 0;
        }

        @Override

        public int getItemId() {
            return itemIdOverride;
        }

        @Override
        public int getOrder() {
            return 0;
        }

        @Override
        public MenuItem setTitle(CharSequence title) {
            return null;
        }

        @Override
        public MenuItem setTitle(int title) {
            return null;
        }

        @Override
        public CharSequence getTitle() {
            return null;
        }

        @Override
        public MenuItem setTitleCondensed(CharSequence title) {
            return null;
        }

        @Override
        public CharSequence getTitleCondensed() {
            return null;
        }

        @Override
        public MenuItem setIcon(Drawable icon) {
            return null;
        }

        @Override
        public MenuItem setIcon(int iconRes) {
            return null;
        }

        @Override
        public Drawable getIcon() {
            return null;
        }

        @Override
        public MenuItem setIntent(Intent intent) {
            return null;
        }

        @Override
        public Intent getIntent() {
            return null;
        }

        @Override
        public MenuItem setShortcut(char numericChar, char alphaChar) {
            return null;
        }

        @Override
        public MenuItem setNumericShortcut(char numericChar) {
            return null;
        }

        @Override
        public char getNumericShortcut() {
            return 0;
        }

        @Override
        public MenuItem setAlphabeticShortcut(char alphaChar) {
            return null;
        }

        @Override
        public char getAlphabeticShortcut() {
            return 0;
        }

        @Override
        public MenuItem setCheckable(boolean checkable) {
            return null;
        }

        @Override
        public boolean isCheckable() {
            return false;
        }

        @Override
        public MenuItem setChecked(boolean checked) {
            return null;
        }

        @Override
        public boolean isChecked() {
            return false;
        }

        @Override
        public MenuItem setVisible(boolean visible) {
            return null;
        }

        @Override
        public boolean isVisible() {
            return false;
        }

        @Override
        public MenuItem setEnabled(boolean enabled) {
            return null;
        }

        @Override
        public boolean isEnabled() {
            return false;
        }

        @Override
        public boolean hasSubMenu() {
            return false;
        }

        @Override
        public SubMenu getSubMenu() {
            return null;
        }

        @Override
        public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) {
            return null;
        }

        @Override
        public ContextMenu.ContextMenuInfo getMenuInfo() {
            return null;
        }

        @Override
        public void setShowAsAction(int actionEnum) {

        }

        @Override
        public MenuItem setShowAsActionFlags(int actionEnum) {
            return null;
        }

        @Override
        public MenuItem setActionView(View view) {
            return null;
        }

        @Override
        public MenuItem setActionView(int resId) {
            return null;
        }

        @Override
        public View getActionView() {
            return null;
        }

        @Override
        public MenuItem setActionProvider(ActionProvider actionProvider) {
            return null;
        }

        @Override
        public ActionProvider getActionProvider() {
            return null;
        }

        @Override
        public boolean expandActionView() {
            return false;
        }

        @Override
        public boolean collapseActionView() {
            return false;
        }

        @Override
        public boolean isActionViewExpanded() {
            return false;
        }

        @Override
        public MenuItem setOnActionExpandListener(OnActionExpandListener listener) {
            return null;
        }
    }

}
