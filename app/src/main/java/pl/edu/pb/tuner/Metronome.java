package pl.edu.pb.tuner;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Handler;
import android.os.SystemClock;

public class Metronome {

    private AudioAttributes audioAttributes;
    private SoundPool soundPool;
    private int sound;
    private boolean running;
    private Handler handler;
    private int tempo;

    public Metronome(int tempo, Context context) {
        this.tempo = tempo;
        running = false;
        handler = new Handler();
        audioAttributes = new AudioAttributes.Builder()
               .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
               .setUsage(AudioAttributes.USAGE_GAME)
               .build();
        soundPool = new SoundPool.Builder()
              .setMaxStreams(2)
              .setAudioAttributes(audioAttributes)
              .build();
        sound = soundPool.load(context,R.raw.metronome2,1);

    }
    public void start() {
        handler.post(metronomeRunnable);
        running = true;
    }

    public void play() {
        soundPool.play(sound,1,1,1,0,1);
    }

    public void stop() {
        handler.removeCallbacks(metronomeRunnable);
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    private Runnable metronomeRunnable = new Runnable() {
        @Override
        public void run() {
            handler.postAtTime(this, SystemClock.uptimeMillis()+60000/tempo);
            play();

        }
    };

    public void setTempo(int tempo) {
       this.tempo = tempo;
    }
}
