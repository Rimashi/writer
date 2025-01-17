package com.example.writer.Database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.writer.moduls.Notes;

import java.util.List;

@Dao
public interface mainDAO {

    @Insert(onConflict = REPLACE)
    void insert(Notes notes);

    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<Notes> getAll();


    @Query("UPDATE notes SET title=:title,notes=:notes,category=:category,realNote=:realNote WHERE ID=:ID")
    void update(int ID, String title, String notes, String category, String realNote);

    //@Query("UPDATE notes SET title=:title,notes=:notes WHERE ID=:ID")
    //void update(int ID, String title, String notes);

    @Delete
    void delete(Notes notes);

    @Query("UPDATE notes SET pinned=:pin WHERE ID=:ID")
    void pin(int ID, boolean pin);

}
