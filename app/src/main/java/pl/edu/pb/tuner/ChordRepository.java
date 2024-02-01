package pl.edu.pb.tuner;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ChordRepository {
    private final ChordDao chordDao;
    private final LiveData<List<Chord>> chords;

    ChordRepository(Application application) {
        ChordDatabase database = ChordDatabase.getDatabase(application);
        chordDao = database.chordDao();
        chords = chordDao.findAll();
    }

    LiveData<List<Chord>> findAllChords() {
        return chords;
    }

    void insert(Chord chord) {
        ChordDatabase.databaseWriteExecutor.execute(()-> chordDao.insert(chord));
    }

    void update(Chord chord) {
        ChordDatabase.databaseWriteExecutor.execute(()-> chordDao.update(chord));
    }

    void delete(Chord chord) {
        ChordDatabase.databaseWriteExecutor.execute(()-> chordDao.delete(chord));
    }
}
