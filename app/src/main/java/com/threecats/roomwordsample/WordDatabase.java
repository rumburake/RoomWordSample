package com.threecats.roomwordsample;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.Random;

@Database(entities = {Word.class}, version = 2)
public abstract class WordDatabase extends RoomDatabase {

    public abstract WordDao wordDao();

    private static WordDatabase sInstance;

    public static WordDatabase getDatabase(final Context context) {
        synchronized (WordDatabase.class) {
            if (sInstance == null) {
                sInstance = Room
                        .databaseBuilder(context, WordDatabase.class, "word_database")
//                        .addCallback(openCallback)
                        .build();
            }
        }
        return sInstance;
    }

    private static RoomDatabase.Callback openCallback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateAsync(sInstance).execute();
        }
    };

    private static class PopulateAsync extends AsyncTask<Void, Void, Void> {

        private WordDao wordDao;

        static final String[] s1 = {"eek", "squeak", "woof", "meow", "hoot"};
        static final String[] s2 = {"mouse", "snake", "cat", "horse", "wolf"};
        static final String[] s3 = {"nightly", "daily", "in the summer", "in the rain", "in the tree"};

        int MAX_WORDS = 10000;

        PopulateAsync(WordDatabase wordDatabase) {
            wordDao = wordDatabase.wordDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            wordDao.deleteAll();
            Random r = new Random();
            for (int i = 0; i < MAX_WORDS; i++) {
                wordDao.insert(new Word(s1[r.nextInt(s1.length)] + " " + s2[r.nextInt(s2.length)] + " " + s3[r.nextInt(s3.length)]));
            }

            Resources res = App.get().getResources();
            String text = String.format(res.getString(R.string.done_populate), MAX_WORDS);
            Toast.makeText(App.get(), text, Toast.LENGTH_LONG).show();

            return null;
        }
    }

}
