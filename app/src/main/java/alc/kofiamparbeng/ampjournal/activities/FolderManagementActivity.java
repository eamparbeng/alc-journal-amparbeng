package alc.kofiamparbeng.ampjournal.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import alc.kofiamparbeng.ampjournal.R;
import alc.kofiamparbeng.ampjournal.data.FolderAdapter;
import alc.kofiamparbeng.ampjournal.data.FolderListViewModel;
import alc.kofiamparbeng.ampjournal.data.FolderSwipeToDeleteHandler;
import alc.kofiamparbeng.ampjournal.entities.JournalFolder;
import alc.kofiamparbeng.ampjournal.sync.JournalSyncUtils;

public class FolderManagementActivity extends AppCompatActivity implements FolderAdapter.FolderEventsListener {
    public static final String EXTRA_FOLDER_SELECTER_SELECTED_FOLDER_NAME = "";
    public static final int NEW_FOLDER_ACTIVITY_REQUEST_CODE = 713;

    private FolderAdapter mFolderAdapter;
    private RecyclerView mRecyclerView;
    private FolderListViewModel mFolderModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_management);

        prepareFloatingActionButton();
        prepareRecyclerView();
        prepareViewModel();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void prepareFloatingActionButton() {
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_new_folder);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FolderManagementActivity.this, EditFolderActivity.class);
                startActivityForResult(intent, NEW_FOLDER_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    private void prepareViewModel() {
        mFolderModel = ViewModelProviders.of(this).get(FolderListViewModel.class);
        mFolderModel.getAllFolders().observe(this, new Observer<List<JournalFolder>>() {
            @Override
            public void onChanged(@Nullable final List<JournalFolder> folders) {
                // Update the cached copy of the words in the adapter.
                mFolderAdapter.setFolders(folders);
            }
        });
    }

    private void prepareRecyclerView() {
        mFolderAdapter = new FolderAdapter(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.folder_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setAdapter(mFolderAdapter);
        mRecyclerView.setLayoutManager(layoutManager);

        mFolderAdapter.setFolderClickListener(this);

        FolderSwipeToDeleteHandler swipeToDeleteHandler = new FolderSwipeToDeleteHandler(this);
        ItemTouchHelper touchHelper = new ItemTouchHelper(swipeToDeleteHandler);
        touchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_FOLDER_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            final String folderName = data.getStringExtra(EditFolderActivity.EXTRA_NEW_FOLDER_NAME);


            if (data.hasExtra(EditFolderActivity.EXTRA_JOURNAL_FOLDER_ID)) {
                final int id = data.getIntExtra(EditFolderActivity.EXTRA_JOURNAL_FOLDER_ID, EditFolderActivity.DEFAULT_JOURNAL_FOLDER_ID);
                final LiveData<JournalFolder> folderLiveData = mFolderModel.getFolderById(id);
                folderLiveData.observe(this, new Observer<JournalFolder>() {
                    @Override
                    public void onChanged(@Nullable final JournalFolder folder) {
                        folderLiveData.removeObserver(this);
                        folder.setName(folderName);
                        folder.setId(id);
                        mFolderModel.update(folder);
                        Toast.makeText(
                                getApplicationContext(),
                                R.string.new_folder_updated_message,
                                Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                JournalFolder folder = new JournalFolder();
                folder.setName(folderName);
                folder.setCreationDate(new Date());
                mFolderModel.insert(folder);
                Toast.makeText(
                        getApplicationContext(),
                        R.string.new_folder_inserted_message,
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.new_folder_cancelled_message,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFolderClicked(String folderName) {

    }

    @Override
    public void onFolderClickedId(int folderId) {
        Intent intent = new Intent(FolderManagementActivity.this, EditFolderActivity.class);
        intent.putExtra(EditFolderActivity.EXTRA_JOURNAL_FOLDER_ID, folderId);
        startActivityForResult(intent, NEW_FOLDER_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onFolderSwipedLeft(JournalFolder folder, int position) {
        mFolderModel.deleteFolderById(folder);
        mFolderAdapter.notifyItemRemoved(position);
        JournalSyncUtils.startImmediateSync(this);
        Toast.makeText(FolderManagementActivity.this,R.string.folder_item_removed_message, Toast.LENGTH_LONG);
    }
}
