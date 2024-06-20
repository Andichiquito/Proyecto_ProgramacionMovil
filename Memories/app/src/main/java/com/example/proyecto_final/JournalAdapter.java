package com.example.proyecto_final;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalViewHolder> {

    private List<JournalEntry> journalEntries;
    private OnEntryClickListener onEntryClickListener;

    public JournalAdapter(List<JournalEntry> journalEntries, OnEntryClickListener onEntryClickListener) {
        this.journalEntries = journalEntries;
        this.onEntryClickListener = onEntryClickListener;
    }

    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.journal_entry_item, parent, false);
        return new JournalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, int position) {
        JournalEntry entry = journalEntries.get(position);
        holder.entryDateView.setText(entry.getDate());
        holder.entryTextView.setText(entry.getText());
        holder.itemView.setOnClickListener(v -> onEntryClickListener.onEntryClick(entry));
    }

    @Override
    public int getItemCount() {
        return journalEntries.size();
    }

    static class JournalViewHolder extends RecyclerView.ViewHolder {
        TextView entryDateView;
        TextView entryTextView;

        JournalViewHolder(View itemView) {
            super(itemView);
            entryDateView = itemView.findViewById(R.id.entry_date);
            entryTextView = itemView.findViewById(R.id.entry_text);
        }
    }

    public interface OnEntryClickListener {
        void onEntryClick(JournalEntry entry);
    }
}