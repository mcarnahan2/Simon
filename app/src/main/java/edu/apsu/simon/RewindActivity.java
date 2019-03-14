package edu.apsu.simon;

import android.content.DialogInterface;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RewindActivity extends AppCompatActivity {
    private ArrayList<Integer> sequence;
    private Random random;
    private int randomButton;
    private boolean blueClicked;
    private boolean greenClicked;
    private boolean yellowClicked;
    private boolean redClicked;
    private Handler handler;
    private SoundPool soundPool;
    private Set<Integer> soundsLoaded;
    int blueSampleId;
    int greenSampleId;
    int yellowSampleId;
    int redSampleId;
    int x=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewind);

        //creates a new array list sequence
        sequence = new ArrayList<>();

        handler = new Handler();

        soundsLoaded = new HashSet<Integer>();

        AudioAttributes.Builder atrBuilder = new AudioAttributes.Builder();
        atrBuilder.setUsage(AudioAttributes.USAGE_GAME);

        SoundPool.Builder spBuilder = new SoundPool.Builder();
        spBuilder.setAudioAttributes(atrBuilder.build());
        spBuilder.setMaxStreams(1);

        soundPool = spBuilder.build();

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if(status == 0){
                    soundsLoaded.add(sampleId);
                }
                else{
                    Log.i("SOUND", "Error, cannot load sound.  Status = " + status);
                }
            }
        });

        blueSampleId = soundPool.load(this, R.raw.blue, 1);
        greenSampleId = soundPool.load(this, R.raw.green, 1);
        yellowSampleId = soundPool.load(this, R.raw.yellow, 1);
        redSampleId = soundPool.load(this, R.raw.red, 1);

        Button b = findViewById(R.id.start_button);
        b.setOnClickListener(new StartListener());

        ImageButton ib = findViewById(R.id.blue_imageButton);
        ib.setOnClickListener(new BlueListener());

        ib = findViewById(R.id.green_imageButton);
        ib.setOnClickListener(new GreenListener());

        ib = findViewById(R.id.yellow_imageButton);
        ib.setOnClickListener(new YellowListener());

        ib = findViewById(R.id.red_imageButton);
        ib.setOnClickListener(new RedListener());
    }

    //calls the sequence to start the game
    class StartListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            createSequence();
            x=0;
            sequence();
        }
    }

    class BlueListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            blueClicked = true;
            playSound(blueSampleId);
        }
    }

    class GreenListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            greenClicked = true;
            playSound(greenSampleId);
        }
    }

    class YellowListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            yellowClicked = true;
            playSound(yellowSampleId);
        }
    }

    class RedListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            redClicked = true;
            playSound(redSampleId);
        }
    }

    private void createSequence(){
        random = new Random();
        randomButton = random.nextInt(4)+1;
        sequence.add(randomButton);

        for (int i=0; i < sequence.size(); i++){
            int seq = sequence.get(i);
            Log.i("COLOR", "Sequence is " + i + "----Value is " + seq);
        }
    }

    private void sequence(){

        if(sequence.get(x) == 1){
            blueFlashOff();
            playSound(blueSampleId);
            blueFlashOn();
            Log.i("COLOR", "BLUE");
        } else if(sequence.get(x) == 2){
            greenFlashOff();
            playSound(greenSampleId);
            greenFlashOn();
            Log.i("COLOR", "GREEN");
        } else if(sequence.get(x) == 3) {
            redFlashOff();
            playSound(redSampleId);
            redFlashOn();
            Log.i("COLOR", "RED");
        } else if(sequence.get(x) == 4) {
            yellowFlashOff();
            playSound(yellowSampleId);
            yellowFlashOn();
            Log.i("COLOR", "YELLOW");
        }

        x++;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(x < sequence.size()){
                    sequence();
                }
            }
        },3000);

    }

    private void blueFlashOff(){
        findViewById(R.id.blue_imageButton).setVisibility(View.INVISIBLE);
    }

    private void blueFlashOn(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.blue_imageButton).setVisibility(View.VISIBLE);
            }
        },500);
    }

    private void greenFlashOff(){
        findViewById(R.id.green_imageButton).setVisibility(View.INVISIBLE);
    }

    private void greenFlashOn(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.green_imageButton).setVisibility(View.VISIBLE);
            }
        }, 500);
    }

    private void redFlashOff(){
        findViewById(R.id.red_imageButton).setVisibility(View.INVISIBLE);
    }

    private void redFlashOn(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.red_imageButton).setVisibility(View.VISIBLE);
            }
        },500);
    }

    private void yellowFlashOff(){
        findViewById(R.id.yellow_imageButton).setVisibility(View.INVISIBLE);
    }

    private void yellowFlashOn(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.yellow_imageButton).setVisibility(View.VISIBLE);
            }
        }, 500);
    }

    private void continueSequence(){

    }

        /*uses a for loop to check the sequence in reverse
        for(int i = sequence.size() - 1; i >= 0; i--)*/


    private void playSound(int sampleId){
        if(soundsLoaded.contains(sampleId)){
            soundPool.play(sampleId, 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }

    private void gameOver(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Game Over");
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();

        sequence.clear();
    }
}
