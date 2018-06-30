package alc.kofiamparbeng.ampjournal.data;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import alc.kofiamparbeng.ampjournal.db.JournalDatabase;

public class NewFolderFactory extends ViewModelProvider.NewInstanceFactory {

        private final JournalDatabase mDb;
        private final int mFolderId;

        public NewFolderFactory(JournalDatabase database, int folderId) {
            mDb = database;
            mFolderId = folderId;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new FolderItemViewModel(mDb, mFolderId);
        }
    }
