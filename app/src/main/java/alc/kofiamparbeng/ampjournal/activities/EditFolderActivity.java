package alc.kofiamparbeng.ampjournal.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import alc.kofiamparbeng.ampjournal.R;
import alc.kofiamparbeng.ampjournal.data.FolderItemViewModel;
import alc.kofiamparbeng.ampjournal.data.JournalEntryViewModel;
import alc.kofiamparbeng.ampjournal.data.NewFolderFactory;
import alc.kofiamparbeng.ampjournal.data.NewJournalEntryFactory;
import alc.kofiamparbeng.ampjournal.db.JournalDatabase;
import alc.kofiamparbeng.ampjournal.entities.JournalEntry;
import alc.kofiamparbeng.ampjournal.entities.JournalFolder;

public class EditFolderActivity extends AppCompatActivity {
    public static final String EXTRA_JOURNAL_FOLDER_ID = "EXTRA_JOURNAL_FOLDER_ID";
    public static final int DEFAULT_JOURNAL_FOLDER_ID = -1;

    private int mFolderId = DEFAULT_JOURNAL_FOLDER_ID;
    private EditText mFolderNameEditText;

    public static final String EXTRA_NEW_FOLDER_NAME = "alc.kofiamparbeng.ampjournal.new_folder_name";

    private JournalDatabase mJournalDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_folder);

        mFolderNameEditText = (EditText) findViewById(R.id.tv_folder_name);

        Button btnSaveFolder = (Button) findViewById(R.id.btn_folder_save);
        btnSaveFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFolder();
            }
        });

        mJournalDatabase = JournalDatabase.getDatabase(getApplicationContext());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent callingIntent = getIntent();
        if (callingIntent.hasExtra(EXTRA_JOURNAL_FOLDER_ID)) {
            btnSaveFolder.setText(R.string.title_activity_update_folder);

            if (mFolderId == DEFAULT_JOURNAL_FOLDER_ID) {
                // populate the UI
                mFolderId = callingIntent.getIntExtra(EXTRA_JOURNAL_FOLDER_ID, DEFAULT_JOURNAL_FOLDER_ID);

                NewFolderFactory factory = new NewFolderFactory(mJournalDatabase, mFolderId);
                final FolderItemViewModel viewModel
                        = ViewModelProviders.of(this, factory).get(FolderItemViewModel.class);
                viewModel.getFolder().observe(this, new Observer<JournalFolder>() {
                    @Override
                    public void onChanged(@Nullable JournalFolder folder) {
                        viewModel.getFolder().removeObserver(this);
                        populateUI(folder);
                    }
                });
            }
        }
    }

    private void populateUI(JournalFolder folder) {
        mFolderNameEditText.setText(folder.getName());
    }

    private void saveFolder() {
        Intent replyIntent = new Intent();
        if (TextUtils.isEmpty(mFolderNameEditText.getText())) {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.new_journal_entry_invalid_message,
                    Toast.LENGTH_LONG).show();
        } else {
            String folderName = mFolderNameEditText.getText().toString();
            replyIntent.putExtra(EXTRA_NEW_FOLDER_NAME, folderName);
            if (mFolderId != DEFAULT_JOURNAL_FOLDER_ID) {
                replyIntent.putExtra(EXTRA_JOURNAL_FOLDER_ID, mFolderId);
            }
            setResult(RESULT_OK, replyIntent);
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_journal_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.cancel_new_journal_form) {
            Intent replyIntent = new Intent();
            setResult(RESULT_CANCELED, replyIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
