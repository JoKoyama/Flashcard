package com.example.flashcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;

public class FlashcardActivity extends AppCompatActivity {
    private int posInList;
    private int posOfCard;
    private ConstraintLayout screen;
    private List<Flashcard> flashcards;
    private TextView frontSide;
    private TextView backSide;
    private float x1,x2;
    private Toolbar toolbar;
    static final int MIN_DISTANCE = 150;
    private static final String TAG = "FlashcardActivity";
    private FloatingActionButton shuffle;
    private Button back;
    public boolean goneFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard3);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Intent lastIntent = getIntent();
        FlashcardInfo cards = lastIntent.getParcelableExtra(ListActivity.EXTRA_FLASHCARD);
        posInList = lastIntent.getParcelableExtra(ListActivity.EXTRA_POSOFFLASHCARD);
        flashcards = cards.getFlashcard();
        wireWidgets();
        setListeners();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_flashcard_menu, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_deleteFlashcard:

                return true;
            case R.id.menu_editFlashcard:
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    final Handler handler = new Handler();
    Runnable mLongPressed = new Runnable() {
        public void run() {
            goneFlag = true;
            Log.d(TAG,"Long press activated");
            FlashcardActivity.this.openOptionsMenu();
        }
    };

    private void setListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuffle();
            }
        });

    }


    private void wireWidgets() {
        frontSide = findViewById(R.id.flashcard_textview_frontSide);
        backSide = findViewById(R.id.flashcard_textview_backSide);
        shuffle = findViewById(R.id.floatingActionButton);
        back = findViewById(R.id.flashcard3_button_back);
        frontSide.setText(flashcards.get(0).getFrontSide());
        backSide.setText(flashcards.get(0).getBackSide());
        posOfCard = 0;
        Log.d(TAG,"Widgets have been wired");
    }
    private void leftCard(){
        if(posOfCard ==0){
            posOfCard =flashcards.size()-1;
        }
        else{
            posOfCard--;}
        Log.d(TAG,"card position has been updated to the left card");
        updateCard();
    }

    private void updateCard() {
        frontSide.setText(flashcards.get(posOfCard).getFrontSide());
        backSide.setText(flashcards.get(posOfCard).getBackSide());
        Log.d(TAG,"Card information has been updated");
        if(backSide.getVisibility()==View.VISIBLE){
            flipCard();
        }
    }

    private void rightCard(){
        if(posOfCard ==flashcards.size()-1){
            posOfCard =0;
        }
        else{
            posOfCard++;}
        Log.d(TAG,"Card position has been updated to the right card");
        updateCard();
    }
    private void flipCard(){
        if(frontSide.getVisibility()==View.VISIBLE){
            frontSide.setVisibility(View.GONE);
            backSide.setVisibility(View.VISIBLE);
        }
        else{
            frontSide.setVisibility(View.VISIBLE);
            backSide.setVisibility(View.GONE);
        }
        Log.d(TAG,"Card has been flipped");
    }
    private void shuffle(){
        Collections.shuffle(flashcards);
            Log.d(TAG, "Cards have been Shuffled");
        updateCard();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                goneFlag =false;
                x1 = event.getX();
                Log.d(TAG,"Screen has been pressed");
                handler.postDelayed(mLongPressed, 1500);

                break;
            case MotionEvent.ACTION_UP:
                handler.removeCallbacks(mLongPressed);
                x2 = event.getX();
                float deltaX = x2 - x1;
                if(!goneFlag) {
                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        if (deltaX > 0) {
                            Log.d(TAG, "Left to right swipe");
                            leftCard();
                        }
                        else {
                            Log.d(TAG, "Right to left swipe");
                            rightCard();
                        }
                    }
                    else {
                        Log.d(TAG, "Touch or vertical swipe");
                        flipCard();
                    }
                    return false;
                }
                Log.d(TAG,"Finger has left the screen");
        }
        return true;
    }
}
