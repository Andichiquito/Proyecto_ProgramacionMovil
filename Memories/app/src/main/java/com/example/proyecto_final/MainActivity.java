package com.example.proyecto_final;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_ENTRY = 1;
    private static final int REQUEST_CODE_EDIT_ENTRY = 2;
    private RecyclerView recyclerView;
    private JournalAdapter adapter;
    private List<JournalEntry> journalEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        journalEntries = loadEntries();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new JournalAdapter(journalEntries, new JournalAdapter.OnEntryClickListener() {
            @Override
            public void onEntryClick(JournalEntry entry) {
                Intent intent = new Intent(MainActivity.this, EntryActivity.class);
                intent.putExtra(EntryActivity.EXTRA_ENTRY_ID, entry.getId());
                intent.putExtra(EntryActivity.EXTRA_ENTRY_TEXT, entry.getText());
                startActivityForResult(intent, REQUEST_CODE_EDIT_ENTRY);
            }
        });
        recyclerView.setAdapter(adapter);

        Button addEntryButton = findViewById(R.id.add_entry_button);
        addEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EntryActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_ENTRY);
            }
        });

        Switch themeSwitch = findViewById(R.id.theme_switch);
        themeSwitch.setChecked(isNightModeEnabled());
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            recreate(); // Apply the theme changes
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String entryText = data.getStringExtra(EntryActivity.EXTRA_ENTRY_TEXT);

            if (requestCode == REQUEST_CODE_ADD_ENTRY) {
                JournalEntry newEntry = new JournalEntry(entryText);
                journalEntries.add(newEntry);
            } else if (requestCode == REQUEST_CODE_EDIT_ENTRY) {
                String entryId = data.getStringExtra(EntryActivity.EXTRA_ENTRY_ID);
                for (JournalEntry entry : journalEntries) {
                    if (entry.getId().equals(entryId)) {
                        entry.setText(entryText);
                        break;
                    }
                }
            }

            adapter.notifyDataSetChanged();
            saveEntries();
        }
    }

    private void saveEntries() {
        SharedPreferences sharedPreferences = getSharedPreferences("JournalApp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> entriesSet = new HashSet<>();
        for (JournalEntry entry : journalEntries) {
            entriesSet.add(entry.getId() + "\n" + entry.getText() + "\n" + entry.getDate());
        }
        editor.putStringSet("entries", entriesSet);
        editor.apply();
    }

    private List<JournalEntry> loadEntries() {
        SharedPreferences sharedPreferences = getSharedPreferences("JournalApp", Context.MODE_PRIVATE);
        Set<String> entriesSet = sharedPreferences.getStringSet("entries", new HashSet<String>());
        List<JournalEntry> entriesList = new ArrayList<>();
        for (String entryString : entriesSet) {
            String[] parts = entryString.split("\n");
            if (parts.length == 3) {
                entriesList.add(new JournalEntry(parts[0], parts[1], parts[2]));
            }
        }
        return entriesList;
    }

    private boolean isNightModeEnabled() {
        int nightMode = getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
        return nightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES;
    }
}
