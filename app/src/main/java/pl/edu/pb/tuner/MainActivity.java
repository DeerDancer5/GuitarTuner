package pl.edu.pb.tuner;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;

import java.util.ArrayList;
import java.util.List;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class MainActivity extends AppCompatActivity {

    private TextView noteText;
    private TextView upText;
    private TextView downText;
    private TextView pitchText;

    private NoteTranslator noteTranslator;

    private HalfGauge halfGauge;

    AudioDispatcher dispatcher;
    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        noteTranslator = new NoteTranslator();
        setContentView(R.layout.activity_main);
        upText = findViewById(R.id.upText);
        downText = findViewById(R.id.downText);
        noteText = findViewById(R.id.noteText);
        pitchText = findViewById(R.id.pitchText);
        halfGauge = findViewById(R.id.halfGauge);
        dispatcher=AudioDispatcherFactory.fromDefaultMicrophone(22050,1024,0);


        Range range1 = new Range();
        range1.setColor(Color.parseColor("#ce0000"));
        range1.setFrom(-4);
        range1.setTo(-1);
        Range range2 = new Range();
        range2.setFrom(-1);
        range2.setTo(1);
        range2.setColor(Color.parseColor("#00b20b"));
        Range range3 = new Range();
        range3.setFrom(1);
        range3.setTo(4);
        range3.setColor(Color.parseColor("#ce0000"));
        List<Range> rangeList = new ArrayList<>();
        rangeList.add(range1);
        rangeList.add(range2);
        rangeList.add(range3);
        halfGauge.setRanges(rangeList);
        halfGauge.enableAnimation(true);
        halfGauge.setMinValue(-4);
        halfGauge.setMaxValue(4);

        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult res, AudioEvent e){
                final float pitchInHz = res.getPitch();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processPitch(pitchInHz);
                    }
                });
            }
        };

        AudioProcessor pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(pitchProcessor);
        Thread audioThread = new Thread(dispatcher, "Audio Thread");
        audioThread.start();
    }

    public void processPitch(float pitchInHz) {

        noteText.setText(noteTranslator.getNote(pitchInHz));
        halfGauge.setValue(noteTranslator.getDifference());

        if(noteTranslator.getDifference()<0 && Math.abs(noteTranslator.getDifference()) > 1) {
            upText.setText("/\\");
            noteText.setTextColor(Color.parseColor("#ce0000"));
            downText.setText("");
        }
        else if(noteTranslator.getDifference()>0&& noteTranslator.getDifference() > 1){
            downText.setText("\\/");
            noteText.setTextColor(Color.parseColor("#ce0000"));
            upText.setText("");
        }
        else {
            upText.setText("");
            noteText.setTextColor(Color.parseColor("#00b20b"));
            downText.setText("");
        }
        if(pitchInHz==-1.0f) {
            noteText.setTextColor(Color.GRAY);
        }

        pitchText.setText(Double.toString(noteTranslator.getDifference()));

    }
}