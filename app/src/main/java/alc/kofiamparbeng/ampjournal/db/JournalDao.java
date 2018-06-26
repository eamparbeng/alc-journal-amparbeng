package alc.kofiamparbeng.ampjournal.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.arch.persistence.room.migration.Migration;

import java.util.List;

import alc.kofiamparbeng.ampjournal.entities.JournalEntry;

@Dao
public interface JournalDao {

    @Insert
    void insert(JournalEntry entry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(JournalEntry entry);

    @Query("DELETE FROM journal_tbl")
    void deleteAll();

    @Query("SELECT * from journal_tbl ORDER BY entryDate DESC")
    LiveData<List<JournalEntry>> getAllEntries();

    @Query("SELECT * from journal_tbl WHERE id = :id")
    LiveData<JournalEntry> getJournalEntryById(int id);

    @Query("SELECT * from journal_tbl WHERE id = :id")
    JournalEntry getJournalEntryByIdNoWatch(int id);
}
