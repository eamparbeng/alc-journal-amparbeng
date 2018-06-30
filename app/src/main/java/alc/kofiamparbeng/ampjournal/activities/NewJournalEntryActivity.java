package alc.kofiamparbeng.ampjournal.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import alc.kofiamparbeng.ampjournal.data.JournalEntryViewModel;
import alc.kofiamparbeng.ampjournal.data.NewJournalEntryFactory;
import alc.kofiamparbeng.ampjournal.db.JournalDatabase;
import alc.kofiamparbeng.ampjournal.entities.JournalEntry;

public class NewJournalEntryActivity extends AppCompatActivity {
    public static final String EXTRA_JOURNAL_ENTRY_ID = "EXTRA_JOURNAL_ENTRY_ID";
    public static final int DEFAULT_JOURNAL_ENTRY_ID = -1;
    public static final int SELECT_FOLDER_ACTIVITY_REQUEST_CODE = 713;

    private int mJournalEntryId = DEFAULT_JOURNAL_ENTRY_ID;
    private EditText mTitileEditText;
    private EditText mBodyEditText;
    private TextView mFolderNameTextView;
    private TextInputLayout mFolderNameTextInputLayout;

    public static final String EXTRA_NEW_ENTRY_TITLE = "alc.kofiamparbeng.ampjournal.new_entry_title";
    public static final String EXTRA_NEW_ENTRY_BODY = "alc.kofiamparbeng.ampjournal.new_entry_body";
    public static final String EXTRA_NEW_ENTRY_FOLDER_NAME = "alc.kofiamparbeng.ampjournal.new_entry_folder";

    private JournalDatabase mJournalDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_journal_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBodyEditText = (EditText) findViewById(R.id.tv_journal_body);
        mTitileEditText = (EditText) findViewById(R.id.tv_journal_title);
        mFolderNameTextView = (TextView)findViewById(R.id.tv_journal_folder);
        mFolderNameTextInputLayout=findViewById(R.id.input_layout_folder);

        mFolderNameTextInputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFolder();
            }
        });
        mFolderNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFolder();
            }
        });

        Button btnSaveNewJournalEntry = (Button) findViewById(R.id.btn_new_journal_entry_save);
        btnSaveNewJournalEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNewJournalEntry();
            }
        });

        mJournalDatabase = JournalDatabase.getDatabase(getApplicationContext());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent callingIntent = getIntent();
        if (callingIntent.hasExtra(EXTRA_JOURNAL_ENTRY_ID)) {
            btnSaveNewJournalEntry.setText(R.string.new_journal_entry_update_button_text);

            if (mJournalEntryId == DEFAULT_JOURNAL_ENTRY_ID) {
                // populate the UI
                mJournalEntryId = callingIntent.getIntExtra(EXTRA_JOURNAL_ENTRY_ID, DEFAULT_JOURNAL_ENTRY_ID);

                NewJournalEntryFactory factory = new NewJournalEntryFactory(mJournalDatabase, mJournalEntryId);
                final JournalEntryViewModel viewModel
                        = ViewModelProviders.of(this, factory).get(JournalEntryViewModel.class);
                viewModel.getJournalEntry().observe(this, new Observer<JournalEntry>() {
                    @Override
                    public void onChanged(@Nullable JournalEntry journalEntry) {
                        viewModel.getJournalEntry().removeObserver(this);
                        populateUI(journalEntry);
                    }
                });
            }
        }else{
            mFolderNameTextView.setText("Diary");
        }
    }

    private void populateUI(JournalEntry journalEntry) {
        mBodyEditText.setText(journalEntry.getBody());
        mTitileEditText.setText(journalEntry.getTitle());
        if(TextUtils.isEmpty(journalEntry.getFolderName())){
            mFolderNameTextView.setText("Diary");
        }else{
            mFolderNameTextView.setText(journalEntry.getFolderName());
        }
    }

    private void saveNewJournalEntry() {
        Intent replyIntent = new Intent();
        if (TextUtils.isEmpty(mTitileEditText.getText())
                || TextUtils.isEmpty(mBodyEditText.getText())) {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.new_journal_entry_invalid_message,
                    Toast.LENGTH_LONG).show();
        } else {
            String title = mTitileEditText.getText().toString();
            String body = mBodyEditText.getText().toString();
            String folderName = mFolderNameTextView.getText().toString();
            replyIntent.putExtra(EXTRA_NEW_ENTRY_BODY, body);
            replyIntent.putExtra(EXTRA_NEW_ENTRY_TITLE, title);
            replyIntent.putExtra(EXTRA_NEW_ENTRY_FOLDER_NAME, folderName);
            if (mJournalEntryId != DEFAULT_JOURNAL_ENTRY_ID) {
                replyIntent.putExtra(EXTRA_JOURNAL_ENTRY_ID, mJournalEntryId);
            }
            setResult(RESULT_OK, replyIntent);
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_FOLDER_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            final String folderName = data.getStringExtra(FolderSelecterActivity.EXTRA_FOLDER_SELECTER_SELECTED_FOLDER_NAME);
            mFolderNameTextView.setText(folderName);
        } else {

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

    private void chooseFolder(){
        Intent folderActivityIntent = new Intent(NewJournalEntryActivity.this, FolderSelecterActivity.class);
        startActivityForResult(folderActivityIntent, SELECT_FOLDER_ACTIVITY_REQUEST_CODE);
    }
}
