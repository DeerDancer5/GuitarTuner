package pl.edu.pb.tuner;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ChordViewModel extends AndroidViewModel {
    private final ChordRepository chordRepository;
    private final LiveData<List<Chord>> chords;

    public ChordViewModel(@NonNull Application application) {
        super(application);
        chordRepository = new ChordRepository(application);
        chords = chordRepository.findAllChords();
    }

    public LiveData<List<Chord>> findAll() {
        return chords;
    }

    public void insert(Chord chord) {
        chordRepository.insert(chord);
    }

    public void update(Chord chord) {
        chordRepository.update(chord);
    }

    public void delete(Chord chord) {
        chordRepository.delete(chord);
    }

}
