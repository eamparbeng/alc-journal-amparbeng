package alc.kofiamparbeng.ampjournal.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import alc.kofiamparbeng.ampjournal.db.JournalDatabase;
import alc.kofiamparbeng.ampjournal.entities.JournalEntry;
import alc.kofiamparbeng.ampjournal.entities.JournalFolder;

public class FolderListViewModel extends AndroidViewModel {
    private JournalRepository mRepository;
    private LiveData<List<JournalFolder>> mAllFolders;

    public FolderListViewModel(Application application) {
        super(application);
        mRepository = new JournalRepository(application);
        mAllFolders = mRepository.getAllFolders();
    }

    public void deleteFolderById(JournalFolder folder) {
        mRepository.deleteFolderById(folder);
    }

    public LiveData<List<JournalFolder>> getAllFolders() {
        return mAllFolders;
    }

    public LiveData<JournalFolder> getFolderById(int id) {
        return mRepository.getFolderById(id);
    }

    public JournalFolder getFolderByIdNoWatch(int id) {
        return mRepository.getFolderByIdNoWatch(id);
    }

    public void insert(JournalFolder entry) {
        mRepository.insert(entry);
    }

    public void update(JournalFolder entry) {
        mRepository.update(entry);
    }
}
