package com.buildbrothers.android.journalapp;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by EA on 6/28/2018.
 */
@Dao
public interface DiaryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(DiaryEntity diaryEntity);

    @Insert
    void insertAll(DiaryEntity... diaryEntities);

    @Query("DELETE FROM journal_table")
    void deleteAll();

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(DiaryEntity diaryEntity);

    @Delete
    void delete(DiaryEntity diaryEntity);

    @Query("SELECT * from journal_table ORDER BY _id DESC")
    LiveData<List<DiaryEntity>> getAllJournals();
}
