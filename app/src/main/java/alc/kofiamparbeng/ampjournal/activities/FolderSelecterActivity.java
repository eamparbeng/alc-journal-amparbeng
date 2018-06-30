package alc.kofiamparbeng.ampjournal.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import alc.kofiamparbeng.ampjournal.R;
import alc.kofiamparbeng.ampjournal.data.FolderAdapter;
import alc.kofiamparbeng.ampjournal.data.FolderListViewModel;
import alc.kofiamparbeng.ampjournal.entities.JournalFolder;

public class FolderSelecterActivity extends AppCompatActivity implements FolderAdapter.FolderEventsListener {
    public static final String EXTRA_FOLDER_SELECTER_SELECTED_FOLDER_NAME = "";

    private FolderAdapter mFolderAdapter;
    private RecyclerView mRecyclerView;
    private FolderListViewModel mFolderModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_selecter);

        prepareRecyclerView();
        prepareViewModel();


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
    }


    @Override
    public void onFolderClicked(String folderName) {
        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_FOLDER_SELECTER_SELECTED_FOLDER_NAME, folderName);
        setResult(RESULT_OK, replyIntent);
        finish();
    }

    @Override
    public void onFolderClickedId(int folderId) {

    }

    @Override
    public void onFolderSwipedLeft(JournalFolder folder, int position) {

    }
}
