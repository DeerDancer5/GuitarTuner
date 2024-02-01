package pl.edu.pb.tuner;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chord")
public class Chord {
        @PrimaryKey(autoGenerate = true)
        private int id;
        private String letter;
        private String extension;

        public String getLetter() {
            return letter;
        }

        public void setLetter(String letter) {
            this.letter = letter;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public Chord(String letter, String extension) {
            this.letter = letter;
            this.extension = extension;
        }
}
