package alc.kofiamparbeng.ampjournal.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import alc.kofiamparbeng.ampjournal.entities.JournalEntry;
import alc.kofiamparbeng.ampjournal.entities.JournalFolder;

@Dao
public interface FolderDao {

    @Insert
    void insert(JournalFolder folder);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(JournalFolder folder);

    @Query("DELETE FROM folder_tbl")
    void deleteAll();

    @Query("SELECT * from folder_tbl ORDER BY folder_name ASC")
    LiveData<List<JournalFolder>> getAllFolders();

    @Query("SELECT * from folder_tbl WHERE id = :id")
    LiveData<JournalFolder> getFolderById(int id);

    @Query("SELECT * from folder_tbl WHERE id = :id")
    JournalFolder getFolderyByIdNoWatch(int id);

    @Query("SELECT * from folder_tbl ORDER BY folder_name ASC")
    List<JournalFolder> getAllFoldersList();

    @Query("DELETE from folder_tbl WHERE id = :id")
    void deleteFolderById(int id);
}
