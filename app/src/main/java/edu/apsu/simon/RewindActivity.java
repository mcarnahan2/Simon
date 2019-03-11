package edu.apsu.simon;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class RewindActivity extends AppCompatActivity {
    private ArrayList<Integer> sequence;
    private Random random;
    private int randomButton;
    private boolean blueClicked;
    private boolean greenClicked;
    private boolean yellowClicked;
    private boolean redClicked;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewind);

        //creates a new array list sequence
        sequence = new ArrayList<>();

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

            checkSequence();
        }
    }

    class BlueListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            blueClicked = true;
        }
    }

    class GreenListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            greenClicked = true;
        }
    }

    class YellowListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            yellowClicked = true;
        }
    }

    class RedListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            redClicked = true;
        }
    }

    private void createSequence(){
        random = new Random();
        randomButton = random.nextInt(4)+1;
        sequence.add(randomButton);
    }

    private void checkSequence(){
        //uses a for loop to check the sequence in reverse
        for(int i = sequence.size() - 1; i >= 0; i--){
            int seq = sequence.get(i);

            if(seq == 1 && blueClicked){

            } else if(seq == 2 && greenClicked){

            } else if(seq == 3 && yellowClicked){

            } else if(seq == 4 && redClicked){

            } else {
                gameOver();
            }

            Log.i("SEQUENCE", "i is " + i + " sequence is " + seq);
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
