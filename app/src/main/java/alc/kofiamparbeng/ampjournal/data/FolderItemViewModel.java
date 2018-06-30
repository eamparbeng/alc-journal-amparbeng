package alc.kofiamparbeng.ampjournal.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import alc.kofiamparbeng.ampjournal.db.JournalDatabase;
import alc.kofiamparbeng.ampjournal.entities.JournalFolder;

public class FolderItemViewModel extends ViewModel {


    private LiveData<JournalFolder> mFolder;

    public FolderItemViewModel(JournalDatabase database, int folderId) {
        mFolder = database.folderDao().getFolderById(folderId);
    }

    public LiveData<JournalFolder> getFolder() { return mFolder; }

}
