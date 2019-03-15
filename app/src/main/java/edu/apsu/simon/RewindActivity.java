package edu.apsu.simon;


import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import static android.view.View.VISIBLE;

public class RewindActivity extends AppCompatActivity implements View.OnClickListener{
    private ArrayList<Integer> sequence;
    private Random random;
    private int randomButton;
    private boolean player;
    private Handler handler;
    private SoundPool soundPool;
    private Set<Integer> soundsLoaded;
    private int blueSampleId;
    private int greenSampleId;
    private int yellowSampleId;
    private int redSampleId;
    private int currentScore=0;
    private int highScore=0;
    private int selection;
    private ImageButton blue;
    private ImageButton green;
    private ImageButton red;
    private ImageButton yellow;
    private Button startButton;
    private Button aboutButton;
    private Button quitButton;
    private TextView playerTv;
    private TextView cs;
    TextView hs;
    private String playerText="";
    private File file = new File("highscoreRewind.txt");

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

        hs = findViewById(R.id.high_score_textView);
        //hs.setText(readData());
        cs = findViewById(R.id.current_score_textView);

        startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(new StartListener());

        aboutButton = findViewById(R.id.about_button);
        aboutButton.setOnClickListener(new AboutListener());

        quitButton = findViewById(R.id.quit_button);
        quitButton.setOnClickListener(new RewindActivity.QuitListener());

        blue = findViewById(R.id.blue_imageButton);
        blue.setOnClickListener(this);

        green = findViewById(R.id.green_imageButton);
        green.setOnClickListener(this);

        red = findViewById(R.id.red_imageButton);
        red.setOnClickListener(this);

        yellow = findViewById(R.id.yellow_imageButton);
        yellow.setOnClickListener(this);

        playerTv=findViewById(R.id.turn_textView);
    }

    /*private Integer readData() {
        try{
              FileInputStream fis = openFileInput(DATA_FILENAME);
              Scanner scanner = new Scanner(fis);
              while(scanner.hasNext()) {
                  String highScore = scanner.next();
              }
          } catch (FileNotFoundException e){
              //Cannot have a file
          }

          return highScore;
    };*/

    //calls the sequence to start the game
    class StartListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            sequence.clear();
            aboutButton.setEnabled(false);
            continueSequence();

        }
    }

    class QuitListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            onBackPressed();
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
                playSound(blueSampleId);
                sequenceChecker(1);
            } else if(v.getId() == green.getId()){
                sequenceActivity(green);
                playSound(greenSampleId);
                sequenceChecker(2);
            } else if(v.getId() == red.getId()){
                sequenceActivity(red);
                playSound(redSampleId);
                sequenceChecker(3);
            } else if(v.getId() == yellow.getId()){
                sequenceActivity(yellow);
                playSound(yellowSampleId);
                sequenceChecker(4);
            }

        }
    }

    /*private void writeDate(){
        try {
            FileOutputStream fos = openFileOutput(DATA_FILENAME, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            PrintWriter pw = new PrintWriter(bw);

            pw.println(highScore);

            pw.close();
        } catch (FileNotFoundException e) {
            Log.e("WRITE_ERR", "Cannot save data: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Error saving data", Toast.LENGTH_SHORT).show();
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
            selection=sequence.size();
            playerText="Player Turn";
            playerTv.setText(playerText);
            player=true;
            blue.setEnabled(true);
            green.setEnabled(true);
            red.setEnabled(true);
            yellow.setEnabled(true);
        }
    }

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
        playerText="Simon Turn";
        playerTv.setText(playerText);
        player = false;
        blue.setEnabled(false);
        green.setEnabled(false);
        red.setEnabled(false);
        yellow.setEnabled(false);
        startButton.setEnabled(false);
        aboutButton.setEnabled(false);


        if(currentScore>highScore){
            highScore=currentScore;
        }

        /*try{
            BufferedReader reader = new BufferedReader((new FileReader(file)));
            String line =
        } catch (NumberFormatException e){
            //ignore invalid score
        }*/

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
        currentScore++;
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

            selection--;
        } else{
            gameOver();
        }

        if(selection>sequence.size()){
            continueSequence();
        }
    }

    private void reverse(){
        Collections.reverse(sequence);
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
        startButton.setEnabled(true);
        currentScore=0;
        String csText = Integer.toString(currentScore);
        cs.setText(csText);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;

            soundsLoaded.clear();
        }
    }
}
