package alc.kofiamparbeng.ampjournal.activities;

import android.arch.lifecycle.LiveData;
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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.List;

import alc.kofiamparbeng.ampjournal.R;
import alc.kofiamparbeng.ampjournal.data.JournalAdapter;
import alc.kofiamparbeng.ampjournal.data.JournalListViewModel;
import alc.kofiamparbeng.ampjournal.data.JournalEntrySwipeToDeleteHandler;
import alc.kofiamparbeng.ampjournal.entities.JournalEntry;
import alc.kofiamparbeng.ampjournal.sync.JournalSyncUtils;

public class MainActivity extends AppCompatActivity implements JournalAdapter.JournalEntryEventListener{
    private JournalAdapter mJournalAdapter;
    private RecyclerView mRecyclerView;
    private JournalListViewModel mJournalEntryViewModel;

    private SwipeRefreshLayout swipeContainer;

    public static final int NEW_JOURNAL_ENTRY_ACTIVITY_REQUEST_CODE = 712;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareFloatingActionButton();
        prepareRecyclerView();
        prepareViewModel();

        preparePullToRefresh();

        prepareSync();

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

    }

    private void prepareSync() {
        JournalSyncUtils.initialize(this);
    }

    private void preparePullToRefresh() {
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
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
        mJournalEntryViewModel = ViewModelProviders.of(this).get(JournalListViewModel.class);
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

        mJournalAdapter.setJournalEntryClickListener(this);

        JournalEntrySwipeToDeleteHandler swipeToDeleteHandler = new JournalEntrySwipeToDeleteHandler(this);
        ItemTouchHelper touchHelper = new ItemTouchHelper(swipeToDeleteHandler);
        touchHelper.attachToRecyclerView(mRecyclerView);
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
            final String title = data.getStringExtra(NewJournalEntryActivity.EXTRA_NEW_ENTRY_TITLE);
            final String body = data.getStringExtra(NewJournalEntryActivity.EXTRA_NEW_ENTRY_BODY);
            final String folderName = data.getStringExtra(NewJournalEntryActivity.EXTRA_NEW_ENTRY_FOLDER_NAME);


            if (data.hasExtra(NewJournalEntryActivity.EXTRA_JOURNAL_ENTRY_ID)) {
                final int id = data.getIntExtra(NewJournalEntryActivity.EXTRA_JOURNAL_ENTRY_ID, NewJournalEntryActivity.DEFAULT_JOURNAL_ENTRY_ID);
                final LiveData<JournalEntry> entryLiveData = mJournalEntryViewModel.getJournalEntryById(id);
                entryLiveData.observe(this, new Observer<JournalEntry>() {
                    @Override
                    public void onChanged(@Nullable final JournalEntry entry) {
                        entryLiveData.removeObserver(this);
                        entry.setBody(body);
                        entry.setTitle(title);
                        entry.setFolderName(folderName);
                        entry.setUpdatedDate(new Date());
                        entry.setId(id);
                        mJournalEntryViewModel.update(entry);
                        Toast.makeText(
                                getApplicationContext(),
                                R.string.new_journal_entry_updated_message,
                                Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                JournalEntry entry = new JournalEntry();
                entry.setBody(body);
                entry.setTitle(title);
                entry.setFolderName(folderName);
                entry.setUpdatedDate(new Date());
                entry.setEntryDate(new Date());
                mJournalEntryViewModel.insert(entry);
                Toast.makeText(
                        getApplicationContext(),
                        R.string.new_journal_entry_inserted_message,
                        Toast.LENGTH_LONG).show();
            }
            JournalSyncUtils.startImmediateSync(this);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.new_journal_entry_cancelled_message,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void logOutCurrentUser() {
        mJournalEntryViewModel.clearDatabase();
        auth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_log_out_current_user) {
            logOutCurrentUser();
            return true;
        }
        if (itemId == R.id.menu_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        if (itemId == R.id.menu_manage_journal_folders) {
            Intent intent = new Intent(MainActivity.this, FolderManagementActivity.class);
            startActivity(intent);
        }if (itemId == R.id.menu_sync_immediately) {
            syncImmediately();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onJournalEntryClicked(int journalEntryId) {
        Intent intent = new Intent(MainActivity.this, NewJournalEntryActivity.class);
        intent.putExtra(NewJournalEntryActivity.EXTRA_JOURNAL_ENTRY_ID, journalEntryId);
        startActivityForResult(intent, NEW_JOURNAL_ENTRY_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onJournalEntrySwipedLeft(JournalEntry journalEntry, int position) {
        mJournalEntryViewModel.deleteJournalEntryById(journalEntry);
        mJournalAdapter.notifyItemRemoved(position);
        JournalSyncUtils.startImmediateSync(this);
        Toast.makeText(MainActivity.this,R.string.journal_item_removed_message, Toast.LENGTH_LONG);
    }

    private void syncImmediately() {
        Toast.makeText(MainActivity.this, R.string.immediate_sync_started_message, Toast.LENGTH_LONG)
                .show();
        JournalSyncUtils.startImmediateSync(this);
    }
}
