package com.example.android.journalapp;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

/**
 * Created by EA on 6/28/2018.
 */

public class DiaryRepository {
    private DiaryDao mDiaryDao;
    private LiveData<List<DiaryEntity>> mAllJournals;

    DiaryRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mDiaryDao = db.diaryDao();
        mAllJournals = mDiaryDao.getAllJournals();
    }

    LiveData<List<DiaryEntity>> getAllWords() {
        return mAllJournals;
    }

    public void insert (DiaryEntity diaryEntity) {
        new insertAsyncTask(mDiaryDao).execute(diaryEntity);
    }

    private static class insertAsyncTask extends AsyncTask<DiaryEntity, Void, Void> {

        private DiaryDao mAsyncTaskDao;

        insertAsyncTask(DiaryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final DiaryEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

	
	//stuffs for deletion
    public void delete (DiaryEntity diaryEntity) {
        new deleteAsyncTask(mDiaryDao).execute(diaryEntity);
    }

    //async task for delete operation
    private static class deleteAsyncTask extends AsyncTask<DiaryEntity, Void, Void> {

        private DiaryDao mAsyncTaskDao;

        deleteAsyncTask(DiaryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final DiaryEntity... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

	//stuffs for update
    public void update (DiaryEntity diaryEntity) {
        new updateAsyncTask(mDiaryDao).execute(diaryEntity);
    }

    //async task for update operation
    private static class updateAsyncTask extends AsyncTask<DiaryEntity, Void, Void> {

        private DiaryDao mAsyncTaskDao;

        updateAsyncTask(DiaryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final DiaryEntity... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
}
