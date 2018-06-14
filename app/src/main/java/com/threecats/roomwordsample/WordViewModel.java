package com.threecats.roomwordsample;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class WordViewModel extends AndroidViewModel {
    private WordRepository wordRepository;
    private LiveData<List<Word>> allWords;

    public WordViewModel(@NonNull Application application) {
        super(application);
        wordRepository = new WordRepository(application);
        allWords = wordRepository.getAllWords();
    }

    LiveData<List<Word>> getAllWords() {
        return allWords;
    }

    void insert(Word word) {
        wordRepository.insert(word);
    }

    void zap() {
        wordRepository.deleteAll();
    }

    void fill() {
        wordRepository.fill();
    }
}
