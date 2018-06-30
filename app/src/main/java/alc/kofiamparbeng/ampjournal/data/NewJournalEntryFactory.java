package alc.kofiamparbeng.ampjournal.data;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import alc.kofiamparbeng.ampjournal.db.JournalDatabase;

public class NewJournalEntryFactory  extends ViewModelProvider.NewInstanceFactory {

        private final JournalDatabase mDb;
        private final int mJournalEntryId;

        public NewJournalEntryFactory(JournalDatabase database, int journalEntryId) {
            mDb = database;
            mJournalEntryId = journalEntryId;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new JournalEntryViewModel(mDb, mJournalEntryId);
        }
    }
