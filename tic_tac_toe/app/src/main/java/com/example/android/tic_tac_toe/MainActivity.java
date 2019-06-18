
package com.example.android.tic_tac_toe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Random;

import static java.lang.StrictMath.max;
import static java.lang.StrictMath.min;

public class MainActivity extends AppCompatActivity {
    TextView result, best;
    Button reset, easy, medium, hard;
    String[][] buttonvalue = new String[3][3];
    String levelselected, turn = "O";
    Button[][] b = new Button[3][3];
    String player = "O", computer = "X";
    String playerturn = player;
    Boolean checklevel = false;
    int x, y; int count=0;
    int moveno = 0;
    private Chronometer chrono;
    private boolean isrunning = false;
    private long pause = 0;
    long besttime = 0;
    static final String SHARED_PREFS = "sharedPrefs";
    static final String time = "time";
    static final String countwins="count";
    display_besttime db;
MediaPlayer addsign,res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = findViewById(R.id.playerturn);
        reset = findViewById(R.id.reset);
        easy = findViewById(R.id.easy);
        medium = findViewById(R.id.medium);
        hard = findViewById(R.id.hard);
        best = findViewById(R.id.best);
        chrono = findViewById(R.id.chronometer);
        addsign=MediaPlayer.create(this,R.raw.add);
        res=MediaPlayer.create(this,R.raw.win);
        best.setVisibility(View.INVISIBLE);
        db=new display_besttime(this);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String id = "b" + i + j;
                int buttonid = getResources().getIdentifier(id, "id", getPackageName());
                buttonvalue[i][j] = "_";
                b[i][j] = findViewById(buttonid);

            }
        }
loadData();
    }

//To set O on click if user turn
    public void play(final View view) {
        String bval = ((Button) view).getText().toString();
        if (checklevel)
            if (bval.equals("") && playerturn.equals(player) && moveno <= 9)

            {     addsign.start();
                   ((Button) view).setText("O");



                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (view == b[i][j]) {  //Toast.makeText(getApplicationContext(),"button "+i+j,Toast.LENGTH_SHORT).show();
                            buttonvalue[i][j] = "O";
                        }

                    }
                }
                playgame();
            }


    }

//If easy level is selected
    public void easy(View view) {
        if (!checklevel) {
            levelselected = "EASY";
            checklevel = true;
            result.setVisibility(View.INVISIBLE);
            view.setBackgroundColor(Color.parseColor("#b77b33"));
            if (turn == computer) {
                playgame();
            }
        }
    }
//if medium level is selected
    public void medium(View view) {
        if (!checklevel) {
            levelselected = "MEDIUM";
            checklevel = true;
            result.setVisibility(View.INVISIBLE);
            view.setBackgroundColor(Color.parseColor("#b77b33"));
            if (turn == computer) {
                playgame();
            }
        }
    }
//if hard level is selected
    public void hard(View view) {
        if (!checklevel) {
            levelselected = "HARD";
            checklevel = true;
            result.setVisibility(View.INVISIBLE);
            view.setBackgroundColor(Color.parseColor("#b77b33"));
            if (turn == computer) {
                playgame();
            }
        }
    }
