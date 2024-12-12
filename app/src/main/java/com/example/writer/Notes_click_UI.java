package com.example.writer;

import androidx.cardview.widget.CardView;

import com.example.writer.adapter.NotesList;
import com.example.writer.moduls.Notes;

public interface Notes_click_UI {
    void onDelete(Notes notes);

    void onClick(Notes notes);

    void onLongClick(Notes notes, CardView cardView);
}
