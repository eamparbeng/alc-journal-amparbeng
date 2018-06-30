package alc.kofiamparbeng.ampjournal.sync;

import android.app.IntentService;
import android.content.Intent;

public class JournalCloudFetchIntentService extends IntentService {

    public JournalCloudFetchIntentService() {
        super("JournalCloudFetchIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        JournalSyncTask.getInstance().syncJournalFromCloud(this);
    }
}