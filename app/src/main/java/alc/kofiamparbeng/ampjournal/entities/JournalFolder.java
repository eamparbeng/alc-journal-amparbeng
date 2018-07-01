package alc.kofiamparbeng.ampjournal.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "folder_tbl")
public class JournalFolder {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "folder_name")
    private String mName;


    @NonNull
    @ColumnInfo(name = "creation_date")
    private Date mCreationDate;

    public void setName(@NonNull String title) {
        this.mName = title;
    }

    public String getName() {
        return mName;
    }

    public void setCreationDate(@NonNull Date date) {
        this.mCreationDate = date;
    }

    public Date getCreationDate() {
        return mCreationDate;
    }


    public int getId() {
        return id;
    }

    public void setId(int newId) {
        this.id = newId;
    }
}
