package alc.kofiamparbeng.ampjournal.sync;

import android.content.Context;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import alc.kofiamparbeng.ampjournal.R;
import alc.kofiamparbeng.ampjournal.db.JournalDatabase;
import alc.kofiamparbeng.ampjournal.entities.JournalEntry;

public class JournalSyncTask {
    private static final String TAG = "JournalSyncTask";

    public static void syncJournal(Context context) {

        Log.d(TAG, "Starting sync");
        boolean shouldSync = PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.pref_sync_data_key),
                        context.getResources().getBoolean(R.bool.pref_sync_data_default_value));
        if (shouldSync) {
            FirebaseAuth auth = FirebaseAuth.getInstance();

            FirebaseUser currentUser = auth.getCurrentUser();
            if (currentUser != null) {
                List<JournalEntry> entries = JournalDatabase.getDatabase(context).journalDao()
                        .getAllEntriesList();
                DatabaseReference fireDatabase = FirebaseDatabase.getInstance().getReference();
                fireDatabase.child("journals").child(currentUser.getUid()).setValue(entries);
            }
        }
        else{
            Log.d(TAG, "Not configured to sync");
        }
    }

}
