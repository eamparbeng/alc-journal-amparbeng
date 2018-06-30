package alc.kofiamparbeng.ampjournal.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import alc.kofiamparbeng.ampjournal.R;
import alc.kofiamparbeng.ampjournal.data.FirebaseDatabaseConstants;
import alc.kofiamparbeng.ampjournal.db.FolderDao;
import alc.kofiamparbeng.ampjournal.db.JournalDao;
import alc.kofiamparbeng.ampjournal.db.JournalDatabase;
import alc.kofiamparbeng.ampjournal.entities.JournalEntry;
import alc.kofiamparbeng.ampjournal.entities.JournalFolder;

public class JournalSyncTask {
    private static final String TAG = "JournalSyncTask";

    public static JournalSyncTask getInstance(){
        return  new JournalSyncTask();
    }

    public static void syncJournalToCloud(Context context) {

        Log.d(TAG, "Starting sync");
        boolean shouldSync = PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.pref_sync_data_key),
                        context.getResources().getBoolean(R.bool.pref_sync_data_default_value));
        if (shouldSync) {
            FirebaseAuth auth = FirebaseAuth.getInstance();

            FirebaseUser currentUser = auth.getCurrentUser();
            if (currentUser != null) {
                JournalDatabase journalDatabase = JournalDatabase.getDatabase(context);
                DatabaseReference fireDatabase = FirebaseDatabase.getInstance().getReference();

                //Sync Journal Entries
                List<JournalEntry> entries = journalDatabase.journalDao()
                        .getAllEntriesList();
                if (entries.size() > 0) {
                    fireDatabase.child(FirebaseDatabaseConstants.JOURNAL_TABLE_NAME).child(currentUser.getUid()).setValue(entries);
                }

                //Sync Journal Folders
                List<JournalFolder> folders = journalDatabase.folderDao()
                        .getAllFoldersList();
                if (folders.size() > 0) {
                    fireDatabase.child(FirebaseDatabaseConstants.FOLDER_TABLE_NAME).child(currentUser.getUid()).setValue(folders);
                }
            }
        } else {
            Log.d(TAG, "Not configured to sync");
        }
    }

    public  void syncJournalFromCloud(final Context context) {

        Log.d(TAG, "Starting sync");
        boolean shouldSync = PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.pref_sync_data_key),
                        context.getResources().getBoolean(R.bool.pref_sync_data_default_value));
        if (shouldSync) {
            FirebaseAuth auth = FirebaseAuth.getInstance();

            FirebaseUser currentUser = auth.getCurrentUser();
            if (currentUser != null) {
                DatabaseReference fireDatabase = FirebaseDatabase.getInstance().getReference();
                fireDatabase.child(FirebaseDatabaseConstants.JOURNAL_TABLE_NAME).child(currentUser.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //Schedule Sync if user has data

                                new CloudJournalSyncTask(context).execute(dataSnapshot);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                fireDatabase.child(FirebaseDatabaseConstants.FOLDER_TABLE_NAME).child(currentUser.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //Schedule Sync if user has data

                                new CloudFolderSyncTask(context).execute(dataSnapshot);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        } else {
            Log.d(TAG, "Not configured to sync");
        }
    }

    public class CloudJournalSyncTask extends AsyncTask<DataSnapshot, Void, Void>{
        private  Context context;

        public CloudJournalSyncTask(Context context){
            this.context = context;
        }

        @Override
        protected Void doInBackground(DataSnapshot... snapshots) {
            JournalDatabase journalDatabase = JournalDatabase.getDatabase(context);
            JournalDao dao = journalDatabase.journalDao();
            for (DataSnapshot entrySnapshot : snapshots[0].getChildren()) {
                JournalEntry incomingEntry = entrySnapshot.getValue(JournalEntry.class);
                JournalEntry existingEntry = dao.getJournalEntryByIdNoWatch(incomingEntry.getId());
                if (existingEntry == null) {
                    dao.insert(incomingEntry);
                }
            }
            return null;
        }
    }

    public class CloudFolderSyncTask extends AsyncTask<DataSnapshot, Void, Void>{
        private  Context context;

        public CloudFolderSyncTask(Context context){
            this.context = context;
        }

        @Override
        protected Void doInBackground(DataSnapshot... snapshots) {
            JournalDatabase journalDatabase = JournalDatabase.getDatabase(context);
            FolderDao dao = journalDatabase.folderDao();
            for (DataSnapshot entrySnapshot : snapshots[0].getChildren()) {
                JournalFolder incomingFolder = entrySnapshot.getValue(JournalFolder.class);
                JournalFolder existingFolder = dao.getFolderyByIdNoWatch(incomingFolder.getId());
                if (existingFolder == null) {
                    dao.insert(incomingFolder);
                }
            }
            return null;
        }
    }
}
