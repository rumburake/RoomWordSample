package com.threecats.roomwordsample;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_ADD_WORD = 101;
    private WordViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton add_b = (FloatingActionButton) findViewById(R.id.add_b);
        add_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newWordIntent = new Intent(MainActivity.this, NewWordActivity.class);
                startActivityForResult(newWordIntent, REQ_ADD_WORD);
            }
        });

        FloatingActionButton zap_b = (FloatingActionButton) findViewById(R.id.zap_b);
        zap_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.zap();
            }
        });

        FloatingActionButton fill_b = (FloatingActionButton) findViewById(R.id.fill_b);
        fill_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.fill();
            }
        });

        RecyclerView wordsView = findViewById(R.id.recycler_view);
        final WordListAdapter wordsAdapter = new WordListAdapter(this);
        wordsView.setAdapter(wordsAdapter);
        wordsView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        viewModel.getAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(@Nullable List<Word> words) {
                wordsAdapter.setWords(words);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_ADD_WORD) {
            if (resultCode == RESULT_OK) {
                Word word = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY));
                viewModel.insert(word);
            } else {
                Toast.makeText(this, R.string.empty_not_saved, Toast.LENGTH_LONG).show();
            }
        }
    }
}
