package com.example.writer;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.writer.Database.RoomDB;
import com.example.writer.adapter.NotesList;
import com.example.writer.moduls.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    RecyclerView recycle;
    RecyclerView pinned;
    FloatingActionButton btn;
    NotesList notesList;
    RoomDB database;
    List<Notes> notes = new ArrayList<>();
    SearchView searchView;
    Notes selected;
    List<Notes> is_pinned = new ArrayList<>();
    TextView text_pin;
    TextView unpin;
    RecyclerView total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchView = findViewById(R.id.searchView);
        recycle = findViewById(R.id.recycler_main);
        btn = findViewById(R.id.floatbtn);
        pinned = findViewById(R.id.is_pinned);
        unpin = findViewById(R.id.unpineded);
        text_pin = findViewById(R.id.pineded);
        //total = findViewById(R.id.total);

        database = RoomDB.getInstance(this);

//        database = Room.databaseBuilder(getApplicationContext(), RoomDB.class, "notesdb")
//                .addMigrations(RoomDB.MIGRATION_1_2)
//                .fallbackToDestructiveMigration()
//                .build();


        for (Notes i : database.mainDAO().getAll()) {
            if (i.isPinned()) {
                is_pinned.add(i);
            } else {
                notes.add(i);
            }
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    filter(newText);
                    return true;
                } else {
                    //updateRecycleView(total);
                    updateRecycleView(is_pinned);
                    updateRecycleView(notes);
                    return true;
                }
            }
        });
        //notes = database.mainDAO().getAll();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, notes_taker.class);
                startActivityForResult(intent, 101);
            }
        });
        //updateRecycleView(total);
        updateRecycleView(is_pinned);
        updateRecycleView(notes);
    }

    private void filter(String newText) {
        List<Notes> filtered_list = new ArrayList<>();
        List<Notes> filtered_pinned_list = new ArrayList<>();
        //List<Notes> total_filter = new ArrayList<>();
        for (Notes singNote : notes) {
            if (singNote.getTitle().toLowerCase().contains(newText.toLowerCase())
                    ||
                    singNote.getRealNote().toLowerCase().contains(newText.toLowerCase())) {
                filtered_list.add(singNote);
            }
        }
        for (Notes singNote : is_pinned) {
            if (singNote.getTitle().toLowerCase().contains(newText.toLowerCase())
                    ||
                    singNote.getRealNote()

                            .toLowerCase().contains(newText.toLowerCase())) {
                filtered_pinned_list.add(singNote);
            }
        }
        if (!filtered_pinned_list.isEmpty()) {
            filtered_list.addAll(0, filtered_pinned_list);
        }
        pinned.setVisibility(View.GONE);
        text_pin.setVisibility(View.GONE);
        unpin.setVisibility(View.GONE);
        notesList.filterList(filtered_list);
//        updateRecycleView(is_pinned);
//        updateRecycleView(notes);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                Notes new_notes = (Notes) data.getSerializableExtra("notes");
                database.mainDAO().insert(new_notes);
                is_pinned.clear();
                notes.clear();
                for (Notes i : database.mainDAO().getAll()) {
                    if (i.isPinned()) {
                        is_pinned.add(i);
                    } else {
                        notes.add(i);
                    }
                }
                //total.clear();
                //total.add(is_pinned);
                //total.add(notes);

                //notes.addAll(database.mainDAO().getAll());
                notesList.notifyDataSetChanged();
            }
        }
        if (requestCode == 102) {
            if (resultCode == Activity.RESULT_OK) {
                Notes new_notes = (Notes) data.getSerializableExtra("notes");
                database.mainDAO().update(new_notes.getID(), new_notes.getTitle(), new_notes.getNote(), new_notes.getCategory(), new_notes.getRealNote());
                is_pinned.clear();
                notes.clear();
                for (Notes i : database.mainDAO().getAll()) {
                    if (i.isPinned()) {
                        is_pinned.add(i);
                    } else {
                        notes.add(i);
                    }
                }
                //total.clear();
                //total.add(is_pinned);
                //total.add(notes);

                //notes.addAll(database.mainDAO().getAll());
                notesList.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycleView(List<Notes> notes) {
        if (!is_pinned.isEmpty()) {
            pinned.setVisibility(View.VISIBLE);
            pinned.setHasFixedSize(true);
            pinned.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
            notesList = new NotesList(MainActivity.this, is_pinned, notes_click_ui);
            pinned.setAdapter(notesList);
            text_pin.setVisibility(View.VISIBLE);
            text_pin.setText("Pinned:");
            if (!notes.isEmpty()) {
                unpin.setVisibility(View.VISIBLE);
                unpin.setText("Unpinned:");
            } else {
                unpin.setVisibility(View.GONE);
            }
        } else {
            pinned.setVisibility(View.GONE);
            text_pin.setVisibility(View.GONE);
            unpin.setVisibility(View.GONE);
        }
        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesList = new NotesList(MainActivity.this, notes, notes_click_ui);
        recycle.setAdapter(notesList);
    }

    private final Notes_click_UI notes_click_ui = new Notes_click_UI() {
        @Override
        public void onDelete(Notes notes) {
            database.mainDAO().delete(notes);
            deleteNoteFromLists(notes);
            notesList.notifyDataSetChanged();
        }

        @Override
        public void onClick(Notes notes) {
            Intent intent = new Intent(MainActivity.this, notes_taker.class);
            intent.putExtra("old_note", notes);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
            selected = new Notes();
            selected = notes;
            showPopUp(cardView);
        }
    };

    private void showPopUp(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.pop_up_menu);
        popupMenu.show();
    }

    private void deleteNoteFromLists(Notes note) {
        if (note.isPinned()) {
            is_pinned.remove(note);
        }
        notes.remove(note);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.pin:
                if (selected.isPinned()) {
                    database.mainDAO().pin(selected.getID(), false);
                    Toast.makeText(MainActivity.this, "Upinned", Toast.LENGTH_SHORT).show();
                } else {
                    database.mainDAO().pin(selected.getID(), true);
                    Toast.makeText(MainActivity.this, "pinned", Toast.LENGTH_SHORT).show();
                }
                is_pinned.clear();
                notes.clear();
                for (Notes i : database.mainDAO().getAll()) {
                    if (i.isPinned()) {
                        is_pinned.add(i);
                    } else {
                        notes.add(i);
                    }
                }
                //notes.addAll(database.mainDAO().getAll());
                notesList.notifyDataSetChanged();
                updateRecycleView(is_pinned);
                updateRecycleView(notes);
                return true;

            case R.id.delete:
                database.mainDAO().delete(selected);
                for (Notes i : is_pinned) {
                    if (selected == i) {
                        is_pinned.remove(selected);
                        notesList.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Note was delete", Toast.LENGTH_SHORT).show();
                        updateRecycleView(is_pinned);
                        updateRecycleView(notes);
                        return true;
                    }
                }
                notes.remove(selected);
                notesList.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Note was delete", Toast.LENGTH_SHORT).show();
                updateRecycleView(is_pinned);
                updateRecycleView(notes);
                updateRecycleView(is_pinned);
                updateRecycleView(notes);
                return true;
            default:
                return false;
        }
    }
}