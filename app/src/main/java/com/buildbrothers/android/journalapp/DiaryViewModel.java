package com.buildbrothers.android.journalapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

/**
 * Created by EA on 6/28/2018.
 */

public class DiaryViewModel extends AndroidViewModel {
    private DiaryRepository mRepository;
    private LiveData<List<DiaryEntity>> mAllJournals;

    public DiaryViewModel (Application application) {
        super(application);
        mRepository = new DiaryRepository(application);
        mAllJournals = mRepository.getAllWords();
    }

    LiveData<List<DiaryEntity>> getAllJournals() { return mAllJournals; }

    public void insert(DiaryEntity diaryEntity) { mRepository.insert(diaryEntity); }

    public void delete(DiaryEntity diaryEntity) {
        mRepository.delete(diaryEntity);
    }

    public void update(DiaryEntity diaryEntity) {
        mRepository.update(diaryEntity);
    }
}
