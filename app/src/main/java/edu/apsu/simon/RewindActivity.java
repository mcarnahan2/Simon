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

import static android.view.View.VISIBLE;

public class RewindActivity extends AppCompatActivity implements View.OnClickListener{
    private ArrayList<Integer> sequence;
    private Random random;
    private int randomButton;
    private boolean blueClicked;
    private boolean greenClicked;
    private boolean yellowClicked;
    private boolean redClicked;
    private boolean buttonClicked;
    private boolean player;
    private Handler handler;
    private SoundPool soundPool;
    private Set<Integer> soundsLoaded;
    private int blueSampleId;
    private int greenSampleId;
    private int yellowSampleId;
    private int redSampleId;
    private int x=0;
    private int y=0;
    private int index=0;
    private int time=5000;
    private int currentScore=0;
    private int highScore=0;
    private int selection;
    private ImageButton blue;
    private ImageButton green;
    private ImageButton red;
    private ImageButton yellow;
    private Button startButton;
    private Button aboutButton;
    private TextView playerTv;

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
        blue.setOnClickListener(this);

        green = findViewById(R.id.green_imageButton);
        green.setOnClickListener(this);

        red = findViewById(R.id.red_imageButton);
        red.setOnClickListener(this);

        yellow = findViewById(R.id.yellow_imageButton);
        yellow.setOnClickListener(this);
    }

    //calls the sequence to start the game
    class StartListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //startButton.setEnabled(false);
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

    @Override
    public void onClick(View v){
        if(player){
            if(v.getId() == blue.getId()){
                sequenceActivity(blue);
                sequenceChecker(1);
            } else if(v.getId() == green.getId()){
                sequenceActivity(blue);
                sequenceChecker(2);
            } else if(v.getId() == red.getId()){
                sequenceActivity(blue);
                sequenceChecker(3);
            } else if(v.getId() == yellow.getId()){
                sequenceActivity(blue);
                sequenceChecker(4);
            }

        }
    }

    /*class BlueListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            blueClicked = true;
            playSound(blueSampleId);
            y=1;
        }
    }

    class GreenListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            greenClicked = true;
            playSound(greenSampleId);
            y=2;
        }
    }

    class YellowListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            yellowClicked = true;
            playSound(yellowSampleId);
            y=4;
        }
    }

    class RedListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            redClicked = true;
            playSound(redSampleId);
            y=3;
        }
    }*/

    private void createSequence(){
        random = new Random();
        randomButton = random.nextInt(4)+1;
        sequence.add(randomButton);
        sequence(0);
    }

    private void sequence(final int x){
        if(x<sequence.size()) {
            activateButton(sequence.get(x));

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sequence(x+1);
                }
            },3000);
        } else {
            selection=0;
            player=true;
            blue.setEnabled(true);
            green.setEnabled(true);
            red.setEnabled(true);
            yellow.setEnabled(true);
            player=true;
        }
    }



    /*private void blueFlashOff(){
       blue.setVisibility(View.INVISIBLE);
    }

    private void blueFlashOn(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                blue.setVisibility(VISIBLE);
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
                green.setVisibility(VISIBLE);
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
                red.setVisibility(VISIBLE);
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
                yellow.setVisibility(VISIBLE);
            }
        }, 500);
    }*/

    private void sequenceActivity(final ImageButton button){
        button.setVisibility(View.INVISIBLE);

        if(blue.isSelected()){
            playSound(blueSampleId);
        }
        if(green.isSelected()){
            playSound(greenSampleId);
        }
        if(red.isSelected()){
            playSound(redSampleId);
        }
        if (yellow.isSelected()) {
            playSound(yellowSampleId);
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                button.setVisibility(VISIBLE);
            }
        }, 500);
    }

    private void continueSequence(){
        player = false;
        blue.setEnabled(false);
        green.setEnabled(false);
        red.setEnabled(false);
        yellow.setEnabled(false);
        startButton.setEnabled(false);
        aboutButton.setEnabled(false);

        TextView hs = findViewById(R.id.high_score_textView);
        TextView cs = findViewById(R.id.current_score_textView);

        String csString = Integer.toString(currentScore);
        String hsString = Integer.toString(highScore);

        cs.setText(csString);
        hs.setText(hsString);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                createSequence();
            }
        }, 3000);

        //time += 1000;

        /*handler.postDelayed(new Runnable() {
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
        }, time);*/
    }

    private void sequenceChecker(int x){
        if(x==sequence.get(selection)){
            if(x==1 && blue.isSelected()){
                Log.i("CORRECT", "i");
            } else if(x==2 && green.isSelected()){
                Log.i("CORRECT", "i");
            } else if(x==3 && red.isSelected()){
                Log.i("CORRECT", "i");
            } else if(x==4 && yellow.isSelected()){
                Log.i("CORRECT", "i");
            }

            selection++;
        } else{
            gameOver();
        }

        if(selection>=sequence.size()){
            continueSequence();
        }
    }

    private void activateButton(int x){
        if(x == 1){
            playSound(blueSampleId);
            sequenceActivity(blue);
        } else if(x == 2){
            playSound(greenSampleId);
            sequenceActivity(green);
        } else if(x == 3){
            playSound(redSampleId);
            sequenceActivity(red);
        } else if(x == 4){
            playSound(yellowSampleId);
            sequenceActivity(yellow);
        }
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
        x = 0;
        startButton.setEnabled(true);
    }
}
