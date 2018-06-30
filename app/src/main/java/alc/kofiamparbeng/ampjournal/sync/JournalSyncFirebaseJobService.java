package alc.kofiamparbeng.ampjournal.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class JournalSyncFirebaseJobService extends JobService {
    private AsyncTask<Void,Void, Void> mSyncJournalTask;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        mSyncJournalTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                //Do the Synchronization
                JournalSyncTask.syncJournalToCloud(context);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //  COMPLETED (6) Once the weather data is sync'd, call jobFinished with the appropriate arguements
                jobFinished(jobParameters, false);
            }
        };
        mSyncJournalTask.execute();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if(mSyncJournalTask!=null){
            mSyncJournalTask.cancel(true);
        }
        return  true;
    }
}