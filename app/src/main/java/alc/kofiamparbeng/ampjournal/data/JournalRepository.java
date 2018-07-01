package alc.kofiamparbeng.ampjournal.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import alc.kofiamparbeng.ampjournal.db.FolderDao;
import alc.kofiamparbeng.ampjournal.db.JournalDao;
import alc.kofiamparbeng.ampjournal.db.JournalDatabase;
import alc.kofiamparbeng.ampjournal.entities.JournalEntry;
import alc.kofiamparbeng.ampjournal.entities.JournalFolder;

public class JournalRepository {
    private JournalDao mJournalDao;
    private FolderDao mFolderDao;
    private LiveData<List<JournalEntry>> mAllEntries;
    private LiveData<List<JournalFolder>> mAllFolders;

    JournalRepository(Application application) {
        JournalDatabase db = JournalDatabase.getDatabase(application);
        mJournalDao = db.journalDao();
        mFolderDao =db.folderDao();
        mAllEntries = mJournalDao.getAllEntries();
        mAllFolders = mFolderDao.getAllFolders();
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

    public void clearDb() {
        new clearDbAsyncTask(mJournalDao, mFolderDao).execute();
    }

    public void deleteJournalEntryById(JournalEntry entry) {
        new deleteJournalTask(mJournalDao).execute(entry);
    }

    public void deleteFolderById(JournalFolder folder) {
        new deleteFolderTask(mFolderDao).execute(folder);
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

    private static class deleteJournalTask extends AsyncTask<JournalEntry, Void, Void> {

        private JournalDao mAsyncTaskDao;

        deleteJournalTask(JournalDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final JournalEntry... params) {
            mAsyncTaskDao.deleteJournalEntryById(params[0].getId());
            return null;
        }
    }

    private static class deleteFolderTask extends AsyncTask<JournalFolder, Void, Void> {

        private FolderDao mAsyncTaskDao;

        deleteFolderTask(FolderDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final JournalFolder... params) {
            mAsyncTaskDao.deleteFolderById(params[0].getId());
            return null;
        }
    }

    private static class clearDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private JournalDao mAsyncJournalTaskDao;
        private FolderDao mAsyncFolderTaskDao;

        clearDbAsyncTask(JournalDao journalDao, FolderDao folderDao) {
            mAsyncFolderTaskDao = folderDao;
            mAsyncJournalTaskDao = journalDao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncJournalTaskDao.deleteAll();
            mAsyncFolderTaskDao.deleteAll();
            return null;
        }
    }

    LiveData<List<JournalFolder>> getAllFolders() {
        return mAllFolders;
    }
    LiveData<JournalFolder> getFolderById(int id) {
        return mFolderDao.getFolderById(id);
    }

    JournalFolder getFolderByIdNoWatch(int id) {
        return mFolderDao.getFolderyByIdNoWatch(id);
    }

    public void insert(JournalFolder word) {
        new insertFolderAsyncTask(mFolderDao).execute(word);
    }

    public void update(JournalFolder word) {
        new updateFolderAsyncTask(mFolderDao).execute(word);
    }

    private static class insertFolderAsyncTask extends AsyncTask<JournalFolder, Void, Void> {

        private FolderDao mAsyncTaskDao;

        insertFolderAsyncTask(FolderDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final JournalFolder... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class updateFolderAsyncTask extends AsyncTask<JournalFolder, Void, Void> {

        private FolderDao mAsyncTaskDao;

        updateFolderAsyncTask(FolderDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final JournalFolder... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
}
