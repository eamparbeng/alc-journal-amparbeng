package alc.kofiamparbeng.ampjournal.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import alc.kofiamparbeng.ampjournal.entities.JournalEntry;
import alc.kofiamparbeng.ampjournal.util.Converters;

@Database(entities = {JournalEntry.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class JournalDatabase extends RoomDatabase {
    public abstract JournalDao journalDao();
    private static JournalDatabase INSTANCE;

    public static JournalDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (JournalDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            JournalDatabase.class, "journal_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}