//Set the moves
    private void playgame() {
        moveno++;
        if (moveno < 10) {
            if (moveno == 1) {
                if (!isrunning) {
                    chrono.setBase(SystemClock.elapsedRealtime() - pause);
                    chrono.start();
                    isrunning = true;
                }

            }
            if (playerturn.equals(player)) {
                if (checkwin(0) == +10) {
                    res.start();
                    result.setVisibility(View.VISIBLE);
                    result.setText("YAY..YOU WON!!");
                    reset.setText("START NEW GAME");
                    int x=checkwin(1);
                    count++;

                    turn = player;
                    if (isrunning) {
                        chrono.stop();
                        pause = SystemClock.elapsedRealtime() - chrono.getBase();
                       if(count==1)
                       {besttime=pause;}
                        else if (pause < besttime) {
                            besttime = pause;
                            }saveData();
                        AddData(levelselected+" -  Game "+count+" : won in "+pause/1000+"s");
                        best.setVisibility(View.VISIBLE);
                        if(besttime>60000)
                        {best.setText("Best time ="+besttime/60000+":"+(besttime%60000)/1000);}
                       else{  if((besttime/1000)<10){best.setText("Best time =00:0"+besttime/1000);}
                            else best.setText("Best time = 00:" + besttime/1000);}
                        isrunning = false;
                    }moveno = 10;
                    return;
                }
                playerturn = computer;
                playgame();
            } else if (playerturn.equals(computer)) {
                if (levelselected.equals("HARD") || levelselected.equals("MEDIUM")) {
                    choosebestmove();
                }
                if (levelselected.equals("EASY")) {
                    chooserandom();
                }
                if (checkwin(0) == -10) {
                    res.start();
                    result.setVisibility(View.VISIBLE);
                    result.setText("OOPS..YOU LOST!!");
                    reset.setText("START NEW GAME");
                    moveno = 10;
                    int x=checkwin(1);
                    turn = computer;
                    if (isrunning) {
                        chrono.stop();
                        pause = SystemClock.elapsedRealtime() - chrono.getBase();
                        isrunning = false;
                    }
                    return;
                }
                playerturn = player;

            }

        }


        if (isDraw()) {
            res.start();
            result.setVisibility(View.VISIBLE);
            result.setText("IT IS A DRAW!");
            reset.setText("START NEW GAME");
            turn = player;
            if (isrunning) {
                chrono.stop();
                pause = SystemClock.elapsedRealtime() - chrono.getBase();
                isrunning = false;
            }
        }


    }
//Checks for winning condition
    private int checkwin(int state) {
        for (int i = 0; i < 3; i++) { //Horizontal check
            if (buttonvalue[i][0].equals(buttonvalue[i][1]) && buttonvalue[i][1].equals(buttonvalue[i][2]))
            {
                if(state==1){b[i][0].setBackgroundColor(Color.parseColor("#b77b33"));
                b[i][1].setBackgroundColor(Color.parseColor("#b77b33"));
                b[i][2].setBackgroundColor(Color.parseColor("#b77b33"));}
                if (buttonvalue[i][0].equals(computer))
                    return -10;
                else if (buttonvalue[i][0].equals(player))
                    return +10;
            }
            //Vertical check
            if (buttonvalue[0][i].equals(buttonvalue[1][i]) && buttonvalue[1][i].equals(buttonvalue[2][i]) && !buttonvalue[0][i].equals("_"))
            {
              if(state==1)
              {b[0][i].setBackgroundColor(Color.parseColor("#b77b33"));
                b[1][i].setBackgroundColor(Color.parseColor("#b77b33"));
                b[2][i].setBackgroundColor(Color.parseColor("#b77b33"));}

                if (buttonvalue[0][i].equals(computer))
                    return -10;
                else if (buttonvalue[0][i].equals(player))
                    return +10;
            }
        }
        //Diagonal check

        if (buttonvalue[0][0].equals(buttonvalue[1][1]) && buttonvalue[1][1].equals(buttonvalue[2][2])) {
            if(state==1) {
                b[0][0].setBackgroundColor(Color.parseColor("#b77b33"));
                b[1][1].setBackgroundColor(Color.parseColor("#b77b33"));
                b[2][2].setBackgroundColor(Color.parseColor("#b77b33"));
            }
            if (buttonvalue[0][0].equals(computer))
                return -10;
            else if (buttonvalue[0][0].equals(player))
                return +10;
        }
        if (buttonvalue[0][2].equals(buttonvalue[1][1]) && buttonvalue[1][1].equals(buttonvalue[2][0]))

        {if(state==1) {
            b[0][2].setBackgroundColor(Color.parseColor("#b77b33"));
            b[1][1].setBackgroundColor(Color.parseColor("#b77b33"));
            b[2][0].setBackgroundColor(Color.parseColor("#b77b33"));
        }
            if (buttonvalue[0][2].equals(computer))
                return -10;
            else if (buttonvalue[0][2].equals(player))
                return +10;
        }
        return 0;
    }
