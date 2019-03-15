package edu.apsu.simon;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.about_button).setOnClickListener(new AboutListener());

        Button b = findViewById(R.id.elimination_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EliminationActivity.class);
                startActivity(intent);
            }
        });

        b = findViewById(R.id.rewind_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RewindActivity.class);
                startActivity(intent);
            }
        });

        b = findViewById(R.id.surprise_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SurpriseActivity.class);
                startActivity(intent);
            }
        });

        b = findViewById(R.id.classic_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ClassicActivity.class);
                startActivity(intent);
            }
        });
    }

    private class AboutListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String message = "<html/>" +
                    "<h1> About Game </h1>" +
                    "<p><b>Song:</b>  </p>" +
                    "<p><b>Creator:</b> Mariah Carnahan & Keenya Gilchrist </p>" +
                    "<p><b>Link:</b> " +
                    "<a href= ''>" +
                    "</a></p>" +
                    "https://opengameart.org/content/oves-essential-game-audio-pack-collection-160-files-updated" +
                    "<p><b>License:</b> CC-BY 3.0 </p>" +
                    "</html>";

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage(Html.fromHtml(message));
            builder.setPositiveButton("OK", null);

            AlertDialog dialog = builder.create();
            dialog.show();

            TextView tv = dialog.findViewById(android.R.id.message);
            tv.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}



