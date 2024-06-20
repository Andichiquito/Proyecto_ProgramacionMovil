package com.example.proyecto_final;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EntryActivity extends AppCompatActivity {

    public static final String EXTRA_ENTRY_ID = "entry_id";
    public static final String EXTRA_ENTRY_TEXT = "entry_text";
    private EditText entryEditText;
    private String entryId;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        entryEditText = findViewById(R.id.entry_text);
        Button saveButton = findViewById(R.id.save_button);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ENTRY_ID)) {
            isEditMode = true;
            entryId = intent.getStringExtra(EXTRA_ENTRY_ID);
            String entryText = intent.getStringExtra(EXTRA_ENTRY_TEXT);
            entryEditText.setText(entryText);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entryText = entryEditText.getText().toString();
                Intent resultIntent = new Intent();
                if (isEditMode) {
                    resultIntent.putExtra(EXTRA_ENTRY_ID, entryId);
                }
                resultIntent.putExtra(EXTRA_ENTRY_TEXT, entryText);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}