package alc.kofiamparbeng.ampjournal.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import java.util.Date;

import alc.kofiamparbeng.ampjournal.entities.JournalEntry;
import alc.kofiamparbeng.ampjournal.entities.JournalFolder;
import alc.kofiamparbeng.ampjournal.util.Converters;

@Database(entities = {JournalEntry.class, JournalFolder.class}, version = 4)
@TypeConverters({Converters.class})
public abstract class JournalDatabase extends RoomDatabase {
    public abstract JournalDao journalDao();
    public abstract FolderDao folderDao();
    private static JournalDatabase INSTANCE;

    public static JournalDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (JournalDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            JournalDatabase.class, "journal_db")
                            .addMigrations(MIGRATION_1_2,MIGRATION_2_3, MIGRATION_3_4)
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE journal_tbl "
                    + " ADD COLUMN updatedDate INTEGER");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE folder_tbl "
                    + " (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL"
                    + " ,folder_name TEXT NOT NULL"
                    + " , creation_date INTEGER NOT NULL)");
            database.execSQL("INSERT INTO folder_tbl "
                    + " (folder_name ,creation_date )"
                    + " VALUES (?, ?)", new Object[]{"Diary", new Date().getTime()});
            database.execSQL("INSERT INTO folder_tbl "
                    + " (folder_name ,creation_date )"
                    + " VALUES (?, ?)", new Object[]{"Reminders", new Date().getTime()});
            database.execSQL("INSERT INTO folder_tbl "
                    + " (folder_name ,creation_date )"
                    + " VALUES (?, ?)", new Object[]{"Tasks", new Date().getTime()});
            database.execSQL("INSERT INTO folder_tbl "
                    + " (folder_name ,creation_date )"
                    + " VALUES (?, ?)", new Object[]{"Notes", new Date().getTime()});
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE journal_tbl "
                    + " ADD COLUMN folder_name TEXT");
        }
    };

}