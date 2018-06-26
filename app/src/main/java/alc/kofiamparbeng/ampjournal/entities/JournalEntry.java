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
    private Date mDate;

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

    public void setDate(@NonNull Date date) {
        this.mDate = date;
    }

    public Date getDate() {
        return mDate;
    }


    public int getId() {
        return id;
    }

    public void setId(int newId) {
        this.id = newId;
    }

}
