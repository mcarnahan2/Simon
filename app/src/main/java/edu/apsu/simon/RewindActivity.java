package edu.apsu.simon;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
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
    private int blueSampleId;
    private int greenSampleId;
    private int yellowSampleId;
    private int redSampleId;
    private int x=0;
    private int time=5000;
    private int currentScore=0;
    private int highScore=0;
    private ImageButton blue;
    private ImageButton green;
    private ImageButton red;
    private ImageButton yellow;
    private Button startButton;
    private Button aboutButton;

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

        Button restartButton = findViewById(R.id.restart_button);
        restartButton.setOnClickListener(new RestartListener());

        startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(new StartListener());

        aboutButton = findViewById(R.id.about_button);
        aboutButton.setOnClickListener(new AboutListener());

        blue = findViewById(R.id.blue_imageButton);
        blue.setOnClickListener(new BlueListener());

        green = findViewById(R.id.green_imageButton);
        green.setOnClickListener(new GreenListener());

        red = findViewById(R.id.red_imageButton);
        red.setOnClickListener(new RedListener());

        yellow = findViewById(R.id.yellow_imageButton);
        yellow.setOnClickListener(new YellowListener());
    }

    //calls the sequence to start the game
    class StartListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            startButton.setEnabled(false);
            aboutButton.setEnabled(false);
            continueSequence();

        }
    }

    class RestartListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            //add alert message
            continueSequence();
        }
    }

    class AboutListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            String message = "<hmtl>" +
                    "<h2>About Game</h2>" +
                    "<p>This game is a variation of the game Simon.  The game will flash a button and play a sound.  The object of this game is to enter the sequence in reverse.  If you get the sequence correctly, " +
                    "you will keep advancing until you miss a button.</p>" +
                    "</html>";

            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setMessage(Html.fromHtml(message));
            builder.setPositiveButton("OK", null);

            AlertDialog dialog = builder.create();
            dialog.show();
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
        if(x < sequence.size()){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                        sequence();

                }
            },3000);
        } else {
            blue.setEnabled(true);
            green.setEnabled(true);
            red.setEnabled(true);
            yellow.setEnabled(true);
        }
    }

    private void blueFlashOff(){
       blue.setVisibility(View.INVISIBLE);
    }

    private void blueFlashOn(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                blue.setVisibility(View.VISIBLE);
            }
        },500);
    }

    private void greenFlashOff(){
        green.setVisibility(View.INVISIBLE);
    }

    private void greenFlashOn(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                green.setVisibility(View.VISIBLE);
            }
        }, 500);
    }

    private void redFlashOff(){
        red.setVisibility(View.INVISIBLE);
    }

    private void redFlashOn(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                red.setVisibility(View.VISIBLE);
            }
        },500);
    }

    private void yellowFlashOff(){
        yellow.setVisibility(View.INVISIBLE);
    }

    private void yellowFlashOn(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                yellow.setVisibility(View.VISIBLE);
            }
        }, 500);
    }

    private void continueSequence(){
        blue.setEnabled(false);
        green.setEnabled(false);
        red.setEnabled(false);
        yellow.setEnabled(false);

        TextView hs = findViewById(R.id.high_score_textView);
        TextView cs = findViewById(R.id.current_score_textView);

        String csString = Integer.toString(currentScore);
        String hsString = Integer.toString(highScore);

        cs.setText(csString);
        hs.setText(hsString);

        createSequence();
        x=0;
        sequence();
        time += 1000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sequenceChecker();
                if(sequenceChecker()){
                    currentScore+=1;
                    if(currentScore > highScore){
                        highScore=currentScore;
                    }
                    continueSequence();
                } else {
                    gameOver();
                }
            }
        }, time);
    }

    private boolean sequenceChecker(){
        //uses a for loop to check the sequence in reverse
        for(int i = sequence.size() - 1; i >= 0; i--){
            int seq = sequence.get(i);

            if(seq == 1 && blueClicked){
                Log.i("PATTERN", "Correct");
            } else if(seq == 2 && greenClicked) {
                Log.i("PATTERN", "Correct");
            } else if(seq == 3 && redClicked) {
                Log.i("PATTERN", "Correct");
            } else if(seq == 4 && yellowClicked) {
                Log.i("PATTERN", "Correct");
            } else {
                return false;
            }
        }
        return true;
    }

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
        startButton.setEnabled(true);
    }
}
