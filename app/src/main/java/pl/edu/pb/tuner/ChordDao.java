package pl.edu.pb.tuner;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ChordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert (Chord chord);

    @Update
    void update(Chord chord);

    @Delete
    void delete(Chord chord);

    @Query("DELETE FROM chord")
    void deleteAll();

    @Query("SELECT * FROM chord ORDER BY letter")
    LiveData<List<Chord>> findAll();

    @Query("SELECT * FROM chord WHERE letter LIKE :letter")
    List<Chord> findBookWithTitle(String letter);
}
