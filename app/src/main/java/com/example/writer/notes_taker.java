package com.example.writer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.writer.moduls.Notes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class notes_taker extends AppCompatActivity {
    private EditText title;
    private EditText note;
    private ImageView save;
    Notes notes;
    boolean isOldNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_taker);

        title = findViewById(R.id.titleText);
        note = findViewById(R.id.noteText);
        save = findViewById(R.id.image_save);

        notes = new Notes();
        try {
            notes = (Notes) getIntent().getSerializableExtra("old_note");
            title.setText(notes.getTitle());
            note.setText(notes.getRealNote());
            isOldNote = true;
        } catch (Exception e) {
            e.printStackTrace();
        }


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title_text = title.getText().toString();
                String note_text = note.getText().toString();
                String real_note_text = note_text;
                if (!note_text.isEmpty()) {
                    SimpleDateFormat formatT = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                    Date date = new Date();

                    if (!isOldNote) {
                        notes = new Notes();
                    }
                    if (title_text.isEmpty()) {
                        notes.setTitle(" ");
                    } else {
                        notes.setTitle(title_text);
                    }
                    if (note_text.length() > 50) {
                        notes.setNote(note_text.substring(0, 50) + "...");
                        notes.setRealNote(real_note_text);
                    } else {
                        notes.setNote(note_text);
                        notes.setRealNote(real_note_text);
                    }
                    notes.setDate(formatT.format(date));

                    Intent intent = new Intent();
                    intent.putExtra("notes", notes);
                    setResult(Activity.RESULT_OK, intent);
                }
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        String title_text = title.getText().toString();
        String note_text = note.getText().toString();
        String real_note_text = note_text;
        if (!note_text.isEmpty()) {
            SimpleDateFormat formatT = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
            Date date = new Date();

            if (!isOldNote) {
                notes = new Notes();
            }
            if (title_text.isEmpty()) {
                notes.setTitle(" ");
            } else {
                notes.setTitle(title_text);
            }
            if (note_text.length() > 50) {
                notes.setNote(note_text.substring(0, 50) + "...");
                notes.setRealNote(real_note_text);
            } else {
                notes.setNote(note_text);
                notes.setRealNote(real_note_text);
            }
            notes.setDate(formatT.format(date));

            Intent intent = new Intent();
            intent.putExtra("notes", notes);
            setResult(Activity.RESULT_OK, intent);
        }

        super.onBackPressed();

    }
}