package alc.kofiamparbeng.ampjournal.sync;

import android.app.IntentService;
import android.content.Intent;

public class JournalSyncIntentService extends IntentService {

    public JournalSyncIntentService() {
        super("JournalSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        JournalSyncTask.syncJournalToCloud(this);
    }
}