//If computer's turn for hard and medium level
    private void choosebestmove() {
        int val = +1000;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttonvalue[i][j].equals("_")) {
                    buttonvalue[i][j] = "X";
                    int move = maxsearch(0);
                    if (move < val) {
                        val = move;
                        x = i;
                        y = j;
                    }
                    buttonvalue[i][j] = "_";

                }
            }
        }
addsign.start();
                buttonvalue[x][y] = "X";
                b[x][y].setText("X");

    }

    int maxsearch(int depth) {
        int score = checkwin(0);
        if (score == 10)
            return score + depth;
        if (score == -10)
            return score - depth;
        score = -1000;
        if (isDraw()) {
            return 0;
        }
        if ((levelselected.equals("MEDIUM") && depth < 1) || levelselected.equals("HARD")) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (buttonvalue[i][j].equals("_")) {
                        buttonvalue[i][j] = "O";
                        score = max(score, minsearch(depth + 1));
                        buttonvalue[i][j] = "_";
                    }
                }
            }
        }
        return score;
    }

    int minsearch(int depth) {
        int score = checkwin(0);
        if (score == 10)
            return score;
        if (score == -10)
            return score;
        if (isDraw()) {
            return 0;
        }
        score = 1000;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttonvalue[i][j].equals("_")) {
                    buttonvalue[i][j] = "X";
                    score = min(score, maxsearch(depth + 1));
                    buttonvalue[i][j] = "_";
                }
            }
        }
        return score;

    }
//If reset button is clicked
    public void reset(View view) {
        if (reset.getText() == "START NEW GAME") {
            reset.setText("RESET");
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                {
                    b[i][j].setText("");
                    buttonvalue[i][j] = "_";
                    b[i][j].setBackgroundColor(Color.parseColor("#5d4037"));
                }
                playerturn = turn;
                result.setVisibility(View.VISIBLE);
                result.setText("Select a difficulty level");
                levelselected = null;
                checklevel = false;
                moveno = 0;
                easy.setBackgroundColor(Color.parseColor("#fbc02d"));
                medium.setBackgroundColor(Color.parseColor("#fbc02d"));
                hard.setBackgroundColor(Color.parseColor("#fbc02d"));
                chrono.stop();isrunning=false;
                chrono.setBase(SystemClock.elapsedRealtime());
                pause = 0;
                best.setVisibility(View.INVISIBLE);
            }
        }
    }
//If easy level and computer's turn
    private void chooserandom() {
        ArrayList<Integer> lbl1 = new ArrayList<Integer>();
        ArrayList<Integer> lbl2 = new ArrayList<Integer>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttonvalue[i][j] == "_") {
                    lbl1.add(i);
                    lbl2.add(j);
                }
            }
        }
        Random random = new Random();
        int ran = random.nextInt(lbl1.size());
        final int m = lbl1.get(ran);
        final int n = lbl2.get(ran);
addsign.start();
               buttonvalue[m][n] = "X";
               b[m][n].setText("X");

    }
//Checking for tie condition
    private boolean isDraw() {
        int cnt = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttonvalue[i][j] == "_") {
                    cnt++;
                }
            }
        }
        if (cnt == 0) {
            return true;
        } else
            return false;
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(time, besttime);
        editor.putInt(countwins,count);
        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        besttime = sharedPreferences.getLong(time, 0);
        count=sharedPreferences.getInt(countwins,0);
    }

    //For SQLite
public void AddData(String pause)
{boolean inserttime=db.addData(pause);
}
//To view the history database
public void history(View view)
{
    Intent intent= new Intent(MainActivity.this,viewTime.class);
    startActivity(intent);
}
}