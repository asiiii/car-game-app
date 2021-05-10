package com.example.w1761097;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Random;

public class ThirdActivity extends AppCompatActivity {
    private static final String LOG_TAG =
            ThirdActivity.class.getSimpleName();
    //ArrayList of all the car makes in the game
    String[] carMakesList = {"audi", "bentley", "bmw", "bugatti", "dodge", "ferrari", "ford", "honda", "jaguar", "lamborghini", "lexus", "maserati", "mercedes",
            "mitsubishi", "nissan", "porsche", "suzuki", "tesla", "toyota", "volkswagen"};
    public static final int MAX_ERRORS = 3;
    ImageView imageView;
    TextView displayGuess, displayResult, displayTimer, displayScore;
    Button submitGuessBtn;
    EditText userInput;
    int errorCount, totalScore = 0;
    int imageId; //holds the carMakesList ArrayList index for the generated car make
    boolean timerStatus;
    String randomCarMake, imageName;
    //guessedLetters holds the correctly guessed letters in the correct position and dashes for the letters not guessed yet
    ArrayList<String> guessedLetters = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hints);
        imageView = findViewById(R.id.random_carmake);
        displayGuess = findViewById(R.id.display_guess);
        userInput = findViewById(R.id.edittext_guess);
        displayResult = findViewById(R.id.textView_display_result);
        displayTimer = findViewById(R.id.textView_timer2);
        displayScore = findViewById(R.id.display_score2);
        submitGuessBtn = findViewById(R.id.button_submit_guess);

        //timer status is received from MainActivity and assigned to timerStatus variable
        Bundle extras = getIntent().getExtras();
        timerStatus = extras.getBoolean("Timer status");
        Log.d(LOG_TAG, "Timer status: " + timerStatus);
        randomImageGenerator();

    }

    //method to generate a new random image
    public void randomImageGenerator(){
        errorCount = 0; //error count is set to zero for the new word

        Random rand = new Random();
        //the car make is randomly chosen from carMakesList ArrayList
        int randomCarMakeNum = rand.nextInt(carMakesList.length);
        randomCarMake = carMakesList[randomCarMakeNum];
        imageId = randomCarMakeNum;

        //random number from 1 to 3 is generated to select which car of the car make is generated
        int randomNumber = rand.nextInt((3-1)+1) + 1;

        //the image resource files are named as car make + number
        //randomCarMake+randomNumber makes up the file name of the generated car make
        imageName= randomCarMake+randomNumber;
        Log.d(LOG_TAG, "Randomly generated image is " + imageName);

        //ID if the generated image is fetched and image is set
        int randomImageId = getResources().getIdentifier(imageName, "drawable", this.getPackageName());
        imageView.setImageResource(randomImageId);

        //StringBuilder is used to generate the dashes for the name of the generated car make and it is displayed
        StringBuilder displayLetters = new StringBuilder();
        for(int i=0;i<randomCarMake.length();i++){
            //guessedLetters is initialized with dashes as elements
            guessedLetters.add("-");
            displayLetters.append("- ");
            if(i < guessedLetters.size() - 1){
                displayLetters.append(" ");
            }
        }
        displayGuess.setText(displayLetters.toString());
        displayGuess.setTextColor(getResources().getColor(R.color.default_text_color));

        //timer starts counting down
        if(timerStatus){
            countDownTimer.start();
        }
    }

    //method to execute the code whenever the button_high_score is clicked or time runs out
    public void buttonAction(View view) {
        if(submitGuessBtn.getText().toString().equals("Submit")){
            //when the button_high_score is set to "Submit"
            displayResult.setText("");

            //user input is fetched and assigned to variable userGuess
            //EditText is then cleared
            String userGuess = userInput.getText().toString().toLowerCase();
            userInput.getText().clear();
            //extracts only the first letter of the user input using substring()
            //the rest is discarded
            if(userGuess.length()>1){
                userGuess = userGuess.substring(0, 1);
            }

            if(errorCount<=MAX_ERRORS){
                //if the error count is less than or equal to 3
                if(!guessedLetters.contains(userGuess) && !userGuess.isEmpty()){
                    //if the userGuess is not empty and not a previously correctly guessed letter
                    //timer stops
                    if(timerStatus){
                        countDownTimer.cancel();
                    }

                    if(randomCarMake.contains(userGuess)){
                        // if the user input is in the word
                        //the places where the letter is in the word replaces the dash in ArrayList guessedLetters
                        int index = randomCarMake.indexOf(userGuess);
                        while(index >=0 ){
                            guessedLetters.set(index, userGuess);
                            index = randomCarMake.indexOf(userGuess, index +1);
                        }

                    }else{
                        //error count increases due to incorrect guess
                        errorCount++;
                    }

                    //StringBuilder is used to generate the current state of the word
                    StringBuilder stringBuilder = new StringBuilder();
                    StringBuilder checker = new StringBuilder();
                    for(int i = 0; i<guessedLetters.size();i++){
                        stringBuilder.append(guessedLetters.get(i).toUpperCase());
                        checker.append(guessedLetters.get(i));
                        if(i < guessedLetters.size() - 1){
                            stringBuilder.append(" ");
                        }
                    }

                    //StringBuilder is displayed
                    displayGuess.setText(stringBuilder.toString());

                    //contentEquals() checks if the entire word is guessed
                    //sets the value of result, result text color and SnackBox message
                    if(randomCarMake.contentEquals(checker.toString())){
                        displayResult.setText(R.string.correct_answer);
                        displayResult.setTextColor(getResources().getColor(R.color.correct));
                        userInput.setEnabled(false);
                        //timer stops
                        if(timerStatus){
                            countDownTimer.cancel();
                        }
                        totalScore++; //totalScore increments
                        displayScore.setText(getString(R.string.score_text, Integer.toString(totalScore)));
                        int stringId = R.string.positive_message;
                        Snackbar snackbar = Snackbar.make(this.findViewById(android.R.id.content).getRootView(), stringId,
                                Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        //button_high_score changes to "Next" to move on to the next image
                        submitGuessBtn.setText(R.string.next_button_label);
                    }else{
                        //error count is displayed
                        displayResult.setText(getString(R.string.error_count, Integer.toString(errorCount)));
                        displayResult.setTextColor(getResources().getColor(R.color.default_text_color));
                        //timer restarts for new attempt
                        if(timerStatus){
                            countDownTimer.start();
                        }
                        //if max attempts have been made, the value of result, result text color and SnackBox message is set
                        if(errorCount == MAX_ERRORS){
                            displayResult.setTextColor(getResources().getColor(R.color.wrong));
                            displayResult.setText(R.string.wrong_answer);
                            displayGuess.setText(randomCarMake.toUpperCase());
                            displayGuess.setTextColor(getResources().getColor(R.color.correct_answer));
                            userInput.setEnabled(false);
                            //timer stops
                            if(timerStatus){
                                countDownTimer.cancel();
                            }
                            int stringId = R.string.encouraging_message;
                            Snackbar snackbar = Snackbar.make(this.findViewById(android.R.id.content).getRootView(), stringId,
                                    Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            //buton is changed to "Next" to move on to the next word
                            submitGuessBtn.setText(R.string.next_button_label);
                        }
                    }
                }else{
                    //messages displayed when user Input is empty or a previously correctly guessed letter is entered are set and displayed
                    if(userGuess.isEmpty()){
                        displayResult.setText(R.string.hints_no_guess_text);
                        displayResult.setTextColor(getResources().getColor(R.color.default_text_color));
                    }else{
                        displayResult.setText(R.string.hints_repeatitive_guess_text);
                        displayResult.setTextColor(getResources().getColor(R.color.default_text_color));
                    }
                }
            }

        }else{
            //when button_high_score is "Next"
            //outcome values are set to default, button_high_score is set to "Submit", a new image is generated
            userInput.getText().clear();
            userInput.setEnabled(true);
            displayResult.setText("");
            displayGuess.setText("");
            submitGuessBtn.setText(R.string.submit_button_label);
            guessedLetters.clear();
            randomImageGenerator();
        }
    }

    //CountDownTimer is starts at 20s and counts down to 1
    //Time left is displayed onTick()
    //onFinish buttonAction() method is called
    /*SOURCE: https://developer.android.com/reference/android/os/CountDownTimer*/
    CountDownTimer countDownTimer = new CountDownTimer(20000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            String time = Long.toString(millisUntilFinished / 1000);
            displayTimer.setText(getString(R.string.display_timer , time));
        }

        @Override
        public void onFinish() {
            buttonAction(submitGuessBtn);
        }
    };

    //saves instances of values that changes with orientation changes
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("RANDOM_IMAGE", imageName);
        outState.putInt("RANDOM_IMAGE_ID", imageId);
        outState.putString("CAR_MAKE", randomCarMake);
        outState.putString("GUESSES", displayGuess.getText().toString());
        outState.putInt("COLOR", displayGuess.getCurrentTextColor());
        outState.putInt("CURRENT_SCORE", totalScore);
        outState.putString("CURRENT_SCORE_TEXT", displayScore.getText().toString());
        outState.putString("BUTTON_STATUS", submitGuessBtn.getText().toString());
        outState.putStringArrayList("GUESS LIST", guessedLetters);
        outState.putString("RESULT", displayResult.getText().toString());
        outState.putInt("RESULT_COLOR", displayResult.getCurrentTextColor());
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState");

    }

    //restores all saved instances after orientation change
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore state members from saved instance
        imageName = savedInstanceState.getString("RANDOM_IMAGE");
        Log.d(LOG_TAG, "Restored image is " + imageName);
        imageView.setImageResource(getResources().getIdentifier(imageName, "drawable", this.getPackageName()));
        imageId = savedInstanceState.getInt("RANDOM_IMAGE_ID");

        randomCarMake= savedInstanceState.getString("CAR_MAKE");
        displayGuess.setText(savedInstanceState.getString("GUESSES"));
        displayGuess.setTextColor(savedInstanceState.getInt("COLOR"));
        guessedLetters = savedInstanceState.getStringArrayList("GUESS LIST");
        submitGuessBtn.setText(savedInstanceState.getString("BUTTON_STATUS"));

        displayResult.setText(savedInstanceState.getString("RESULT"));
        displayResult.setTextColor(savedInstanceState.getInt("RESULT_COLOR"));

        totalScore = savedInstanceState.getInt("CURRENT_SCORE");
        String currentScoreText = savedInstanceState.getString("CURRENT_SCORE_TEXT");
        displayScore.setText(currentScoreText);
    }
}
