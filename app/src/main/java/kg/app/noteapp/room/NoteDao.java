package kg.app.noteapp.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import kg.app.noteapp.models.Note;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM note")
    List<Note> getAll();

    @Query("DELETE FROM note")
    void deletable();

    @Query("SELECT * FROM `note` ORDER BY title ASC")
    List<Note> getNoteAlphabet();

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);
}
