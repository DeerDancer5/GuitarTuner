package pl.edu.pb.tuner;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Chord.class}, version = 1, exportSchema = false)
public abstract class ChordDatabase extends RoomDatabase {
   private static ChordDatabase databaseInstance;
   static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();

   public abstract ChordDao chordDao();

   static ChordDatabase getDatabase(final Context context) {
       if(databaseInstance == null) {
           databaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                   ChordDatabase.class,"chord_database").addCallback(roomDatabaseCallback).build();
       }
       return databaseInstance;
   }

    private static final RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(()-> {
                ChordDao dao = databaseInstance.chordDao();

                List<Chord> generated = generateChords();
                for(Chord chord : generated) {
                    dao.insert(chord);
                }
            });
        }
    };

   public static List<Chord> generateChords() {
       String[] notes = new String[]{"A","Ab","B","Bb","C","D","Db","E","Eb","F","G","Gb"};
       String[] extensions = new String[]{"","m","7","m7","maj7"};

       List<Chord> generated = new ArrayList<>();

       for(String note : notes) {
           for(String extension: extensions) {
               Chord chord = new Chord(note,extension);
               generated.add(chord);
           }
       }
       return generated;
   }
}
