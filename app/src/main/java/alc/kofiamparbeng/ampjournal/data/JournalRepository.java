package alc.kofiamparbeng.ampjournal.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import alc.kofiamparbeng.ampjournal.db.JournalDao;
import alc.kofiamparbeng.ampjournal.db.JournalDatabase;
import alc.kofiamparbeng.ampjournal.entities.JournalEntry;

public class JournalRepository {
    private JournalDao mJournalDao;
    private LiveData<List<JournalEntry>> mAllEntries;

    JournalRepository(Application application) {
        JournalDatabase db = JournalDatabase.getDatabase(application);
        mJournalDao = db.journalDao();
        mAllEntries = mJournalDao.getAllEntries();
    }

    LiveData<List<JournalEntry>> getAllEntries() {
        return mAllEntries;
    }
    LiveData<JournalEntry> getJournalEntryById(int id) {
        return mJournalDao.getJournalEntryById(id);
    }

    JournalEntry getJournalEntryByIdNoWatch(int id) {
        return mJournalDao.getJournalEntryByIdNoWatch(id);
    }

    public void insert(JournalEntry word) {
        new insertAsyncTask(mJournalDao).execute(word);
    }

    public void update(JournalEntry word) {
        new updateAsyncTask(mJournalDao).execute(word);
    }

    private static class insertAsyncTask extends AsyncTask<JournalEntry, Void, Void> {

        private JournalDao mAsyncTaskDao;

        insertAsyncTask(JournalDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final JournalEntry... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<JournalEntry, Void, Void> {

        private JournalDao mAsyncTaskDao;

        updateAsyncTask(JournalDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final JournalEntry... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
}
