package alc.kofiamparbeng.ampjournal.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import alc.kofiamparbeng.ampjournal.entities.JournalEntry;

public class JournalEntryViewModel extends AndroidViewModel {
    private JournalRepository mRepository;
    private LiveData<List<JournalEntry>> mAllEntries;

    public JournalEntryViewModel (Application application) {
        super(application);
        mRepository = new JournalRepository(application);
        mAllEntries = mRepository.getAllEntries();
    }

    public LiveData<List<JournalEntry>> getAllEntries() { return mAllEntries; }
    public void insert(JournalEntry entry) { mRepository.insert(entry); }
}
