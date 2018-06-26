package alc.kofiamparbeng.ampjournal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import alc.kofiamparbeng.ampjournal.R;

public class NewJournalEntryActivity extends AppCompatActivity {
    private TextView mTitileTextView;
    private TextView mBodyTextView;

    public static final String EXTRA_NEW_ENTRY_TITLE = "alc.kofiamparbeng.ampjournal.new_entry_title";
    public static final String EXTRA_NEW_ENTRY_BODY = "alc.kofiamparbeng.ampjournal.new_entry_body";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_journal_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBodyTextView = (TextView) findViewById(R.id.tv_journal_body);
        mTitileTextView = (TextView) findViewById(R.id.tv_journal_title);

        Button btnSaveNewJournalEntry = (Button) findViewById(R.id.btn_new_journal_entry_save);
        btnSaveNewJournalEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNewJournalEntry();
            }
        });
    }

    private void saveNewJournalEntry() {
        Intent replyIntent = new Intent();
        if (TextUtils.isEmpty(mTitileTextView.getText())
                || TextUtils.isEmpty(mBodyTextView.getText())) {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.new_journal_entry_invalid_message,
                    Toast.LENGTH_LONG).show();
        } else {
            String title = mTitileTextView.getText().toString();
            String body = mBodyTextView.getText().toString();
            replyIntent.putExtra(EXTRA_NEW_ENTRY_BODY, body);
            replyIntent.putExtra(EXTRA_NEW_ENTRY_TITLE, title);
            setResult(RESULT_OK, replyIntent);
            finish();
        }
    }

}
