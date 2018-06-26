package alc.kofiamparbeng.ampjournal.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import alc.kofiamparbeng.ampjournal.R;
import alc.kofiamparbeng.ampjournal.data.JournalAdapter;
import alc.kofiamparbeng.ampjournal.data.JournalEntryViewModel;
import alc.kofiamparbeng.ampjournal.entities.JournalEntry;

public class MainActivity extends AppCompatActivity {
    private JournalAdapter mJournalAdapter;
    private RecyclerView mRecyclerView;
    private JournalEntryViewModel mJournalEntryViewModel;

    private SwipeRefreshLayout swipeContainer;

    public static final int NEW_JOURNAL_ENTRY_ACTIVITY_REQUEST_CODE = 712;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareFloatingActionButton();
        prepareRecyclerView();
        prepareViewModel();

        preparePullToRefresh();
    }

    private void preparePullToRefresh(){
        swipeContainer = (SwipeRefreshLayout)findViewById(R.id.swipeContainer);
        swipeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               prepareViewModel();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorAccent,
                R.color.colorPrimary,
                android.R.color.holo_orange_light,
                R.color.colorPrimaryDark);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prepareViewModel();
                swipeContainer.setRefreshing(false);
            }
        });
    }

    private void prepareViewModel() {
        mJournalEntryViewModel = ViewModelProviders.of(this).get(JournalEntryViewModel.class);
        mJournalEntryViewModel.getAllEntries().observe(this, new Observer<List<JournalEntry>>() {
            @Override
            public void onChanged(@Nullable final List<JournalEntry> entries) {
                // Update the cached copy of the words in the adapter.
                mJournalAdapter.setEntries(entries);
            }
        });
    }

    private void prepareRecyclerView() {
        mJournalAdapter = new JournalAdapter(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.journal_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setAdapter(mJournalAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void prepareFloatingActionButton() {
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_new_journal_entry);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewJournalEntryActivity.class);
                startActivityForResult(intent, NEW_JOURNAL_ENTRY_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_JOURNAL_ENTRY_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String title = data.getStringExtra(NewJournalEntryActivity.EXTRA_NEW_ENTRY_TITLE);
            String body = data.getStringExtra(NewJournalEntryActivity.EXTRA_NEW_ENTRY_BODY);
            JournalEntry entry = new JournalEntry();
            entry.setBody(body);
            entry.setTitle(title);
            entry.setDate(new Date());
            mJournalEntryViewModel.insert(entry);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.new_journal_entry_cancelled_message,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}
