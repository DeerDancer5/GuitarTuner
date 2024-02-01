package pl.edu.pb.tuner;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

    private AudioDispatcher dispatcher;
    @SuppressLint({"MissingInflatedId", "SetTextI18n", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        noteTranslator = new NoteTranslator();
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView= findViewById(R.id.navigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_tuner);



        upText = findViewById(R.id.upText);
        downText = findViewById(R.id.downText);
        noteText = findViewById(R.id.noteText);
        halfGauge = findViewById(R.id.halfGauge);
        halfGauge.setNeedleColor(Color.WHITE);
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

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId()==R.id.bottom_tuner) {
                return true;
            }
            else if(item.getItemId()==R.id.bottom_metronome) {
                startActivity(new Intent(getApplicationContext(),MetronomeActivity.class));
                overridePendingTransition(0,0);
                finish();
                return true;
            }
            else if(item.getItemId()==R.id.bottom_chords) {
                startActivity(new Intent(getApplicationContext(),ChordsActivity.class));
                overridePendingTransition(0,0);
                finish();
                return true;
            }
            return false;
        });
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


    }
}