package alc.kofiamparbeng.ampjournal.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import alc.kofiamparbeng.ampjournal.entities.JournalEntry;

public class JournalListViewModel extends AndroidViewModel {
    private JournalRepository mRepository;
    private LiveData<List<JournalEntry>> mAllEntries;

    public JournalListViewModel(Application application) {
        super(application);
        mRepository = new JournalRepository(application);
        mAllEntries = mRepository.getAllEntries();
    }

    public LiveData<List<JournalEntry>> getAllEntries() {
        return mAllEntries;
    }

    public LiveData<JournalEntry> getJournalEntryById(int id) {
        return mRepository.getJournalEntryById(id);
    }

    public JournalEntry getJournalEntryByIdNoWatch(int id) {
        return mRepository.getJournalEntryByIdNoWatch(id);
    }

    public void insert(JournalEntry entry) {
        mRepository.insert(entry);
    }

    public void update(JournalEntry entry) {
        mRepository.update(entry);
    }
}
