package alc.kofiamparbeng.ampjournal.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "journal_tbl")
public class JournalEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "title")
    private String mTitle;
    @NonNull
    @ColumnInfo(name = "body")
    private String mBody;
    @NonNull
    @ColumnInfo(name = "entryDate")
    private Date mEntryDate;

    @ColumnInfo(name = "updatedDate")
    private Date mUpdatedDate;

    @ColumnInfo(name = "folder_name")
    private String mFolderName;

    public void setTitle(@NonNull String title) {
        this.mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setBody(@NonNull String body) {
        this.mBody = body;
    }

    public String getBody() {
        return mBody;
    }

    public void setEntryDate(@NonNull Date date) {
        this.mEntryDate = date;
    }

    public Date getEntryDate() {
        return mEntryDate;
    }


    public int getId() {
        return id;
    }

    public void setId(int newId) {
        this.id = newId;
    }

    public void setUpdatedDate(@NonNull Date date) {
        this.mUpdatedDate = date;
    }

    public Date getUpdatedDate() {
        return mUpdatedDate;
    }

    public void setFolderName(@NonNull String folderName) {
        this.mFolderName = folderName;
    }

    public String getFolderName() {
        return mFolderName;
    }
}
