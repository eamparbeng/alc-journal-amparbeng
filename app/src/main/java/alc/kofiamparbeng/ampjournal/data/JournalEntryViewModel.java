package alc.kofiamparbeng.ampjournal.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import alc.kofiamparbeng.ampjournal.db.JournalDatabase;
import alc.kofiamparbeng.ampjournal.entities.JournalEntry;

public class JournalEntryViewModel extends ViewModel {
    private LiveData<JournalEntry> mJournalEntry;

    public JournalEntryViewModel(JournalDatabase database, int journalEntryId) {
        mJournalEntry = database.journalDao().getJournalEntryById(journalEntryId);
    }

    public LiveData<JournalEntry> getJournalEntry() { return mJournalEntry; }
}
