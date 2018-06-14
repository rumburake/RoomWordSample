package com.threecats.roomwordsample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    private final LayoutInflater layoutInflater;
    private List<Word> words;

    WordListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        if (words != null) {
            holder.wordView.setText(words.get(position).getWord());
        } else {
            holder.wordView.setText("--");
        }
    }

    @Override
    public int getItemCount() {
        if (words == null) return 0;
        return words.size();
    }

    public class WordViewHolder extends RecyclerView.ViewHolder {
        private final TextView wordView;

        public WordViewHolder(View itemView) {
            super(itemView);
            wordView = itemView.findViewById(R.id.textView);
        }
    }

    void setWords(List<Word> words) {
        this.words = words;
        notifyDataSetChanged();
    }

}
