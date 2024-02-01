package pl.edu.pb.tuner;

public class NoteTranslator {
    private float frequency;
    private final String[] notes;
    private int note;
    private int octave;

    public NoteTranslator() {
        notes = new String[]
                {"A","A#","B","C","C#","D","D#","E","F","F#","G","G#"};
        octave = 1;
    }

    public String getNote(float frequency) {
        this.frequency = frequency;
        if(frequency!=-1.0f) {
            int number = (int) Math.round(12 * (Math.log(frequency / 440) / Math.log(2)) + 49);
            note = (number - 1) % 12;
            octave = (number + 8) / 12;
            return notes[note];
        }
        return "-";
    }

    public double getDifference() {
        if(frequency!=-1.0f) {
            int semitones = (int) Math.round(12 * Math.log(frequency / 440) / Math.log(2));
            double nearest = Math.pow(2d, (semitones / 12d)) * 440d;
            return Math.round(frequency - nearest);
        }
        return 0;
    }
}
