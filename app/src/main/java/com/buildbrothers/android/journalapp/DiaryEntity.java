package com.buildbrothers.android.journalapp;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by EA on 6/26/2018.
 */

@Entity(tableName = "journal_table")
public class DiaryEntity {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int _id;
    private String text;
    private String date_added;
    private String last_modified;
    private int sync_status;

    public DiaryEntity(int _id, String text, String date_added, String last_modified, int sync_status) {
        this._id = _id;
        this.text = text;
        this.date_added = date_added;
        this.last_modified = last_modified;
        this.sync_status = sync_status;
    }

    @NonNull
    public int getId() {
        return _id;
    }

    public void setId(@NonNull int _id) {
        this._id = _id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getLast_modified() {
        return last_modified;
    }

    public void setLast_modified(String last_modified) {
        this.last_modified = last_modified;
    }

    public int getSync_status() {
        return sync_status;
    }

    public void setSync_status(int sync_status) {
        this.sync_status = sync_status;
    }
}
