package com.threecats.roomwordsample;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

public class WordRepository {

    private WordDao wordDao;
    private LiveData<List<Word>> allWords;

    WordRepository(Application app) {
        WordDatabase wordDatabase = WordDatabase.getDatabase(app);
        wordDao = wordDatabase.wordDao();
        allWords = wordDao.getAllWords();
    }

    public LiveData<List<Word>> getAllWords() {
        return allWords;
    }

    public void insert(Word word) {
        new InsertAsyncTask(wordDao).execute(word);
    }

    public void deleteAll() {
        new DeleteAsyncTask(wordDao).execute();
    }

    public void fill() {
        new FillAsyncTask(wordDao).execute();
    }

    private static class FillAsyncTask extends AsyncTask<Void, Void, Void> {

        private WordDao wordDao;

        FillAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        static final String[] s1 = {"eek", "squeak", "woof", "meow", "hoot"};
        static final String[] s2 = {"mouse", "snake", "cat", "horse", "wolf"};
        static final String[] s3 = {"nightly", "daily", "in the summer", "in the rain", "in the tree"};

        int MAX_WORDS = 10000;


        @Override
        protected Void doInBackground(Void... voids) {
            Random r = new Random();
            for (int i = 0; i < MAX_WORDS; i++) {
                wordDao.insert(new Word(s1[r.nextInt(s1.length)] + " " + s2[r.nextInt(s2.length)] + " " + s3[r.nextInt(s3.length)]));
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Resources res = App.get().getResources();
            String text = String.format(res.getString(R.string.done_populate), MAX_WORDS);
            Toast.makeText(App.get(), text, Toast.LENGTH_LONG).show();
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Void, Void, Void> {

        private WordDao wordDao;

        DeleteAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            wordDao.deleteAll();
            return null;
        }
    }

    private static class InsertAsyncTask extends AsyncTask<Word, Void, Void> {

        private WordDao wordDao;

        InsertAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.insert(words[0]);
            return null;
        }
    }
}
